package com.medi.testproject.crawl;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
public class TestCrawl {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.get("https://news.naver.com");
        String siteTitle = driver.getTitle();

        try {

            log.info("{} :::::: Started!!!!!",  siteTitle);

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
//            Thread.sleep(5000); // 로그인 후 페이지 로딩 대기

            log.info("현재 페이지 URL: {}", driver.getCurrentUrl()); // 로그인 페이지 확인용 로그
            log.info("{} :::::: Ended!!!!!", siteTitle);

        } catch (Exception e) {

            throw new RuntimeException(e);
        } finally {
            driver.quit();

        }

    }

}
