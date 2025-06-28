package com.example.conditeons;

import io.restassured.response.Response;

//import org.apache.coyote.Response;

public interface Conditeon {
    void check(Response response);
}
