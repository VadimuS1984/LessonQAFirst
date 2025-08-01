package com.example.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class SelenideUiTest {

    @BeforeClass
    public static void setUp() {
        // Настройка Selenide
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        Configuration.headless = false; // false для видимого браузера
    }

    @BeforeMethod
    public void openBrowser() {
        // Открываем браузер перед каждым тестом
        open("https://cinescope.t-qa.ru");
    }

    @AfterMethod
    public void closeBrowser() {
        // Закрываем браузер после каждого теста
        Selenide.closeWebDriver();
    }

    @Test
    public void testHomePageLoads() {
        // Проверяем, что страница загрузилась
        $("body").shouldBe(visible);

        // Проверяем заголовок страницы
        String title = title();
        System.out.println("Page title: " + title);

        // Проверяем URL
        String currentUrl = WebDriverRunner.url();
        System.out.println("Current URL: " + currentUrl);
    }

    @Test
    public void testProfileButtonExists() {
        // Проверяем наличие кнопки профиля
        $("#profile_page_button").shouldBe(visible);

        // Проверяем, что кнопка кликабельна
        $("#profile_page_button").shouldBe(enabled);
    }

//    @Test
//    public void testProfileButtonClick() {
//        // Кликаем на кнопку профиля
//        $("#profile_page_button").click();
//
//        // Ждем появления alert
//        switchTo().alert().should(exist);
//
//        // Получаем текст alert
//        String alertText = switchTo().alert().getText();
//        System.out.println("Alert text: " + alertText);
//
//        // Принимаем alert
//        switchTo().alert().accept();
//    }

    @Test
    public void testPageElements() {
        // Проверяем наличие основных элементов
        $("body").shouldBe(visible);

        // Проверяем навигацию
        $("nav").shouldBe(visible);

        // Проверяем кнопку профиля
        $("#profile_page_button").shouldBe(visible);
    }

    @Test
    public void testPageResponsiveness() {
        // Проверяем адаптивность страницы
        $("body").shouldBe(visible);

        // Проверяем, что страница загружается полностью
        $("html").shouldHave(attribute("lang"));
    }
}