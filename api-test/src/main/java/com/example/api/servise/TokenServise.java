package com.example.api.servise;

import com.example.api.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class TokenServise {
    protected RequestSpecification generateToken(){
        return RestAssured
                .given()
                .header("Authorization", TokenManager.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .log().all();
    }
}
