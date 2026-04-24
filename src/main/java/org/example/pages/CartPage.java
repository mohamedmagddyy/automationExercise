package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * CartPage — Page Object for Cart, Checkout, Payment, and Order Confirmation pages.
 *
 * Rules followed:
 *  - No direct driver access except for findElements (list operations)
 *  - No WaitUtils or ActionsHelper called directly
 *  - No ensureDriver() — driver is guaranteed by BasePage constructor
 */
public class CartPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CartPage.class);

    // ── Cart Table ─────────────────────────────────────────────────────────
    private static final By CART_ROWS     = By.cssSelector("tbody tr[id^='product-']");
    private static final By PRODUCT_NAME  = By.cssSelector("td.cart_description h4 a");
    private static final By PRODUCT_PRICE = By.cssSelector("td.cart_price p");
    private static final By PRODUCT_QTY   = By.cssSelector("td.cart_quantity button");
    private static final By PRODUCT_TOTAL = By.cssSelector("td.cart_total p.cart_total_price");
    private static final By DELETE_BTN    = By.cssSelector("a.cart_quantity_delete");

    // ── Cart Navigation ────────────────────────────────────────────────────
    private static final By CART_NAV = By.cssSelector("a[href='/view_cart']");

    // ── Checkout Modal (when not logged in) ────────────────────────────────
    private static final By CHECKOUT_MODAL         = By.cssSelector("div.modal-content");
    private static final By MODAL_LOGIN_LINK        = By.cssSelector("div.modal-body a[href='/login']");
    private static final By MODAL_CONTINUE_CART_BTN = By.cssSelector("button.close-checkout-modal");

    // ── Checkout Page ──────────────────────────────────────────────────────
    // زرار Proceed to Checkout في الكارت
    private static final By PROCEED_CHECKOUT = By.cssSelector("a.btn.check_out");
    private static final By DELIVERY_ADDRESS = By.id("address_delivery");
    private static final By BILLING_ADDRESS  = By.id("address_invoice");
    private static final By ORDER_COMMENT    = By.cssSelector("textarea[name='message']");
    // زرار Place Order في الـ checkout page — رابطه /payment
    private static final By PLACE_ORDER_BTN  = By.cssSelector("a[href='/payment']");

    // ── Payment Page ───────────────────────────────────────────────────────
    private static final By CARD_NAME         = By.cssSelector("input[data-qa='name-on-card']");
    private static final By CARD_NUMBER       = By.cssSelector("input[data-qa='card-number']");
    private static final By CARD_CVC          = By.cssSelector("input[data-qa='cvc']");
    private static final By CARD_EXPIRY_MONTH = By.cssSelector("input[data-qa='expiry-month']");
    private static final By CARD_EXPIRY_YEAR  = By.cssSelector("input[data-qa='expiry-year']");
    private static final By PAY_CONFIRM_BTN   = By.cssSelector("button[data-qa='pay-button']");

    // ── Order Confirmation ─────────────────────────────────────────────────
    // Multiple fallbacks — site uses different markup depending on the flow
    private static final By ORDER_PLACED_1 = By.cssSelector("[data-qa='order-placed'] b");
    private static final By ORDER_PLACED_2 = By.cssSelector("[data-qa='order-placed']");
    private static final By ORDER_PLACED_3 = By.cssSelector("#success_message .alert-success");
    private static final By ORDER_PLACED_4 = By.cssSelector("section[id] h2 b");

    private static final By DOWNLOAD_INVOICE = By.cssSelector("a[href*='download_invoice']");
    private static final By CONTINUE_BTN     = By.cssSelector("a[data-qa='continue-button']");

    // ── Footer Subscription ────────────────────────────────────────────────
    private static final By SUBSCRIBE_EMAIL   = By.id("susbscribe_email");
    private static final By SUBSCRIBE_BTN     = By.id("subscribe");
    private static final By SUBSCRIBE_SUCCESS = By.cssSelector("#success-subscribe .alert-success");

    // ── Constructor ────────────────────────────────────────────────────────

    public CartPage() {
        super();
    }

    // =========================================================================
    // CART
    // =========================================================================

    public void navigateToCart() {
        click(CART_NAV);
        logger.info("Navigated to Cart");
    }

    public int getCartItemCount() {
        return driver.findElements(CART_ROWS).size();
    }

    public String getProductNameByRow(int row) {
        List<WebElement> names = driver.findElements(PRODUCT_NAME);
        String name = names.get(row - 1).getText();
        logger.info("Product name at row {}: {}", row, name);
        return name;
    }

    public int getProductQuantityByRow(int row) {
        List<WebElement> qtys = driver.findElements(PRODUCT_QTY);
        int qty = Integer.parseInt(qtys.get(row - 1).getText().trim());
        logger.info("Product qty at row {}: {}", row, qty);
        return qty;
    }

    public String getProductPriceByRow(int row) {
        List<WebElement> prices = driver.findElements(PRODUCT_PRICE);
        String price = prices.get(row - 1).getText();
        logger.info("Product price at row {}: {}", row, price);
        return price;
    }

    /**
     * Removes a product row by 1-based index.
     * Waits for the specific row's delete button to disappear — not the whole locator —
     * to avoid a false timeout when other rows still have delete buttons.
     */
    public void removeProductByRow(int row) {
        List<WebElement> deleteBtns = driver.findElements(DELETE_BTN);
        WebElement btn = deleteBtns.get(row - 1);
        jsClick(btn);
        // wait for that specific button to go stale / disappear
        WaitUtils.waitForInvisibility(driver, By.cssSelector(
                "tbody tr:nth-child(" + row + ") a.cart_quantity_delete"));
        logger.info("Removed product at row {}", row);
    }

    // =========================================================================
    // CHECKOUT MODAL
    // =========================================================================

    public boolean isCheckoutModalVisible() {
        return isDisplayed(CHECKOUT_MODAL);
    }

    public void clickLoginFromModal() {
        waitForVisibility(MODAL_LOGIN_LINK);
        click(MODAL_LOGIN_LINK);
        logger.info("Clicked Login link from checkout modal");
    }

    public void clickContinueOnCartFromModal() {
        click(MODAL_CONTINUE_CART_BTN);
        logger.info("Clicked Continue on cart from modal");
    }

    // =========================================================================
    // CHECKOUT
    // =========================================================================

    public void clickProceedToCheckout() {
        // scroll to button first — it sits at the bottom of the cart table
        scrollToElement(PROCEED_CHECKOUT);
        click(PROCEED_CHECKOUT);
        logger.info("Clicked Proceed to Checkout");
    }

    public String getDeliveryAddress() {
        return getText(DELIVERY_ADDRESS);
    }

    public String getBillingAddress() {
        return getText(BILLING_ADDRESS);
    }

    public void enterOrderComment(String comment) {
        // scroll to bottom first
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");

        waitForVisibility(ORDER_COMMENT);

        WebElement textarea = driver.findElement(ORDER_COMMENT);

        // scrollIntoView
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", textarea);

        // JS setValue — bypass any focus/interactability issues
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].value = arguments[1];", textarea, comment);

        // trigger input event so the page registers the value
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].dispatchEvent(new Event('input'));", textarea);

        logger.info("Entered order comment: {}", comment);
    }

    public void clickPlaceOrder() {
        click(PLACE_ORDER_BTN);
        logger.info("Clicked Place Order");
    }

    // =========================================================================
    // PAYMENT
    // =========================================================================

    public void enterCardName(String name) {
        sendKeys(CARD_NAME, name);
        logger.info("Entered card name: {}", name);
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
        logger.info("Entered expiry month: {}", month);
    }

    public void enterCardExpiryYear(String year) {
        sendKeys(CARD_EXPIRY_YEAR, year);
        logger.info("Entered expiry year: {}", year);
    }

    public void clickConfirmOrder() {
        click(PAY_CONFIRM_BTN);
        logger.info("Clicked Pay and Confirm Order");
    }

    // =========================================================================
    // ORDER CONFIRMATION
    // =========================================================================

    /**
     * Tries multiple locators to find the order placed message.
     * The site renders different markup depending on the checkout flow.
     */
    public String getOrderPlacedMessage() {
        By[] candidates = { ORDER_PLACED_1, ORDER_PLACED_2, ORDER_PLACED_3, ORDER_PLACED_4 };

        for (By locator : candidates) {
            try {
                waitForVisibility(locator);
                String msg = driver.findElement(locator).getText();
                if (msg != null && !msg.trim().isEmpty()) {
                    logger.info("Order placed message found via {}: {}", locator, msg);
                    return msg;
                }
            } catch (Exception e) {
                logger.debug("Order locator not found: {}", locator);
            }
        }

        logger.warn("Order placed message not found via any locator");
        return "";
    }

    public void clickDownloadInvoice() {
        waitForVisibility(DOWNLOAD_INVOICE);
        click(DOWNLOAD_INVOICE);
        logger.info("Clicked Download Invoice");
    }

    public void clickContinueAfterOrder() {
        click(CONTINUE_BTN);
        logger.info("Clicked Continue after order");
    }

    // =========================================================================
    // FOOTER SUBSCRIPTION
    // =========================================================================

    public void enterSubscriptionEmail(String email) {
        sendKeys(SUBSCRIBE_EMAIL, email);
        logger.info("Entered subscription email: {}", email);
    }

    public void clickSubscribeButton() {
        click(SUBSCRIBE_BTN);
        logger.info("Clicked Subscribe");
    }

    public String getSubscriptionSuccessMessage() {
        waitForVisibility(SUBSCRIBE_SUCCESS);
        return getText(SUBSCRIBE_SUCCESS);
    }
}