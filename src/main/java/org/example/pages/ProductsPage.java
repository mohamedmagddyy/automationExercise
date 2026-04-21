package org.example.pages;

import org.example.base.BasePage;
import org.example.driver.DriverFactory;
import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * ProductsPage - Page Object for Products and Product Detail pages
 */
public class ProductsPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProductsPage.class);

    // ── Navigation ────────────────────────────────────────────────
    private static final By PRODUCTS_NAV        = By.cssSelector("a[href='/products']");

    // ── Search ────────────────────────────────────────────────────
    private static final By SEARCH_INPUT         = By.id("search_product");
    private static final By SEARCH_BTN           = By.id("submit_search");

    // ── Product Grid ──────────────────────────────────────────────
    // كل كارد منتج
    private static final By PRODUCT_CARDS        = By.cssSelector("div.product-image-wrapper");
    // اسم المنتج داخل الكارد
    private static final By PRODUCT_NAME_IN_CARD = By.cssSelector("div.productinfo > p");
    // زرار Add to cart داخل الكارد (في productinfo)
    private static final By ADD_TO_CART_BTN      = By.cssSelector("div.productinfo a.add-to-cart");
    // زرار View Product
    private static final By VIEW_PRODUCT_BTN     = By.cssSelector("div.choose a[href^='/product_details/']");

    // ── Cart Modal (بعد الإضافة) ──────────────────────────────────
    private static final By CONTINUE_BTN         = By.cssSelector("button.close-modal");
    private static final By VIEW_CART_BTN         = By.cssSelector("#cartModal a[href='/view_cart']");

    // ── Product Detail Page ───────────────────────────────────────
    private static final By DETAIL_NAME          = By.cssSelector("div.product-information h2");
    private static final By DETAIL_PRICE         = By.cssSelector("div.product-information span span");
    private static final By QTY_INPUT            = By.id("quantity");
    private static final By ADD_TO_CART_DETAIL   = By.cssSelector("div.product-information button.cart");
    private static final By DETAIL_PARAS         = By.cssSelector("div.product-information p");

    // ── Category Sidebar ──────────────────────────────────────────
    // رابط الكاتيجوري الرئيسية مثلاً Women / Men / Kids
    private static final By CATEGORY_LINKS       = By.cssSelector("#accordian .panel-title a");
    // رابط السب كاتيجوري
    private static final By SUBCATEGORY_LINKS    = By.cssSelector("#accordian .panel-body a");
    // عنوان صفحة الكاتيجوري
    private static final By CATEGORY_TITLE       = By.cssSelector("h2.title.text-center");

    // ── Brand Sidebar ─────────────────────────────────────────────
    private static final By BRAND_LINKS          = By.cssSelector("div.brands-name a");
    private static final By BRAND_TITLE          = By.cssSelector("h2.title.text-center");

    // ── Review ────────────────────────────────────────────────────
    private static final By REVIEW_NAME          = By.id("name");
    private static final By REVIEW_EMAIL         = By.id("email");
    private static final By REVIEW_TEXT          = By.id("review");
    private static final By REVIEW_BTN           = By.id("button-review");
    private static final By REVIEW_SUCCESS       = By.cssSelector("#review-section .alert-success span");

    // ─────────────────────────────────────────────────────────────

    public ProductsPage() {
        super();
    }

    // ── Navigation ────────────────────────────────────────────────

    public void navigateToProductsPage() {
        click(PRODUCTS_NAV);
        logger.info("Navigated to products page");
    }

    // ── Search ────────────────────────────────────────────────────

    public void searchProduct(String productName) {
        sendKeys(SEARCH_INPUT, productName);
        click(SEARCH_BTN);
        logger.info("Searched for: " + productName);
    }

    public int getSearchResultCount() {
        List<WebElement> cards = driver.findElements(PRODUCT_CARDS);
        logger.info("Product count: " + cards.size());
        return cards.size();
    }

    // ── Product Grid ──────────────────────────────────────────────

    /**
     * إضافة منتج للكارت بالـ index (1-based)
     * بستخدم JavaScript click عشان الإعلانات بتغطي الزرار
     */
    public void addProductToCartByIndex(int index) {
        List<WebElement> cards = driver.findElements(PRODUCT_CARDS);
        WebElement card = cards.get(index - 1);

        // scroll للمنتج الأول
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", card);

        // الزرار جوه الكارد
        WebElement btn = card.findElement(By.cssSelector("div.productinfo a.add-to-cart"));

        // JS click عشان الإعلان ممكن يكون فوقيه
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        logger.info("Added product at index " + index + " to cart via JS click");
    }

    /**
     * View Product بالـ index (1-based)
     */
    public void clickViewProductByIndex(int index) {
        List<WebElement> cards = driver.findElements(PRODUCT_CARDS);
        WebElement card = cards.get(index - 1);
        WebElement viewBtn = card.findElement(By.cssSelector("div.choose a[href^='/product_details/']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewBtn);
        logger.info("Clicked view product at index " + index);
    }

    // ── Cart Modal ────────────────────────────────────────────────

    public void clickContinueShopping() {
        WaitUtils.waitForVisibility(driver, CONTINUE_BTN);
        click(CONTINUE_BTN);
        logger.info("Clicked Continue Shopping");
    }

    public void clickViewCart() {
        WaitUtils.waitForVisibility(driver, VIEW_CART_BTN);
        click(VIEW_CART_BTN);
        logger.info("Clicked View Cart from modal");
    }

    // ── Product Detail ────────────────────────────────────────────

    public String getProductName() {
        String name = getText(DETAIL_NAME);
        logger.info("Product name: " + name);
        return name;
    }

    public String getProductPrice() {
        String price = getText(DETAIL_PRICE);
        logger.info("Product price: " + price);
        return price;
    }

    public String getProductCategory() {
        List<WebElement> paras = driver.findElements(DETAIL_PARAS);
        for (WebElement p : paras) {
            if (p.getText().contains("Category")) return p.getText();
        }
        return "";
    }

    public String getProductBrand() {
        List<WebElement> paras = driver.findElements(DETAIL_PARAS);
        for (WebElement p : paras) {
            if (p.getText().contains("Brand")) return p.getText();
        }
        return "";
    }

    public String getProductAvailability() {
        List<WebElement> paras = driver.findElements(DETAIL_PARAS);
        for (WebElement p : paras) {
            if (p.getText().contains("Availability")) return p.getText();
        }
        return "";
    }

    public String getProductCondition() {
        List<WebElement> paras = driver.findElements(DETAIL_PARAS);
        for (WebElement p : paras) {
            if (p.getText().contains("Condition")) return p.getText();
        }
        return "";
    }

    public void setProductQuantity(int quantity) {
        WaitUtils.waitForVisibility(driver, QTY_INPUT);
        WebElement qtyField = driver.findElement(QTY_INPUT);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '';", qtyField);
        qtyField.sendKeys(String.valueOf(quantity));
        logger.info("Set quantity to: " + quantity);
    }

    public void clickAddToCart() {
        WaitUtils.waitForVisibility(driver, ADD_TO_CART_DETAIL);
        WebElement btn = driver.findElement(ADD_TO_CART_DETAIL);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        logger.info("Clicked Add to cart on detail page");
    }

    public void clickContinueShopping_detail() {
        clickContinueShopping();
    }

    // ── Category ──────────────────────────────────────────────────

    /**
     * category = "Women" / "Men" / "Kids"
     * subCategory = "Dress" / "Tops" / etc.
     */
    public void navigateToCategoryByName(String category, String subCategory) {
        // افتح الـ accordion الخاص بالكاتيجوري
        List<WebElement> catLinks = driver.findElements(CATEGORY_LINKS);
        for (WebElement link : catLinks) {
            if (link.getText().trim().equalsIgnoreCase(category)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                break;
            }
        }

        // انتظر السب كاتيجوري وانقر عليه
        List<WebElement> subLinks = WaitUtils.waitForVisibility(driver,
                By.cssSelector("#accordian .panel-collapse.in a")) != null
                ? driver.findElements(By.cssSelector("#accordian .panel-collapse.in a"))
                : driver.findElements(SUBCATEGORY_LINKS);

        for (WebElement link : subLinks) {
            if (link.getText().trim().toLowerCase().contains(subCategory.toLowerCase())) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                logger.info("Navigated to category: " + category + " > " + subCategory);
                return;
            }
        }
        logger.warn("SubCategory not found: " + subCategory);
    }

    public String getCategoryPageTitle() {
        return getText(CATEGORY_TITLE);
    }

    // ── Brand ─────────────────────────────────────────────────────

    public void navigateToBrandByName(String brandName) {
        List<WebElement> brandLinks = driver.findElements(BRAND_LINKS);
        for (WebElement link : brandLinks) {
            if (link.getText().trim().toLowerCase().contains(brandName.toLowerCase())) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                logger.info("Navigated to brand: " + brandName);
                return;
            }
        }
        logger.warn("Brand not found: " + brandName);
    }

    public String getBrandPageTitle() {
        return getText(BRAND_TITLE);
    }

    // ── Review ────────────────────────────────────────────────────

    public void addReview(String name, String email, String review) {
        sendKeys(REVIEW_NAME, name);
        sendKeys(REVIEW_EMAIL, email);
        sendKeys(REVIEW_TEXT, review);
        click(REVIEW_BTN);
        logger.info("Submitted review");
    }

    public String getReviewSuccessMessage() {
        WaitUtils.waitForVisibility(driver, REVIEW_SUCCESS);
        return getText(REVIEW_SUCCESS);
    }
}