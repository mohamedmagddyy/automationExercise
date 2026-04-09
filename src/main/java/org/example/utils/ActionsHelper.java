package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ActionsHelper - Handles complex user interactions using Selenium Actions
 */
public class ActionsHelper {

    private static final Logger logger = LogManager.getLogger(ActionsHelper.class);

    /**
     * Scroll to element and click
     *
     * @param driver WebDriver instance
     * @param element WebElement to click
     */
    public static void scrollToElementAndClick(WebDriver driver, WebElement element) {
        scrollToElement(driver, element);
        element.click();
        logger.info("Scrolled to element and clicked");
    }

    /**
     * Hover over an element
     *
     * @param driver WebDriver instance
     * @param element WebElement to hover
     */
    public static void hoverOverElement(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        logger.info("Hovered over element");
    }

    /**
     * Scroll to element
     *
     * @param driver WebDriver instance
     * @param element WebElement to scroll to
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        logger.info("Scrolled to element");
    }

    /**
     * Scroll to bottom of page
     *
     * @param driver WebDriver instance
     */
    public static void scrollToBottom(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        logger.info("Scrolled to bottom of page");
    }

    /**
     * Scroll to top of page
     *
     * @param driver WebDriver instance
     */
    public static void scrollToTop(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0)");
        logger.info("Scrolled to top of page");
    }
}
