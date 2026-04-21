package org.example.base;

import org.example.driver.DriverFactory;
import org.example.utils.ActionsHelper;
import org.example.utils.AlertHandler;
import org.example.utils.ConfigReader;
import org.example.utils.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage() {
        // Driver is initialized lazily on first use, not here
    }

    /**
     * Ensure driver is initialized before any interaction.
     * This is called at the start of every public method to safely lazy-load the driver.
     */
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
    // OVERLAY HANDLING (One-time per page)
    // =====================

    /**
     * Handle blocking UI elements (overlays, consent popups, alerts).
     * Call this manually after navigation if needed, or rely on BaseTest.setup() for initial page load.
     * This is NOT called before every interaction - only once per page for performance.
     */
    public void handleOverlaysOnce() {
        try {
            ensureDriver();
            AlertHandler.handleAllBlockersIfPresent(driver);
        } catch (Exception e) {
            logger.debug("Error during overlay handling: " + e.getMessage());
        }
    }

    // =====================
    // CLICK ACTIONS
    // =====================

    /**
     * Safe click by locator - handles retries if interactable exception occurs
     */
    public void click(By locator) {
        ensureDriver();
        try {
            WaitUtils.waitForClickability(driver, locator);
            WebElement element = driver.findElement(locator);
            ActionsHelper.scrollToElement(driver, element);
            element.click();
            logger.info("Clicked: " + locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable, retrying with JS click: " + locator);
            ActionsHelper.jsClick(driver, locator);
        }
    }

    /**
     * Safe click by WebElement - handles retries if interactable exception occurs
     */
    public void click(WebElement element) {
        ensureDriver();
        try {
            WaitUtils.waitForClickability(driver, element);
            ActionsHelper.scrollToElement(driver, element);
            element.click();
            logger.info("Clicked WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable, retrying with JS click");
            ActionsHelper.jsClick(driver, element);
        }
    }

    // =====================
    // SEND KEYS ACTIONS
    // =====================

    /**
     * Safe sendKeys by locator - handles retries if interactable exception occurs
     */
    public void sendKeys(By locator, String text) {
        ensureDriver();
        try {
            WaitUtils.waitForVisibility(driver, locator);
            WebElement element = driver.findElement(locator);
            ActionsHelper.scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed '" + text + "' in: " + locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on sendKeys, handling and retrying");
            WaitUtils.waitForVisibility(driver, locator);
            WebElement element = driver.findElement(locator);
            ActionsHelper.scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Successfully typed after retry: " + text);
        }
    }

    /**
     * Safe sendKeys by WebElement - handles retries if interactable exception occurs
     */
    public void sendKeys(WebElement element, String text) {
        ensureDriver();
        try {
            WaitUtils.waitForVisibility(driver, element);
            ActionsHelper.scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed in WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on sendKeys, handling and retrying");
            WaitUtils.waitForVisibility(driver, element);
            ActionsHelper.scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Successfully typed in WebElement after retry");
        }
    }

    // =====================
    // GET TEXT
    // =====================

    /**
     * Get text from element by locator
     */
    public String getText(By locator) {
        ensureDriver();
        WaitUtils.waitForVisibility(driver, locator);
        String text = driver.findElement(locator).getText();
        logger.info("Got text: " + text);
        return text;
    }

    /**
     * Get text from WebElement
     */
    public String getText(WebElement element) {
        ensureDriver();
        WaitUtils.waitForVisibility(driver, element);
        String text = element.getText();
        logger.info("Got text: " + text);
        return text;
    }

    // =====================
    // DISPLAY CHECK
    // =====================

    /**
     * Check if element is displayed by locator
     */
    public boolean isDisplayed(By locator) {
        ensureDriver();
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if WebElement is displayed
     */
    public boolean isDisplayed(WebElement element) {
        ensureDriver();
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // =====================
    // CLEAR
    // =====================

    /**
     * Clear element by locator
     */
    public void clear(By locator) {
        ensureDriver();
        WaitUtils.waitForVisibility(driver, locator);
        driver.findElement(locator).clear();
        logger.info("Cleared element: " + locator);
    }

    // =====================
    // WAIT HELPERS
    // =====================

    /**
     * Wait for element to be visible - delegates to WaitUtils
     */
    protected WebElement waitForVisibility(By locator) {
        ensureDriver();
        return WaitUtils.waitForVisibility(driver, locator);
    }

    /**
     * Wait for element to be clickable - delegates to WaitUtils
     */
    protected WebElement waitForClickability(By locator) {
        ensureDriver();
        return WaitUtils.waitForClickability(driver, locator);
    }

    /**
     * Wait for overlay to disappear - delegates to WaitUtils
     */
    protected void waitForOverlay() {
        ensureDriver();
        WaitUtils.waitForOverlayToDisappear(driver);
    }

    // =====================
    // SCROLL HELPERS
    // =====================

    /**
     * Scroll to bottom - delegates to ActionsHelper
     */
    protected void scrollToBottom() {
        ensureDriver();
        ActionsHelper.scrollToBottom(driver);
    }

    /**
     * Scroll to top - delegates to ActionsHelper
     */
    protected void scrollToTop() {
        ensureDriver();
        ActionsHelper.scrollToTop(driver);
    }

    /**
     * Scroll to specific element - delegates to ActionsHelper
     */
    protected void scrollToElement(WebElement element) {
        ensureDriver();
        ActionsHelper.scrollToElement(driver, element);
    }

    // =====================
    // HOVER HELPER
    // =====================

    /**
     * Hover over element - delegates to ActionsHelper
     */
    protected void hoverOver(WebElement element) {
        ensureDriver();
        ActionsHelper.hoverOverElement(driver, element);
    }

    /**
     * Hide ad iframes - delegates to AlertHandler
     */
    protected void hideAdIframes() {
        ensureDriver();
        AlertHandler.hideAdIframes(driver);
    }
}