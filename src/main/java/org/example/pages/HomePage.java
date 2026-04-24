package org.example.pages;

import org.example.base.BasePage;
import org.openqa.selenium.By;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HomePage — Page Object for the Home page.
 */
public class HomePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    // ── Navigation ─────────────────────────────────────────────────────────
    private static final By TEST_CASES_LINK = By.cssSelector("a[href='/test_cases']");
    private static final By PRODUCTS_LINK   = By.cssSelector("a[href='/products']");
    private static final By CART_LINK       = By.cssSelector("a[href='/view_cart']");
    private static final By CONTACT_LINK    = By.cssSelector("a[href='/contact_us']");

    // ── Subscription ───────────────────────────────────────────────────────
    private static final By SUBSCRIPTION_EMAIL   = By.id("susbscribe_email");
    private static final By SUBSCRIBE_BTN        = By.id("subscribe");
    private static final By SUBSCRIPTION_SUCCESS = By.cssSelector("div.alert-success");

    // ── Scroll Up Arrow ────────────────────────────────────────────────────
    private static final By SCROLL_UP_ARROW = By.id("scrollUp");

    // ── Footer ─────────────────────────────────────────────────────────────
    private static final By FOOTER = By.cssSelector("footer");

    // ── Recommended Items ──────────────────────────────────────────────────
    // الـ carousel عنده active slide بس — الـ buttons في الـ inactive slides موجودة في الـ DOM
    // بس مش visible — لازم نكلك على الـ active بس
    private static final By RECOMMENDED_SECTION   = By.cssSelector("div.recommended_items");
    private static final By RECOMMENDED_ACTIVE_BTN = By.cssSelector(
            "div.recommended_items div.item.active div.productinfo a.add-to-cart");

    // من الـ HTML: <button class="btn btn-success close-modal" data-dismiss="modal">
    private static final By MODAL_CLOSE_BTN = By.cssSelector("button.btn.btn-success.close-modal");

    // ── Constructor ────────────────────────────────────────────────────────
    public HomePage() {
        super();
    }

    // =========================================================================
    // NAVIGATION
    // =========================================================================

    public void navigateToTestCases() {
        click(TEST_CASES_LINK);
        logger.info("Navigated to Test Cases page");
    }

    public void navigateToProducts() {
        click(PRODUCTS_LINK);
        logger.info("Navigated to Products page");
    }

    public void navigateToCart() {
        click(CART_LINK);
        logger.info("Navigated to Cart page");
    }

    public void navigateToContactUs() {
        click(CONTACT_LINK);
        logger.info("Navigated to Contact Us page");
    }

    // =========================================================================
    // SUBSCRIPTION
    // =========================================================================

    public void enterSubscriptionEmail(String email) {
        sendKeys(SUBSCRIPTION_EMAIL, email);
        logger.info("Entered subscription email: {}", email);
    }

    public void clickSubscribeButton() {
        click(SUBSCRIBE_BTN);
        logger.info("Clicked Subscribe button");
    }

    public String getSubscriptionSuccessMessage() {
        return getText(SUBSCRIPTION_SUCCESS);
    }

    // =========================================================================
    // SCROLL
    // =========================================================================

    public void scrollDownToFooter() {
        scrollToBottom();
        waitForVisibility(FOOTER);
        logger.info("Scrolled down to footer");
    }

    public void clickScrollUpArrowButton() {
        scrollDownToFooter();
        click(SCROLL_UP_ARROW);
        logger.info("Clicked scroll-up arrow button");
    }

    public void scrollPageToTop() {
        scrollToTop();
        logger.info("Scrolled page to top");
    }

    public boolean isScrolledToTop() {
        return super.isScrolledToTop();
    }

    /**
     * Returns true if the top navigation bar is visible.
     * Used in TC25 to verify the page scrolled back to top.
     */
    public boolean isNavigationVisible() {
        return isDisplayed(PRODUCTS_LINK);
    }

    // =========================================================================
    // RECOMMENDED ITEMS
    // =========================================================================

    /**
     * Scrolls to the recommended items carousel, clicks Add-to-Cart on the
     * first product inside the ACTIVE slide only, then closes the modal.
     *
     * المشكلة القديمة: الـ selector كان بياخد كل الـ buttons في كل الـ slides
     * حتى الـ hidden منها — والـ jsClick على hidden element مش بيفتح الـ modal.
     *
     * الحل: نحدد بـ div.item.active عشان نوصل للـ visible buttons بس.
     */
    public void addFirstRecommendedItemToCart() {
        scrollToElement(RECOMMENDED_SECTION);
        hideAdIframes();

        waitForVisibility(RECOMMENDED_ACTIVE_BTN);
        jsClick(RECOMMENDED_ACTIVE_BTN);
        logger.info("Clicked Add to Cart on first recommended item in active slide");

        waitForVisibility(MODAL_CLOSE_BTN);
        click(MODAL_CLOSE_BTN);
        logger.info("Closed cart modal");
    }
}