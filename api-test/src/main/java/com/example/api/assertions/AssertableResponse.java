package com.example.api.assertions;

import com.example.api.conditeons.Condition;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;


// С Loombok, конструкто автоматически формируется
//@RequiredArgsConstructor
@Slf4j
public class AssertableResponse {

    private final Response response;

    // Конструктор для инициализации response
    public AssertableResponse(Response response) {
        this.response = response;
    }
    // возвращаем this. Через Condition conditeon обернули Response response
    public AssertableResponse shouldHave(Condition conditeon) {
       log.info("About to check condition {}", conditeon);
        conditeon.check(response);
        return this;
    }

    // Добавляем метод для получения оригинального Response
    public Response response() {
        return this.response;
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
