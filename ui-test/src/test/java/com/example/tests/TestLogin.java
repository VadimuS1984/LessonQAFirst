package com.example.tests;

import com.codeborne.selenide.Selenide;
import org.testng.annotations.Test;

public class TestLogin {

    @Test
    public void userCanLoginWithValidCredentials() {
        Selenide.open("https://cinescope.t-qa.ru");
    }
}
