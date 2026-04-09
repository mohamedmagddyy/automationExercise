package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.ProductsPage;
import org.example.utils.ExtentReportManager;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ProductsTests - Test class for Products page functionality
 */
public class ProductsTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ProductsTests.class);

    @Test
    public void TC08_VerifyAllProductsAndProductDetail() {
        logger.info("Starting TC08_VerifyAllProductsAndProductDetail");
        ExtentReportManager.getTest().info("Verify All Products and Product Detail Test");

        ProductsPage productsPage = new ProductsPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC09_SearchProduct() {
        logger.info("Starting TC09_SearchProduct");
        ExtentReportManager.getTest().info("Search Product Test");

        ProductsPage productsPage = new ProductsPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC18_ViewCategoryProducts() {
        logger.info("Starting TC18_ViewCategoryProducts");
        ExtentReportManager.getTest().info("View Category Products Test");

        ProductsPage productsPage = new ProductsPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC19_ViewAndCartBrandProducts() {
        logger.info("Starting TC19_ViewAndCartBrandProducts");
        ExtentReportManager.getTest().info("View and Cart Brand Products Test");

        ProductsPage productsPage = new ProductsPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC20_SearchProductsAndVerifyCartAfterLogin() {
        logger.info("Starting TC20_SearchProductsAndVerifyCartAfterLogin");
        ExtentReportManager.getTest().info("Search Products and Verify Cart After Login Test");

        ProductsPage productsPage = new ProductsPage(getDriver());
        // TODO: implement test steps
    }

    @Test
    public void TC21_AddReviewOnProduct() {
        logger.info("Starting TC21_AddReviewOnProduct");
        ExtentReportManager.getTest().info("Add Review on Product Test");

        ProductsPage productsPage = new ProductsPage(getDriver());
        // TODO: implement test steps
    }
}
