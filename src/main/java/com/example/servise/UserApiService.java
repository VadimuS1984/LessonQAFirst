package com.example.servise;

import com.example.api.UserPayload;
import com.example.assertions.AssertableResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class UserApiService extends ApiServise {
    // пишем сюда все что свчзанно с User
    public AssertableResponse registerUser(UserPayload userPayload) {
        return new AssertableResponse(setup()
                .body(userPayload)
                .when()
                .post("/register"));
    }

    /**
     * @param userPayload
     * @return new AssertableResponse
     */
    public AssertableResponse loginUser(UserPayload userPayload) {
        return new AssertableResponse(setup()
                .body(userPayload)
                .when()
                .post("/login"));
    }
}
