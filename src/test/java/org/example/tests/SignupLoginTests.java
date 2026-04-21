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

public class SignupLoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SignupLoginTests.class);
    private SignupLoginPage page;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPage() {
        page = new SignupLoginPage();
        logger.info("SignupLoginPage initialized");
    }

    @Test
    public void TC01_RegisterUser() {
        logger.info("Starting TC01_RegisterUser");

        String email = TestDataReader.generateDynamicEmail();
        String name  = TestDataReader.getRequiredProperty("register.name");
        logger.info("Test Data - Name: " + name + ", Email: " + email);

        logger.info("Opening login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Entering signup details");
        page.enterSignupName(name);
        page.enterSignupEmail(email);
        page.clickSignupButton();

        logger.info("Filling registration form");
        page.fillRegistrationForm(
                TestDataReader.getRequiredProperty("register.title"),
                TestDataReader.getRequiredProperty("register.password"),
                TestDataReader.getRequiredProperty("register.day"),
                TestDataReader.getRequiredProperty("register.month"),
                TestDataReader.getRequiredProperty("register.year"),
                TestDataReader.getRequiredProperty("register.firstname"),
                TestDataReader.getRequiredProperty("register.lastname"),
                TestDataReader.getRequiredProperty("register.company"),
                TestDataReader.getRequiredProperty("register.address1"),
                TestDataReader.getRequiredProperty("register.address2"),
                TestDataReader.getRequiredProperty("register.country"),
                TestDataReader.getRequiredProperty("register.state"),
                TestDataReader.getRequiredProperty("register.city"),
                TestDataReader.getRequiredProperty("register.zipcode"),
                TestDataReader.getRequiredProperty("register.mobile")
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
        logger.info("Test Data - Email: " + TestDataReader.getRequiredProperty("valid.email"));

        logger.info("Opening login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Logging in with valid credentials");
        page.enterLoginEmail(TestDataReader.getRequiredProperty("valid.email"));
        page.enterLoginPassword(TestDataReader.getRequiredProperty("valid.password"));
        page.clickLoginButton();

        logger.info("Verifying login success");
        String user = page.getLoggedInUsername();
        Assert.assertFalse(user.isEmpty(), "Username should not be empty after login");


        logger.info("TC02 completed successfully");
    }

    @Test
    public void TC03_LoginWithIncorrectCredentials() {
        logger.info("Starting TC03_LoginWithIncorrectCredentials");

        logger.info("Opening login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Logging in with invalid credentials");
        page.enterLoginEmail(TestDataReader.getRequiredProperty("invalid.email"));
        page.enterLoginPassword(TestDataReader.getRequiredProperty("invalid.password"));
        page.clickLoginButton();

        logger.info("Verifying error message");
        String error = page.getLoginErrorMessage();
        Assert.assertEquals(error, "Your email or password is incorrect!");

        logger.info("TC03 completed successfully");
    }

    @Test
    public void TC04_LogoutUser() {
        logger.info("Starting TC04_LogoutUser");

        logger.info("Opening login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Logging in");
        page.enterLoginEmail(TestDataReader.getRequiredProperty("valid.email"));
        page.enterLoginPassword(TestDataReader.getRequiredProperty("valid.password"));
        page.clickLoginButton();

        logger.info("Logging out");
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
        page.enterSignupName(TestDataReader.getRequiredProperty("register.name"));
        page.enterSignupEmail(TestDataReader.getRequiredProperty("valid.email"));
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
        page.fillContactForm(
                TestDataReader.getRequiredProperty("contact.name"),
                TestDataReader.getRequiredProperty("contact.email"),
                TestDataReader.getRequiredProperty("contact.subject"),
                TestDataReader.getRequiredProperty("contact.message")
        );
        page.clickSubmitContactForm();

        logger.info("Verifying success message");
        String msg = page.getContactSuccessMessage();
        Assert.assertTrue(msg.contains("Success"));

        logger.info("TC06 completed successfully");
    }
}