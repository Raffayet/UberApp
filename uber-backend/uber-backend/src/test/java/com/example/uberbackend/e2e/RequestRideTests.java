package com.example.uberbackend.e2e;

import com.example.uberbackend.model.Driver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class RequestRideTests extends TestBase{



    @Test
    public void requestRideSuccessWithoutInvites()
    {
        //login section - client
        HomePage homePageClient = new HomePage(driver);
        homePageClient.gotoLoginPage();
        homePageClient.fillOutUsernameField("sasalukic@gmail.com");
        homePageClient.fillOutPasswordField("sasa123");
        homePageClient.pressLoginButton();
        String actualLoginStatusClient = homePageClient.getTextFromToastMessage();
        String expectedLoginStatusClient = "Successfully logged in!";

        Assertions.assertEquals(expectedLoginStatusClient, actualLoginStatusClient);

        //filling ride request form
        RideRequestPage rideRequestPage = new RideRequestPage(driver);
        rideRequestPage.fillOutFirstLocationField("Cara Dusana Novi Sad");
        rideRequestPage.pressNewLocationButton();
        rideRequestPage.fillOutSecondLocationField("Cara Lazara Novi Sad");
        rideRequestPage.pressNextButton();
        rideRequestPage.openVehicleTypeSelect();

        rideRequestPage.selectStandardVehicleType();
        rideRequestPage.selectCustomRouteType();
        rideRequestPage.pressSecondNextButton();

        //login section - driver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        ChromeDriver driver2 = new ChromeDriver(options);
        driver2.manage().window().setPosition(new Point(1000, 0));
        driver2.manage().window().setSize(new Dimension(1000, 1050));

        HomePage homePageDriver = new HomePage(driver2);
        homePageDriver.gotoLoginPage();
        homePageDriver.fillOutUsernameField("dejanmatic@gmail.com");
        homePageDriver.fillOutPasswordField("sasa123");
        homePageDriver.pressLoginButton();
        String actualLoginStatusDriver = homePageDriver.getTextFromToastMessage();
        String expectedLoginStatusDriver = "Successfully logged in!";

        Assertions.assertEquals(expectedLoginStatusDriver, actualLoginStatusDriver);

        //Initializing driver dashboard
        DriverDashboardPage driverDashboardPage = new DriverDashboardPage(driver2);

        //submit previous filled form when driver signs in
        rideRequestPage.submitRequest();

        driverDashboardPage.clickDriveRequestIcon();
        driverDashboardPage.acceptRideRequest();
        String actualDriverStatus = driverDashboardPage.getBusyLabelText();

        Assertions.assertEquals("BUSY", actualDriverStatus);
        Assertions.assertEquals("Driver has accepted. Enjoy your ride!", rideRequestPage.getNotificationText());

        rideRequestPage.driveSimulation();
        rideRequestPage.insertComment("Good ride!");
        rideRequestPage.clickSubmitCommentButton();

        driver2.quit();
    }

    @Test
    public void requestRideSuccessWithInvites()
    {
        //login section - client
        HomePage homePageClient = new HomePage(driver);
        homePageClient.gotoLoginPage();
        homePageClient.fillOutUsernameField("sasalukic@gmail.com");
        homePageClient.fillOutPasswordField("sasa123");
        homePageClient.pressLoginButton();
        String actualLoginStatusClient = homePageClient.getTextFromToastMessage();
        String expectedLoginStatusClient = "Successfully logged in!";

        Assertions.assertEquals(expectedLoginStatusClient, actualLoginStatusClient);

        //filling ride request form
        RideRequestPage rideRequestPage = new RideRequestPage(driver);
        rideRequestPage.fillOutFirstLocationField("Cara Dusana Novi Sad");
        rideRequestPage.pressNewLocationButton();
        rideRequestPage.fillOutSecondLocationField("Cara Lazara Novi Sad");
        rideRequestPage.pressNextButton();
        rideRequestPage.openVehicleTypeSelect();

        rideRequestPage.selectStandardVehicleType();
        rideRequestPage.selectCustomRouteType();
        rideRequestPage.pressSecondNextButton();
        rideRequestPage.invitePerson("milicamatic@gmail.com");
        rideRequestPage.pressSplitFareButton();

        //login section - invited client
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        ChromeDriver driver2 = new ChromeDriver(options);
        driver2.manage().window().setPosition(new Point(1000, 0));
        driver2.manage().window().setSize(new Dimension(1000, 1050));

        HomePage homePageInvitedClient = new HomePage(driver2);
        homePageInvitedClient.gotoLoginPage();
        homePageInvitedClient.fillOutUsernameField("milicamatic@gmail.com");
        homePageInvitedClient.fillOutPasswordField("sasa123");
        homePageInvitedClient.pressLoginButton();
        String actualLoginStatusDriver = homePageInvitedClient.getTextFromToastMessage();
        String expectedLoginStatusDriver = "Successfully logged in!";

        Assertions.assertEquals(expectedLoginStatusDriver, actualLoginStatusDriver);

        //accepting ride invite
        InvitedClientDashboardPage invitedClientDashboardPage = new InvitedClientDashboardPage(driver2);
        invitedClientDashboardPage.clickOnRideInviteIcon();
        invitedClientDashboardPage.acceptRideInvite();

        String expectedRideInviteResponse = "milicamatic@gmail.com has accepted your ride invite";
        String actualRideInviteResponse = rideRequestPage.getRideInviteResponse();
        Assertions.assertEquals(expectedRideInviteResponse, actualRideInviteResponse);

//        //Initializing driver dashboard
//        DriverDashboardPage driverDashboardPage = new DriverDashboardPage(driver2);
//
//        //submit previous filled form when driver signs in
//        rideRequestPage.submitRequest();
//
//        driverDashboardPage.clickDriveRequestIcon();
//        driverDashboardPage.acceptRideRequest();
//        String actualDriverStatus = driverDashboardPage.getBusyLabelText();
//
//        Assertions.assertEquals("BUSY", actualDriverStatus);
//        Assertions.assertEquals("Driver has accepted. Enjoy your ride!", rideRequestPage.getNotificationText());
//
//        rideRequestPage.driveSimulation();
//        rideRequestPage.insertComment("Good ride!");
//        rideRequestPage.clickSubmitCommentButton();

        driver2.quit();
    }
}
