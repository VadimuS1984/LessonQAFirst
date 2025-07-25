package com.example.api;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Fluent API методы для удобства
    public LoginRequest email(String email) {
        this.email = email;
        return this;
    }

    public LoginRequest password(String password) {
        this.password = password;
        return this;
    }
}