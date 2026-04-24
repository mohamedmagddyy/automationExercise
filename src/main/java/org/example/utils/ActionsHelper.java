package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ActionsHelper — The single engine for ALL WebDriver interactions.
 *
 * Responsibilities:
 *  - Wait (via WaitUtils)
 *  - Scroll into view
 *  - Interact (click / sendKeys / getText / select / hover / drag)
 *  - JS fallback when element is not interactable
 *
 * AlertHandler is NOT called here — overlays are handled once per page
 * navigation via BasePage.handleOverlaysOnce().
 */
public class ActionsHelper {

    private static final Logger logger = LogManager.getLogger(ActionsHelper.class);

    // =========================================================================
    // CLICK
    // =========================================================================

    /**
     * Click by locator.
     * Flow: waitForClickability → scrollIntoView → click → fallback JS click
     */
    public static void click(WebDriver driver, By locator) {
        try {
            WebElement element = WaitUtils.waitForClickability(driver, locator);
            scrollToElement(driver, element);
            element.click();
            logger.info("Clicked: {}", locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on {}, retrying with JS click", locator);
            jsClick(driver, locator);
        } catch (WebDriverException e) {
            if (e.getMessage() != null && e.getMessage().contains("intercepted")) {
                logger.warn("Click intercepted on {}, retrying with JS click", locator);
                jsClick(driver, locator);
            } else {
                throw e;
            }
        }
    }

    /**
     * Click by WebElement.
     * Flow: waitForClickability → scrollIntoView → click → fallback JS click
     */
    public static void click(WebDriver driver, WebElement element) {
        try {
            WaitUtils.waitForClickability(driver, element);
            scrollToElement(driver, element);
            element.click();
            logger.info("Clicked WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on WebElement, retrying with JS click");
            jsClick(driver, element);
        } catch (WebDriverException e) {
            if (e.getMessage() != null && e.getMessage().contains("intercepted")) {
                logger.warn("Click intercepted on WebElement, retrying with JS click");
                jsClick(driver, element);
            } else {
                throw e;
            }
        }
    }

    // =========================================================================
    // SEND KEYS
    // =========================================================================

    /**
     * Type into field by locator.
     * Flow: waitForVisibility → scrollIntoView → clear → sendKeys → fallback JS setValue
     */
    public static void sendKeys(WebDriver driver, By locator, String text) {
        try {
            WebElement element = WaitUtils.waitForVisibility(driver, locator);
            scrollToElement(driver, element);
            clearField(element);
            element.sendKeys(text);
            logger.info("Typed '{}' into: {}", text, locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("sendKeys failed on {}, retrying with JS setValue — reason: {}", locator, e.getMessage());
            jsSetValue(driver, locator, text);
        }
    }

    /**
     * Type into field by WebElement.
     * Flow: waitForVisibility → scrollIntoView → clear → sendKeys → fallback JS setValue
     */
    public static void sendKeys(WebDriver driver, WebElement element, String text) {
        try {
            WaitUtils.waitForVisibility(driver, element);
            scrollToElement(driver, element);
            clearField(element);
            element.sendKeys(text);
            logger.info("Typed '{}' into WebElement", text);
        } catch (ElementNotInteractableException e) {
            logger.warn("sendKeys failed on WebElement, retrying with JS setValue — reason: {}", e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", element, text);
            logger.info("JS setValue succeeded for WebElement");
        }
    }

    /**
     * Safe field clear: clear() + select-all + delete to handle tricky inputs.
     */
    private static void clearField(WebElement element) {
        element.clear();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.DELETE);
    }

    // =========================================================================
    // GET TEXT
    // =========================================================================

    public static String getText(WebDriver driver, By locator) {
        WaitUtils.waitForVisibility(driver, locator);
        String text = driver.findElement(locator).getText().trim();
        logger.info("getText from {}: '{}'", locator, text);
        return text;
    }

    public static String getText(WebDriver driver, WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        String text = element.getText().trim();
        logger.info("getText from WebElement: '{}'", text);
        return text;
    }

    // =========================================================================
    // DISPLAY CHECK
    // =========================================================================

    public static boolean isDisplayed(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public static boolean isDisplayed(WebDriver driver, WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    // =========================================================================
    // CLEAR
    // =========================================================================

    public static void clear(WebDriver driver, By locator) {
        WaitUtils.waitForVisibility(driver, locator);
        clearField(driver.findElement(locator));
        logger.info("Cleared field: {}", locator);
    }

    // =========================================================================
    // SELECT DROPDOWNS
    // =========================================================================

    /**
     * Select dropdown option by value attribute.
     */
    public static void selectByValue(WebDriver driver, By locator, String value) {
        WaitUtils.waitForVisibility(driver, locator);
        Select select = new Select(driver.findElement(locator));
        select.selectByValue(value);
        logger.info("Selected by value '{}' from: {}", value, locator);
    }

    /**
     * Select dropdown option by visible text.
     */
    public static void selectByVisibleText(WebDriver driver, By locator, String text) {
        WaitUtils.waitForVisibility(driver, locator);
        Select select = new Select(driver.findElement(locator));
        select.selectByVisibleText(text);
        logger.info("Selected by text '{}' from: {}", text, locator);
    }

    /**
     * Select dropdown option by index.
     */
    public static void selectByIndex(WebDriver driver, By locator, int index) {
        WaitUtils.waitForVisibility(driver, locator);
        Select select = new Select(driver.findElement(locator));
        select.selectByIndex(index);
        logger.info("Selected by index '{}' from: {}", index, locator);
    }

    // =========================================================================
    // SCROLL
    // =========================================================================

    /**
     * Scroll element to center of viewport.
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
            logger.debug("Scrolled element into view");
        } catch (Exception e) {
            logger.warn("scrollToElement failed: {}", e.getMessage());
        }
    }

    /**
     * Scroll element to center by locator.
     */
    public static void scrollToElement(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        scrollToElement(driver, element);
    }

    /**
     * Scroll to bottom of page.
     */
    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        logger.info("Scrolled to bottom");
    }

    /**
     * Scroll to top of page.
     */
    public static void scrollToTop(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        logger.info("Scrolled to top");
    }

    /**
     * Scroll by pixel offset.
     */
    public static void scrollBy(WebDriver driver, int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
        logger.info("Scrolled by x={}, y={}", x, y);
    }

    /**
     * Get current vertical scroll position.
     */
    public static long getScrollPositionY(WebDriver driver) {
        return (long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
    }

    /**
     * Check if page is scrolled to the very top.
     */
    public static boolean isScrolledToTop(WebDriver driver) {
        return getScrollPositionY(driver) == 0;
    }

    // =========================================================================
    // HOVER
    // =========================================================================

    public static void hoverOverElement(WebDriver driver, WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).moveToElement(element).perform();
        logger.info("Hovered over WebElement");
    }

    public static void hoverOverElement(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        new Actions(driver).moveToElement(element).perform();
        logger.info("Hovered over: {}", locator);
    }

    public static void hoverAndClick(WebDriver driver, WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).moveToElement(element).click().perform();
        logger.info("Hovered and clicked WebElement");
    }

    // =========================================================================
    // DRAG AND DROP
    // =========================================================================

    public static void dragAndDrop(WebDriver driver, WebElement source, WebElement target) {
        WaitUtils.waitForVisibility(driver, source);
        WaitUtils.waitForVisibility(driver, target);
        new Actions(driver).dragAndDrop(source, target).perform();
        logger.info("Drag and drop performed");
    }

    // =========================================================================
    // KEYBOARD
    // =========================================================================

    public static void pressKey(WebDriver driver, WebElement element, Keys key) {
        WaitUtils.waitForVisibility(driver, element);
        new Actions(driver).sendKeys(element, key).perform();
        logger.info("Pressed key: {}", key.name());
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

    // =========================================================================
    // JAVASCRIPT HELPERS
    // =========================================================================

    public static void jsClick(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.info("JS click on WebElement");
    }

    public static void jsClick(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.info("JS click on: {}", locator);
    }

    public static void jsSetValue(WebDriver driver, By locator, String value) {
        WebElement element = WaitUtils.waitForVisibility(driver, locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", element, value);
        logger.info("JS setValue '{}' on: {}", value, locator);
    }

    public static void highlightElement(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.border='3px solid red'", element);
            logger.debug("Element highlighted");
        } catch (Exception e) {
            logger.debug("Could not highlight element: {}", e.getMessage());
        }
    }

    // =========================================================================
    // DOUBLE / RIGHT CLICK
    // =========================================================================

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