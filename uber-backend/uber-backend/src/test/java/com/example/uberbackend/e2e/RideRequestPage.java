package com.example.uberbackend.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RideRequestPage {
    private WebDriver driver;
    private Actions actions;
    private static final String PAGE_URL = "http://localhost:4200/client/(ClientRouter:request-ride-page)";

    public RideRequestPage(WebDriver driver)
    {
        this.driver = driver;
        this.driver.get(PAGE_URL);
        this.actions = new Actions(this.driver);
        PageFactory.initElements(this.driver, this);
    }

    @FindBy(xpath = "/html/body/app-root/app-client-dashboard/ride-request-page/div/div[1]/app-location-picker/div/div/div[1]/mat-form-field/div[1]/div[2]/div/input")
    private WebElement firstLocationInput;

    @FindBy(xpath = "/html/body/app-root/app-client-dashboard/ride-request-page/div/div[1]/app-location-picker/div/div/div[2]/mat-form-field/div[1]/div[2]/div/input")
    private WebElement secondLocationInput;

    @FindBy(xpath = "/html/body/div/div/div/div/mat-option[1]")
    private WebElement firstLocationOption;

    @FindBy(xpath = "/html/body/div/div/div/div/mat-option[2]")
    private WebElement secondLocationOption;

    @FindBy(xpath = "/html/body/app-root/app-client-dashboard/ride-request-page/div/div[1]/app-location-picker/div/div/div[1]/button")
    private WebElement addLocationButton;

    @FindBy(xpath = "/html/body/app-root/app-client-dashboard/ride-request-page/div/div[1]/app-location-picker/div/div/div[3]")
    private WebElement nextButton;

    @FindBy(xpath = "/html/body/app-root/app-client-dashboard/ride-request-page/div/div[1]/app-ride-type/div/div/mat-form-field/div[1]/div[2]/div/mat-select/div/div[1]")
    private WebElement vehicleTypesSelect;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/div/mat-option[1]")
    private WebElement standardVehicleType;

    @FindBy(xpath = "/html/body/app-root/app-client-dashboard/ride-request-page/div/div[1]/app-ride-type/div/div/mat-expansion-panel/div/div/mat-radio-group/mat-radio-button[1]")
    private WebElement customRouteType;

    public void fillOutFirstLocationField(String text){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(firstLocationInput));

        firstLocationInput.sendKeys(text);

        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(firstLocationOption));
        firstLocationOption.click();
    }

    public void fillOutSecondLocationField(String text){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(secondLocationInput));

        secondLocationInput.sendKeys(text);

        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(secondLocationOption));
        secondLocationOption.click();
    }

    public void pressNewLocationButton(){
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(addLocationButton));

        addLocationButton.click();

    }

    public void pressNextButton()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(nextButton));
        this.nextButton.click();
    }

    public void openVehicleTypeSelect()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(vehicleTypesSelect));
        vehicleTypesSelect.click();
    }

    public void selectStandardVehicleType()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(standardVehicleType));
        standardVehicleType.click();
    }

    public void selectCustomRouteType()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(customRouteType));
        customRouteType.click();
    }
}
