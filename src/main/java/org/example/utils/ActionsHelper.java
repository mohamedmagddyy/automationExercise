package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class ActionsHelper {

    private static final Logger logger = LogManager.getLogger(ActionsHelper.class);

    // =====================
    // SCROLL ACTIONS
    // =====================

    /**
     * Scroll element into view
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            logger.debug("Scrolled element into view");
        } catch (Exception e) {
            logger.warn("Could not scroll to element: " + e.getMessage());
        }
    }

    /**
     * Scroll to bottom of page then wait for overlays
     */
    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WaitUtils.waitForOverlayToDisappear(driver);
        logger.info("Scrolled to bottom");
    }

    /**
     * Scroll to top of page then wait for overlays
     */
    public static void scrollToTop(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        WaitUtils.waitForOverlayToDisappear(driver);
        logger.info("Scrolled to top");
    }

    /**
     * Scroll by specific pixels (x, y)
     */
    public static void scrollBy(WebDriver driver, int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(" + x + "," + y + ")");
        logger.info("Scrolled by x=" + x + ", y=" + y);
    }

    /**
     * Scroll to element and click it
     */
    public static void scrollToElementAndClick(WebDriver driver, WebElement element) {
        scrollToElement(driver, element);
        WaitUtils.waitForClickability(driver, element);
        element.click();
        logger.info("Scrolled to element and clicked");
    }

    // =====================
    // HOVER ACTIONS
    // =====================

    /**
     * Hover over element
     */
    public static void hoverOverElement(WebDriver driver, WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).moveToElement(element).perform();
        logger.info("Hovered over element");
    }

    /**
     * Hover over element then click
     */
    public static void hoverAndClick(WebDriver driver, WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).moveToElement(element).click().perform();
        logger.info("Hovered and clicked element");
    }

    /**
     * Hover over element by locator
     */
    public static void hoverOverElement(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        new Actions(driver).moveToElement(element).perform();
        logger.info("Hovered over element: " + locator);
    }

    // =====================
    // DRAG AND DROP
    // =====================

    /**
     * Drag element and drop to target
     */
    public static void dragAndDrop(WebDriver driver, WebElement source, WebElement target) {
        WaitUtils.waitForVisibility(driver, source);
        WaitUtils.waitForVisibility(driver, target);
        new Actions(driver).dragAndDrop(source, target).perform();
        logger.info("Drag and drop performed");
    }

    // =====================
    // KEYBOARD ACTIONS
    // =====================

    /**
     * Press a key on element
     */
    public static void pressKey(WebDriver driver, WebElement element, Keys key) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).sendKeys(element, key).perform();
        logger.info("Pressed key: " + key.name());
    }

    /**
     * Press Enter on element
     */
    public static void pressEnter(WebDriver driver, WebElement element) {
        pressKey(driver, element, Keys.ENTER);
    }

    /**
     * Press Tab on element
     */
    public static void pressTab(WebDriver driver, WebElement element) {
        pressKey(driver, element, Keys.TAB);
    }

    /**
     * Press Escape
     */
    public static void pressEscape(WebDriver driver) {
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        logger.info("Pressed Escape");
    }

    // =====================
    // JAVASCRIPT ACTIONS
    // =====================

    /**
     * JS click - use when normal click fails
     */
    public static void jsClick(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.info("JS click performed");
    }

    /**
     * JS click by locator
     */
    public static void jsClick(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.info("JS click performed on: " + locator);
    }

    /**
     * Get page scroll position (Y)
     */
    public static long getScrollPositionY(WebDriver driver) {
        return (long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
    }

    /**
     * Check if page is scrolled to top
     */
    public static boolean isScrolledToTop(WebDriver driver) {
        return getScrollPositionY(driver) == 0;
    }

    /**
     * Highlight element (useful for debugging)
     */
    public static void highlightElement(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='3px solid red'", element);
            logger.debug("Element highlighted");
        } catch (Exception e) {
            logger.debug("Could not highlight element: " + e.getMessage());
        }
    }

    // =====================
    // DOUBLE CLICK
    // =====================

    /**
     * Double click on element
     */
    public static void doubleClick(WebDriver driver, WebElement element) {
        WaitUtils.waitForClickability(driver, element);
        new Actions(driver).doubleClick(element).perform();
        logger.info("Double clicked element");
    }

    // =====================
    // RIGHT CLICK
    // =====================

    /**
     * Right click on element
     */
    public static void rightClick(WebDriver driver, WebElement element) {
        WaitUtils.waitForClickability(driver, element);
        new Actions(driver).contextClick(element).perform();
        logger.info("Right clicked element");
    }

    // =====================
    // WAIT + SCROLL COMBO
    // =====================

    /**
     * Scroll to element, wait for it to be visible, then return it
     */
    public static WebElement scrollAndWait(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        scrollToElement(driver, element);
        WaitUtils.waitForVisibility(driver, element);
        return element;
    }
}