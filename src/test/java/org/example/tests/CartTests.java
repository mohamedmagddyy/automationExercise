package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CartPage;
import org.example.pages.ProductsPage;
import org.example.pages.SignupLoginPage;
import org.example.utils.ConfigReader;
import org.example.utils.TestDataReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class CartTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CartTests.class);

    private CartPage cartPage;
    private ProductsPage productsPage;
    private SignupLoginPage signupLoginPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPages() {
        getDriver().manage().deleteAllCookies();
        getDriver().get(ConfigReader.getBaseUrl());
        cartPage        = new CartPage();
        productsPage    = new ProductsPage();
        signupLoginPage = new SignupLoginPage();
        logger.info("CartTests pages initialized — fresh session");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper: login with valid credentials
    // ─────────────────────────────────────────────────────────────────────────
    private void loginWithValidCredentials() {
        getDriver().get(ConfigReader.getBaseUrl() + "login");
        signupLoginPage.enterLoginEmail(TestDataReader.getRequiredProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getRequiredProperty("valid.password"));
        signupLoginPage.clickLoginButton();
        logger.info("Logged in with valid credentials");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper: fill payment form and confirm order
    // ─────────────────────────────────────────────────────────────────────────
    private void fillPaymentAndConfirm() {
        cartPage.enterCardName(TestDataReader.getRequiredProperty("card.name"));
        cartPage.enterCardNumber(TestDataReader.getRequiredProperty("card.number"));
        cartPage.enterCardCVC(TestDataReader.getRequiredProperty("card.cvc"));
        cartPage.enterCardExpiryMonth(TestDataReader.getRequiredProperty("card.expiryMonth"));
        cartPage.enterCardExpiryYear(TestDataReader.getRequiredProperty("card.expiryYear"));
        cartPage.clickConfirmOrder();
        logger.info("Payment form filled and order confirmed");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper: assert order placed message
    // ─────────────────────────────────────────────────────────────────────────
    private void assertOrderPlaced(String testName) {
        String msg = cartPage.getOrderPlacedMessage();
        Assert.assertTrue(
                msg.toLowerCase().contains("placed") || msg.toLowerCase().contains("order"),
                testName + " FAILED — Order placed message not found. Actual: " + msg);
        logger.info("{} PASSED — order message: {}", testName, msg);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC11 - Verify subscription in Cart page
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC11_VerifySubscriptionInCartPage() {
        logger.info("Starting TC11_VerifySubscriptionInCartPage");

        cartPage.navigateToCart();
        cartPage.enterSubscriptionEmail(TestDataReader.getRequiredProperty("subscription.email"));
        cartPage.clickSubscribeButton();

        String msg = cartPage.getSubscriptionSuccessMessage();
        Assert.assertTrue(
                msg.toLowerCase().contains("success"),
                "TC11 FAILED — Subscription success message not found. Actual: " + msg);

        logger.info("TC11 PASSED — message: {}", msg);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC12 - Add two products to cart and verify
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"smoke", "regression", "priority:critical"})
    @Severity(SeverityLevel.CRITICAL)
    public void TC12_AddProductsInCart() {
        logger.info("Starting TC12_AddProductsInCart");

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickContinueShopping();

        productsPage.addProductToCartByIndex(2);
        productsPage.clickViewCart();

        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= 2,
                "TC12 FAILED — Cart should have at least 2 items. Actual: " + itemCount);

        logger.info("TC12 PASSED — cart items: {}", itemCount);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC13 - Verify product quantity in cart
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC13_VerifyProductQuantityInCart() {
        logger.info("Starting TC13_VerifyProductQuantityInCart");

        productsPage.navigateToProductsPage();
        productsPage.clickViewProductByIndex(1);
        productsPage.setProductQuantity(5);
        productsPage.clickAddToCart();
        productsPage.clickViewCart();

        int qty = cartPage.getProductQuantityByRow(1);
        Assert.assertEquals(qty, 5,
                "TC13 FAILED — Product quantity should be 5. Actual: " + qty);

        logger.info("TC13 PASSED — quantity: {}", qty);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC14 - Place order: register while checkout
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:critical"})
    @Severity(SeverityLevel.CRITICAL)
    public void TC14_PlaceOrderRegisterWhileCheckout() {
        logger.info("Starting TC14_PlaceOrderRegisterWhileCheckout");

        String email = TestDataReader.generateDynamicEmail();

        // أضف منتج للكارت
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();

        // checkout → modal → register
        cartPage.clickProceedToCheckout();
        cartPage.clickLoginFromModal();

        signupLoginPage.enterSignupName(TestDataReader.getRequiredProperty("register.name"));
        signupLoginPage.enterSignupEmail(email);
        signupLoginPage.clickSignupButton();

        signupLoginPage.fillRegistrationForm(
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
        signupLoginPage.clickCreateAccountButton();
        signupLoginPage.clickContinueAfterAccount();

        // ارجع للكارت وأكمل الأوردر
        cartPage.navigateToCart();
        cartPage.clickProceedToCheckout();
        cartPage.enterOrderComment(TestDataReader.getRequiredProperty("order.comment.delivery.time"));
        cartPage.clickPlaceOrder();

        fillPaymentAndConfirm();
        assertOrderPlaced("TC14");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC15 - Place order: register before checkout
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:critical"})
    @Severity(SeverityLevel.CRITICAL)
    public void TC15_PlaceOrderRegisterBeforeCheckout() {
        logger.info("Starting TC15_PlaceOrderRegisterBeforeCheckout");

        String email = TestDataReader.generateDynamicEmail();

        // register أولاً
        getDriver().get(ConfigReader.getBaseUrl() + "login");
        signupLoginPage.enterSignupName(TestDataReader.getRequiredProperty("register.name"));
        signupLoginPage.enterSignupEmail(email);
        signupLoginPage.clickSignupButton();

        signupLoginPage.fillRegistrationForm(
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
        signupLoginPage.clickCreateAccountButton();
        signupLoginPage.clickContinueAfterAccount();

        // أضف منتج وأكمل الأوردر
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();
        cartPage.enterOrderComment(TestDataReader.getRequiredProperty("order.comment.safe.delivery"));
        cartPage.clickPlaceOrder();

        fillPaymentAndConfirm();
        assertOrderPlaced("TC15");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC16 - Place order: login before checkout
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"smoke", "regression", "priority:critical"})
    @Severity(SeverityLevel.CRITICAL)
    public void TC16_PlaceOrderLoginBeforeCheckout() {
        logger.info("Starting TC16_PlaceOrderLoginBeforeCheckout");

        loginWithValidCredentials();

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();

        String deliveryAddress = cartPage.getDeliveryAddress();
        Assert.assertFalse(deliveryAddress.isEmpty(),
                "TC16 FAILED — Delivery address should not be empty");

        cartPage.enterOrderComment(TestDataReader.getRequiredProperty("order.comment.convenience"));
        cartPage.clickPlaceOrder();

        fillPaymentAndConfirm();
        assertOrderPlaced("TC16");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC17 - Remove product from cart
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC17_RemoveProductsFromCart() {
        logger.info("Starting TC17_RemoveProductsFromCart");

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();

        int before = cartPage.getCartItemCount();
        cartPage.removeProductByRow(1);
        int after = cartPage.getCartItemCount();

        Assert.assertTrue(after < before,
                "TC17 FAILED — Cart count should decrease after removal. Before: " + before + ", After: " + after);

        logger.info("TC17 PASSED — before: {}, after: {}", before, after);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC23 - Verify address details in checkout
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC23_VerifyAddressDetailsInCheckout() {
        logger.info("Starting TC23_VerifyAddressDetailsInCheckout");

        loginWithValidCredentials();

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();

        String delivery = cartPage.getDeliveryAddress();
        String billing  = cartPage.getBillingAddress();

        Assert.assertFalse(delivery.isEmpty(),
                "TC23 FAILED — Delivery address should not be empty");
        Assert.assertFalse(billing.isEmpty(),
                "TC23 FAILED — Billing address should not be empty");

        logger.info("TC23 PASSED — delivery and billing addresses verified");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC24 - Download invoice after purchase
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC24_DownloadInvoiceAfterPurchase() {
        logger.info("Starting TC24_DownloadInvoiceAfterPurchase");

        loginWithValidCredentials();

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();
        cartPage.enterOrderComment(TestDataReader.getRequiredProperty("order.comment.test"));
        cartPage.clickPlaceOrder();

        fillPaymentAndConfirm();
        assertOrderPlaced("TC24");

        cartPage.clickDownloadInvoice();

        logger.info("TC24 PASSED — invoice download clicked");
    }
}