package com.example.uberbackend.e2e;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestRideTests extends TestBase{
    @Test
    public void requestRideSuccessWithoutInvites()
    {
        //login section
        HomePage homePage = new HomePage(driver);
        homePage.gotoLoginPage();
        homePage.fillOutUsernameField("sasalukic@gmail.com");
        homePage.fillOutPasswordField("sasa123");
        homePage.pressLoginButton();
        String actual = homePage.getTextFromToastMessage();
        String expected = "Successfully logged in!";

        Assertions.assertEquals(expected, actual);

        RideRequestPage rideRequestPage = new RideRequestPage(driver);
        rideRequestPage.fillOutFirstLocationField("Cara Dusana Novi Sad");
        rideRequestPage.pressNewLocationButton();
        rideRequestPage.fillOutSecondLocationField("Cara Lazara Novi Sad");
//        rideRequestPage.pressNextButton();
//        rideRequestPage.openVehicleTypeSelect();
//        rideRequestPage.selectStandardVehicleType();
//        rideRequestPage.selectCustomRouteType();
    }
}
