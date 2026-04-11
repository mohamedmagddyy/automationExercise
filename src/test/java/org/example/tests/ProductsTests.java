package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.ProductsPage;

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


        ProductsPage productsPage = new ProductsPage();
        // TODO: implement test steps
    }

    @Test
    public void TC09_SearchProduct() {
        logger.info("Starting TC09_SearchProduct");


        ProductsPage productsPage = new ProductsPage();
        // TODO: implement test steps
    }

    @Test
    public void TC18_ViewCategoryProducts() {
        logger.info("Starting TC18_ViewCategoryProducts");


        ProductsPage productsPage = new ProductsPage();
        // TODO: implement test steps
    }

    @Test
    public void TC19_ViewAndCartBrandProducts() {
        logger.info("Starting TC19_ViewAndCartBrandProducts");


        ProductsPage productsPage = new ProductsPage();
        // TODO: implement test steps
    }

    @Test
    public void TC20_SearchProductsAndVerifyCartAfterLogin() {
        logger.info("Starting TC20_SearchProductsAndVerifyCartAfterLogin");


        ProductsPage productsPage = new ProductsPage();
        // TODO: implement test steps
    }

    @Test
    public void TC21_AddReviewOnProduct() {
        logger.info("Starting TC21_AddReviewOnProduct");


        ProductsPage productsPage = new ProductsPage();
        // TODO: implement test steps
    }
}
