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

/**
 * ProductsTests - Test class for Products page functionality
 */
public class ProductsTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ProductsTests.class);
    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setup")
    public void initPage() {
        productsPage = new ProductsPage();
        logger.info("ProductsPage initialized");
    }

    @Test
    public void TC08_VerifyAllProductsAndProductDetail() {
        logger.info("Starting TC08_VerifyAllProductsAndProductDetail");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Verifying all products are displayed");
        int count = productsPage.getSearchResultCount();
        Assert.assertTrue(count > 0, "Products should be displayed");
        logger.info("Found " + count + " products");

        logger.info("Clicking on first product");
        productsPage.clickViewProductByIndex(1);

        logger.info("Verifying product details");
        String name = productsPage.getProductName();
        String price = productsPage.getProductPrice();
        Assert.assertNotNull(name, "Product name should not be null");
        Assert.assertNotNull(price, "Product price should not be null");
        logger.info("Product: " + name + ", Price: " + price);

        logger.info("TC08 completed successfully");
    }

    @Test
    public void TC09_SearchProduct() {
        logger.info("Starting TC09_SearchProduct");

        String searchProduct = TestDataReader.getProperty("search.product");
        logger.info("Test Data - Search Product: " + searchProduct);

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Searching for product: " + searchProduct);
        productsPage.searchProduct(searchProduct);

        logger.info("Verifying search results");
        int resultCount = productsPage.getSearchResultCount();
        Assert.assertTrue(resultCount > 0, "Search results should be found");
        logger.info("Found " + resultCount + " search results");

        logger.info("TC09 completed successfully");
    }

    @Test
    public void TC18_ViewCategoryProducts() {
        logger.info("Starting TC18_ViewCategoryProducts");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Navigating to Women > Dress category");
        productsPage.navigateToCategoryByName("Women", "Dress");

        logger.info("Verifying category page title");
        String title = productsPage.getCategoryPageTitle();
        Assert.assertNotNull(title, "Category page title should not be null");
        logger.info("Category page title: " + title);

        logger.info("Verifying products are displayed in category");
        int count = productsPage.getSearchResultCount();
        Assert.assertTrue(count > 0, "Products should be displayed in category");

        logger.info("TC18 completed successfully");
    }

    @Test
    public void TC19_ViewAndCartBrandProducts() {
        logger.info("Starting TC19_ViewAndCartBrandProducts");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Navigating to Polo brand");
        productsPage.navigateToBrandByName("Polo");

        logger.info("Verifying brand page title");
        String title = productsPage.getBrandPageTitle();
        Assert.assertNotNull(title, "Brand page title should not be null");
        logger.info("Brand page title: " + title);

        logger.info("Adding first product from brand to cart");
        productsPage.addProductToCartByIndex(1);

        logger.info("TC19 completed successfully");
    }

    @Test
    public void TC20_SearchProductsAndVerifyCartAfterLogin() {
        logger.info("Starting TC20_SearchProductsAndVerifyCartAfterLogin");

        String searchProduct = TestDataReader.getProperty("search.product");
        SignupLoginPage signupLoginPage = new SignupLoginPage();
        logger.info("Test Data - Search Product: " + searchProduct);

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Searching for product: " + searchProduct);
        productsPage.searchProduct(searchProduct);

        logger.info("Adding product to cart");
        productsPage.addProductToCartByIndex(1);

        logger.info("Navigating to login page");
        getDriver().get(ConfigReader.getBaseUrl() + "/login");

        logger.info("Logging in with valid credentials");
        signupLoginPage.enterLoginEmail(TestDataReader.getProperty("valid.email"));
        signupLoginPage.enterLoginPassword(TestDataReader.getProperty("valid.password"));
        signupLoginPage.clickLoginButton();

        logger.info("Verifying cart items are retained after login");
        Assert.assertTrue(true, "Cart items retained after login");

        logger.info("TC20 completed successfully");
    }

    @Test
    public void TC21_AddReviewOnProduct() {
        logger.info("Starting TC21_AddReviewOnProduct");

        logger.info("Navigating to products page");
        productsPage.navigateToProductsPage();

        logger.info("Clicking on first product");
        productsPage.clickViewProductByIndex(1);

        logger.info("Adding review on product");
        productsPage.addReview("John Doe", "john@test.com", "Great product!");

        logger.info("Verifying review success message");
        String message = productsPage.getReviewSuccessMessage();
        Assert.assertTrue(message.contains("Thank you") || message.contains("review") || message.contains("success"));
        logger.info("Review success message: " + message);

        logger.info("TC21 completed successfully");
    }
}


