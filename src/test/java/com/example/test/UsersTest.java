package com.example.test;


import com.example.api.UserPayload;
import com.example.servise.UserApiService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;


public class UsersTest {

    private final UserApiService userApiService = new UserApiService();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://auth.cinescope.t-qa.ru";
    }

    @Test
    public void testCanRegisterNewUser() {
        String timestamp = String.valueOf(System.currentTimeMillis()); //????

        //given - данные подготавливаем
        UserPayload userPayload = new UserPayload()
                .fullName("Иван Иванов")  // Только буквы и пробелы
                .email("test@mail.com")
                .password("Test12345")     // Минимум 8 символов, заглавная буква
                .passwordRepeat("Test12345"); // Пароли должны совпадать

        Response response = userApiService.registerUser(userPayload);

        response
//                .shouldHave()
                .then()
                .log().all()
                .assertThat()
                .statusCode(409)
                .body("id", not(emptyString())); // id не пустое поле
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
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(userPayload)
                .when()
                .post("/register")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("id", not(emptyString()));
    }


    @Test
    public void testCanRegisterNewUserWithValidData() {
        String timestamp = String.valueOf(System.currentTimeMillis());

        UserPayload userPayload = new UserPayload()
                .fullName("Тест Пользователь")
                .email("test" + timestamp + "@example.com")  // Уникальный email
                .password("ValidPass123")  // 12 символов, заглавная буква, цифры
                .passwordRepeat("ValidPass123");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(userPayload)
                .when()
                .post("/register")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("id", not(emptyString()));
    }
    //Альтернативный вариант с валидацией ошибок:
    @Test
    public void testRegistrationValidation() {
        UserPayload userPayload = new UserPayload()
                .fullName("Test123")  // Неправильное имя (содержит цифры)
                .email("test@mail.com")
                .password("test")      // Неправильный пароль (короткий, без заглавной)
                .passwordRepeat("test123"); // Пароли не совпадают

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(userPayload)
                .when()
                .post("/register")
                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("message", hasItems(
                        "Поле ФИО должно содержать только буквы и пробелы",
                        "Пароль должен содержать хотя бы одну заглавную букву",
                        "Минимальная длина пароля 8 символов",
                        "Пароли не совпадают"
                ));
    }
    // Использование утилитного метода: (смотри класс TestDataGenerator)
    @Test
    public void testCanRegisterNewUserWithHelper() {
        UserPayload userPayload = TestDataGenerator.createValidUserPayload();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(userPayload)
                .when()
                .post("/register")
                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("id", not(emptyString()));
    }

//    @Test
//    public void testCanNotRegisterSameUserTwice() {
//        UserPayload userPayload = new UserPayload()
//                .fullName(RandomStringUtils.randomAlphanumeric(6))
//                .email("test@mail.com")
//                .passwordRepeat("12345678Aa")
//                .password("test123");
//
//        RestAssured
//                .given()
//                .contentType(ContentType.JSON)
//                .log().all()
//                .body(userPayload)
//                .when()
//                .post("/register")
//                .then()
//                .log().all()
//                .assertThat()
//                .statusCode(500);
//        RestAssured
//                .given()
//                .contentType(ContentType.JSON)
//                .log().all()
//                .body(userPayload)
//                .when()
//                .post("/register")
//                .then()
//                .log().all()
//                .assertThat()
//                .statusCode(400)
//                .body("id", not(emptyString()));
//
//    }


//  @Test // Класический тест на rest Assurt, плохо маштабируется и дебажится
//  void testCanRegisterNewUser1() {
//      UserPayload user = new UserPayload()
//                .fullName("RandomStringUtils.randomAlphanumeric(6)")
//              .email("test@mail.com")
//              .passwordRepeat("12345678Aa")
//              .password("test123");
//  RestAssured
//  .given().contentType(ContentType.JSON).log().all()
//  .body(user)
//  .when()
//  .post("register")
//  .then().log().all()
//  .assertThat()
//  .statusCode(200)
//  .body("id", not(isEmptyString()));
//  }

}


