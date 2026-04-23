package org.example.base;

import org.example.driver.DriverFactory;
import org.example.utils.ActionsHelper;
import org.example.utils.AlertHandler;
import org.example.utils.WaitUtils;
import org.openqa.selenium.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasePage {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage() {
        // Driver is initialized lazily on first use
    }

    // =====================
    // DRIVER INIT
    // =====================

    protected void ensureDriver() {
        if (driver == null) {
            driver = DriverFactory.getDriver();
            if (driver == null) {
                throw new RuntimeException(
                        "WebDriver is not initialized. Ensure BaseTest.setup() is called before page object methods."
                );
            }
        }
    }

    // =====================
    // OVERLAY HANDLING
    // =====================

    public void handleOverlaysOnce() {
        ensureDriver();
        AlertHandler.handleAllBlockersIfPresent(driver);
    }

    // =====================
    // CLICK
    // =====================

    public void click(By locator) {
        ensureDriver();
        ActionsHelper.click(driver, locator);
    }

    public void click(WebElement element) {
        ensureDriver();
        ActionsHelper.click(driver, element);
    }

    // =====================
    // SEND KEYS
    // =====================

    public void sendKeys(By locator, String text) {
        ensureDriver();
        ActionsHelper.sendKeys(driver, locator, text);
    }

    public void sendKeys(WebElement element, String text) {
        ensureDriver();
        ActionsHelper.sendKeys(driver, element, text);
    }

    // =====================
    // GET TEXT
    // =====================

    public String getText(By locator) {
        ensureDriver();
        return ActionsHelper.getText(driver, locator);
    }

    public String getText(WebElement element) {
        ensureDriver();
        return ActionsHelper.getText(driver, element);
    }

    // =====================
    // DISPLAY CHECK
    // =====================

    public boolean isDisplayed(By locator) {
        ensureDriver();
        return ActionsHelper.isDisplayed(driver, locator);
    }

    public boolean isDisplayed(WebElement element) {
        ensureDriver();
        return ActionsHelper.isDisplayed(driver, element);
    }

    // =====================
    // CLEAR
    // =====================

    public void clear(By locator) {
        ensureDriver();
        ActionsHelper.clear(driver, locator);
    }

    // =====================
    // WAIT HELPERS
    // =====================

    protected WebElement waitForVisibility(By locator) {
        ensureDriver();
        return WaitUtils.waitForVisibility(driver, locator);
    }

    protected WebElement waitForClickability(By locator) {
        ensureDriver();
        return WaitUtils.waitForClickability(driver, locator);
    }

    protected void waitForOverlay() {
        ensureDriver();
        WaitUtils.waitForOverlayToDisappear(driver);
    }

    // =====================
    // SCROLL HELPERS
    // =====================

    protected void scrollToBottom() {
        ensureDriver();
        ActionsHelper.scrollToBottom(driver);
    }

    protected void scrollToTop() {
        ensureDriver();
        ActionsHelper.scrollToTop(driver);
    }

    protected void scrollToElement(WebElement element) {
        ensureDriver();
        ActionsHelper.scrollToElement(driver, element);
    }

    // =====================
    // HOVER HELPER
    // =====================

    protected void hoverOver(WebElement element) {
        ensureDriver();
        ActionsHelper.hoverOverElement(driver, element);
    }

    // =====================
    // AD HELPER
    // =====================

    protected void hideAdIframes() {
        ensureDriver();
        AlertHandler.hideAdIframes(driver);
    }
}