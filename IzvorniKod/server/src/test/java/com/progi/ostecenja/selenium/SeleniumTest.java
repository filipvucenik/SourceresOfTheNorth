package com.progi.ostecenja.selenium;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SeleniumTest {
    private static WebDriver driver;

    @BeforeEach
    public void initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\ChromeDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        // driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://progi-projekt-test.onrender.com/");
    }

    @AfterEach
    public void quitDriver() {
        driver.quit();
    }

    // Test 1
    @Test
    public void testLoginUser() {
        String eMail = "abc@gmail.com";
        String password = "pass1234";

        // log-in button
        driver.findElement(By.id("loginDropdown")).click();
        // login user
        driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > div:nth-child(1) > ul > li:nth-child(1) > a")).click();

        // e-mail input
        driver.findElement(By.id("UserLogin")).sendKeys(eMail);
        // password input
        driver.findElement(By.id("passLogin")).sendKeys(password);
        // log-in button
        driver.findElement(By.id("loginButton")).click();

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > a > button")).isDisplayed());

        String redirectURL = driver.getCurrentUrl();
        assertEquals("https://progi-projekt-test.onrender.com/", redirectURL);

        // e-mail u buttonu
        assertEquals(eMail, driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > a > button")) .getText());
    }

    // Test 2
    @Test
    public void testLoginUserInvalidPassword() {
        String eMail = "abc@gmail.com";
        String password = "pass123456";

        // log-in button
        driver.findElement(By.id("loginDropdown")).click();
        // login user
        driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > div:nth-child(1) > ul > li:nth-child(1) > a")).click();

        // e-mail input
        driver.findElement(By.id("UserLogin")).sendKeys(eMail);
        // password input
        driver.findElement(By.id("passLogin")).sendKeys(password);
        // log-in button
        driver.findElement(By.id("loginButton")).click();

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> driver.findElement(By.cssSelector("body > div:nth-child(3) > p")).isDisplayed());

        assertEquals("Greška kod prijave: neispravan mail ili lozinka!", driver.findElement(By.cssSelector("body > div:nth-child(3) > p")).getText());
    }

    // Test 3
    @Test
    public void registerUser() {
        String name = "Ime", surname = "Prezime", eMail = "novi.user.1@gmail.com", password = "lozinka123";

        // register button
        driver.findElement(By.cssSelector("#registerDropdown")).click();
        // register user
        driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > div:nth-child(2) > ul > li:nth-child(1) > a")).click();

        // name input
        driver.findElement(By.cssSelector("#imeReg")).sendKeys(name);
        // surname input
        driver.findElement(By.cssSelector("#prezReg")).sendKeys(surname);
        // e-mail button
        driver.findElement(By.cssSelector("#mailReg")).sendKeys(eMail);
        // password input
        driver.findElement(By.cssSelector("#passReg")).sendKeys(password);
        // register button
        driver.findElement(By.cssSelector("#root > div > form > button")).click();

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > a > button")).isDisplayed());

        String redirectURL = driver.getCurrentUrl();
        assertEquals("https://progi-projekt-test.onrender.com/", redirectURL);
        // e-mail u buttonu
        assertEquals(eMail, driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > a > button")) .getText());
    }

    // Test 4
    @Test
    public void registerExistingUser() {
        String name = "xx", surname = "yy", eMail = "abc@gmail.com", password = "pass1234";

        // register button
        driver.findElement(By.cssSelector("#registerDropdown")).click();
        // register user
        driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > div:nth-child(2) > ul > li:nth-child(1) > a")).click();

        // name input
        driver.findElement(By.cssSelector("#imeReg")).sendKeys(name);
        // surname input
        driver.findElement(By.cssSelector("#prezReg")).sendKeys(surname);
        // e-mail button
        driver.findElement(By.cssSelector("#mailReg")).sendKeys(eMail);
        // password input
        driver.findElement(By.cssSelector("#passReg")).sendKeys(password);
        // register button
        driver.findElement(By.cssSelector("#root > div > form > button")).click();

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> driver.findElement(By.cssSelector("body > div:nth-child(3) > button")).isDisplayed());

        driver.findElement(By.cssSelector("body > div:nth-child(3) > button")).click();

        assertEquals("https://progi-projekt-test.onrender.com/user-register", driver.getCurrentUrl());
    }

    // Test 5
    @Test
    public void deleteUser() {
        String eMail = "novi.user.1@gmail.com", password = "lozinka123";
        // log-in button
        driver.findElement(By.id("loginDropdown")).click();
        // login user
        driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > div:nth-child(1) > ul > li:nth-child(1) > a")).click();
        // e-mail input
        driver.findElement(By.id("UserLogin")).sendKeys(eMail);
        // password input
        driver.findElement(By.id("passLogin")).sendKeys(password);
        // log-in button
        driver.findElement(By.id("loginButton")).click();

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > a > button")).isDisplayed());
        // go to profile page
        driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > a > button")).click();
        driver.findElement(By.cssSelector("#root > div > header > div > div > div > div > ul > li:nth-child(1) > a")).click();
        // delete account
        driver.findElement(By.cssSelector("#root > div > form > button:nth-child(4)")).click();

        wait.until(d -> driver.findElement(By.cssSelector("body > div:nth-child(3) > p")).isDisplayed());
        assertEquals("Profile successfully deleted", driver.findElement(By.cssSelector("body > div:nth-child(3) > p")).getText());
        driver.findElement(By.cssSelector("body > div:nth-child(3) > button")).click();
        wait.until(d -> driver.findElement(By.cssSelector("#root > div > div:nth-child(2) > h1")).isDisplayed());
        String redirectURL = driver.getCurrentUrl();
        assertEquals("https://progi-projekt-test.onrender.com/", redirectURL);
    }

    // Test 6
    @Test
    public void statisticsReview() {
        driver.findElement(By.cssSelector("#root > div > header > div > div > ul > li:nth-child(3) > a > button")).click();

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> driver.findElement(By.cssSelector("#root > div > h1")).isDisplayed());

        assertEquals("https://progi-projekt-test.onrender.com/statistika", driver.getCurrentUrl());

        driver.findElement(By.cssSelector("#root > div > form > label:nth-child(1) > select")).sendKeys("r");
        driver.findElement(By.cssSelector("#root > div > form > label:nth-child(2) > input[type=date]")).sendKeys("01012024");
        driver.findElement(By.cssSelector("#root > div > form > label:nth-child(3) > input[type=date]")).sendKeys("02022024");
        driver.findElement(By.cssSelector("#root > div > form > button")).click();

        assertTrue(driver.findElement(By.cssSelector("#root > div > ul > li > p:nth-child(1)")).isDisplayed());
    }
}
