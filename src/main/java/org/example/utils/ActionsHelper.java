package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionsHelper {

    private static final Logger logger = LogManager.getLogger(ActionsHelper.class);

    // =====================
    // CLICK ACTIONS
    // =====================

    /**
     * Safe click by locator — falls back to JS click if not interactable
     */
    public static void click(WebDriver driver, By locator) {
        try {
            WaitUtils.waitForClickability(driver, locator);
            WebElement element = driver.findElement(locator);
            scrollToElement(driver, element);
            element.click();
            logger.info("Clicked: " + locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable, retrying with JS click: " + locator);
            jsClick(driver, locator);
        }
    }

    /**
     * Safe click by WebElement — falls back to JS click if not interactable
     */
    public static void click(WebDriver driver, WebElement element) {
        try {
            WaitUtils.waitForClickability(driver, element);
            scrollToElement(driver, element);
            element.click();
            logger.info("Clicked WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable, retrying with JS click");
            jsClick(driver, element);
        }
    }

    // =====================
    // SEND KEYS ACTIONS
    // =====================

    /**
     * Safe sendKeys by locator
     */
    public static void sendKeys(WebDriver driver, By locator, String text) {
        try {
            WaitUtils.waitForVisibility(driver, locator);
            WebElement element = driver.findElement(locator);
            scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed '" + text + "' in: " + locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on sendKeys, retrying with JS: " + locator);
            WebElement element = WaitUtils.waitForVisibility(driver, locator);
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];", element, text);
            logger.info("Successfully typed using JS: " + text);
        }
    }

    /**
     * Safe sendKeys by WebElement
     */
    public static void sendKeys(WebDriver driver, WebElement element, String text) {
        try {
            WaitUtils.waitForVisibility(driver, element);
            scrollToElement(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed in WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on sendKeys WebElement, retrying with JS");
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];", element, text);
            logger.info("Successfully typed using JS in WebElement");
        }
    }

    // =====================
    // GET TEXT
    // =====================

    /**
     * Get visible text from element by locator — waits for visibility first
     */
    public static String getText(WebDriver driver, By locator) {
        WaitUtils.waitForVisibility(driver, locator);
        String text = driver.findElement(locator).getText().trim();
        logger.info("Got text: " + text);
        return text;
    }

    /**
     * Get visible text from WebElement — waits for visibility first
     */
    public static String getText(WebDriver driver, WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        String text = element.getText().trim();
        logger.info("Got text: " + text);
        return text;
    }

    // =====================
    // DISPLAY CHECK
    // =====================

    /**
     * Check if element is displayed by locator — returns false on exception
     */
    public static boolean isDisplayed(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if WebElement is displayed — returns false on exception
     */
    public static boolean isDisplayed(WebDriver driver, WebElement element) {
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
    public static void clear(WebDriver driver, By locator) {
        WaitUtils.waitForVisibility(driver, locator);
        driver.findElement(locator).clear();
        logger.info("Cleared element: " + locator);
    }

    // =====================
    // SCROLL ACTIONS
    // =====================

    /**
     * Scroll element into view
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", element);
            logger.debug("Scrolled element into view");
        } catch (Exception e) {
            logger.warn("Could not scroll to element: " + e.getMessage());
        }
    }

    /**
     * Scroll to bottom then wait for overlays
     */
    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WaitUtils.waitForOverlayToDisappear(driver);
        logger.info("Scrolled to bottom");
    }

    /**
     * Scroll to top then wait for overlays
     */
    public static void scrollToTop(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        WaitUtils.waitForOverlayToDisappear(driver);
        logger.info("Scrolled to top");
    }

    /**
     * Scroll by specific pixels
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

    /**
     * Scroll to element, wait for visibility, return it
     */
    public static WebElement scrollAndWait(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        scrollToElement(driver, element);
        WaitUtils.waitForVisibility(driver, element);
        return element;
    }

    // =====================
    // HOVER ACTIONS
    // =====================

    public static void hoverOverElement(WebDriver driver, WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).moveToElement(element).perform();
        logger.info("Hovered over element");
    }

    public static void hoverAndClick(WebDriver driver, WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).moveToElement(element).click().perform();
        logger.info("Hovered and clicked element");
    }

    public static void hoverOverElement(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        new Actions(driver).moveToElement(element).perform();
        logger.info("Hovered over element: " + locator);
    }

    // =====================
    // DRAG AND DROP
    // =====================

    public static void dragAndDrop(WebDriver driver, WebElement source, WebElement target) {
        WaitUtils.waitForVisibility(driver, source);
        WaitUtils.waitForVisibility(driver, target);
        new Actions(driver).dragAndDrop(source, target).perform();
        logger.info("Drag and drop performed");
    }

    // =====================
    // KEYBOARD ACTIONS
    // =====================

    public static void pressKey(WebDriver driver, WebElement element, Keys key) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).sendKeys(element, key).perform();
        logger.info("Pressed key: " + key.name());
    }

    public static void pressEnter(WebDriver driver, WebElement element) {
        pressKey(driver, element, Keys.ENTER);
    }

    public static void pressTab(WebDriver driver, WebElement element) {
        pressKey(driver, element, Keys.TAB);
    }

    public static void pressEscape(WebDriver driver) {
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        logger.info("Pressed Escape");
    }

    // =====================
    // JAVASCRIPT ACTIONS
    // =====================

    public static void jsClick(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.info("JS click performed");
    }

    public static void jsClick(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.info("JS click performed on: " + locator);
    }

    public static long getScrollPositionY(WebDriver driver) {
        return (long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
    }

    public static boolean isScrolledToTop(WebDriver driver) {
        return getScrollPositionY(driver) == 0;
    }

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
    // DOUBLE / RIGHT CLICK
    // =====================

    public static void doubleClick(WebDriver driver, WebElement element) {
        WaitUtils.waitForClickability(driver, element);
        new Actions(driver).doubleClick(element).perform();
        logger.info("Double clicked element");
    }

    public static void rightClick(WebDriver driver, WebElement element) {
        WaitUtils.waitForClickability(driver, element);
        new Actions(driver).contextClick(element).perform();
        logger.info("Right clicked element");
    }
}