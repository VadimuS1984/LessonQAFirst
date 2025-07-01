package com.example.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class User {
    @JsonProperty("id")
    private String id;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roles")
    private String[] roles;

    @JsonProperty("verified")
    private Boolean verified;

    @JsonProperty("banned")
    private Boolean banned;
}

