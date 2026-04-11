package org.example.tests;

import io.qameta.allure.*;
import org.example.base.BaseTest;
import org.example.pages.SignupLoginPage;
import org.example.utils.ConfigReader;
import org.example.utils.TestDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.qameta.allure.Allure;

/**
 * SignupLoginTests - Clean and maintainable test class
 */
@Epic("User Authentication")
@Feature("Signup and Login")
public class SignupLoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SignupLoginTests.class);

    private SignupLoginPage page;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPage() {
        page = new SignupLoginPage();
        page.acceptConsentIfPresent(); // ✅ بتقفل الـ popup قبل أي test
        logger.info("SignupLoginPage initialized");
    }

    // =========================
    // TC01 - Register User
    // =========================
    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Register a new user with dynamic email")
    public void TC01_RegisterUser() {

        logger.info("Starting TC01_RegisterUser");

        String email = TestDataReader.generateDynamicEmail();

        Allure.addAttachment(
                "Test Data",
                "Name: " + TestDataReader.getProperty("register.name")
                        + "\nEmail: " + email
        );

        Allure.step("Open login page", () ->
                getDriver().get(ConfigReader.getBaseUrl() + "/login"));

        Allure.step("Register new user", () -> {
            page.enterSignupName(TestDataReader.getProperty("register.name"));
            page.enterSignupEmail(email);
            page.clickSignupButton();

            page.fillRegistrationForm(
                    "Mr",
                    "password123",
                    "15",
                    "5",
                    "1990",
                    "John",
                    "Doe",
                    "ABC Corp",
                    "123 Street",
                    "Apt 4",
                    "United States",
                    "California",
                    "Los Angeles",
                    "90001",
                    "1234567890"
            );

            page.clickCreateAccountButton();
        });

        Allure.step("Verify account creation", () -> {
            String msg = page.getAccountCreatedMessage();
            Assert.assertEquals(msg, "ACCOUNT CREATED!");
        });

        logger.info("TC01 completed successfully");
    }

    // =========================
    // TC02 - Login Success
    // =========================
    @Test
    @Story("User Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Login with valid credentials")
    public void TC02_LoginWithCorrectCredentials() {

        logger.info("Starting TC02");

        Allure.addAttachment(
                "Test Data",
                "Email: " + TestDataReader.getProperty("valid.email")
        );

        Allure.step("Open login page", () ->
                getDriver().get(ConfigReader.getBaseUrl() + "/login"));

        Allure.step("Login with valid credentials", () -> {
            page.enterLoginEmail(TestDataReader.getProperty("valid.email"));
            page.enterLoginPassword(TestDataReader.getProperty("valid.password"));
            page.clickLoginButton();
        });

        Allure.step("Verify login success", () -> {
            String user = page.getLoggedInUsername();
            Assert.assertTrue(user.contains("Logged in as"));
        });
    }

    // =========================
    // TC03 - Login Failure
    // =========================
    @Test
    @Story("User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Login with invalid credentials")
    public void TC03_LoginWithIncorrectCredentials() {

        logger.info("Starting TC03");

        Allure.step("Open login page", () ->
                getDriver().get(ConfigReader.getBaseUrl() + "/login"));

        Allure.step("Login with invalid credentials", () -> {
            page.enterLoginEmail(TestDataReader.getProperty("invalid.email"));
            page.enterLoginPassword(TestDataReader.getProperty("invalid.password"));
            page.clickLoginButton();
        });

        Allure.step("Verify error message", () -> {
            String error = page.getLoginErrorMessage();
            Assert.assertEquals(error, "Your email or password is incorrect!");
        });
    }

    // =========================
    // TC04 - Logout
    // =========================
    @Test
    @Story("User Logout")
    @Severity(SeverityLevel.NORMAL)
    @Description("Logout user successfully")
    public void TC04_LogoutUser() {

        logger.info("Starting TC04");

        Allure.step("Login first", () -> {
            getDriver().get(ConfigReader.getBaseUrl() + "/login");
            page.enterLoginEmail(TestDataReader.getProperty("valid.email"));
            page.enterLoginPassword(TestDataReader.getProperty("valid.password"));
            page.clickLoginButton();
        });

        Allure.step("Logout user", page::clickLogoutButton);

        Allure.step("Verify logout", () ->
                Assert.assertTrue(getDriver().getCurrentUrl().contains("/login")));
    }

    // =========================
    // TC05 - Existing Email
    // =========================
    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Register with existing email")
    public void TC05_RegisterWithExistingEmail() {

        logger.info("Starting TC05");

        Allure.step("Open login page", () ->
                getDriver().get(ConfigReader.getBaseUrl() + "/login"));

        Allure.step("Try register with existing email", () -> {
            page.enterSignupName(TestDataReader.getProperty("register.name"));
            page.enterSignupEmail(TestDataReader.getProperty("valid.email"));
            page.clickSignupButton();
        });

        Allure.step("Verify error message", () -> {
            String msg = page.getSignupErrorMessage();
            Assert.assertEquals(msg, "Email Address already exist!");
        });
    }

    // =========================
    // TC06 - Contact Us
    // =========================
    @Test
    @Story("Contact Us")
    @Severity(SeverityLevel.MINOR)
    @Description("Submit contact form")
    public void TC06_ContactUsForm() {

        logger.info("Starting TC06");

        Allure.step("Open contact page", () ->
                getDriver().get(ConfigReader.getBaseUrl() + "/contact_us"));

        Allure.step("Submit contact form", () -> {
            page.fillContactForm("John Doe", "john@test.com", "Subject", "Message");
            page.clickSubmitContactForm();
        });

        Allure.step("Verify success message", () -> {
            String msg = page.getContactSuccessMessage();
            Assert.assertTrue(msg.contains("Success"));
        });
    }
}