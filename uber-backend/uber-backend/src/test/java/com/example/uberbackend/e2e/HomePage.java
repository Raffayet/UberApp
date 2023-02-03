package com.example.uberbackend.e2e;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;
    private Actions actions;
    private static final String PAGE_URL = "http://localhost:4200";

    public HomePage(WebDriver driver){
        this.driver = driver;
        this.driver.get(PAGE_URL);
        this.actions = new Actions(this.driver);
        PageFactory.initElements(this.driver, this);
    }

    @FindBy(xpath = "//a[contains(text(), 'Log In')]")
    private WebElement loginLink;

    @FindBy(xpath = "//input[@id='float-input']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@label='Submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[@role='alert']")
    private WebElement toastDiv;

    public void gotoLoginPage(){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(loginLink));

        actions.click(loginLink).perform();
    }

    public void fillOutUsernameField(String text){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(usernameInput));

        usernameInput.sendKeys(text);
    }

    public void fillOutPasswordField(String text){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(passwordInput));

        passwordInput.sendKeys(text);
    }

    public void pressLoginButton(){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(loginButton));

        loginButton.click();
    }

    public String getTextFromToastMessage(){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(toastDiv));

        return toastDiv.getText();
    }

    public boolean isLoginButtonDisabled() {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(loginButton));

        return Boolean.parseBoolean(loginButton.getAttribute("disabled"));
    }

}
