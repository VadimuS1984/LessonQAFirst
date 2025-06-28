package com.example.servise;

import com.example.api.UserPayload;
import com.example.assertions.AssertableResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class UserApiService  {
//public class UserApiService extends ApiServise {
    // пишем сюда все что свчзанно с User
    public Response registerUser(UserPayload userPayload) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(userPayload)
                .when()
                .post("/register");
    }
//    return new AssertableResponse(
//                .body(userPayload)
//                .when()
//                .post("/register"));
//    }
}
