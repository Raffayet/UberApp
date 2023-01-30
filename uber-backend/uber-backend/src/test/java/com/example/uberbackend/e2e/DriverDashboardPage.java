package com.example.uberbackend.e2e;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverDashboardPage {
    private WebDriver driver;
    private Actions actions;
    private static final String PAGE_URL = "http://localhost:4200/driver/(DriverRouter:profile-page)";

    @FindBy(xpath = "//button[contains(@color, 'warn')]")
    private WebElement driveRequestIcon;

    @FindBy(xpath = "//*[@id=\"mat-mdc-dialog-0\"]/div/div/app-ride-to-take-dialog/div/div[2]/button[1]")
    private WebElement acceptButton;

    @FindBy(xpath = "//label[contains(@id, 'toggle')]")
    private WebElement busyLabel;

    public DriverDashboardPage(WebDriver driver)
    {
        this.driver = driver;
        this.driver.get(PAGE_URL);
        this.actions = new Actions(this.driver);
        PageFactory.initElements(this.driver, this);
    }

    public void clickDriveRequestIcon()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(driveRequestIcon));
        driveRequestIcon.click();
    }

    public void acceptRideRequest()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(acceptButton));
        acceptButton.click();
    }

    public String getBusyLabelText()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.textToBePresentInElement(busyLabel, "BUSY"));
        return busyLabel.getText();
    }
}
