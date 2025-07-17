package com.medi.testproject.crawl;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

@Slf4j
public class TestCrawl {

    private static final String NEWS_HOME_URL = "https://news.naver.com";

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.get(NEWS_HOME_URL);
        String siteTitle = driver.getTitle();

        try {

            log.info("{} :::::: Started!!!!!",  siteTitle);

            // 상단 탭 메뉴 리스트 선택
            List<WebElement> tabMenu = driver.findElements(By.cssSelector("li.Nlist_item"));

            // 상단 탭 메뉴에 대한 링크 선택
            List<WebElement> tabLink = driver.findElements(By.cssSelector("a.Nitem_link"));

            int tebMenuCount = tabMenu.size();

            for (int i = 1; i < tebMenuCount; i++) {

                if(i > 6) break; // 뉴스-랭킹 탭 이후엔 조회 X

                WebElement targetTab = tabMenu.get(i);      // 해당 탭 선택
                WebElement targetLink = tabLink.get(i);     // 해당 탭에 대한 url

                String targetTabName = targetTab.getText();
                String targetTabLink = targetLink.getDomAttribute("href");

                log.info(" Tab " + i + " ::::: {} / {}", targetTabName, targetTabLink);

                targetLink.click();
                Thread.sleep(2000);

                // 헤드라인
                WebElement headLineTitle = driver.findElement(By.cssSelector("a.sa_head_link"));
                log.info("📰 {}", headLineTitle.getText());
                List<WebElement> headLines = driver.findElements(By.cssSelector("li.sa_item._SECTION_HEADLINE:not(.is_blind)"));

                int idx = 1;

                for(WebElement headLine : headLines) {
                    // a 태그 찾기 (href 값 가져오기)
                    WebElement linkElement = headLine.findElement(By.cssSelector("a.sa_text_title"));
                    String href = linkElement.getDomAttribute("href");

                    // strong 태그 찾기 (제목 텍스트)
                    WebElement titleElement = linkElement.findElement(By.cssSelector("strong.sa_text_strong"));
                    String title = trimTitleMax40(titleElement.getText());

                    log.info("{}. {} → {} / 제목길이 : {}", idx, title,  href, title.length());
                    idx++;
                }

                // 다시 상단 탭 리스트 새로 가져오기 (DOM 재로딩 문제 방지)
                tabMenu = driver.findElements(By.cssSelector("li.Nlist_item"));
                tabLink = driver.findElements(By.cssSelector("a.Nitem_link"));
                log.info("=======================================================\n");
            }


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
            Thread.sleep(5000); // 로그인 후 페이지 로딩 대기

            log.info("현재 페이지 URL: {}", driver.getCurrentUrl()); // 페이지 확인용 로그
            log.info("{} :::::: Ended!!!!!", siteTitle);

        } catch (Exception e) {

            log.error("{} :::::: ERROR AND EXIT!!!!!", e.toString());

        } finally {
            driver.quit();

        }

    }

    private static String trimTitleMax40(String text) {
        if (text.length() > 40) {
            return text.substring(0, 40) + "...";
        }
        return text;
    }

}
