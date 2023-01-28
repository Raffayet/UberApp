package com.example.uberbackend.login.e2e;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTests extends TestBase{

    @Test
    public void failedLogin(){
        HomePage homePage = new HomePage(driver);
        homePage.gotoLoginPage();

        homePage.fillOutUsernameField("jovancevic@gmail.com");
        homePage.fillOutPasswordField("123");

        homePage.pressLoginButton();
        String actual = homePage.getTextFromToastMessage();
        String expected = "Credentials are wrong!";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void successfulLogin(){
        HomePage homePage = new HomePage(driver);
        homePage.gotoLoginPage();

        homePage.fillOutUsernameField("sasalukic@gmail.com");
        homePage.fillOutPasswordField("sasa123");

        homePage.pressLoginButton();
        String actual = homePage.getTextFromToastMessage();
        String expected = "Successfully logged in!";

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void tryToLoginWithoutPassword(){
        HomePage homePage = new HomePage(driver);
        homePage.gotoLoginPage();

        homePage.fillOutUsernameField("sasalukic@gmail.com");

        homePage.pressLoginButton();

        Assertions.assertTrue(homePage.isLoginButtonDisabled());
    }

    @Test
    public void tryToLoginWithoutUsername(){
        HomePage homePage = new HomePage(driver);
        homePage.gotoLoginPage();

        homePage.fillOutPasswordField("sasa123");

        homePage.pressLoginButton();

        Assertions.assertTrue(homePage.isLoginButtonDisabled());
    }

    @Test
    public void tryToLoginWithoutPoorlyFormattedUsername(){
        HomePage homePage = new HomePage(driver);
        homePage.gotoLoginPage();

        homePage.fillOutUsernameField("sasalukic");
        homePage.fillOutPasswordField("sasa123");

        homePage.pressLoginButton();
        String actual = homePage.getTextFromToastMessage();
        String expected = "Credentials are poorly formatted!";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void tryToLoginWithoutCredentials(){
        HomePage homePage = new HomePage(driver);
        homePage.gotoLoginPage();

        homePage.pressLoginButton();

        Assertions.assertTrue(homePage.isLoginButtonDisabled());
    }
}
