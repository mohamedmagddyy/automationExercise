package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.ActionsHelper;
import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage extends BasePage {

    // ================= NAVIGATION =================
    private static final By PRODUCTS_NAV = By.cssSelector("a[href='/products']");
    private static final By CART_NAV     = By.cssSelector("a[href='/view_cart']");

    // ================= SEARCH =================
    private static final By SEARCH_INPUT = By.id("search_product");
    private static final By SEARCH_BTN   = By.id("submit_search");

    // ================= PRODUCT GRID =================
    private static final By GRID_CONTAINER = By.cssSelector("div.features_items");
    private static final By PRODUCT_CARDS  = By.cssSelector("div.product-image-wrapper");

    private static final By PRODUCT_NAME   = By.cssSelector("div.productinfo > p");
    private static final By PRODUCT_PRICE  = By.cssSelector("div.productinfo > h2");

    private static final By ADD_TO_CART    = By.cssSelector("a.add-to-cart[data-product-id]");
    private static final By VIEW_PRODUCT   = By.cssSelector("a[href^='/product_details/']");

    // ================= MODAL =================
    private static final By MODAL          = By.id("cartModal");
    private static final By CONTINUE_BTN   = By.cssSelector("#cartModal button[data-dismiss='modal']");
    private static final By VIEW_CART_BTN  = By.cssSelector("#cartModal a[href='/view_cart']");

    // ================= DETAIL PAGE =================
    private static final By DETAIL_NAME    = By.cssSelector("div.product-information h2");
    private static final By DETAIL_PRICE   = By.cssSelector("div.product-information span span");
    private static final By QTY_INPUT      = By.id("quantity");
    private static final By ADD_DETAIL_BTN  = By.cssSelector("button.cart");

    private static final By DETAIL_PARAS   = By.cssSelector("div.product-information p");

    // ================= REVIEW =================
    private static final By REVIEW_NAME   = By.id("name");
    private static final By REVIEW_EMAIL  = By.id("email");
    private static final By REVIEW_TEXT   = By.id("review");
    private static final By REVIEW_BTN    = By.id("button-review");

    // =====================================================
    // NAVIGATION
    // =====================================================

    public void openProductsPage() {
        click(PRODUCTS_NAV);
    }

    // =====================================================
    // SEARCH
    // =====================================================

    public void search(String text) {
        sendKeys(SEARCH_INPUT, text);
        click(SEARCH_BTN);

    }

    // =====================================================
    // PRODUCTS (NO INDEX — STABLE APPROACH)
    // =====================================================

    public List<WebElement> getAllProducts() {

        return driver.findElements(PRODUCT_CARDS);
    }

    public WebElement getProductByName(String name) {
        return getAllProducts()
                .stream()
                .filter(p -> p.findElement(PRODUCT_NAME)
                        .getText().trim().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found: " + name));
    }

    public void addToCart(String productName) {
        WebElement product = getProductByName(productName);

        ActionsHelper.hoverOverElement(driver, product);
        product.findElement(ADD_TO_CART).click();

        handleModalContinueShopping();
    }

    public void viewProduct(String productName) {
        WebElement product = getProductByName(productName);
        ActionsHelper.scrollToElement(driver, product);
        product.findElement(VIEW_PRODUCT).click();
    }

    // =====================================================
    // DETAIL PAGE
    // =====================================================

    public String getProductName() {
        return getText(DETAIL_NAME);
    }

    public String getProductPrice() {
        return getText(DETAIL_PRICE);
    }

    public void setQuantity(int qty) {
        WebElement input = driver.findElement(QTY_INPUT);
        input.clear();
        input.sendKeys(String.valueOf(qty));
    }

    public void addToCartFromDetail() {
        click(ADD_DETAIL_BTN);
    }

    // =====================================================
    // DETAIL INFO PARSING
    // =====================================================

    public String getInfo(String label) {
        List<WebElement> paras = driver.findElements(DETAIL_PARAS);

        for (WebElement p : paras) {
            String text = p.getText().trim();
            if (text.startsWith(label)) {
                return text.replace(label, "").trim();
            }
        }
        return "";
    }

    public String getCategory() {
        return getInfo("Category:");
    }

    public String getBrand() {
        return getInfo("Brand:");
    }

    public String getAvailability() {
        return getInfo("Availability:");
    }

    // =====================================================
    // REVIEW
    // =====================================================

    public void submitReview(String name, String email, String review) {
        sendKeys(REVIEW_NAME, name);
        sendKeys(REVIEW_EMAIL, email);
        sendKeys(REVIEW_TEXT, review);
        click(REVIEW_BTN);
    }

    // =====================================================
    // MODAL
    // =====================================================

    public void handleModalContinueShopping() {
        try {
            WaitUtils.waitForVisibility(driver, MODAL);
            click(CONTINUE_BTN);
            WaitUtils.waitForInvisibility(driver, MODAL);
        } catch (Exception ignored) {
        }
    }

    public void goToCartFromModal() {
        click(VIEW_CART_BTN);
    }




}