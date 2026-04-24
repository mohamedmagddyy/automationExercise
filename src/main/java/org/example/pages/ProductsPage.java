package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * ProductsPage — Page Object for the Products list and Product Detail pages.
 *
 * Rules followed:
 *  - No direct driver access except where a List<WebElement> is unavoidable
 *    (findElements returns a list — no BasePage wrapper for that yet)
 *  - JS click used for Add-to-Cart buttons because ads intercept them
 *  - No WaitUtils or ActionsHelper called directly
 */
public class ProductsPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProductsPage.class);

    // ── Navigation ─────────────────────────────────────────────────────────
    private static final By PRODUCTS_NAV = By.xpath("//a[@href='/products']");

    // ── Search ─────────────────────────────────────────────────────────────
    private static final By SEARCH_INPUT = By.id("search_product");
    private static final By SEARCH_BTN   = By.id("submit_search");

    // ── Product Grid ───────────────────────────────────────────────────────
    private static final By PRODUCT_CARDS      = By.cssSelector("div.product-image-wrapper");
    private static final By SEARCH_RESULTS     = By.cssSelector("div.features_items div.single-products");

    // ── Cart Modal (appears after Add to Cart) ─────────────────────────────
    private static final By CONTINUE_SHOPPING_BTN = By.cssSelector("button.close-modal");
    private static final By VIEW_CART_BTN          = By.cssSelector("#cartModal a[href='/view_cart']");

    // ── Product Detail Page ────────────────────────────────────────────────
    private static final By DETAIL_NAME        = By.cssSelector("div.product-information h2");
    private static final By DETAIL_PRICE       = By.cssSelector("div.product-information span span");
    private static final By DETAIL_PARAS       = By.cssSelector("div.product-information p");
    private static final By QTY_INPUT          = By.id("quantity");
    private static final By ADD_TO_CART_DETAIL = By.cssSelector("div.product-information button.cart");

    // ── Category Sidebar ───────────────────────────────────────────────────
    private static final By CATEGORY_TITLE = By.cssSelector("div.features_items h2.title.text-center");

    // ── Brand Sidebar ──────────────────────────────────────────────────────
    private static final By BRAND_LINKS  = By.cssSelector("div.brands-name a");
    private static final By BRAND_TITLE  = By.cssSelector("h2.title.text-center");

    // ── Review ─────────────────────────────────────────────────────────────
    private static final By REVIEW_NAME    = By.id("name");
    private static final By REVIEW_EMAIL   = By.id("email");
    private static final By REVIEW_TEXT    = By.id("review");
    private static final By REVIEW_BTN     = By.id("button-review");
    private static final By REVIEW_SUCCESS = By.cssSelector("#review-section .alert-success span");

    // ── Constructor ────────────────────────────────────────────────────────

    public ProductsPage() {
        super();
    }

    // =========================================================================
    // NAVIGATION
    // =========================================================================

    public void navigateToProductsPage() {
        click(PRODUCTS_NAV);
        logger.info("Navigated to Products page");
    }

    // =========================================================================
    // SEARCH
    // =========================================================================

    public void searchProduct(String productName) {
        sendKeys(SEARCH_INPUT, productName);
        click(SEARCH_BTN);
        logger.info("Searched for: {}", productName);
    }

    public int getSearchResultCount() {
        return driver.findElements(SEARCH_RESULTS).size();
    }

    // =========================================================================
    // PRODUCT GRID
    // =========================================================================

    /**
     * Adds a product to cart by its 1-based index in the product grid.
     * Uses JS click because ad iframes often intercept the Add-to-Cart button.
     */
    public void addProductToCartByIndex(int index) {
        List<WebElement> cards = driver.findElements(PRODUCT_CARDS);
        WebElement card = cards.get(index - 1);
        scrollToElement(card);
        WebElement addBtn = card.findElement(By.cssSelector("div.productinfo a.add-to-cart"));
        jsClick(addBtn);
        logger.info("Added product at index {} to cart", index);
    }

    /**
     * Clicks View Product for a card by 1-based index.
     */
    public void clickViewProductByIndex(int index) {
        List<WebElement> cards = driver.findElements(PRODUCT_CARDS);
        WebElement card = cards.get(index - 1);
        WebElement viewBtn = card.findElement(By.cssSelector("div.choose a[href^='/product_details/']"));
        jsClick(viewBtn);
        logger.info("Clicked View Product at index {}", index);
    }

    // =========================================================================
    // CART MODAL
    // =========================================================================

    public void clickContinueShopping() {
        waitForVisibility(CONTINUE_SHOPPING_BTN);
        click(CONTINUE_SHOPPING_BTN);
        logger.info("Clicked Continue Shopping");
    }

    public void clickViewCart() {
        waitForVisibility(VIEW_CART_BTN);
        click(VIEW_CART_BTN);
        logger.info("Clicked View Cart from modal");
    }

    // =========================================================================
    // PRODUCT DETAIL
    // =========================================================================

    public String getProductName() {
        return getText(DETAIL_NAME);
    }

    public String getProductPrice() {
        return getText(DETAIL_PRICE);
    }

    public String getProductCategory() {
        return getDetailParagraphContaining("Category");
    }

    public String getProductBrand() {
        return getDetailParagraphContaining("Brand");
    }

    public String getProductAvailability() {
        return getDetailParagraphContaining("Availability");
    }

    public String getProductCondition() {
        return getDetailParagraphContaining("Condition");
    }

    /**
     * Sets the quantity field on the product detail page.
     * Clears via JS first to handle the default value reliably.
     */
    public void setProductQuantity(int quantity) {
        waitForVisibility(QTY_INPUT);
        WebElement qtyField = driver.findElement(QTY_INPUT);
        // JS setValue — number inputs are often not interactable via sendKeys
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].removeAttribute('readonly');" +
                                "arguments[0].value = arguments[1];",
                        qtyField, String.valueOf(quantity));
        logger.info("Set quantity to: {}", quantity);
    }

    public void clickAddToCart() {
        waitForVisibility(ADD_TO_CART_DETAIL);
        jsClick(ADD_TO_CART_DETAIL);
        logger.info("Clicked Add to Cart on detail page");
    }

    // ── Private helper ─────────────────────────────────────────────────────

    private String getDetailParagraphContaining(String keyword) {
        List<WebElement> paras = driver.findElements(DETAIL_PARAS);
        for (WebElement p : paras) {
            if (p.getText().contains(keyword)) return p.getText();
        }
        logger.warn("Detail paragraph containing '{}' not found", keyword);
        return "";
    }

    // =========================================================================
    // CATEGORY
    // =========================================================================

    /**
     * Expands a category accordion and clicks the subcategory link.
     *
     * @param category    e.g. "Women", "Men", "Kids"
     * @param subCategory e.g. "Dress", "Tops"
     */
    public void navigateToCategoryByName(String category, String subCategory) {
        By parentLocator = By.xpath(String.format("//a[normalize-space()='%s']", category));
        click(parentLocator);
        logger.info("Expanded category: {}", category);

        By childLocator = By.xpath(
                String.format("//div[@id='%s']//a[normalize-space()='%s']", category, subCategory));
        waitForVisibility(childLocator);
        click(childLocator);
        logger.info("Navigated to: {} > {}", category, subCategory);
    }

    public String getCategoryPageTitle() {
        return getText(CATEGORY_TITLE);
    }

    // =========================================================================
    // BRAND
    // =========================================================================

    public void navigateToBrandByName(String brandName) {
        List<WebElement> brandLinks = driver.findElements(BRAND_LINKS);
        for (WebElement link : brandLinks) {
            if (link.getText().trim().equalsIgnoreCase(brandName)) {
                jsClick(link);
                logger.info("Navigated to brand: {}", brandName);
                return;
            }
        }
        logger.warn("Brand not found: {}", brandName);
    }

    public String getBrandPageTitle() {
        return getText(BRAND_TITLE);
    }

    // =========================================================================
    // REVIEW
    // =========================================================================

    public void addReview(String name, String email, String review) {
        sendKeys(REVIEW_NAME, name);
        sendKeys(REVIEW_EMAIL, email);
        sendKeys(REVIEW_TEXT, review);
        click(REVIEW_BTN);
        logger.info("Review submitted");
    }

    public String getReviewSuccessMessage() {
        waitForVisibility(REVIEW_SUCCESS);
        return getText(REVIEW_SUCCESS);
    }
}