package com.example.api.test.test;


import com.example.api.api.UserPayload;
import com.example.api.assertions.AssertableResponse;
import com.example.api.conditeons.Conditions;
import com.example.api.constants.HttpStatusCodes;
import com.example.api.servise.UserApiService;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;


public class TestUserRegistration {

    private final UserApiService userApiService = new UserApiService();
//private final Faker
        @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://auth.dev-cinescope.t-qa.ru";
    }

    /**
     * Вспомогательный метод для регистрации пользователя через RestAssured
     * @param userPayload данные пользователя
     * @param expectedStatusCode ожидаемый статус-код
     * @return AssertableResponse с результатом запроса
     */
    private AssertableResponse registerUser(UserPayload userPayload, int expectedStatusCode) {
        return userApiService.registerUser(userPayload)
                .shouldHave(Conditions.statusCode(expectedStatusCode));
    }

    @Test
    public void testCanRegisterNewUser() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "testVP" + timestamp + "@mail.com";
        // fixme дисерилизация два поля -????
        // fixme алюр? статика отчет по тестам ТЕСТ РЕПОРТ в браузере
        // fixme метод лоя токина, тест на ЛОГ возвращяет токин и его в заголовок авторезейшен
        //given - payload (данные для запроса)
        UserPayload userPayload = new UserPayload()
                .fullName("Вадим Порохов")  // Только буквы и пробелы
                .email("testVP1@mail.com")
//                .email(uniqueEmail) // Уникальное имя
                .password("Test12345")     // Минимум 8 символов, заглавная буква
                .passwordRepeat("Test12345"); // Пароли должны совпадать

        // 1. Первая регистрация - должна быть успешной
        registerUser(userPayload, HttpStatusCodes.CREATED)
                .shouldHave(Conditions.bodyField("id", not(emptyString())))
                .shouldHave(Conditions.bodyField("email", equalTo("testVP1@mail.com")));

        // 2. Повторная регистрация с тем же email - должна вернуть конфликт
        // Ожидаем конфликт, так как email уже существует
        registerUser(userPayload, HttpStatusCodes.CONFLICT);

//        userApiService.registerUser(userPayload)
////                .shouldHave(Condirions.statusCode(200)) - fixme
//                .shouldHave(Conditions.statusCode(409))
//                .shouldHave(Conditions.bodyField("id", not(emptyString())));
    }
    @Test
    public void testCanRegisterNewUserWithRandomName() {
        // Генерируем случайное имя только из букв
        String randomName = RandomStringUtils.randomAlphabetic(5) + " " +
                RandomStringUtils.randomAlphabetic(6);
        //given
        UserPayload userPayload = new UserPayload()
                .fullName(randomName)
                .email("test" + RandomStringUtils.randomNumeric(3) + "@mail.com")
                .password("Test12345")
                .passwordRepeat("Test12345");
        // expect
        // Ожидаем успешное создание
        registerUser(userPayload, HttpStatusCodes.CREATED)
                .shouldHave(Conditions.bodyField("id", not(emptyString())));

//        userApiService.registerUser(userPayload)
//                .shouldHave(new StatusCodeCondition(201));
//        //                .shouldHave(Condirions.bodyField("id", not(emptyString())));

    }


    @Test
    public void testCanRegisterNewUserWithValidData() {
        String timestamp = String.valueOf(System.currentTimeMillis());

        //given
        UserPayload userPayload = new UserPayload()
                .fullName("Тест Пользователь")
                .email("test" + timestamp + "@example.com")  // Уникальный email
                .password("ValidPass123")  // 12 символов, заглавная буква, цифры
                .passwordRepeat("ValidPass123");
        // expect
        // Ожидаем успешное создание
        registerUser(userPayload, HttpStatusCodes.CREATED)
                .shouldHave(Conditions.bodyField("id", not(emptyString())));
    }

    //Альтернативный вариант с валидацией ошибок:
    @Test
    public void testRegistrationValidation() {
        UserPayload userPayload = new UserPayload()
                .fullName("Test123")  // Неправильное имя (содержит цифры)
                .email("test@mail.com")
                .password("test")      // Неправильный пароль (короткий, без заглавной)
                .passwordRepeat("test123"); // Пароли не совпадают

        // Ожидаем ошибку валидации
        registerUser(userPayload, HttpStatusCodes.BAD_REQUEST)
                .shouldHave(Conditions.bodyField("message", hasItems(
                        "Поле ФИО должно содержать только буквы и пробелы",
                        "Пароль должен содержать хотя бы одну заглавную букву",
                        "Минимальная длина пароля 8 символов",
                        "Пароли не совпадают"
                )));
    }
    // Использование утилитного метода: (смотри класс TestDataGenerator)
    @Test
    public void testCanRegisterNewUserWithHelper() {

        // Используем TestDataGenerator для создания валидных данных
        UserPayload userPayload = TestDataGenerator.createValidUserPayload();

        // Ожидаем успешное создание
        registerUser(userPayload, HttpStatusCodes.CREATED)
                .shouldHave(Conditions.bodyField("id", not(emptyString())));
    }
}


