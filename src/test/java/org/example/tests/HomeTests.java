package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CartPage;
import org.example.pages.HomePage;
import org.example.utils.ConfigReader;
import org.example.utils.TestDataReader;
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
    public void initPages() {
        homePage = new HomePage();
        logger.info("HomeTests pages initialized");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC07 - Verify Test Cases page
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"smoke", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC07_VerifyTestCasesPage() {
        logger.info("Starting TC07_VerifyTestCasesPage");

        getDriver().get(ConfigReader.getBaseUrl() + "test_cases");

        String title = getDriver().getTitle();
        Assert.assertTrue(title.contains("Test Cases"),
                "TC07 FAILED — Page title should contain 'Test Cases'. Actual: " + title);

        logger.info("TC07 PASSED — title: {}", title);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC10 - Verify subscription in Home page
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC10_VerifySubscriptionInHomePage() {
        logger.info("Starting TC10_VerifySubscriptionInHomePage");

        homePage.scrollDownToFooter();
        homePage.enterSubscriptionEmail(TestDataReader.getRequiredProperty("subscription.email"));
        homePage.clickSubscribeButton();

        String msg = homePage.getSubscriptionSuccessMessage();
        Assert.assertTrue(msg.toLowerCase().contains("success"),
                "TC10 FAILED — Subscription success message not found. Actual: " + msg);

        logger.info("TC10 PASSED — message: {}", msg);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC22 - Add to cart from recommended items
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC22_AddToCartFromRecommendedItems() {
        logger.info("Starting TC22_AddToCartFromRecommendedItems");

        homePage.scrollDownToFooter();
        homePage.addFirstRecommendedItemToCart();

        getDriver().get(ConfigReader.getBaseUrl() + "view_cart");

        CartPage cartPage = new CartPage();
        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount > 0,
                "TC22 FAILED — Cart should have at least one item after adding recommended product");

        logger.info("TC22 PASSED — cart items: {}", itemCount);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC25 - Scroll up using arrow button
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC25_ScrollUpWithArrowButton() {
        logger.info("Starting TC25_ScrollUpWithArrowButton");

        homePage.scrollDownToFooter();
        homePage.clickScrollUpArrowButton();

        // نستنى شوية عشان الـ scroll animation يخلص
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}

        Assert.assertTrue(homePage.isNavigationVisible(),
                "TC25 FAILED — Products nav link should be visible after scrolling to top");

        logger.info("TC25 PASSED — page scrolled to top via arrow button");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC26 - Scroll up without arrow button (JavaScript)
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC26_ScrollUpWithoutArrowButton() {
        logger.info("Starting TC26_ScrollUpWithoutArrowButton");

        homePage.scrollDownToFooter();
        homePage.scrollPageToTop();

        Assert.assertTrue(homePage.isScrolledToTop(),
                "TC26 FAILED — Page should be at scroll position 0 after scrollToTop()");

        logger.info("TC26 PASSED — page scrolled to top via JavaScript");
    }
}