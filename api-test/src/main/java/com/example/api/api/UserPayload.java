package com.example.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
//@Accessors(chain = true) ????????
@Accessors(fluent = true) // переменные в test class
//@AllArgsConstructor
//@NoArgsConstructor
public class UserPayload {
    @JsonProperty("password")
    private String password;

    @JsonProperty("passwordRepeat")
    private String passwordRepeat;

    @JsonProperty("email")
    private String email;

    @JsonProperty("fullName")
    private String fullName;
}
