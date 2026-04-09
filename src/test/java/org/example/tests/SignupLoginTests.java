package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.SignupLoginPage;
import org.example.utils.ExtentReportManager;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SignupLoginTests - Test class for Signup and Login functionality
 */
public class SignupLoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SignupLoginTests.class);

    @Test
    public void TC01_RegisterUser() {
        logger.info("Starting TC01_RegisterUser");
        ExtentReportManager.getTest().info("Register User Test");

        SignupLoginPage signupPage = new SignupLoginPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC02_LoginWithCorrectCredentials() {
        logger.info("Starting TC02_LoginWithCorrectCredentials");
        ExtentReportManager.getTest().info("Login with Correct Credentials Test");

        SignupLoginPage loginPage = new SignupLoginPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC03_LoginWithIncorrectCredentials() {
        logger.info("Starting TC03_LoginWithIncorrectCredentials");
        ExtentReportManager.getTest().info("Login with Incorrect Credentials Test");

        SignupLoginPage loginPage = new SignupLoginPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC04_LogoutUser() {
        logger.info("Starting TC04_LogoutUser");
        ExtentReportManager.getTest().info("Logout User Test");

        SignupLoginPage loginPage = new SignupLoginPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC05_RegisterWithExistingEmail() {
        logger.info("Starting TC05_RegisterWithExistingEmail");
        ExtentReportManager.getTest().info("Register with Existing Email Test");

        SignupLoginPage signupPage = new SignupLoginPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC06_ContactUsForm() {
        logger.info("Starting TC06_ContactUsForm");
        ExtentReportManager.getTest().info("Contact Us Form Test");

        SignupLoginPage contactPage = new SignupLoginPage(getDriver());
        // TODO: implement test steps
    }
}
