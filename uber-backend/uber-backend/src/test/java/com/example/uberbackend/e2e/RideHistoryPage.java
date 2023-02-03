package com.example.uberbackend.e2e;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RideHistoryPage {
    private WebDriver driver;
    private Actions actions;
    private static final String PAGE_URL = "http://localhost:4200/client/(ClientRouter:history)";

    public RideHistoryPage(WebDriver driver)
    {
        this.driver = driver;
        this.driver.get(PAGE_URL);
        this.actions = new Actions(this.driver);
        PageFactory.initElements(this.driver, this);
    }

    @FindBy(xpath = "//tr")
    private List<WebElement> previousRides;

    @FindBy(xpath = "//a[contains(text(),'Home')]")
    private WebElement rideRequestLink;

    public int countEndedRides(){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOfAllElements(previousRides));

        return previousRides.size();
    }

    public void goToRideRequest() {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.elementToBeClickable(rideRequestLink)).click();
    }
}
