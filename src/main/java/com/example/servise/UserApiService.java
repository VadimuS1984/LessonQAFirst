package com.example.servise;

import com.example.api.LoginRequest;
import com.example.api.UserPayload;
import com.example.assertions.AssertableResponse;
import com.example.utils.TokenManager;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserApiService extends ApiServise {

    private TokenManager tokenManager;

    /**
     * Конструктор с явным указанием TokenManager
     * @param tokenManager менеджер токенов
     */
    public UserApiService(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    /**
     * Регистрирует нового пользователя
     * @param userPayload данные пользователя
     * @return AssertableResponse с результатом запроса
     */
    public AssertableResponse registerUser(UserPayload userPayload) {
        return new AssertableResponse(setup()
                .body(userPayload)
                .when()
                .post("/register"));
    }

    /**
     * Выполняет вход пользователя
     * @param loginRequest данные для входа
     * @return AssertableResponse с результатом запроса
     */
    public AssertableResponse loginUser(LoginRequest loginRequest) {
        return new AssertableResponse(setup()
                .body(loginRequest)
                .when()
                .post("/login"));
    }

    /**
     * Выполняет авторизованный запрос с токеном
     * @param endpoint эндпоинт для запроса
     * @return Response объект
     */
    public Response authorizedGet(String endpoint) {
        return setupWithAuth(tokenManager.getTestUserToken())
                .when()
                .get(endpoint);
    }

    /**
     * Выполняет авторизованный POST запрос с токеном
     * @param endpoint эндпоинт для запроса
     * @param body тело запроса
     * @return Response объект
     */
    public Response authorizedPost(String endpoint, Object body) {
        return setupWithAuth(tokenManager.getTestUserToken())
                .body(body)
                .when()
                .post(endpoint);
    }

    /**
     * Выполняет авторизованный PUT запрос с токеном
     * @param endpoint эндпоинт для запроса
     * @param body тело запроса
     * @return Response объект
     */
    public Response authorizedPut(String endpoint, Object body) {
        return setupWithAuth(tokenManager.getTestUserToken())
                .body(body)
                .when()
                .put(endpoint);
    }

    /**
     * Выполняет авторизованный DELETE запрос с токеном
     * @param endpoint эндпоинт для запроса
     * @return Response объект
     */
    public Response authorizedDelete(String endpoint) {
        return setupWithAuth(tokenManager.getTestUserToken())
                .when()
                .delete(endpoint);
    }
}
