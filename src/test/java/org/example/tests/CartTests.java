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

public class CartTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CartTests.class);
    private CartPage cartPage;
    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPage() {
        cartPage = new CartPage();
        productsPage = new ProductsPage();
        logger.info("CartPage and ProductsPage initialized");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC11 - Subscription في الـ footer من صفحة الكارت
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC11_VerifySubscriptionInCartPage() {
        logger.info("Starting TC11");

        cartPage.navigateToCart();

        cartPage.enterSubscriptionEmail(TestDataReader.getProperty("subscription.email"));
        cartPage.clickSubscribeButton();

        String message = cartPage.getSubscriptionSuccessMessage();
        Assert.assertTrue(
                message.contains("success") || message.contains("Success"),
                "Subscription success message not found"
        );

        logger.info("TC11 completed - message: " + message);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC12 - إضافة منتجين للكارت والتحقق
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC12_AddProductsInCart() {
        logger.info("Starting TC12");

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickContinueShopping();

        productsPage.addProductToCartByIndex(2);
        productsPage.clickViewCart();

        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= 2, "Cart should have at least 2 items");

        logger.info("TC12 completed - items: " + itemCount);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC13 - التحقق من الكمية في الكارت
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC13_VerifyProductQuantityInCart() {
        logger.info("Starting TC13");

        productsPage.navigateToProductsPage();
        productsPage.clickViewProductByIndex(1);
        productsPage.setProductQuantity(5);
        productsPage.clickAddToCart();
        productsPage.clickViewCart();

        int quantity = cartPage.getProductQuantityByRow(1);
        Assert.assertEquals(quantity, 5, "Product quantity should be 5");

        logger.info("TC13 completed - quantity: " + quantity);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC14 - الأوردر مع التسجيل أثناء الـ checkout
    // الـ flow: كارت ← checkout ← modal ← login/register ← ارجع كارت ← أوردر
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC14_PlaceOrderRegisterWhileCheckout() {
        logger.info("Starting TC14");

        String email = TestDataReader.generateDynamicEmail();
        SignupLoginPage signupLoginPage = new SignupLoginPage();

        // أضف منتج للكارت
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();

        // اضغط checkout - هيطلع modal لأنك مش logged in
        cartPage.clickProceedToCheckout();

        // من الـ modal اضغط Register/Login
        cartPage.clickLoginFromModal();

        // سجل اكانت جديد
        signupLoginPage.enterSignupName(TestDataReader.getProperty("register.name"));
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

        cartPage.enterCardName(TestDataReader.getRequiredProperty("card.name"));
        cartPage.enterCardNumber(TestDataReader.getRequiredProperty("card.number"));
        cartPage.enterCardCVC(TestDataReader.getRequiredProperty("card.cvc"));
        cartPage.enterCardExpiryMonth(TestDataReader.getRequiredProperty("card.expiryMonth"));
        cartPage.enterCardExpiryYear(TestDataReader.getRequiredProperty("card.expiryYear"));
        cartPage.clickConfirmOrder();

        String message = cartPage.getOrderPlacedMessage();
        Assert.assertTrue(
                message.contains("Order Placed") || message.contains("placed"),
                "Order placed message not found"
        );

        logger.info("TC14 completed - message: " + message);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC15 - التسجيل قبل الـ checkout
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC15_PlaceOrderRegisterBeforeCheckout() {
        logger.info("Starting TC15");

        String email = TestDataReader.generateDynamicEmail();
        SignupLoginPage signupLoginPage = new SignupLoginPage();

        // سجل اكانت جديد
        getDriver().get(ConfigReader.getBaseUrl() + "/login");
        signupLoginPage.enterSignupName(TestDataReader.getProperty("register.name"));
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

        cartPage.enterCardName(TestDataReader.getRequiredProperty("card.name"));
        cartPage.enterCardNumber(TestDataReader.getRequiredProperty("card.number"));
        cartPage.enterCardCVC(TestDataReader.getRequiredProperty("card.cvc"));
        cartPage.enterCardExpiryMonth(TestDataReader.getRequiredProperty("card.expiryMonth"));
        cartPage.enterCardExpiryYear(TestDataReader.getRequiredProperty("card.expiryYear"));
        cartPage.clickConfirmOrder();

        String message = cartPage.getOrderPlacedMessage();
        Assert.assertTrue(
                message.contains("Order Placed") || message.contains("placed"),
                "Order placed message not found"
        );

        logger.info("TC15 completed - message: " + message);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC16 - اللوجين قبل الـ checkout
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC16_PlaceOrderLoginBeforeCheckout() {
        logger.info("Starting TC16");

        SignupLoginPage signupLoginPage = new SignupLoginPage();

        getDriver().get(ConfigReader.getBaseUrl() + "/login");
        signupLoginPage.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();

        Assert.assertFalse(cartPage.getDeliveryAddress().isEmpty(), "Delivery address should not be empty");

        cartPage.enterOrderComment(TestDataReader.getRequiredProperty("order.comment.convenience"));
        cartPage.clickPlaceOrder();

        cartPage.enterCardName(TestDataReader.getRequiredProperty("card.name"));
        cartPage.enterCardNumber(TestDataReader.getRequiredProperty("card.number"));
        cartPage.enterCardCVC(TestDataReader.getRequiredProperty("card.cvc"));
        cartPage.enterCardExpiryMonth(TestDataReader.getRequiredProperty("card.expiryMonth"));
        cartPage.enterCardExpiryYear(TestDataReader.getRequiredProperty("card.expiryYear"));
        cartPage.clickConfirmOrder();

        String message = cartPage.getOrderPlacedMessage();
        Assert.assertTrue(
                message.contains("Order Placed") || message.contains("placed"),
                "Order placed message not found"
        );

        logger.info("TC16 completed - message: " + message);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC17 - حذف منتج من الكارت
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC17_RemoveProductsFromCart() {
        logger.info("Starting TC17");

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickContinueShopping();
        productsPage.addProductToCartByIndex(2);
        productsPage.clickViewCart();

        int initialCount = cartPage.getCartItemCount();
        Assert.assertTrue(initialCount >= 2, "Cart should have at least 2 items before removal");

        cartPage.removeProductByRow(1);

        int finalCount = cartPage.getCartItemCount();
        Assert.assertEquals(finalCount, initialCount - 1, "Cart should have one less item after removal");

        logger.info("TC17 completed - before: " + initialCount + " after: " + finalCount);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC23 - التحقق من العناوين في الـ checkout
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC23_VerifyAddressDetailsInCheckout() {
        logger.info("Starting TC23");

        SignupLoginPage signupLoginPage = new SignupLoginPage();

        getDriver().get(ConfigReader.getBaseUrl() + "/login");
        signupLoginPage.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();

        Assert.assertFalse(cartPage.getDeliveryAddress().isEmpty(), "Delivery address should not be empty");
        Assert.assertFalse(cartPage.getBillingAddress().isEmpty(), "Billing address should not be empty");

        logger.info("TC23 completed successfully");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC24 - تحميل الفاتورة بعد الأوردر
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    public void TC24_DownloadInvoiceAfterPurchase() {
        logger.info("Starting TC24");

        SignupLoginPage signupLoginPage = new SignupLoginPage();

        getDriver().get(ConfigReader.getBaseUrl() + "/login");
        signupLoginPage.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();

        cartPage.enterOrderComment(TestDataReader.getRequiredProperty("order.comment.test"));
        cartPage.clickPlaceOrder();

        cartPage.enterCardName(TestDataReader.getRequiredProperty("card.name"));
        cartPage.enterCardNumber(TestDataReader.getRequiredProperty("card.number"));
        cartPage.enterCardCVC(TestDataReader.getRequiredProperty("card.cvc"));
        cartPage.enterCardExpiryMonth(TestDataReader.getRequiredProperty("card.expiryMonth"));
        cartPage.enterCardExpiryYear(TestDataReader.getRequiredProperty("card.expiryYear"));
        cartPage.clickConfirmOrder();

        String message = cartPage.getOrderPlacedMessage();
        Assert.assertTrue(
                message.contains("Order Placed") || message.contains("placed"),
                "Order placed message not found"
        );

        cartPage.clickDownloadInvoice();

        logger.info("TC24 completed successfully");
    }
}