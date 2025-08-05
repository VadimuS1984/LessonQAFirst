package com.example.ui.tests;

import com.codeborne.selenide.Selenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.codeborne.selenide.Configuration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginFlowTest
{
    private static String authToken;

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
    public void testProfileButtonNavigation() {
        // Проверяем наличие кнопки профиля
        $("[data-testid='profile-button'], .profile-button, #profile-btn").shouldBe(visible);

        // Кликаем на кнопку профиля
        $("[data-testid='profile-button'], .profile-button, #profile-btn").click();

        // Проверяем, что перешли на страницу логина
        $("input[type='email'], input[name='email'], #email").shouldBe(visible);
        $("input[type='password'], input[name='password'], #password").shouldBe(visible);
    }

    @Test
    public void testLoginFormValidation() {
        // Переходим на страницу логина
        $("[data-testid='profile-button'], .profile-button, #profile-btn").click();

        // Проверяем наличие полей формы
        $("input[type='email'], input[name='email'], #email").shouldBe(visible);
        $("input[type='password'], input[name='password'], #password").shouldBe(visible);
        $("button[type='submit'], input[type='submit'], .login-btn").shouldBe(visible);
    }

    @Test
    public void testSuccessfulLogin() {
        // Переходим на страницу логина
        $("[data-testid='profile-button'], .profile-button, #profile-btn").click();

        // Заполняем форму
        $("input[type='email'], input[name='email'], #email").setValue("test@example.com");
        $("input[type='password'], input[name='password'], #password").setValue("password123");

        // Отправляем форму
        $("button[type='submit'], input[type='submit'], .login-btn").click();

        // Проверяем успешную авторизацию
        $(".user-profile, .profile-info, [data-testid='user-info']").shouldBe(visible);

        // Сохраняем токен для последующих тестов
        authToken = getAuthToken();
        System.out.println("Auth token saved: " + authToken);
    }

    @Test
    public void testInvalidLogin() {
        // Переходим на страницу логина
        $("[data-testid='profile-button'], .profile-button, #profile-btn").click();

        // Заполняем форму неверными данными
        $("input[type='email'], input[name='email'], #email").setValue("invalid@example.com");
        $("input[type='password'], input[name='password'], #password").setValue("wrongpassword");

        // Отправляем форму
        $("button[type='submit'], input[type='submit'], .login-btn").click();

        // Проверяем сообщение об ошибке
        $(".error-message, .alert-error, [data-testid='error-message']").shouldBe(visible);
    }

    @Test
    public void testAuthorizedUserAccess() {
        // Проверяем, что пользователь авторизован
        if (authToken != null) {
            // Устанавливаем токен в localStorage или cookies
            executeJavaScript("localStorage.setItem('authToken', '" + authToken + "')");

            // Перезагружаем страницу
            refresh();

            // Проверяем, что видим профиль пользователя
            $(".user-profile, .profile-info, [data-testid='user-info']").shouldBe(visible);
        }
    }

    private String getAuthToken() {
        // Получаем токен из localStorage или cookies
        return executeJavaScript("return localStorage.getItem('authToken') || document.cookie.match(/authToken=([^;]+)/)?.[1]");
    }
}