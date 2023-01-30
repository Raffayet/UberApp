package com.example.uberbackend.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

    @FindBy(xpath = "//input[contains(@id, 'mat-input-0')]")
    private WebElement firstLocationInput;

    @FindBy(xpath = "//input[contains(@id, 'mat-input-2')]")
    private WebElement secondLocationInput;

//    @FindBy(xpath = "//*[@id=\"mat-option-0\"]")
//    private WebElement firstLocationOption;

    @FindBy(xpath = "//mat-option[1]")
    private WebElement firstLocationOption;

//    @FindBy(xpath = "//*[@id=\"mat-option-8\"]")
//    private WebElement secondLocationOption;
//
    @FindBy(xpath = "//mat-option[1]")
    private WebElement secondLocationOption;

    @FindBy(xpath = "//*[@id=\"mat-option-3\"]")
    private WebElement thirdLocationOption;

    @FindBy(xpath = "//*[@id=\"cdk-drop-list-0\"]/div[1]/button")
    private WebElement addLocationButton;

    @FindBy(xpath = "//button[@class='next-button mdc-button mdc-button--raised mat-mdc-raised-button mat-unthemed mat-mdc-button-base']")
    private WebElement nextButton;

    @FindBy(xpath = "//*[contains(@class, 'mat-select')]")
    private WebElement vehicleTypesSelect;

    @FindBy(xpath = "//mat-option[1]")
    private WebElement standardVehicleType;

    @FindBy(xpath = "//*[@id=\"mat-radio-2\"]")
    private WebElement customRouteType;

    @FindBy(xpath = "//button[@class='next-button mdc-button mdc-button--raised mat-mdc-raised-button mat-unthemed mat-mdc-button-base ng-star-inserted']")
    private WebElement secondNextButton;

    @FindBy(xpath = "//button[(contains(@class, 'submit-button'))]")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@role='alert']")
    private WebElement toastDiv;

    @FindBy(xpath = "//img[contains(@class, 'leaflet-marker-icon')]")
    private WebElement carIcon;

    @FindBy(xpath = "//textarea[contains(@id, 'mat-input')]")
    private WebElement commentTextArea;

//    @FindBy(xpath = "//button[contains(@ng-reflect-dialog-result, '[object Object]')]")
    @FindBy(xpath = "//button[contains(@id,'submit')]")
    private WebElement submitCommentButton;

    @FindBy(xpath = "//input[@placeholder='New person...']")
    private WebElement inviteAutocomplete;

    @FindBy(xpath = "//button[contains(@class, 'split-fare-button')]")
    private WebElement splitFareButton;

    @FindBy(xpath = "//a[contains(text(),'History')]")
    private WebElement rideHistoryLink;

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

    public void pressSecondNextButton()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(secondNextButton));
        secondNextButton.click();
    }

    public void submitRequest()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(this.driver.findElement(By.xpath("//*[(contains(@class, 'chip__text'))]"))));

        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(submitButton));
        submitButton.click();
    }

    public String getNotificationText()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(toastDiv));
        return toastDiv.getText();
    }

    public void driveSimulation()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(200)))
                .until(ExpectedConditions.visibilityOf(carIcon));
    }

    public void insertComment(String comment)
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(200)))
                .until(ExpectedConditions.visibilityOf(commentTextArea));
        commentTextArea.sendKeys(comment);
    }

    public void clickSubmitCommentButton()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(100)))
                .until(ExpectedConditions.visibilityOf(submitCommentButton));
        (new WebDriverWait(this.driver, Duration.ofSeconds(100)))
                .until(ExpectedConditions.elementToBeClickable(submitCommentButton));

        this.actions.moveToElement(submitCommentButton).click().perform();
    }

    public void invitePerson(String clientEmail)
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(inviteAutocomplete));
        inviteAutocomplete.sendKeys(clientEmail);
        actions.sendKeys(Keys.RETURN).build().perform();
    }

    public void pressSplitFareButton()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(splitFareButton));
        splitFareButton.click();
    }

    public String getRideInviteResponse()
    {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(toastDiv));
        return toastDiv.getText();
    }

    public void goToRideHistory() {
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.visibilityOf(rideHistoryLink));
        (new WebDriverWait(this.driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.elementToBeClickable(rideHistoryLink));

        this.actions.moveToElement(rideHistoryLink).click().perform();
    }
}
