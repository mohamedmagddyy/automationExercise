package org.example.utils;

import org.example.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.Duration;

/**
 * WaitUtils - Handles all explicit wait operations
 */
public class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);

    /**
     * Get WebDriverWait instance with configured timeout
     *
     * @param driver WebDriver instance
     * @return WebDriverWait instance
     */
    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getWaitTimeout()));
    }

    /**
     * Wait for an element to be visible
     *
     * @param driver WebDriver instance
     * @param locator By locator of the element
     * @return WebElement if visible
     */
    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        logger.debug("Waiting for element visibility: " + locator);
        WebElement element = getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        logger.debug("Element is now visible: " + locator);
        return element;
    }

    /**
     * Wait for an element to be visible using WebElement
     *
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @return WebElement if visible
     */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        logger.debug("Waiting for element visibility");
        WebElement visibleElement = getWait(driver).until(ExpectedConditions.visibilityOf(element));
        logger.debug("Element is now visible");
        return visibleElement;
    }

    /**
     * Wait for an element to be clickable
     *
     * @param driver WebDriver instance
     * @param locator By locator of the element
     * @return WebElement if clickable
     */
    public static WebElement waitForClickability(WebDriver driver, By locator) {
        logger.debug("Waiting for element clickability: " + locator);
        WebElement element = getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
        logger.debug("Element is now clickable: " + locator);
        return element;
    }

    /**
     * Wait for an element to be clickable using WebElement
     *
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @return WebElement if clickable
     */
    public static WebElement waitForClickability(WebDriver driver, WebElement element) {
        logger.debug("Waiting for element clickability");
        WebElement clickableElement = getWait(driver).until(ExpectedConditions.elementToBeClickable(element));
        logger.debug("Element is now clickable");
        return clickableElement;
    }

    /**
     * Wait for an element to be invisible
     *
     * @param driver WebDriver instance
     * @param locator By locator of the element
     * @return true if element is invisible, false otherwise
     */
    public static boolean waitForInvisibility(WebDriver driver, By locator) {
        logger.debug("Waiting for element invisibility: " + locator);
        boolean result = getWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        logger.debug("Element is now invisible: " + locator);
        return result;
    }

    /**
     * Wait for text to be present in element
     *
     * @param driver WebDriver instance
     * @param locator By locator of the element
     * @param text Text to wait for
     * @return true if text is present, false otherwise
     */
    public static boolean waitForTextPresent(WebDriver driver, By locator, String text) {
        logger.debug("Waiting for text present in element: " + locator + ", text: " + text);
        boolean result = getWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        logger.debug("Text is now present in element: " + locator);
        return result;
    }

    /**
     * Wait for alert to be present
     *
     * @param driver WebDriver instance
     * @return Alert if present
     */
    public static org.openqa.selenium.Alert waitForAlertPresent(WebDriver driver) {
        logger.debug("Waiting for alert to be present");
        org.openqa.selenium.Alert alert = getWait(driver).until(ExpectedConditions.alertIsPresent());
        logger.debug("Alert is now present");
        return alert;
    }
}
