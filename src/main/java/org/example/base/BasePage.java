package org.example.base;

import org.example.driver.DriverFactory;
import org.example.utils.ActionsHelper;
import org.example.utils.AlertHandler;
import org.example.utils.WaitUtils;
import org.openqa.selenium.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BasePage — Facade layer over ActionsHelper.
 *
 * Rules:
 *  - NEVER talks to WaitUtils or AlertHandler directly (except handleOverlaysOnce)
 *  - NEVER contains business logic
 *  - NEVER duplicates ActionsHelper logic
 *  - ensureDriver() is called ONCE per method group, not per line
 *
 * Overlay strategy:
 *  - Overlays are handled ONCE at page load in BaseTest.setup()
 *  - If a test navigates to a new page, call handleOverlaysOnce() manually
 *  - click() and sendKeys() do NOT check for overlays — they are fast by design
 */
public class BasePage {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        if (this.driver == null) {
            throw new RuntimeException(
                    "WebDriver is not initialized. Ensure BaseTest.setup() runs before any Page Object is created."
            );
        }
    }

    // =========================================================================
    // OVERLAY — call manually after page navigation inside a test
    // =========================================================================

    /**
     * Call this after navigating to a NEW page inside a test method.
     * Do NOT call before every click — that kills performance.
     */
    public void handleOverlaysOnce() {
        AlertHandler.handleAllBlockersIfPresent(driver);
        logger.debug("handleOverlaysOnce() completed");
    }

    // =========================================================================
    // CLICK
    // =========================================================================

    public void click(By locator) {
        ActionsHelper.click(driver, locator);
    }

    public void click(WebElement element) {
        ActionsHelper.click(driver, element);
    }

    // =========================================================================
    // SEND KEYS
    // =========================================================================

    public void sendKeys(By locator, String text) {
        ActionsHelper.sendKeys(driver, locator, text);
    }

    public void sendKeys(WebElement element, String text) {
        ActionsHelper.sendKeys(driver, element, text);
    }

    // =========================================================================
    // GET TEXT
    // =========================================================================

    public String getText(By locator) {
        return ActionsHelper.getText(driver, locator);
    }

    public String getText(WebElement element) {
        return ActionsHelper.getText(driver, element);
    }

    // =========================================================================
    // DISPLAY CHECK
    // =========================================================================

    public boolean isDisplayed(By locator) {
        return ActionsHelper.isDisplayed(driver, locator);
    }

    public boolean isDisplayed(WebElement element) {
        return ActionsHelper.isDisplayed(driver, element);
    }

    // =========================================================================
    // CLEAR
    // =========================================================================

    public void clear(By locator) {
        ActionsHelper.clear(driver, locator);
    }

    // =========================================================================
    // SELECT DROPDOWNS — replaces direct new Select() calls in page objects
    // =========================================================================

    public void selectByValue(By locator, String value) {
        ActionsHelper.selectByValue(driver, locator, value);
    }

    public void selectByVisibleText(By locator, String text) {
        ActionsHelper.selectByVisibleText(driver, locator, text);
    }

    public void selectByIndex(By locator, int index) {
        ActionsHelper.selectByIndex(driver, locator, index);
    }

    // =========================================================================
    // WAIT HELPERS — thin wrappers, use sparingly in page objects
    // =========================================================================

    protected WebElement waitForVisibility(By locator) {
        return WaitUtils.waitForVisibility(driver, locator);
    }

    protected WebElement waitForClickability(By locator) {
        return WaitUtils.waitForClickability(driver, locator);
    }

    protected boolean waitForInvisibility(By locator) {
        return WaitUtils.waitForInvisibility(driver, locator);
    }

    // =========================================================================
    // SCROLL HELPERS
    // =========================================================================

    protected void scrollToBottom() {
        ActionsHelper.scrollToBottom(driver);
    }

    protected void scrollToTop() {
        ActionsHelper.scrollToTop(driver);
    }

    protected void scrollToElement(WebElement element) {
        ActionsHelper.scrollToElement(driver, element);
    }

    protected void scrollToElement(By locator) {
        ActionsHelper.scrollToElement(driver, locator);
    }

    protected boolean isScrolledToTop() {
        return ActionsHelper.isScrolledToTop(driver);
    }

    // =========================================================================
    // HOVER
    // =========================================================================

    protected void hoverOver(WebElement element) {
        ActionsHelper.hoverOverElement(driver, element);
    }

    protected void hoverOver(By locator) {
        ActionsHelper.hoverOverElement(driver, locator);
    }

    // =========================================================================
    // JS HELPERS
    // =========================================================================

    protected void jsClick(By locator) {
        ActionsHelper.jsClick(driver, locator);
    }

    protected void jsClick(WebElement element) {
        ActionsHelper.jsClick(driver, element);
    }

    protected void hideAdIframes() {
        AlertHandler.hideAdIframes(driver);
    }
}