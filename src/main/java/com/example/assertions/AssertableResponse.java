package com.example.assertions;

//import lombok.RequiredArgsConstructor;
import io.restassured.response.Response;
import com.example.conditeons.Condition;
import lombok.RequiredArgsConstructor;


// С Loombok, конструкто автоматически формируется
@RequiredArgsConstructor
public class AssertableResponse {
    private final Response response;

    public AssertableResponse shouldHave(Condition conditeon) {
        conditeon.check(response);
        return this;
    }
}

// Без Loombok
//public class AssertableResponse {
//    // создаем конструктор
//    private Response response;
//
//    public AssertableResponse(Response response) {
//        this.response = response;
//    }
//        public void shouldHave(Conditeon conditeon) {
//    }
//}
