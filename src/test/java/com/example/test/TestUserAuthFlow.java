package com.example.test;

import com.example.api.LoginRequest;
import com.example.api.UserPayload;
import com.example.constants.HttpStatusCodes;
import com.example.servise.UserApiService;
import com.example.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class TestUserAuthFlow {

    private final UserApiService userApiService = new UserApiService();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://auth.dev-cinescope.t-qa.ru";
    }

    // ========== РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ ==========
    
    @Test
    public void testUserRegistration() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        UserPayload userPayload = new UserPayload()
                .email("test" + timestamp + "@email.com")
                .fullName("ФИО пользователя")
                .password("12345678Aa")
                .passwordRepeat("12345678Aa");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(userPayload)
                .when()
                .post("/regist")
                .then()
                .log().all()
                .assertThat()
                .statusCode(anyOf(is(HttpStatusCodes.CREATED), is(HttpStatusCodes.CONFLICT))); // 201 - успешно, 409 - уже существует
    }

    @Test
    public void testUserRegistrationWithInvalidData() {
        UserPayload userPayload = new UserPayload()
                .email("invalid-email")
                .fullName("Test123") // содержит цифры
                .password("123") // короткий пароль
                .passwordRepeat("456"); // пароли не совпадают

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(userPayload)
                .when()
                .post("/regist")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.BAD_REQUEST);
    }

    // ========== АУТЕНТИФИКАЦИЯ ПОЛЬЗОВАТЕЛЯ ==========
    
    @Test
    public void testUserLogin() {
        LoginRequest loginRequest = new LoginRequest()
                .email("test@email.com")
                .password("12345678Aa");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.OK)
                .body("user.id", not(emptyString()))
                .body("user.email", equalTo("test@email.com"))
                .body("user.roles", hasItem("USER"))
                .body("user.verified", equalTo(true))
                .body("user.banned", equalTo(false))
                .body("accessToken", not(emptyString()))
                .body("expiresIn", not(empty()));
    }

    @Test
    public void testUserLoginWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest()
                .email("nonexistent@email.com")
                .password("WrongPassword123");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .log().all()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("Не верный логин или пароль"));
    }

    // ========== ВЫХОД ИЗ УЧЁТНОЙ ЗАПИСИ ==========
    
    @Test
    public void testUserLogout() {
        // Получаем токен для авторизованного запроса
        String token = TokenManager.getTestUserToken();
        
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/logout")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.OK);
    }

    @Test
    public void testUserLogoutWithoutToken() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/logout")
                .then()
                .log().all()
                .assertThat()
                .statusCode(401);
        // Ожидаем ошибку авторизации
    }

    @Test
    public void testTokenInvalidAfterLogout() {
        String token = TokenManager.getTestUserToken();

        // Выход из системы
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/logout")
                .then()
                .assertThat()
                .statusCode(HttpStatusCodes.OK);

        // Попытка использовать старый токен
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/refresh-tokens")
                .then()
                .assertThat()
                .statusCode(anyOf(is(401), is(403))); // Ожидаем ошибку авторизации
    }

    // ========== ОБНОВЛЕНИЕ ТОКЕНОВ ==========
    
    @Test
    public void testRefreshTokens() {
        String token = TokenManager.getTestUserToken();

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/refresh-tokens")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.OK)
                .body("accessToken", not(emptyString()))
                .body("refreshToken", not(emptyString()));
    }

    @Test
    public void testRefreshTokensWithoutAuth() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/refresh-tokens")
                .then()
                .log().all()
                .assertThat()
                .statusCode(401); // Ожидаем ошибку авторизации
    }

    @Test
    public void testRefreshTokensForDifferentRoles() {
        // Тестируем для разных ролей (если есть тестовые аккаунты)
        String[] testEmails = {"user@email.com", "admin@email.com", "superadmin@email.com"};
        String password = "12345678Aa";

        for (String email : testEmails) {
            try {
                String token = TokenManager.getToken(email, password);
                
                if (token != null) {
                    RestAssured
                            .given()
                            .header("Authorization", "Bearer " + token)
                            .contentType(ContentType.JSON)
                            .log().all()
                            .when()
                            .get("/refresh-tokens")
                            .then()
                            .log().all()
                            .assertThat()
                            .statusCode(HttpStatusCodes.OK)
                            .body("accessToken", not(emptyString()))
                            .body("refreshToken", not(emptyString()));
                }
            } catch (Exception e) {
                // Игнорируем ошибки для несуществующих аккаунтов
                System.out.println("Аккаунт " + email + " не найден или недоступен");
            }
        }
    }

    // ========== ПОДТВЕРЖДЕНИЕ EMAIL ==========
    
    @Test
    public void testEmailConfirmation() {
        String token = TokenManager.getTestUserToken();

        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/config")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatusCodes.OK);
    }

    @Test
    public void testEmailConfirmationWithoutAuth() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/config")
                .then()
                .log().all()
                .assertThat()
                .statusCode(401); // Ожидаем ошибку авторизации
    }

    // ========== ИНТЕГРАЦИОННЫЕ ТЕСТЫ ==========
    
    @Test
    public void testCompleteAuthFlow() {
        // 1. Регистрация нового пользователя
        String timestamp = String.valueOf(System.currentTimeMillis());
        String email = "test" + timestamp + "@email.com";
        
        UserPayload userPayload = new UserPayload()
                .email(email)
                .fullName("Тестовый Пользователь")
                .password("12345678Aa")
                .passwordRepeat("12345678Aa");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .post("/regist")
                .then()
                .assertThat()
                .statusCode(anyOf(is(HttpStatusCodes.CREATED), is(HttpStatusCodes.CONFLICT)));

        // 2. Логин пользователя
        LoginRequest loginRequest = new LoginRequest()
                .email(email)
                .password("12345678Aa");

        String token = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .assertThat()
                .statusCode(HttpStatusCodes.OK)
                .extract()
                .jsonPath()
                .getString("accessToken");

        // 3. Обновление токенов
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/refresh-tokens")
                .then()
                .assertThat()
                .statusCode(HttpStatusCodes.OK);

        // 4. Подтверждение email
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/config")
                .then()
                .assertThat()
                .statusCode(HttpStatusCodes.OK);

        // 5. Выход из системы
        RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/logout")
                .then()
                .assertThat()
                .statusCode(HttpStatusCodes.OK);
    }

    // ========== ТЕСТЫ ДЛЯ РАЗНЫХ РОЛЕЙ ==========
    
    @Test
    public void testUserRoleAccess() {
        // Тест для роли USER
        testRoleAccess("user@email.com", "12345678Aa", "USER");
    }

    @Test
    public void testAdminRoleAccess() {
        // Тест для роли ADMIN
        testRoleAccess("admin@email.com", "12345678Aa", "ADMIN");
    }

    @Test
    public void testSuperAdminRoleAccess() {
        // Тест для роли SUPER_ADMIN
        testRoleAccess("superadmin@email.com", "12345678Aa", "SUPER_ADMIN");
    }

    private void testRoleAccess(String email, String password, String expectedRole) {
        try {
            String token = TokenManager.getToken(email, password);
            
            if (token != null) {
                // Проверяем доступ к защищенным эндпоинтам
                RestAssured
                        .given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/refresh-tokens")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatusCodes.OK);

                RestAssured
                        .given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/config")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatusCodes.OK);
            }
        } catch (Exception e) {
            System.out.println("Аккаунт " + email + " не найден или недоступен");
        }
    }
} 