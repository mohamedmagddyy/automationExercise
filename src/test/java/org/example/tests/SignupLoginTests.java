package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.SignupLoginPage;
import org.example.utils.ConfigReader;
import org.example.utils.TestDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * SignupLoginTests - Clean and maintainable test class
 */
public class SignupLoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SignupLoginTests.class);

    private SignupLoginPage page;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPage() {
        page = new SignupLoginPage();
        page.acceptConsentIfPresent();
        logger.info("SignupLoginPage initialized");
    }

    @Test
    public void TC01_RegisterUser() {

        logger.info("Starting TC01_RegisterUser");

        String email = TestDataReader.generateDynamicEmail();
        logger.info("Test Data - Name: " + TestDataReader.getProperty("register.name") + ", Email: " + email);

        logger.info("Opening login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Registering new user");
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

        logger.info("Verifying account creation");
        String msg = page.getAccountCreatedMessage();
        Assert.assertEquals(msg, "ACCOUNT CREATED!");

        logger.info("TC01 completed successfully");
    }

    @Test
    public void TC02_LoginWithCorrectCredentials() {

        logger.info("Starting TC02_LoginWithCorrectCredentials");
        logger.info("Test Data - Email: " + TestDataReader.getProperty("valid.email"));

        logger.info("Opening login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Logging in with valid credentials");
        page.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        page.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        page.clickLoginButton();

        logger.info("Verifying login success");
        String user = page.getLoggedInUsername();
        Assert.assertTrue(user.contains("Logged in as"));

        logger.info("TC02 completed successfully");
    }

    @Test
    public void TC03_LoginWithIncorrectCredentials() {

        logger.info("Starting TC03_LoginWithIncorrectCredentials");

        logger.info("Opening login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Logging in with invalid credentials");
        page.enterLoginEmail(TestDataReader.getProperty("invalid.email"));
        page.enterLoginPassword(TestDataReader.getProperty("invalid.password"));
        page.clickLoginButton();

        logger.info("Verifying error message");
        String error = page.getLoginErrorMessage();
        Assert.assertEquals(error, "Your email or password is incorrect!");

        logger.info("TC03 completed successfully");
    }

    @Test
    public void TC04_LogoutUser() {

        logger.info("Starting TC04_LogoutUser");

        logger.info("Logging in first");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");
        page.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        page.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        page.clickLoginButton();

        logger.info("Logging out user");
        page.clickLogoutButton();

        logger.info("Verifying logout");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/login"));

        logger.info("TC04 completed successfully");
    }

    @Test
    public void TC05_RegisterWithExistingEmail() {

        logger.info("Starting TC05_RegisterWithExistingEmail");

        logger.info("Opening login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Attempting to register with existing email");
        page.enterSignupName(TestDataReader.getProperty("register.name"));
        page.enterSignupEmail(TestDataReader.getProperty("valid.email"));
        page.clickSignupButton();

        logger.info("Verifying error message");
        String msg = page.getSignupErrorMessage();
        Assert.assertEquals(msg, "Email Address already exist!");

        logger.info("TC05 completed successfully");
    }

    @Test
    public void TC06_ContactUsForm() {

        logger.info("Starting TC06_ContactUsForm");

        logger.info("Opening contact page");
        getDriver().get(ConfigReader.getBaseUrl() + "/contact_us");

        logger.info("Submitting contact form");
        page.fillContactForm("John Doe", "john@test.com", "Subject", "Message");
        page.clickSubmitContactForm();

        logger.info("Verifying success message");
        String msg = page.getContactSuccessMessage();
        Assert.assertTrue(msg.contains("Success"));

        logger.info("TC06 completed successfully");
    }
}