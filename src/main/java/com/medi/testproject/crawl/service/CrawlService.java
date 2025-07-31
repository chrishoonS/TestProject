package com.medi.testproject.crawl.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class CrawlService {

    private static final String NEWS_HOME_URL = "https://news.naver.com";
    private static final String NEWS_TOP_MENU = "li.Nlist_item";
    private static final String NEWS_TOP_MENU_LINK = "a.Nitem_link";
    private static final String NEWS_HEADLINE_LIST = "li.sa_item._SECTION_HEADLINE:not(.is_blind)";
    private static final String NEWS_HEADLINE_A_TAG_LINK = "a.sa_text_title";
    private static final String NEWS_HEADLINE_TITLE = "strong.sa_text_strong";

    // 결과 저장 경로 (서버 환경에 따라 절대경로 또는 configurable)
    private static final String FILE_PATH = "/Users/song/Downloads/news/";

    // 뉴스 카테고리별 이모지
    private static final String[] CATEGORY_EMOJIS_ARR = {"", "🏢", "💲", "👥", "🎬", "💻", "🌏"};

    /**
     * 네이버 뉴스 크롤링 스케줄러
     * 평일 09:00 ~ 17:00 매 정시에 실행
     */
    @Scheduled(cron = "0 0 9-18 ? * MON-FRI")
    public void crawlNaverNews() {

        // 실행 시간 로깅
        log.info("[스케줄 실행] 평일 시간대 헤드라인 뉴스 크롤링 시작");

        // 실행 시점 타임스탬프 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String fileName = String.format("NAVER_Headline_News_%s.txt", timestamp);
        String finalFilePath = FILE_PATH + fileName;

        // Headless 크롬 옵션 설정
        WebDriver driver = initWebDriver();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(finalFilePath, false))) {

            // 네이버 뉴스 메인 접속
            driver.get(NEWS_HOME_URL);
            log.info("{} :::::: Started!!!!!", driver.getTitle());

            // 파일 헤더 작성
            writer.write("====================\n네이버 뉴스 크롤링 결과\n====================\n\n");

            // 상단 탭 메뉴 요소 가져오기
            List<WebElement> tabMenu = driver.findElements(By.cssSelector(NEWS_TOP_MENU));
            List<WebElement> tabLinks = driver.findElements(By.cssSelector(NEWS_TOP_MENU_LINK));

            int tabMenuCount = tabMenu.size();
            
            // 탭 순회 (정치~세계까지만, 인덱스 1~6)
            for (int i = 1; i < tabMenuCount && i <= 6; i++) {

                // 탭명 및 URL
                String tabName = tabMenu.get(i).getText();
                String tabUrl = tabLinks.get(i).getDomAttribute("href");

                // 탭 클릭 후 잠시 대기
                tabLinks.get(i).click();
                Thread.sleep(300);

                // 헤드라인 섹션 로깅 및 파일 기록
                String header = String.format("%s %s 주요 헤드라인 (%s)", CATEGORY_EMOJIS_ARR[i], tabName, tabUrl);
                log.info(header);
                writer.write("----------------------------------------------------------\n" + header + "\n----------------------------------------------------------\n");

                // 헤드라인 기사 크롤링
                List<WebElement> headlines = driver.findElements(By.cssSelector(NEWS_HEADLINE_LIST));
                writeHeadlines(writer, headlines);

                writer.write("\n\n");

                // DOM 새로 로드된 후 상단 탭 요소 다시 가져오기
                tabMenu = driver.findElements(By.cssSelector(NEWS_TOP_MENU));
                tabLinks = driver.findElements(By.cssSelector(NEWS_TOP_MENU_LINK));
            }

            log.info("✅ 크롤링 결과 저장 완료: {}", finalFilePath);

        } catch (NoSuchElementException e) {
            log.error("❌ 기사 요소 파싱 실패: {}", e.getMessage());
        } catch (IOException e) {
            log.error("❌ 파일 저장 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.error("❌ 크롤링 중 오류 발생: {}", e.toString());
        } finally {
            driver.quit(); // 브라우저 세션 종료
        }
    }

    /**
     * WebDriver 초기화 (Headless 크롬)
     */
    private WebDriver initWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");             // 크롬창 없이 크롤링
        options.addArguments("--disable-gpu");              // GPU 가속 비활성화
        options.addArguments("--no-sandbox");               // Linux 환경 호환성
        options.addArguments("--disable-dev-shm-usage");    // 메모리 부족 방지

        ChromeDriverService service = new ChromeDriverService.Builder()
                .withSilent(true) // 드라이버 로그 비활성화
                .build();

        return new ChromeDriver(service, options);
    }

    /**
     * 헤드라인 기사 정보 (제목 + URL) 로그 출력 및 파일 저장
     */
    private void writeHeadlines(BufferedWriter writer, List<WebElement> headlines) throws IOException {
        int index = 1;
        for (WebElement headline : headlines) {
            try {
                WebElement linkElement = headline.findElement(By.cssSelector(NEWS_HEADLINE_A_TAG_LINK));
                String href = linkElement.getDomAttribute("href");
                String title = linkElement.findElement(By.cssSelector(NEWS_HEADLINE_TITLE)).getText();

                String articleInfo = String.format("%d. %s (%s)", index++, title, href);
                log.info(articleInfo);
                writer.write(articleInfo + "\n");
            } catch (NoSuchElementException e) {
                log.warn("⚠️ 특정 헤드라인 요소 파싱 실패: {}", e.getMessage());
            }
        }
    }
}
