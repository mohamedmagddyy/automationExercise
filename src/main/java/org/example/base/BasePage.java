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
        this.driver = DriverFactory.getDriver();
    }

    // =====================
    // PREPARATION
    // =====================

    /**
     * Handle any blocking UI elements before interaction
     */
    protected void prepareForInteraction() {
        try {
            AlertHandler.handleAllBlockersIfPresent(driver);
        } catch (Exception e) {
            logger.debug("Error during interaction preparation: " + e.getMessage());
        }
    }

    // =====================
    // CLICK ACTIONS
    // =====================

    /**
     * Safe click by locator - handles overlays and retries
     */
    public void click(By locator) {
        try {
            prepareForInteraction();
            WaitUtils.waitForClickability(driver, locator);
            WebElement element = driver.findElement(locator);
            ActionsHelper.scrollToElement(driver, element);
            element.click();
            logger.info("Clicked: " + locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable, retrying with JS click: " + locator);
            prepareForInteraction();
            ActionsHelper.jsClick(driver, locator);
        }
    }

    /**
     * Safe click by WebElement - handles overlays and retries
     */
    public void click(WebElement element) {
        try {
            prepareForInteraction();
            WaitUtils.waitForClickability(driver, element);
            ActionsHelper.scrollToElement(driver, element);
            element.click();
            logger.info("Clicked WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable, retrying with JS click");
            prepareForInteraction();
            ActionsHelper.jsClick(driver, element);
        }
    }

    // =====================
    // SEND KEYS ACTIONS
    // =====================

    /**
     * Safe sendKeys by locator - handles overlays and retries
     */
    public void sendKeys(By locator, String text) {
        try {
            prepareForInteraction();
            WaitUtils.waitForVisibility(driver, locator);
            WebElement element = driver.findElement(locator);
            ActionsHelper.scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed '" + text + "' in: " + locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on sendKeys, handling overlays and retrying");
            prepareForInteraction();
            WaitUtils.waitForVisibility(driver, locator);
            WebElement element = driver.findElement(locator);
            ActionsHelper.scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Successfully typed after retry: " + text);
        }
    }

    /**
     * Safe sendKeys by WebElement - handles overlays and retries
     */
    public void sendKeys(WebElement element, String text) {
        try {
            prepareForInteraction();
            WaitUtils.waitForVisibility(driver, element);
            ActionsHelper.scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed in WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on sendKeys, handling overlays and retrying");
            prepareForInteraction();
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
        WaitUtils.waitForVisibility(driver, locator);
        String text = driver.findElement(locator).getText();
        logger.info("Got text: " + text);
        return text;
    }

    /**
     * Get text from WebElement
     */
    public String getText(WebElement element) {
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
        return WaitUtils.waitForVisibility(driver, locator);
    }

    /**
     * Wait for element to be clickable - delegates to WaitUtils
     */
    protected WebElement waitForClickability(By locator) {
        return WaitUtils.waitForClickability(driver, locator);
    }

    /**
     * Wait for overlay to disappear - delegates to WaitUtils
     */
    protected void waitForOverlay() {
        WaitUtils.waitForOverlayToDisappear(driver);
    }

    // =====================
    // SCROLL HELPERS
    // =====================

    /**
     * Scroll to bottom - delegates to ActionsHelper
     */
    protected void scrollToBottom() {
        ActionsHelper.scrollToBottom(driver);
    }

    /**
     * Scroll to top - delegates to ActionsHelper
     */
    protected void scrollToTop() {
        ActionsHelper.scrollToTop(driver);
    }

    /**
     * Scroll to specific element - delegates to ActionsHelper
     */
    protected void scrollToElement(WebElement element) {
        ActionsHelper.scrollToElement(driver, element);
    }

    // =====================
    // HOVER HELPER
    // =====================

    /**
     * Hover over element - delegates to ActionsHelper
     */
    protected void hoverOver(WebElement element) {
        ActionsHelper.hoverOverElement(driver, element);
    }
}