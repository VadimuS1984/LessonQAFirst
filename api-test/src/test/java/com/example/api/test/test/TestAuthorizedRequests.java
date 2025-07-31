package com.example.api.test.test;

import com.example.api.api.UserPayload;
import com.example.api.constants.HttpStatusCodes;
import com.example.api.servise.UserApiService;
import com.example.api.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class TestAuthorizedRequests {

    private final UserApiService userApiService = new UserApiService();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://auth.cinescope.t-qa.ru";
    }

    @Test
    public void testGetUserProfileWithToken() {
        // Получаем токен авторизации
        String token = TokenManager.getTestUserToken();

        // Проверяем, что токен получен
        assert token != null : "Токен авторизации не получен";

        // Выполняем авторизованный запрос к профилю пользователя
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/profile")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND)
                .body("email", not(emptyString()));
    }

    @Test
    public void testGetUserProfileUsingService() {
        // Используем метод сервиса для авторизованного запроса
        userApiService.authorizedGet("/profile")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND)
                .body("email", not(emptyString()));
    }

    @Test
    public void testUpdateUserProfileWithToken() {
        // Данные для обновления профиля
        UserPayload updatePayload = new UserPayload()
                .fullName("Обновленное Имя")
                .email("updated@email.com");

        // Выполняем авторизованный PUT запрос
        userApiService.authorizedPut("/profile", updatePayload)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND)
                .body("fullName", equalTo("Обновленное Имя"));
    }

    @Test
    public void testCreateUserPostWithToken() {
        // Данные для создания поста
        String postData = "{\"title\":\"Тестовый пост\",\"content\":\"Содержание поста\"}";

        // Выполняем авторизованный POST запрос
        userApiService.authorizedPost("/posts", postData)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND)
                .body("title", equalTo("Тестовый пост"));
    }

    @Test
    public void testDeleteUserPostWithToken() {
        // Выполняем авторизованный DELETE запрос
        userApiService.authorizedDelete("/posts/123")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND);
    }

    @Test
    public void testTokenReuse() {
        // Получаем токен первый раз
        String token1 = TokenManager.getTestUserToken();

        // Получаем токен второй раз (должен быть тот же самый)
        String token2 = TokenManager.getTestUserToken();

        // Проверяем, что токены одинаковые (переиспользование)
        assert token1.equals(token2) : "Токены должны быть одинаковыми";

        // Выполняем запрос с переиспользованным токеном
        userApiService.authorizedGet("/profile")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND);
    }

    @Test
    public void testTokenRefresh() {
        // Очищаем токен
        TokenManager.clearToken();

        // Проверяем, что токен очищен
        assert !TokenManager.hasToken() : "Токен должен быть очищен";

        // Получаем новый токен
        String newToken = TokenManager.getTestUserToken();
        assert newToken != null : "Новый токен должен быть получен";

        // Выполняем запрос с новым токеном
        userApiService.authorizedGet("/profile")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND);
    }

    @Test
    public void testMultipleAuthorizedRequests() {
        // Выполняем несколько авторизованных запросов подряд
        // Токен должен переиспользоваться автоматически

        // Запрос 1: Получение профиля
        userApiService.authorizedGet("/profile")
                .then()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND);

        // Запрос 2: Получение настроек
        userApiService.authorizedGet("/settings")
                .then()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND);

        // Запрос 3: Получение истории
        userApiService.authorizedGet("/history")
                .then()
                .assertThat()
                .statusCode(HttpStatusCodes.NOT_FOUND);
    }
}
