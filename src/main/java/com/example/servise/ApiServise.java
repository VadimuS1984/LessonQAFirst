package com.example.servise;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiServise {
    // первоначальный вариант
//    protected RequestSpecification setup(){
//        return RestAssured
//                .given()
//                .contentType(ContentType.JSON)
//                .log().all();
//    }

    // Второй вариант
    // добаляем из LOMBOK filters
//    protected RequestSpecification setup(){
//        return RestAssured
//                .given().contentType(ContentType.JSON)
//        // комментинруя filters, можем отключать и выключать логи
//                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
//    }


    protected RequestSpecification setup(){
        return RestAssured
                .given().contentType(ContentType.JSON)
                .filters(getFilterList());
    }

    private List<Filter> getFilterList() {
        Boolean enable = Boolean.valueOf(System.getProperty("logging", "true"));
        if (enable) {
            return Arrays.asList(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
        return Collections.emptyList();
    }

    protected RequestSpecification setupWithAuth(String token) {
        return RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .log().all();
    }
}
