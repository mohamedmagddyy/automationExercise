package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.HomePage;
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


        HomePage homePage = new HomePage();
        // TODO: implement test steps
    }

    @Test
    public void TC10_VerifySubscriptionInHomePage() {
        logger.info("Starting TC10_VerifySubscriptionInHomePage");


        HomePage homePage = new HomePage();
        // TODO: implement test steps
    }

    @Test
    public void TC22_AddToCartFromRecommendedItems() {
        logger.info("Starting TC22_AddToCartFromRecommendedItems");

        HomePage homePage = new HomePage();
        // TODO: implement test steps
    }

    @Test
    public void TC25_ScrollUpWithArrowButton() {
        logger.info("Starting TC25_ScrollUpWithArrowButton");


        HomePage homePage = new HomePage();
        // TODO: implement test steps
    }

    @Test
    public void TC26_ScrollUpWithoutArrowButton() {
        logger.info("Starting TC26_ScrollUpWithoutArrowButton");


        HomePage homePage = new HomePage();
        // TODO: implement test steps
    }
}
