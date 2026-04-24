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

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPages() {
        getDriver().manage().deleteAllCookies();
        productsPage    = new ProductsPage();
        signupLoginPage = new SignupLoginPage();
        logger.info("ProductsTests pages initialized");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC08 - Verify all products and product detail
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"smoke", "regression", "priority:critical"})
    @Severity(SeverityLevel.CRITICAL)
    public void TC08_VerifyAllProductsAndProductDetail() {
        logger.info("Starting TC08_VerifyAllProductsAndProductDetail");

        productsPage.navigateToProductsPage();

        int count = productsPage.getSearchResultCount();
        Assert.assertTrue(count > 0,
                "TC08 FAILED — Products list should not be empty");

        productsPage.clickViewProductByIndex(1);

        String name  = productsPage.getProductName();
        String price = productsPage.getProductPrice();

        Assert.assertFalse(name.isEmpty(),
                "TC08 FAILED — Product name should not be empty");
        Assert.assertFalse(price.isEmpty(),
                "TC08 FAILED — Product price should not be empty");

        logger.info("TC08 PASSED — products: {}, name: {}, price: {}", count, name, price);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC09 - Search product
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC09_SearchProduct() {
        logger.info("Starting TC09_SearchProduct");

        String searchTerm = TestDataReader.getRequiredProperty("search.product");

        productsPage.navigateToProductsPage();
        productsPage.searchProduct(searchTerm);

        int resultCount = productsPage.getSearchResultCount();
        Assert.assertTrue(resultCount > 0,
                "TC09 FAILED — Search results should not be empty for: " + searchTerm);

        logger.info("TC09 PASSED — found {} results for '{}'", resultCount, searchTerm);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC18 - View category products
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC18_ViewCategoryProducts() {
        logger.info("Starting TC18_ViewCategoryProducts");

        productsPage.navigateToProductsPage();
        productsPage.navigateToCategoryByName("Women", "Dress");

        String title = productsPage.getCategoryPageTitle();
        String normalizedTitle = title.trim().toLowerCase();

        Assert.assertTrue(
                normalizedTitle.contains("women") || normalizedTitle.contains("dress"),
                "TC18 FAILED — Category title mismatch. Actual: " + title
        );

        int count = productsPage.getSearchResultCount();
        Assert.assertTrue(count > 0,
                "TC18 FAILED — Category products should not be empty");

        logger.info("TC18 PASSED — title: {}, products: {}", title, count);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC19 - View and cart brand products
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC19_ViewAndCartBrandProducts() {
        logger.info("Starting TC19_ViewAndCartBrandProducts");

        productsPage.navigateToProductsPage();
        productsPage.navigateToBrandByName("Polo");

        String title = productsPage.getBrandPageTitle();
        Assert.assertFalse(title.isEmpty(),
                "TC19 FAILED — Brand page title should not be empty");

        int count = productsPage.getSearchResultCount();
        Assert.assertTrue(count > 0,
                "TC19 FAILED — Brand products should not be empty");

        logger.info("TC19 PASSED — brand: {}, products: {}", title, count);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC20 - Search products and verify cart after login
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "regression", "priority:high"})
    @Severity(SeverityLevel.NORMAL)
    public void TC20_SearchProductsAndVerifyCartAfterLogin() {
        logger.info("Starting TC20_SearchProductsAndVerifyCartAfterLogin");

        String searchTerm = TestDataReader.getRequiredProperty("search.product");

        productsPage.navigateToProductsPage();
        productsPage.searchProduct(searchTerm);
        productsPage.addProductToCartByIndex(1);

        getDriver().get(ConfigReader.getBaseUrl() + "login");
        signupLoginPage.enterLoginEmail(TestDataReader.getRequiredProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getRequiredProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        String loggedInUser = signupLoginPage.getLoggedInUsername();
        Assert.assertFalse(loggedInUser.isEmpty(),
                "TC20 FAILED — User should be logged in after login");

        logger.info("TC20 PASSED — logged in as: {}", loggedInUser);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC21 - Add review on product
    // ─────────────────────────────────────────────────────────────────────────
    @Test(groups = {"functional", "priority:medium"})
    @Severity(SeverityLevel.MINOR)
    public void TC21_AddReviewOnProduct() {
        logger.info("Starting TC21_AddReviewOnProduct");

        productsPage.navigateToProductsPage();
        productsPage.clickViewProductByIndex(1);

        productsPage.addReview(
                TestDataReader.getRequiredProperty("review.name"),
                TestDataReader.getRequiredProperty("review.email"),
                TestDataReader.getRequiredProperty("review.text")
        );

        String msg = productsPage.getReviewSuccessMessage();
        Assert.assertTrue(
                msg.toLowerCase().contains("thank")
                        || msg.toLowerCase().contains("success")
                        || msg.toLowerCase().contains("review"),
                "TC21 FAILED — Review success message not found. Actual: " + msg);

        logger.info("TC21 PASSED — review message: {}", msg);
    }
}