package com.example.uberbackend.login.e2e;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver driver;
    private Actions actions;
    private static final String PAGE_URL = "http://localhost:4200/login";

    public LoginPage(WebDriver driver){
        this.driver = driver;
        this.driver.get(PAGE_URL);
        this.actions = new Actions(this.driver);
        PageFactory.initElements(this.driver, this);
    }
}