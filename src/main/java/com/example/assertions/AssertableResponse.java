package com.example.assertions;

//import lombok.RequiredArgsConstructor;
import io.restassured.response.Response;
import com.example.conditeons.Conditeon;


// С Loombok, конструкто автоматически формируется
//@RequiredArgsConstructor
//public class AssertableResponse {
//    private final Response response;
//
//    public void shouldHave(Condition) {
//    }
//}

// Без Loombok
public class AssertableResponse {
    // создаем конструктор
    private Response response;

    public AssertableResponse(Response response) {
        this.response = response;
    }
        public void shouldHave(Conditeon conditeon) {
    }
}
