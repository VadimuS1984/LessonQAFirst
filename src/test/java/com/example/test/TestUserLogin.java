package com.example.test;

import com.example.api.UserPayload;
import com.example.conditeons.Condirions;
import com.example.servise.UserApiService;
import io.restassured.RestAssured;
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
        UserPayload loginPayload = new UserPayload()
                .email("test@email.com")
                .password("12345678Aa");
        // expect
        userApiService.loginUser(loginPayload)
                .shouldHave(Condirions.statusCode(200))
                .shouldHave(Condirions.bodyField("user.id", not(emptyString())))
                .shouldHave(Condirions.bodyField("user.email", equalTo("test@email.com")))
                .shouldHave(Condirions.bodyField("user.roles", hasItem("USER")))
                .shouldHave(Condirions.bodyField("user.verified", equalTo(true)))
                .shouldHave(Condirions.bodyField("user.banned", equalTo(false)))
                .shouldHave(Condirions.bodyField("accessToken", not(emptyString())))
                .shouldHave(Condirions.bodyField("expiresIn", not(empty())));
    }
}
