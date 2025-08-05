package com.example.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class AuthorizedUserTest {

    @BeforeClass
    public static void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        Configuration.headless = false;
    }

    @BeforeMethod
    public void openBrowser() {
        open("https://cinescope.t-qa.ru");
    }

    @AfterMethod
    public void closeBrowser() {
        Selenide.closeWebDriver();
    }

    @Test
    public void testAuthorizedUserProfile() {
        // Проверяем, что пользователь авторизован
        $(".user-profile, .profile-info, [data-testid='user-info']").shouldBe(visible);

        // Проверяем наличие кнопки профиля
        $("[data-testid='login_page_button'], .profile-button, #profile-btn").shouldBe(visible);

        // Кликаем на профиль авторизованного пользователя
        $("[data-testid='login_page_button'], .profile-button, #profile-btn").click();

        // Проверяем, что видим информацию профиля, а не форму логина
        $(".profile-details, .user-info, [data-testid='login_page_button']").shouldBe(visible);
        $("input[type='email']").shouldNotBe(visible); // Форма логина не должна быть видна
    }

    @Test
    public void testUserLogout() {
        // Проверяем, что пользователь авторизован
        $(".user-profile, .profile-info, [data-testid='user-info']").shouldBe(visible);

        // Открываем меню профиля
        $("[data-testid='profile-button'], .profile-button, #profile-btn").click();

        // Ищем кнопку выхода
        $("[data-testid='logout-button'], .logout-btn, #logout").click();

        // Проверяем, что вышли из системы
        $(".login-form, [data-testid='login-form']").shouldBe(visible);
        $(".user-profile, .profile-info, [data-testid='user-info']").shouldNotBe(visible);
    }

    @Test
    public void testUserSettings() {
        // Проверяем, что пользователь авторизован
        $(".user-profile, .profile-info, [data-testid='user-info']").shouldBe(visible);

        // Открываем профиль
        $("[data-testid='profile-button'], .profile-button, #profile-btn").click();

        // Переходим в настройки
        $("[data-testid='settings-link'], .settings-link, #settings").click();

        // Проверяем, что находимся на странице настроек
        $(".settings-page, [data-testid='settings-page']").shouldBe(visible);
    }
}
