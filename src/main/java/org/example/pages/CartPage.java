package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * CartPage - Page Object for Cart, Checkout, and Payment pages
 */
public class CartPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CartPage.class);

    // ── Cart Table ────────────────────────────────────────────────
    private static final By CART_ROWS          = By.cssSelector("tbody tr[id^='product-']");
    private static final By PRODUCT_NAME       = By.cssSelector("td.cart_description h4 a");
    private static final By PRODUCT_PRICE      = By.cssSelector("td.cart_price p");
    private static final By PRODUCT_QTY        = By.cssSelector("td.cart_quantity button");
    private static final By PRODUCT_TOTAL      = By.cssSelector("td.cart_total p.cart_total_price");
    private static final By DELETE_BTN         = By.cssSelector("a.cart_quantity_delete");

    // ── Cart Navigation ───────────────────────────────────────────
    private static final By CART_NAV           = By.cssSelector("a[href='/view_cart']");

    // ── Checkout Modal (if not logged in) ─────────────────────────
    private static final By CHECKOUT_MODAL          = By.cssSelector("div.modal-content");
    private static final By MODAL_LOGIN_LINK         = By.cssSelector("div.modal-body a[href='/login']");
    private static final By MODAL_CONTINUE_CART_BTN  = By.cssSelector("button.close-checkout-modal");

    // ── Checkout Page ─────────────────────────────────────────────
    private static final By PROCEED_CHECKOUT   = By.cssSelector("a.btn.check_out");
    private static final By DELIVERY_ADDRESS   = By.id("address_delivery");
    private static final By BILLING_ADDRESS    = By.id("address_invoice");
    private static final By ORDER_COMMENT      = By.cssSelector("#ordermsg textarea[name='message']");
    private static final By PLACE_ORDER_BTN    = By.cssSelector("a.btn.check_out");

    // ── Payment Page ──────────────────────────────────────────────
    private static final By CARD_NAME          = By.cssSelector("input[data-qa='name-on-card']");
    private static final By CARD_NUMBER        = By.cssSelector("input[data-qa='card-number']");
    private static final By CARD_CVC           = By.cssSelector("input[data-qa='cvc']");
    private static final By CARD_EXPIRY_MONTH  = By.cssSelector("input[data-qa='expiry-month']");
    private static final By CARD_EXPIRY_YEAR   = By.cssSelector("input[data-qa='expiry-year']");
    private static final By PAY_CONFIRM_BTN    = By.cssSelector("button[data-qa='pay-button']");

    // ── Order Confirmed Page ──────────────────────────────────────
    // Multiple fallback locators because the site uses different selectors depending on flow
    private static final By ORDER_PLACED_MSG_1 = By.cssSelector("[data-qa='order-placed'] b");
    private static final By ORDER_PLACED_MSG_2 = By.cssSelector("[data-qa='order-placed']");
    private static final By ORDER_PLACED_MSG_3 = By.cssSelector("#success_message .alert-success");
    private static final By ORDER_PLACED_MSG_4 = By.cssSelector("section[id] h2 b");

    private static final By DOWNLOAD_INVOICE   = By.cssSelector("a[href*='download_invoice']");
    private static final By CONTINUE_BTN       = By.cssSelector("a[data-qa='continue-button']");

    // ── Footer Subscription ───────────────────────────────────────
    private static final By SUBSCRIBE_EMAIL    = By.id("susbscribe_email");
    private static final By SUBSCRIBE_BTN      = By.id("subscribe");
    private static final By SUBSCRIBE_SUCCESS  = By.cssSelector("#success-subscribe .alert-success");

    // ─────────────────────────────────────────────────────────────

    public CartPage() {
        super();
    }

    // ── Cart ──────────────────────────────────────────────────────

    public void navigateToCart() {
        click(CART_NAV);
        logger.info("Navigated to cart");
    }

    public int getCartItemCount() {
        ensureDriver();  // FIX: must call ensureDriver() before using driver directly
        List<WebElement> rows = driver.findElements(CART_ROWS);
        logger.info("Cart item count: " + rows.size());
        return rows.size();
    }

    public String getProductNameByRow(int row) {
        ensureDriver();  // FIX
        List<WebElement> names = driver.findElements(PRODUCT_NAME);
        String name = names.get(row - 1).getText();
        logger.info("Product name row " + row + ": " + name);
        return name;
    }

    public int getProductQuantityByRow(int row) {
        ensureDriver();  // FIX
        List<WebElement> qtys = driver.findElements(PRODUCT_QTY);
        int qty = Integer.parseInt(qtys.get(row - 1).getText().trim());
        logger.info("Product qty row " + row + ": " + qty);
        return qty;
    }

    public String getProductPriceByRow(int row) {
        ensureDriver();  // FIX
        List<WebElement> prices = driver.findElements(PRODUCT_PRICE);
        String price = prices.get(row - 1).getText();
        logger.info("Product price row " + row + ": " + price);
        return price;
    }

    public void removeProductByRow(int row) {
        ensureDriver();
        List<WebElement> deleteBtns = driver.findElements(DELETE_BTN);
        WebElement btn = deleteBtns.get(row - 1);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        WaitUtils.waitForInvisibility(driver, DELETE_BTN);

        logger.info("Removed product at row: " + row);
    }



    // ── Checkout Modal ────────────────────────────────────────────

    public boolean isCheckoutModalVisible() {
        return isDisplayed(CHECKOUT_MODAL);
    }

    public void clickLoginFromModal() {
        WaitUtils.waitForVisibility(driver, MODAL_LOGIN_LINK);
        click(MODAL_LOGIN_LINK);
        logger.info("Clicked login link from checkout modal");
    }

    public void clickContinueOnCartFromModal() {
        click(MODAL_CONTINUE_CART_BTN);
        logger.info("Clicked continue on cart from modal");
    }

    // ── Checkout ──────────────────────────────────────────────────

    public void clickProceedToCheckout() {
        click(PROCEED_CHECKOUT);
        logger.info("Clicked Proceed to Checkout");
    }

    public String getDeliveryAddress() {
        String address = getText(DELIVERY_ADDRESS);
        logger.info("Delivery address: " + address);
        return address;
    }

    public String getBillingAddress() {
        String address = getText(BILLING_ADDRESS);
        logger.info("Billing address: " + address);
        return address;
    }

    public void enterOrderComment(String comment) {
        sendKeys(ORDER_COMMENT, comment);
        logger.info("Entered order comment: " + comment);
    }

    public void clickPlaceOrder() {
        click(PLACE_ORDER_BTN);
        logger.info("Clicked Place Order");
    }

    // ── Payment ───────────────────────────────────────────────────

    public void enterCardName(String name) {
        sendKeys(CARD_NAME, name);
        logger.info("Entered card name: " + name);
    }

    public void enterCardNumber(String number) {
        sendKeys(CARD_NUMBER, number);
        logger.info("Entered card number");
    }

    public void enterCardCVC(String cvc) {
        sendKeys(CARD_CVC, cvc);
        logger.info("Entered CVC");
    }

    public void enterCardExpiryMonth(String month) {
        sendKeys(CARD_EXPIRY_MONTH, month);
        logger.info("Entered expiry month: " + month);
    }

    public void enterCardExpiryYear(String year) {
        sendKeys(CARD_EXPIRY_YEAR, year);
        logger.info("Entered expiry year: " + year);
    }

    public void clickConfirmOrder() {
        click(PAY_CONFIRM_BTN);
        logger.info("Clicked Pay and Confirm Order");
    }

    // ── Order Confirmed ───────────────────────────────────────────

    /**
     * FIX: The site renders the order confirmation with the text inside a <b> tag
     * under [data-qa='order-placed'], OR sometimes in #success_message.
     * We try multiple locators and return the first non-empty text found.
     */
    public String getOrderPlacedMessage() {
        ensureDriver();
        By[] candidates = {
                ORDER_PLACED_MSG_1,
                ORDER_PLACED_MSG_2,
                ORDER_PLACED_MSG_3,
                ORDER_PLACED_MSG_4
        };

        for (By locator : candidates) {
            try {
                WaitUtils.waitForVisibility(driver, locator);
                String msg = driver.findElement(locator).getText();
                if (msg != null && !msg.trim().isEmpty()) {
                    logger.info("Order placed message found via " + locator + ": " + msg);
                    return msg;
                }
            } catch (Exception e) {
                logger.debug("Locator not found: " + locator);
            }
        }

        logger.warn("Order placed message not found via any locator");
        return "";
    }

    public void clickDownloadInvoice() {
        WaitUtils.waitForVisibility(driver, DOWNLOAD_INVOICE);
        click(DOWNLOAD_INVOICE);
        logger.info("Clicked Download Invoice");
    }

    public void clickContinueAfterOrder() {
        click(CONTINUE_BTN);
        logger.info("Clicked Continue after order");
    }

    // ── Footer Subscription ───────────────────────────────────────

    public void enterSubscriptionEmail(String email) {
        sendKeys(SUBSCRIBE_EMAIL, email);
        logger.info("Entered subscription email: " + email);
    }

    public void clickSubscribeButton() {
        click(SUBSCRIBE_BTN);
        logger.info("Clicked subscribe button");
    }

    public String getSubscriptionSuccessMessage() {
        WaitUtils.waitForVisibility(driver, SUBSCRIBE_SUCCESS);
        String msg = getText(SUBSCRIBE_SUCCESS);
        logger.info("Subscription success: " + msg);
        return msg;
    }

    // ── Kept for compatibility ────────────────────────────────────
    public String getOrderCommentText() {
        return getText(ORDER_COMMENT);
    }
}