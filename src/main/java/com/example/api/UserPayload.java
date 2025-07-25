package com.example.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Getter
@Setter
//@Accessors(chain = true) ????????
@Accessors(fluent = true) // переменные в test class
public class UserPayload {
    @JsonProperty("password")
    private String password;

    @JsonProperty("passwordRepeat")
    private String passwordRepeat;

    @JsonProperty("email")
    private String email;

    @JsonProperty("fullName")
    private String fullName;

//    public static class User {
//        @JsonProperty("id")
//        private String id;
//
//        @JsonProperty("email")
//        private String email;
//
//        @JsonProperty("roles")
//        private String[] roles;
//
//        @JsonProperty("verified")
//        private Boolean verified;
//
//        @JsonProperty("banned")
//        private Boolean banned;
//    }
}
