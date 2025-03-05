package org.dyplom.aplikacja.security;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

  private WebDriver driver;

  @BeforeEach
  public void setUp() {
    System.setProperty("webdriver.chrome.driver", "E://Java_Programowanie//chromedriver-win64//chromedriver.exe");

    driver = new ChromeDriver();
  }

  @Test
  public void testLogin() {
    driver.get("http://localhost:3000/login");

    WebElement usernameField = driver.findElement(By.id("username"));
    WebElement passwordField = driver.findElement(By.id("password"));
    WebElement loginButton = driver.findElement(By.id("login-button"));

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.urlContains("/login"));

    usernameField.sendKeys("elektryk");
    passwordField.sendKeys("elektryk");

    loginButton.click();

    wait.until(ExpectedConditions.urlContains("/home"));

    String currentUrl = driver.getCurrentUrl();
    assertEquals("http://localhost:3000/home", currentUrl);

    driver.quit();
  }

  @Test
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}
