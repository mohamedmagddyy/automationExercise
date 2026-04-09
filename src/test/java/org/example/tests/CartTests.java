package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CartPage;
import org.example.utils.ExtentReportManager;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CartTests - Test class for Cart page functionality
 */
public class CartTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CartTests.class);

    @Test
    public void TC11_VerifySubscriptionInCartPage() {
        logger.info("Starting TC11_VerifySubscriptionInCartPage");
        ExtentReportManager.getTest().info("Verify Subscription in Cart Page Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC12_AddProductsInCart() {
        logger.info("Starting TC12_AddProductsInCart");
        ExtentReportManager.getTest().info("Add Products in Cart Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC13_VerifyProductQuantityInCart() {
        logger.info("Starting TC13_VerifyProductQuantityInCart");
        ExtentReportManager.getTest().info("Verify Product Quantity in Cart Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC14_PlaceOrderRegisterWhileCheckout() {
        logger.info("Starting TC14_PlaceOrderRegisterWhileCheckout");
        ExtentReportManager.getTest().info("Place Order Register While Checkout Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC15_PlaceOrderRegisterBeforeCheckout() {
        logger.info("Starting TC15_PlaceOrderRegisterBeforeCheckout");
        ExtentReportManager.getTest().info("Place Order Register Before Checkout Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC16_PlaceOrderLoginBeforeCheckout() {
        logger.info("Starting TC16_PlaceOrderLoginBeforeCheckout");
        ExtentReportManager.getTest().info("Place Order Login Before Checkout Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC17_RemoveProductsFromCart() {
        logger.info("Starting TC17_RemoveProductsFromCart");
        ExtentReportManager.getTest().info("Remove Products From Cart Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC23_VerifyAddressDetailsInCheckout() {
        logger.info("Starting TC23_VerifyAddressDetailsInCheckout");
        ExtentReportManager.getTest().info("Verify Address Details in Checkout Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC24_DownloadInvoiceAfterPurchase() {
        logger.info("Starting TC24_DownloadInvoiceAfterPurchase");
        ExtentReportManager.getTest().info("Download Invoice After Purchase Test");

        CartPage cartPage = new CartPage(getDriver());
        // TODO: implement test steps
    }
}
