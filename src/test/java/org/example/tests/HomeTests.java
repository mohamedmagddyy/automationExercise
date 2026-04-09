package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.HomePage;
import org.example.utils.ExtentReportManager;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HomeTests - Test class for Home page functionality
 */
public class HomeTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HomeTests.class);

    @Test
    public void TC07_VerifyTestCasesPage() {
        logger.info("Starting TC07_VerifyTestCasesPage");
        ExtentReportManager.getTest().info("Verify Test Cases Page Test");

        HomePage homePage = new HomePage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC10_VerifySubscriptionInHomePage() {
        logger.info("Starting TC10_VerifySubscriptionInHomePage");
        ExtentReportManager.getTest().info("Verify Subscription in Home Page Test");

        HomePage homePage = new HomePage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC22_AddToCartFromRecommendedItems() {
        logger.info("Starting TC22_AddToCartFromRecommendedItems");
        ExtentReportManager.getTest().info("Add to Cart from Recommended Items Test");

        HomePage homePage = new HomePage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC25_ScrollUpWithArrowButton() {
        logger.info("Starting TC25_ScrollUpWithArrowButton");
        ExtentReportManager.getTest().info("Scroll Up with Arrow Button Test");

        HomePage homePage = new HomePage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC26_ScrollUpWithoutArrowButton() {
        logger.info("Starting TC26_ScrollUpWithoutArrowButton");
        ExtentReportManager.getTest().info("Scroll Up without Arrow Button Test");

        HomePage homePage = new HomePage(getDriver());
        // TODO: implement test steps
    }
}
