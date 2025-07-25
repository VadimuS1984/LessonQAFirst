package com.example.utils;

import com.example.api.LoginRequest;
import com.example.api.LoginResponse;
import com.example.servise.UserApiService;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenManager {
    private static String currentToken;
    private static final UserApiService userApiService = new UserApiService();

    /**
     * Получает токен авторизации для указанных учетных данных
     * @param email email пользователя
     * @param password пароль пользователя
     * @return токен авторизации или null если авторизация не удалась
     */
    public static String getToken(String email, String password) {
        LoginRequest loginRequest = new LoginRequest()
                .email(email)
                .password(password);

        try {
            var response = userApiService.loginUser(loginRequest);
            if (response.response().getStatusCode() == 200) {
                currentToken = response.response().jsonPath().getString("accessToken");
                return currentToken;
            }
        } catch (Exception e) {
            // Логируем ошибку, но не падаем
            System.err.println("Ошибка при получении токена: " + e.getMessage());
        }

        return null;
    }

    /**
     * Получает токен для тестового пользователя
     * @return токен авторизации
     */
    public static String getTestUserToken() {
        if (currentToken == null) {
            currentToken = getToken("test@email.com", "12345678Aa");
        }
        return currentToken;
    }

    /**
     * Получает заголовок авторизации для использования в запросах
     * @return строка заголовка Authorization
     */
    public static String getAuthorizationHeader() {
        String token = getTestUserToken();
        return token != null ? "Bearer " + token : null;
    }

    /**
     * Очищает текущий токен (например, при выходе из системы)
     */
    public static void clearToken() {
        currentToken = null;
    }

    /**
     * Проверяет, есть ли активный токен
     * @return true если токен существует
     */
    public static boolean hasToken() {
        return currentToken != null;
    }
}
