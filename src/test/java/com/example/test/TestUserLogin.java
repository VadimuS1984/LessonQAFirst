package com.example.test;

import com.example.api.LoginRequest;
import com.example.conditeons.Conditions;
import com.example.constants.HttpStatusCodes;
import com.example.servise.UserApiService;
import com.example.utils.TokenManager;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class TestUserLogin {

    private final UserApiService userApiService = new UserApiService();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://auth.dev-cinescope.t-qa.ru";
    }

    @Test
    public void testCanLoginWithValidCredentials() {

        //given - payload (данные для запроса)
        LoginRequest loginRequest = new LoginRequest()
                .email("testVP1@email.com")
                .password("Test12345");
        // expect
        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.OK))
                .shouldHave(Conditions.bodyField("user.id", not(emptyString())))
                .shouldHave(Conditions.bodyField("user.email", equalTo("testVP1@email.com")))
                .shouldHave(Conditions.bodyField("user.roles", hasItem("USER")))
                .shouldHave(Conditions.bodyField("user.verified", equalTo(true)))
                .shouldHave(Conditions.bodyField("user.banned", equalTo(false)))
                .shouldHave(Conditions.bodyField("accessToken", not(emptyString())))
                .shouldHave(Conditions.bodyField("expiresIn", not(empty())));
    }

    @Test
    public void testTokenManagerCanGetToken() {
        // Тестируем получение токена через TokenManager
        String token = TokenManager.getToken("test@email.com", "12345678Aa");

        // Проверяем, что токен получен
        assert token != null : "Токен должен быть получен";
        assert !token.isEmpty() : "Токен не должен быть пустым";

        // Проверяем, что токен сохранен в TokenManager
        assert TokenManager.hasToken() : "Токен должен быть сохранен в TokenManager";

        // Проверяем, что заголовок авторизации формируется правильно
        String authHeader = TokenManager.getAuthorizationHeader();
        assert authHeader != null : "Заголовок авторизации должен быть сформирован";
        assert authHeader.startsWith("Bearer ") : "Заголовок должен начинаться с 'Bearer '";
    }

    @Test
    public void testTokenManagerReuse() {
        // Получаем токен первый раз
        String token1 = TokenManager.getTestUserToken();

        // Получаем токен второй раз (должен быть тот же самый)
        String token2 = TokenManager.getTestUserToken();

        // Проверяем, что токены одинаковые (переиспользование)
        assert token1.equals(token2) : "Токены должны быть одинаковыми при переиспользовании";
    }

    @Test
    public void testTokenManagerClear() {
        // Получаем токен
        String token = TokenManager.getTestUserToken();
        assert token != null : "Токен должен быть получен";

        // Очищаем токен
        TokenManager.clearToken();

        // Проверяем, что токен очищен
        assert !TokenManager.hasToken() : "Токен должен быть очищен";

        // Получаем новый токен
        String newToken = TokenManager.getTestUserToken();
        assert newToken != null : "Новый токен должен быть получен";
        assert !token.equals(newToken) : "Новый токен должен отличаться от старого";
    }

    @Test
    public void testCanLoginWithRandomValidCredentials() {
        // Генерируем случайные валидные данные
        String randomEmail = "test" + RandomStringUtils.randomNumeric(3) + "@email.com";
        String validPassword = "Test12345";

        LoginRequest loginRequest = new LoginRequest()
                .email(randomEmail)
                .password(validPassword);

        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.UNAUTHORIZED)) // Ожидаем 401, так как пользователь не зарегистрирован
                .shouldHave(Conditions.bodyField("message", equalTo("Не верный логин или пароль")));
    }

    @Test
    public void testCannotLoginWithInvalidEmail() {
        LoginRequest loginRequest = new LoginRequest()
                .email("invalid-email")
                .password("12345678Aa");
        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.BAD_REQUEST))
                .shouldHave(Conditions.bodyField("message", equalTo("Неверные данные")));
    }

    @Test
    public void testCannotLoginWithShortPassword() {
        LoginRequest loginRequest = new LoginRequest()
                .email("test@email.com")
                .password("123");

        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.BAD_REQUEST))
                .shouldHave(Conditions.bodyField("message", equalTo("Неверные данные")));
    }

    @Test
    public void testCannotLoginWithWrongPassword() {
        LoginRequest loginRequest = new LoginRequest()
                .email("test@email.com")
                .password("WrongPassword123");
        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.UNAUTHORIZED))
                .shouldHave(Conditions.bodyField("message", equalTo("Не верный логин или пароль")));
    }

    @Test
    public void testCannotLoginWithNonExistentUser() {
        LoginRequest loginRequest = new LoginRequest()
                .email("nonexistent@email.com")
                .password("12345678Aa");

        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.NOT_FOUND))
                .shouldHave(Conditions.bodyField("message", equalTo("Пользователь не найден")));
    }

    @Test
    public void testCannotLoginWithEmptyCredentials() {
        LoginRequest loginRequest = new LoginRequest()
                .email("")
                .password("");

        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.BAD_REQUEST))
                .shouldHave(Conditions.bodyField("message", equalTo("Неверные данные")));
    }

    @Test
    public void testCannotLoginWithNullCredentials() {
        // Создаем payload без установки email и password (null значения)
        LoginRequest loginRequest = new LoginRequest();

        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.BAD_REQUEST))
                .shouldHave(Conditions.bodyField("message", equalTo("Неверные данные")));
    }

    @Test
    public void testLoginResponseStructure() {
        LoginRequest loginRequest = new LoginRequest()
                .email("test@email.com")
                .password("12345678Aa");

        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.OK))
                .shouldHave(Conditions.bodyField("user", notNullValue()))
                .shouldHave(Conditions.bodyField("user.id", not(emptyString())))
                .shouldHave(Conditions.bodyField("user.email", not(emptyString())))
                .shouldHave(Conditions.bodyField("user.roles", not(empty())))
                .shouldHave(Conditions.bodyField("user.verified", notNullValue()))
                .shouldHave(Conditions.bodyField("user.banned", notNullValue()))
                .shouldHave(Conditions.bodyField("accessToken", not(emptyString())))
                .shouldHave(Conditions.bodyField("expiresIn", not(empty())));
    }

    @Test
    public void testLoginWithUnverifiedUser() {
        // Тест для пользователя, который не подтвердил email
        LoginRequest loginRequest = new LoginRequest()
                .email("unverified@email.com")
                .password("12345678Aa");
        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.FORBIDDEN))
                .shouldHave(Conditions.bodyField("message", equalTo("Пользователь не подтверждён")));
    }

    @Test
    public void testLoginWithBannedUser() {
        // Тест для заблокированного пользователя
        LoginRequest loginRequest = new LoginRequest()
                .email("banned@email.com")
                .password("12345678Aa");
        userApiService.loginUser(loginRequest)
                .shouldHave(Conditions.statusCode(HttpStatusCodes.FORBIDDEN))
                .shouldHave(Conditions.bodyField("message", equalTo("Пользователь заблокирован")));
    }
}
