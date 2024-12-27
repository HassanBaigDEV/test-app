package com.example.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        driver = new ChromeDriver(options);
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testHomePageLinks() {
        driver.get(baseUrl);
        assertTrue(driver.findElement(By.id("register-link")).isDisplayed());
        assertTrue(driver.findElement(By.id("login-link")).isDisplayed());
    }

    @Test
    public void testSuccessfulRegistration() {
        driver.get(baseUrl + "/register");
        driver.findElement(By.id("username")).sendKeys("testuser1");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("email")).sendKeys("test1@example.com");
        driver.findElement(By.id("submit-button")).click();
        
        assertTrue(driver.getCurrentUrl().endsWith("/login"));
    }

    @Test
    public void testDuplicateRegistration() {
        // First registration
        driver.get(baseUrl + "/register");
        driver.findElement(By.id("username")).sendKeys("testuser2");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("email")).sendKeys("test2@example.com");
        driver.findElement(By.id("submit-button")).click();

        // Try registering with same username
        driver.get(baseUrl + "/register");
        driver.findElement(By.id("username")).sendKeys("testuser2");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("email")).sendKeys("test2@example.com");
        driver.findElement(By.id("submit-button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("div[style*='color: red']"));
        assertEquals("Username already exists", errorMessage.getText());
    }

    @Test
    public void testSuccessfulLogin() {
        // Register first
        driver.get(baseUrl + "/register");
        driver.findElement(By.id("username")).sendKeys("testuser3");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("email")).sendKeys("test3@example.com");
        driver.findElement(By.id("submit-button")).click();

        // Then login
        driver.get(baseUrl + "/login");
        driver.findElement(By.id("username")).sendKeys("testuser3");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("submit-button")).click();

        assertTrue(driver.getCurrentUrl().endsWith("/dashboard"));
    }

    @Test
    public void testFailedLoginWithWrongPassword() {
        driver.get(baseUrl + "/login");
        driver.findElement(By.id("username")).sendKeys("testuser3");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.id("submit-button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("div[style*='color: red']"));
        assertEquals("Invalid credentials", errorMessage.getText());
    }

    @Test
    public void testFailedLoginWithNonexistentUser() {
        driver.get(baseUrl + "/login");
        driver.findElement(By.id("username")).sendKeys("nonexistentuser");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("submit-button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("div[style*='color: red']"));
        assertEquals("Invalid credentials", errorMessage.getText());
    }

    @Test
    public void testDashboardAccessAfterLogin() {
        // Register and login first
        driver.get(baseUrl + "/register");
        driver.findElement(By.id("username")).sendKeys("testuser4");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("email")).sendKeys("test4@example.com");
        driver.findElement(By.id("submit-button")).click();

        driver.get(baseUrl + "/login");
        driver.findElement(By.id("username")).sendKeys("testuser4");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("submit-button")).click();

        WebElement welcomeMessage = driver.findElement(By.id("welcome-message"));
        assertTrue(welcomeMessage.isDisplayed());
        assertEquals("You are successfully logged in!", welcomeMessage.getText());
    }

    @Test
    public void testRegistrationFormValidation() {
        driver.get(baseUrl + "/register");
        
        // Test empty submission
        driver.findElement(By.id("submit-button")).click();
        
        // Check if still on registration page (form validation prevented submission)
        assertTrue(driver.getCurrentUrl().endsWith("/register"));
    }

    @Test
    public void testLoginFormValidation() {
        driver.get(baseUrl + "/login");
        
        // Test empty submission
        driver.findElement(By.id("submit-button")).click();
        
        // Check if still on login page (form validation prevented submission)
        assertTrue(driver.getCurrentUrl().endsWith("/login"));
    }

    @Test
    public void testEmailValidationOnRegistration() {
        driver.get(baseUrl + "/register");
        driver.findElement(By.id("username")).sendKeys("testuser5");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("email")).sendKeys("invalid-email");
        
        // The HTML5 email validation should prevent form submission
        driver.findElement(By.id("submit-button")).click();
        
        // Should still be on registration page
        assertTrue(driver.getCurrentUrl().endsWith("/register"));
    }
} 