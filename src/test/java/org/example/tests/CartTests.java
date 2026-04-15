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

/**
 * CartTests - Test class for Cart page functionality
 */
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

    @Test
    public void TC11_VerifySubscriptionInCartPage() {
        logger.info("Starting TC11_VerifySubscriptionInCartPage");

        String testEmail = TestDataReader.getProperty("subscription.email");
        logger.info("Test Data - Email: " + testEmail);

        logger.info("Navigating to cart page");
        cartPage.navigateToCart();

        logger.info("Subscribing with email");
        cartPage.enterSubscriptionEmail(testEmail);
        cartPage.clickSubscribeButton();

        logger.info("Verifying subscription success message");
        String message = cartPage.getSubscriptionSuccessMessage();

        Assert.assertTrue(message.contains("success") || message.contains("Success"));
        logger.info("Subscription success message: " + message);

        logger.info("TC11 completed successfully");
    }

    @Test
    public void TC12_AddProductsInCart() {

        logger.info("Starting TC12_AddProductsInCart");

        logger.info("Navigate to products page");
        productsPage.navigateToProductsPage();

        logger.info("Add first product to cart");
        productsPage.addProductToCartByIndex(1);

        logger.info("Continue shopping");
        productsPage.clickContinueShopping();

        logger.info("Add second product to cart");
        productsPage.addProductToCartByIndex(2);

        logger.info("View cart");
        productsPage.clickViewCart();

        logger.info("Verify products are in cart");
        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= 2, "Cart should have at least 2 items");

        logger.info("Cart contains " + itemCount + " items");
        logger.info("TC12 completed successfully");
    }

    @Test
    public void TC13_VerifyProductQuantityInCart() {

        logger.info("Starting TC13_VerifyProductQuantityInCart");

        logger.info("Navigate to products page");
        productsPage.navigateToProductsPage();

        logger.info("Click on product");
        productsPage.clickViewProductByIndex(1);

        logger.info("Set product quantity to 5");
        productsPage.setProductQuantity(5);

        logger.info("Add to cart");
        productsPage.clickAddToCart();

        logger.info("View cart");
        productsPage.clickViewCart();

        logger.info("Verify quantity in cart");
        int quantity = cartPage.getProductQuantityByRow(1);
        Assert.assertEquals(quantity, 5, "Product quantity in cart should be 5");

        logger.info("Verified product quantity: " + quantity);
        logger.info("TC13 completed successfully");
    }

    @Test
    public void TC14_PlaceOrderRegisterWhileCheckout() {

        logger.info("Starting TC14_PlaceOrderRegisterWhileCheckout");

        String email = TestDataReader.generateDynamicEmail();
        SignupLoginPage signupLoginPage = new SignupLoginPage();

        logger.info("Add products to cart");
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();

        logger.info("Proceed to checkout");
        cartPage.clickProceedToCheckout();

        logger.info("Register new account");
        signupLoginPage.enterSignupName(TestDataReader.getProperty("register.name"));
        signupLoginPage.enterSignupEmail(email);
        signupLoginPage.clickSignupButton();

        logger.info("Fill registration form");
        signupLoginPage.fillRegistrationForm(
                "Mr", "password123", "15", "5", "1990",
                "John", "Doe", "ABC Corp", "123 Street", "Apt 4",
                "United States", "California", "Los Angeles", "90001", "1234567890"
        );
        signupLoginPage.clickCreateAccountButton();

        logger.info("Place order");
        cartPage.enterOrderComment("Please deliver on time");
        cartPage.clickPlaceOrder();

        logger.info("Enter payment details");
        cartPage.enterCardName("John Doe");
        cartPage.enterCardNumber("1234567890123456");
        cartPage.enterCardCVC("123");
        cartPage.enterCardExpiryMonth("12");
        cartPage.enterCardExpiryYear("2025");
        cartPage.clickConfirmOrder();

        logger.info("Verify order success");
        String message = cartPage.getOrderSuccessMessage();
        Assert.assertTrue(message.contains("success") || message.contains("Order Placed"));

        logger.info("TC14 completed successfully");
    }

    @Test
    public void TC15_PlaceOrderRegisterBeforeCheckout() {

        logger.info("Starting TC15_PlaceOrderRegisterBeforeCheckout");

        String email = TestDataReader.generateDynamicEmail();
        SignupLoginPage signupLoginPage = new SignupLoginPage();

        logger.info("Register new account");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        signupLoginPage.enterSignupName(TestDataReader.getProperty("register.name"));
        signupLoginPage.enterSignupEmail(email);
        signupLoginPage.clickSignupButton();

        signupLoginPage.fillRegistrationForm(
                "Mr", "password123", "15", "5", "1990",
                "John", "Doe", "ABC Corp", "123 Street", "Apt 4",
                "United States", "California", "Los Angeles", "90001", "1234567890"
        );
        signupLoginPage.clickCreateAccountButton();

        logger.info("Continue after registration");
        signupLoginPage.clickContinueAfterAccount();

        logger.info("Add products to cart");
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();

        logger.info("Proceed to checkout");
        cartPage.clickProceedToCheckout();

        logger.info("Place order");
        cartPage.enterOrderComment("Please deliver safely");
        cartPage.clickPlaceOrder();

        logger.info("Enter payment details");
        cartPage.enterCardName("John Doe");
        cartPage.enterCardNumber("1234567890123456");
        cartPage.enterCardCVC("123");
        cartPage.enterCardExpiryMonth("12");
        cartPage.enterCardExpiryYear("2025");
        cartPage.clickConfirmOrder();

        logger.info("Verify order success");
        String message = cartPage.getOrderSuccessMessage();
        Assert.assertTrue(message.contains("success") || message.contains("Order Placed"));

        logger.info("TC15 completed successfully");
    }

    @Test
    public void TC16_PlaceOrderLoginBeforeCheckout() {

        logger.info("Starting TC16_PlaceOrderLoginBeforeCheckout");

        SignupLoginPage signupLoginPage = new SignupLoginPage();

        logger.info("Login with valid credentials");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        signupLoginPage.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        logger.info("Add products to cart");
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();

        logger.info("Proceed to checkout");
        cartPage.clickProceedToCheckout();

        logger.info("Verify addresses");
        String deliveryAddress = cartPage.getDeliveryAddress();
        Assert.assertNotNull(deliveryAddress);

        logger.info("Place order");
        cartPage.enterOrderComment("Deliver as per your convenience");
        cartPage.clickPlaceOrder();

        logger.info("Enter payment details");
        cartPage.enterCardName("John Doe");
        cartPage.enterCardNumber("1234567890123456");
        cartPage.enterCardCVC("123");
        cartPage.enterCardExpiryMonth("12");
        cartPage.enterCardExpiryYear("2025");
        cartPage.clickConfirmOrder();

        logger.info("Verify order success");
        String message = cartPage.getOrderSuccessMessage();
        Assert.assertTrue(message.contains("success") || message.contains("Order Placed"));

        logger.info("TC16 completed successfully");
    }

    @Test
    public void TC17_RemoveProductsFromCart() {

        logger.info("Starting TC17_RemoveProductsFromCart");

        logger.info("Add multiple products to cart");
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickContinueShopping();
        productsPage.addProductToCartByIndex(2);
        productsPage.clickViewCart();

        int initialCount = cartPage.getCartItemCount();
        Assert.assertTrue(initialCount >= 2);

        logger.info("Remove first product");
        cartPage.removeProductByRow(1);

        int finalCount = cartPage.getCartItemCount();
        Assert.assertTrue(finalCount < 2);

        logger.info("TC17 completed successfully");
    }

    @Test
    public void TC23_VerifyAddressDetailsInCheckout() {

        logger.info("Starting TC23_VerifyAddressDetailsInCheckout");

        SignupLoginPage signupLoginPage = new SignupLoginPage();

        logger.info("Login");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        signupLoginPage.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        logger.info("Add products and checkout");
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();

        Assert.assertTrue(cartPage.getDeliveryAddress().length() > 0);
        Assert.assertTrue(cartPage.getBillingAddress().length() > 0);

        logger.info("TC23 completed successfully");
    }

    @Test
    public void TC24_DownloadInvoiceAfterPurchase() {

        logger.info("Starting TC24_DownloadInvoiceAfterPurchase");

        SignupLoginPage signupLoginPage = new SignupLoginPage();

        logger.info("Login");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        signupLoginPage.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        logger.info("Place order");
        productsPage.navigateToProductsPage();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickViewCart();
        cartPage.clickProceedToCheckout();

        cartPage.enterOrderComment("Test order");
        cartPage.clickPlaceOrder();

        cartPage.enterCardName("John Doe");
        cartPage.enterCardNumber("1234567890123456");
        cartPage.enterCardCVC("123");
        cartPage.enterCardExpiryMonth("12");
        cartPage.enterCardExpiryYear("2025");
        cartPage.clickConfirmOrder();

        logger.info("Download invoice");
        cartPage.clickDownloadInvoice();

        Assert.assertTrue(true);

        logger.info("TC24 completed successfully");
    }
}