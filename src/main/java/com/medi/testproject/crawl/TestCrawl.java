package com.medi.testproject.crawl;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class TestCrawl {

    private static final String NEWS_HOME_URL = "https://news.naver.com";
    private static final String NEWS_TOP_MENU = "li.Nlist_item";
    private static final String NEWS_TOP_MENU_LINK = "a.Nitem_link";
    private static final String NEWS_HEADLINE_LIST = "li.sa_item._SECTION_HEADLINE:not(.is_blind)";
    private static final String NEWS_HEADLINE_A_TAG_LINK = "a.sa_text_title";
    private static final String NEWS_HEADLINE_TITLE = "strong.sa_text_strong";

    // 결과 저장할 파일 경로
    private static final String filePath = "/Users/song/Downloads/";

    public static void main(String[] args) {

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String regDttm = ldt.format(dtf);

        WebDriver driver = new ChromeDriver();
        driver.get(NEWS_HOME_URL);
        String siteTitle = driver.getTitle();

        String fileName = "NAVER_Headline_News_" + regDttm + ".txt";
        String finalFilePath = filePath + fileName;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(finalFilePath, false))) {

            log.info("{} :::::: Started!!!!!",  siteTitle);
            writer.write("====================\n네이버 뉴스 크롤링 결과\n====================\n\n");


            // 상단 탭 메뉴 리스트 선택
            List<WebElement> tabMenu = driver.findElements(By.cssSelector(NEWS_TOP_MENU));

            // 상단 탭 메뉴에 대한 링크 선택
            List<WebElement> tabLink = driver.findElements(By.cssSelector(NEWS_TOP_MENU_LINK));

            int tabMenuCount = tabMenu.size();

            // 헤드라인 이모지
            String[] emojiArr = new String[]{"", "🏢", "💲", "👥", "🎬", "💻", "🌏"};

            for (int i = 1; i < tabMenuCount; i++) {

                if(i > 6) break; // 뉴스-랭킹 탭 이후엔 조회 X

                WebElement targetTab = tabMenu.get(i);      // 해당 탭 선택
                WebElement targetLink = tabLink.get(i);     // 해당 탭에 대한 url

                String targetTabName = targetTab.getText();
                String targetTabLink = targetLink.getDomAttribute("href");

                targetLink.click();
                Thread.sleep(500);
                
                // 헤드라인
                String headlineInfo = String.format("%s %s 주요 헤드라인(%s)", emojiArr[i], targetTabName, targetTabLink);
                log.info(headlineInfo);
                writer.write("----------------------------------------------------------\n" + headlineInfo + "\n----------------------------------------------------------\n");

                List<WebElement> headLines = driver.findElements(By.cssSelector(NEWS_HEADLINE_LIST));
                int idx = 1;

                for(WebElement headLine : headLines) {
                    // a 태그 찾기 (href 값 가져오기)
                    WebElement linkElement = headLine.findElement(By.cssSelector(NEWS_HEADLINE_A_TAG_LINK));
                    String href = linkElement.getDomAttribute("href");

                    // strong 태그 찾기 (제목 텍스트)
                    WebElement titleElement = linkElement.findElement(By.cssSelector(NEWS_HEADLINE_TITLE));
                    String headLineTitle = titleElement.getText();

                    String articleInfo = String.format("%d. %s(%s)", idx, headLineTitle, href);
                    log.info(articleInfo);
                    writer.write(articleInfo + "\n");
                    idx++;

                }

                writer.write("\n\n");

                // 다시 상단 탭 리스트 새로 가져오기 (DOM 재로딩 문제 방지)
                tabMenu = driver.findElements(By.cssSelector(NEWS_TOP_MENU));
                tabLink = driver.findElements(By.cssSelector(NEWS_TOP_MENU_LINK));
            }

            log.info("크롤링 결과가 \"{}\" 경로에 저장되었습니다.", finalFilePath);


//            // 로그인 버튼 찾기
//            WebElement loginButton = driver.findElement(By.cssSelector("#gnb_login_button"));
//
//            // 로그인 버튼 클릭
//            loginButton.click();
//            Thread.sleep(3000);
//
//            // 아이디 입력
//            WebElement idField = driver.findElement(By.id("id"));
//            idField.sendKeys("thdwlgns113");
//
//            // 비밀번호 입력
//            WebElement pwField = driver.findElement(By.id("pw"));
//            pwField.sendKeys("hooney90#");
//
//            // 로그인 버튼 클릭
//            WebElement submitButton = driver.findElement(By.id("log.login"));
//            submitButton.click();
//
            Thread.sleep(2000); // 페이지 로딩 대기


        } catch (NoSuchElementException e) {
            log.error("기사 파싱 실패: {}", e.getMessage());
        } catch (IOException e) {
            log.error("파일 저장 오류: {}", e.getMessage());
        } catch (Exception e) {
            log.error("{} :::::: ERROR AND EXIT!!!!!", e.toString());
        }finally {
            driver.quit();

        }

    }

}
