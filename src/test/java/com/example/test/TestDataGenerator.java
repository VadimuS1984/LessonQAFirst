package com.example.test;

import com.example.api.UserPayload;
import org.apache.commons.lang3.RandomStringUtils;

public class TestDataGenerator {
    public static String generateValidName() {
        return RandomStringUtils.randomAlphabetic(5) + " " +
                RandomStringUtils.randomAlphabetic(6);
    }

    public static String generateValidPassword() {
        return "Test" + RandomStringUtils.randomAlphanumeric(5) + "123";
    }

    public static String generateUniqueEmail() {
        return "test" + RandomStringUtils.randomNumeric(3) + "@example.com";
    }

    public static UserPayload createValidUserPayload() {
        return new UserPayload()
                .fullName(generateValidName())
                .email(generateUniqueEmail())
                .password(generateValidPassword())
                .passwordRepeat(generateValidPassword());
    }
}
