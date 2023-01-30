package com.example.uberbackend.e2e;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {
    public WebDriver driver;

    @BeforeAll
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        this.driver = new ChromeDriver(options);
        this.driver.manage().window().setSize(new Dimension(1000, 1050));
    }

    @AfterAll
    public void cleanup(){
        driver.quit();
    }
}
