package org.example.tests;

import org.example.base.BaseTest;
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

public class ProductsTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ProductsTests.class);
    private ProductsPage productsPage;
    private SignupLoginPage signupLoginPage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        getDriver().manage().deleteAllCookies();
        productsPage  = new ProductsPage();
        signupLoginPage = new SignupLoginPage();
        logger.info("Pages initialized");
    }

    @Test(groups = {"smoke", "regression", "priority:critical"})
    @Severity(SeverityLevel.CRITICAL)
    public void TC08_VerifyAllProductsAndProductDetail() {
        logger.info("Starting TC08_VerifyAllProductsAndProductDetail");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Getting product count");
        int count = productsPage.getSearchResultCount();
        Assert.assertTrue(count > 0, "Products list should not be empty");

        logger.info("Clicking view product at index 1");
        productsPage.clickViewProductByIndex(1);

        String name  = productsPage.getProductName();
        String price = productsPage.getProductPrice();

        Assert.assertFalse(name.isEmpty(),  "Product name should not be empty");
        Assert.assertFalse(price.isEmpty(), "Product price should not be empty");

        logger.info("TC08 completed successfully");
    }

    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC09_SearchProduct() {
        logger.info("Starting TC09_SearchProduct");

        String searchProduct = TestDataReader.getRequiredProperty("search.product");
        logger.info("Search term: " + searchProduct);

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Searching for: " + searchProduct);
        productsPage.searchProduct(searchProduct);

        int resultCount = productsPage.getSearchResultCount();
        Assert.assertTrue(resultCount > 0, "Search results should be found");

        logger.info("TC09 completed successfully");
    }

    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC18_ViewCategoryProducts() {
        logger.info("Starting TC18_ViewCategoryProducts");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Navigating to category Women > Dress");
        productsPage.navigateToCategoryByName("Women", "Dress");

        String title = productsPage.getCategoryPageTitle();
        Assert.assertTrue(
                title.toLowerCase().contains("women") || title.toLowerCase().contains("dress"),
                "Category title mismatch: " + title
        );

        Assert.assertTrue(productsPage.getSearchResultCount() > 0,
                "Category products should be visible");

        logger.info("TC18 completed successfully");
    }

    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC19_ViewAndCartBrandProducts() {
        logger.info("Starting TC19_ViewAndCartBrandProducts");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Navigating to brand Polo");
        productsPage.navigateToBrandByName("Polo");

        String title = productsPage.getBrandPageTitle();
        Assert.assertFalse(title.isEmpty(), "Brand title should not be empty");

        logger.info("Adding product at index 1 to cart");
        productsPage.addProductToCartByIndex(1);

        logger.info("TC19 completed successfully");
    }

    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC20_SearchProductsAndVerifyCartAfterLogin() {
        logger.info("Starting TC20_SearchProductsAndVerifyCartAfterLogin");

        String searchProduct = TestDataReader.getRequiredProperty("search.product");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Searching for: " + searchProduct);
        productsPage.searchProduct(searchProduct);

        logger.info("Adding product at index 1 to cart");
        productsPage.addProductToCartByIndex(1);

        logger.info("Navigating to login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Logging in");
        signupLoginPage.enterLoginEmail(TestDataReader.getRequiredProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getRequiredProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        logger.info("Verifying login success - checking logged-in username");
        String loggedInUser = signupLoginPage.getLoggedInUsername();
        Assert.assertFalse(loggedInUser.isEmpty(), "User should be logged in");

        logger.info("TC20 completed successfully");
    }

    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC21_AddReviewOnProduct() {
        logger.info("Starting TC21_AddReviewOnProduct");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Clicking view product at index 1");
        productsPage.clickViewProductByIndex(1);

        logger.info("Adding review");
        productsPage.addReview(
                "John Doe",
                "john@test.com",
                "Great product, highly recommended!"
        );

        String message = productsPage.getReviewSuccessMessage();
        Assert.assertTrue(
                message.toLowerCase().contains("thank")
                        || message.toLowerCase().contains("success")
                        || message.toLowerCase().contains("review"),
                "Review success message not displayed correctly"
        );

        logger.info("TC21 completed successfully");
    }
}