package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.HomePage;
import org.example.pages.SignupLoginPage;
import org.example.utils.ConfigReader;
import org.example.utils.TestDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class SignupLoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SignupLoginTests.class);

    private SignupLoginPage page;
    private HomePage homePage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPages() {
        page     = new SignupLoginPage();
        homePage = new HomePage();
        logger.info("SignupLoginTests pages initialized");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC01 - Register new user
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"smoke", "regression", "priority:critical"})
    @Severity(SeverityLevel.CRITICAL)
    public void TC01_RegisterUser() {
        logger.info("Starting TC01_RegisterUser");

        String email = TestDataReader.generateDynamicEmail();
        String name  = TestDataReader.getRequiredProperty("register.name");

        getDriver().get(ConfigReader.getBaseUrl() + "login");

        page.enterSignupName(name);
        page.enterSignupEmail(email);
        page.clickSignupButton();

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

        String msg = page.getAccountCreatedMessage();
        Assert.assertEquals(msg, "ACCOUNT CREATED!",
                "TC01 FAILED — Account Created message mismatch. Actual: " + msg);

        logger.info("TC01 PASSED — message: {}", msg);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC02 - Login with correct credentials
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"smoke", "regression", "priority:critical"})
    @Severity(SeverityLevel.CRITICAL)
    public void TC02_LoginWithCorrectCredentials() {
        logger.info("Starting TC02_LoginWithCorrectCredentials");

        getDriver().get(ConfigReader.getBaseUrl() + "login");

        page.enterLoginEmail(TestDataReader.getRequiredProperty("valid.email"));
        page.enterLoginPassword(TestDataReader.getRequiredProperty("valid.password"));
        page.clickLoginButton();

        String user = page.getLoggedInUsername();
        Assert.assertFalse(user.isEmpty(),
                "TC02 FAILED — Logged-in username is empty after login");

        logger.info("TC02 PASSED — logged in as: {}", user);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC03 - Login with incorrect credentials
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC03_LoginWithIncorrectCredentials() {
        logger.info("Starting TC03_LoginWithIncorrectCredentials");

        getDriver().get(ConfigReader.getBaseUrl() + "login");

        page.enterLoginEmail(TestDataReader.getRequiredProperty("invalid.email"));
        page.enterLoginPassword(TestDataReader.getRequiredProperty("invalid.password"));
        page.clickLoginButton();

        String error = page.getLoginErrorMessage();
        Assert.assertEquals(error, "Your email or password is incorrect!",
                "TC03 FAILED — Error message mismatch. Actual: " + error);

        logger.info("TC03 PASSED — error message verified");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC04 - Logout user
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"smoke", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC04_LogoutUser() {
        logger.info("Starting TC04_LogoutUser");

        getDriver().get(ConfigReader.getBaseUrl() + "login");

        page.enterLoginEmail(TestDataReader.getRequiredProperty("valid.email"));
        page.enterLoginPassword(TestDataReader.getRequiredProperty("valid.password"));
        page.clickLoginButton();

        page.clickLogoutButton();

        boolean onLoginPage   = getDriver().getCurrentUrl().contains("/login");
        boolean loginBtnShown = homePage.isDisplayed(By.cssSelector("a[href='/login']"));

        Assert.assertTrue(onLoginPage || loginBtnShown,
                "TC04 FAILED — Should be on login page or show login button after logout");

        logger.info("TC04 PASSED — user logged out successfully");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC05 - Register with existing email
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC05_RegisterWithExistingEmail() {
        logger.info("Starting TC05_RegisterWithExistingEmail");

        getDriver().get(ConfigReader.getBaseUrl() + "login");

        page.enterSignupName(TestDataReader.getRequiredProperty("register.name"));
        page.enterSignupEmail(TestDataReader.getRequiredProperty("valid.email"));
        page.clickSignupButton();

        String msg = page.getSignupErrorMessage();
        Assert.assertEquals(msg, "Email Address already exist!",
                "TC05 FAILED — Signup error message mismatch. Actual: " + msg);

        logger.info("TC05 PASSED — existing email error verified");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC06 - Contact Us form
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC06_ContactUsForm() {
        logger.info("Starting TC06_ContactUsForm");

        getDriver().get(ConfigReader.getBaseUrl() + "contact_us");

        page.fillContactForm(
                TestDataReader.getRequiredProperty("contact.name"),
                TestDataReader.getRequiredProperty("contact.email"),
                TestDataReader.getRequiredProperty("contact.subject"),
                TestDataReader.getRequiredProperty("contact.message")
        );
        page.clickSubmitContactForm();

        String msg = page.getContactSuccessMessage();
        Assert.assertTrue(msg.contains("Success"),
                "TC06 FAILED — Contact success message not found. Actual: " + msg);

        logger.info("TC06 PASSED — contact form submitted successfully");
    }
}