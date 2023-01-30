package com.example.uberbackend.e2e;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InvitedClientDashboardPage {
    private WebDriver driver;
    private Actions actions;
    private static final String PAGE_URL = "http://localhost:4200/client/(ClientRouter:request-ride-page)";

    @FindBy(xpath = "//button[contains(@color, 'warn')]")
    private WebElement rideInviteIcon;

    @FindBy(xpath = "//*[@id=\"mat-mdc-dialog-0\"]/div/div/app-ride-invite-dialog/div/div[2]/button[1]")
    private WebElement acceptButton;

    @FindBy(xpath = "//button[contains(@class, 'logout-button')]")
    private WebElement logoutButton;

    public InvitedClientDashboardPage(WebDriver driver)
    {
        this.driver = driver;
        this.driver.get(PAGE_URL);
        this.actions = new Actions(this.driver);
        PageFactory.initElements(this.driver, this);
    }

    public void clickOnRideInviteIcon()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(rideInviteIcon));
        rideInviteIcon.click();
    }

    public void acceptRideInvite()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(acceptButton));
        acceptButton.click();
    }

    public void logout()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(logoutButton));
        logoutButton.click();
    }
}
