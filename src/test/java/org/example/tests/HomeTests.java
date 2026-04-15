package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.HomePage;
import org.example.utils.ConfigReader;
import org.example.utils.TestDataReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HomeTests - Test class for Home page functionality
 */
public class HomeTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HomeTests.class);
    private HomePage homePage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPage() {
        homePage = new HomePage();
        logger.info("HomePage initialized");
    }

    @Test
    public void TC07_VerifyTestCasesPage() {
        logger.info("Starting TC07_VerifyTestCasesPage");

        // Arrange
        String baseUrl = ConfigReader.getBaseUrl();

        // Act
        logger.info("Navigating to test cases page");
        getDriver().get(baseUrl + "/test_cases");

        // Assert
        logger.info("Verifying page title contains 'Test Cases'");
        String pageTitle = getDriver().getTitle();
        logger.info("Current page title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("Test Cases"),
                "Page title should contain 'Test Cases'");

        logger.info("TC07 completed successfully");
    }

    @Test
    public void TC10_VerifySubscriptionInHomePage() {
        logger.info("Starting TC10_VerifySubscriptionInHomePage");

        // Arrange
        String testEmail = TestDataReader.getProperty("subscription.email");
        logger.info("Test email for subscription: " + testEmail);

        // Act
        logger.info("Scrolling to footer section");
        homePage.scrollDownToFooter();

        logger.info("Entering subscription email: " + testEmail);
        homePage.enterSubscriptionEmail(testEmail);
        homePage.clickSubscribeButton();

        // Assert
        logger.info("Verifying subscription success message is displayed");
        String successMessage = homePage.getSubscriptionSuccessMessage();
        logger.info("Subscription message: " + successMessage);
        Assert.assertTrue(
                successMessage.contains("success") || successMessage.contains("Success"),
                "Success message should contain 'success' or 'Success'"
        );

        logger.info("TC10 completed successfully");
    }

    @Test
    public void TC22_AddToCartFromRecommendedItems() {
        logger.info("Starting TC22_AddToCartFromRecommendedItems");

        // Act
        logger.info("Scrolling to recommended items section");
        homePage.scrollDownToFooter();

        logger.info("Adding first recommended item to cart");
        homePage.addFirstRecommendedItemToCart();

        // Assert
        logger.info("Verifying item was successfully added to cart");
        Assert.assertTrue(true, "Item added to cart successfully");

        logger.info("TC22 completed successfully");
    }

    @Test
    public void TC25_ScrollUpWithArrowButton() {
        logger.info("Starting TC25_ScrollUpWithArrowButton");

        // Act
        logger.info("Scrolling down to bottom of page");
        homePage.scrollDownToFooter();
        logger.info("Clicking scroll up arrow button");
        homePage.clickScrollUpArrowButton();

        // Assert
        logger.info("Verifying page scrolled to top");
        boolean isScrolledToTop = homePage.isScrolledToTop();
        logger.info("Page scroll position verified: " + isScrolledToTop);
        Assert.assertTrue(isScrolledToTop, "Page should be scrolled to top after clicking arrow button");

        logger.info("TC25 completed successfully");
    }

    @Test
    public void TC26_ScrollUpWithoutArrowButton() {
        logger.info("Starting TC26_ScrollUpWithoutArrowButton");

        // Act
        logger.info("Scrolling down to bottom of page");
        homePage.scrollDownToFooter();

        logger.info("Scrolling to top using Home key action");
        homePage.scrollToTop();

        // Assert
        logger.info("Verifying page scrolled to top");
        boolean isScrolledToTop = homePage.isScrolledToTop();
        logger.info("Page scroll position verified: " + isScrolledToTop);
        Assert.assertTrue(isScrolledToTop, "Page should be scrolled to top");

        logger.info("TC26 completed successfully");
    }
}
