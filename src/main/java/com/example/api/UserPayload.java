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

//    private String fullName;
//    private String email;
//    private String passwordRepeat;
//    private String password;

//    public UserPayload fullName(String fullName) {
//        this.fullName = fullName;
//        return this;
//    }
//
//    public UserPayload email(String email) {
//        this.email = email;
//        return this;
//    }
//
//    public UserPayload passwordRepeat(String passwordRepeat) {
//        this.passwordRepeat = passwordRepeat;
//        return this;
//    }
//
//    public UserPayload password(String password) {
//        this.password = password;
//        return this;
//    }


}
