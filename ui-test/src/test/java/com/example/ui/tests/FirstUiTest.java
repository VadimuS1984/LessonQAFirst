package com.example.ui.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class FirstUiTest {

    WebDriver driver;
    WebDriverWait wait;

    // static!! выполняется один раз для всего класса (БД, web driver)
    @BeforeClass
    public static void webDriverInstall() {
        WebDriverManager.chromedriver().setup();
    }

    // последовательность перед каждым тестом (переход на страницу, авторизация)
    @BeforeMethod
    public void webDriverStart() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void webDriverStop() {
        if (driver != null) {
            driver.quit(); // Используем quit() вместо close() для полного закрытия
        }
    }

    @Test
    public void testProfilePageButton() {
        driver.get("https://cinescope.t-qa.ru");

        // Ждем появления кнопки профиля
        wait.until(ExpectedConditions.elementToBeClickable(By.id("profile_page_button")));
        driver.findElement(By.id("profile_page_button")).click();

        // Ждем появления alert
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        System.out.println("Alert text: " + alertText);
        alert.accept();
    }

    @Test
    public void testHomePageLoads() {
        driver.get("https://cinescope.t-qa.ru");

        // Проверяем, что страница загрузилась
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // Проверяем заголовок страницы
        String title = driver.getTitle();
        System.out.println("Page title: " + title);

        // Проверяем URL
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
    }

    @Test
    public void testNavigationElements() {
        driver.get("https://cinescope.t-qa.ru");

        // Проверяем наличие основных элементов навигации
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("nav")));

        // Проверяем кнопку профиля
        boolean profileButtonExists = driver.findElements(By.id("profile_page_button")).size() > 0;
        System.out.println("Profile button exists: " + profileButtonExists);
    }
}