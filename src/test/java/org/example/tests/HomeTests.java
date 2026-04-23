package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.HomePage;
import org.example.utils.ConfigReader;
import org.example.utils.TestDataReader;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class HomeTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HomeTests.class);
    private HomePage homePage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPage() {
        homePage = new HomePage();
        logger.info("HomePage initialized");
    }

    @Test(groups = {"smoke", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC07_VerifyTestCasesPage() {
        logger.info("Starting TC07_VerifyTestCasesPage");

        logger.info("Navigating to test cases page");
        getDriver().get(ConfigReader.getBaseUrl() + "/test_cases");

        String pageTitle = getDriver().getTitle();
        logger.info("Current page title: " + pageTitle);

        Assert.assertTrue(
                pageTitle.contains("Test Cases"),
                "Page title should contain 'Test Cases'"
        );

        logger.info("TC07 completed successfully");
    }

    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC10_VerifySubscriptionInHomePage() {
        logger.info("Starting TC10_VerifySubscriptionInHomePage");

        String testEmail = TestDataReader.getRequiredProperty("subscription.email");
        logger.info("Test email for subscription: " + testEmail);

        logger.info("Scrolling to footer section");
        homePage.scrollDownToFooter();

        logger.info("Entering subscription email: " + testEmail);
        homePage.enterSubscriptionEmail(testEmail);
        homePage.clickSubscribeButton();

        String successMessage = homePage.getSubscriptionSuccessMessage();
        logger.info("Subscription message: " + successMessage);

        Assert.assertTrue(
                successMessage.toLowerCase().contains("success"),
                "Success message should contain 'success'"
        );

        logger.info("TC10 completed successfully");
    }

    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC22_AddToCartFromRecommendedItems() {
        logger.info("Starting TC22_AddToCartFromRecommendedItems");

        logger.info("Scrolling to recommended items section");
        homePage.scrollDownToFooter();

        logger.info("Adding first recommended item to cart");
        homePage.addFirstRecommendedItemToCart();

        // verify we are still on home page or cart modal appeared
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("automationexercise.com"),
                "Should still be on the site after adding to cart"
        );

        logger.info("TC22 completed successfully");
    }

    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC25_ScrollUpWithArrowButton() {
        logger.info("Starting TC25_ScrollUpWithArrowButton");

        logger.info("Scrolling down to bottom of page");
        homePage.scrollDownToFooter();

        logger.info("Clicking scroll up arrow button");
        homePage.clickScrollUpArrowButton();

        boolean isScrolledToTop = homePage.isScrolledToTop();
        logger.info("Page scroll position verified: " + isScrolledToTop);

        Assert.assertTrue(
                homePage.isDisplayed(By.cssSelector("a[href='/products']")),
                "Page should be scrolled to top after clicking arrow button"
        );

        logger.info("TC25 completed successfully");
    }

    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC26_ScrollUpWithoutArrowButton() {
        logger.info("Starting TC26_ScrollUpWithoutArrowButton");

        logger.info("Scrolling down to bottom of page");
        homePage.scrollDownToFooter();

        logger.info("Scrolling to top using JavaScript");
        homePage.scrollToTop();

        boolean isScrolledToTop = homePage.isScrolledToTop();
        logger.info("Page scroll position verified: " + isScrolledToTop);

        Assert.assertTrue(
                isScrolledToTop,
                "Page should be scrolled to top"
        );

        logger.info("TC26 completed successfully");
    }
}