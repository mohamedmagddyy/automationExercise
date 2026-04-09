package org.example.base;

import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base Page Class - Contains common methods for all page objects
 */
public class BasePage {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    /**
     * Constructor to initialize the WebDriver
     *
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Click on a WebElement
     *
     * @param element WebElement to click
     */
    public void click(WebElement element) {
        WaitUtils.waitForClickability(driver, element);
        element.click();
        logger.info("Clicked on element");
    }

    /**
     * Click on an element using By locator
     *
     * @param locator By locator of the element
     */
    public void click(By locator) {
        WaitUtils.waitForClickability(driver, locator);
        driver.findElement(locator).click();
        logger.info("Clicked on element: " + locator);
    }

    /**
     * Send keys to a WebElement
     *
     * @param element WebElement to send keys to
     * @param text    Text to send
     */
    public void sendKeys(WebElement element, String text) {
        WaitUtils.waitForVisibility(driver, element);
        element.clear();
        element.sendKeys(text);
        logger.info("Entered text: " + text);
    }

    /**
     * Send keys to an element using By locator
     *
     * @param locator By locator of the element
     * @param text    Text to send
     */
    public void sendKeys(By locator, String text) {
        WaitUtils.waitForVisibility(driver, locator);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
        logger.info("Entered text in element: " + locator);
    }

    /**
     * Get text from a WebElement
     *
     * @param element WebElement to get text from
     * @return Text content of the element
     */
    public String getText(WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        String text = element.getText();
        logger.info("Retrieved text: " + text);
        return text;
    }

    /**
     * Get text from an element using By locator
     *
     * @param locator By locator of the element
     * @return Text content of the element
     */
    public String getText(By locator) {
        WaitUtils.waitForVisibility(driver, locator);
        String text = driver.findElement(locator).getText();
        logger.info("Retrieved text from element: " + locator);
        return text;
    }

    /**
     * Clear the text from a WebElement
     *
     * @param element WebElement to clear
     */
    public void clear(WebElement element) {
        WaitUtils.waitForVisibility(driver, element);
        element.clear();
        logger.info("Cleared element");
    }

    /**
     * Clear the text from an element using By locator
     *
     * @param locator By locator of the element
     */
    public void clear(By locator) {
        WaitUtils.waitForVisibility(driver, locator);
        driver.findElement(locator).clear();
        logger.info("Cleared element: " + locator);
    }

    /**
     * Check if a WebElement is displayed
     *
     * @param element WebElement to check
     * @return true if element is displayed, false otherwise
     */
    public boolean isDisplayed(WebElement element) {
        try {
            WaitUtils.waitForVisibility(driver, element);
            boolean displayed = element.isDisplayed();
            logger.info("Element displayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Element not displayed");
            return false;
        }
    }

    /**
     * Check if an element is displayed using By locator
     *
     * @param locator By locator of the element
     * @return true if element is displayed, false otherwise
     */
    public boolean isDisplayed(By locator) {
        try {
            WaitUtils.waitForVisibility(driver, locator);
            boolean displayed = driver.findElement(locator).isDisplayed();
            logger.info("Element displayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Element not displayed: " + locator);
            return false;
        }
    }
}
