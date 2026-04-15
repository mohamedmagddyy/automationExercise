package org.example.base;

import org.example.driver.DriverFactory;
import org.example.utils.WaitUtils;
import org.example.utils.AlertHandler;
import org.openqa.selenium.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasePage {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage() {
        this.driver = DriverFactory.getDriver();
    }

    /**
     * Prepare for interaction by handling any blocking UI elements
     * Called automatically before critical actions
     */
    protected void prepareForInteraction() {
        try {
            AlertHandler.handleAllBlockersIfPresent(driver);
        } catch (Exception e) {
            logger.debug("Error during interaction preparation: " + e.getMessage());
        }
    }

    /**
     * Safe click - handles overlays and retries on ElementNotInteractableException
     */
    public void click(By locator) {
        try {
            prepareForInteraction();
            WaitUtils.waitForClickability(driver, locator);
            WebElement element = driver.findElement(locator);
            scrollIntoViewIfNeeded(element);
            element.click();
            logger.info("Clicked: " + locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on first attempt, handling overlays and retrying");
            prepareForInteraction();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
            WaitUtils.waitForClickability(driver, locator);
            WebElement element = driver.findElement(locator);
            scrollIntoViewIfNeeded(element);
            element.click();
            logger.info("Successfully clicked after handling overlays: " + locator);
        }
    }

    /**
     * Safe click on WebElement
     */
    public void click(WebElement element) {
        try {
            prepareForInteraction();
            element = waitForElementClickable(element);
            scrollIntoViewIfNeeded(element);
            element.click();
            logger.info("Clicked WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on first attempt, handling overlays and retrying");
            prepareForInteraction();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
            element = waitForElementClickable(element);
            scrollIntoViewIfNeeded(element);
            element.click();
            logger.info("Successfully clicked WebElement after handling overlays");
        }
    }

    /**
     * Safe sendKeys - handles overlays and retries on ElementNotInteractableException
     */
    public void sendKeys(By locator, String text) {
        try {
            prepareForInteraction();
            WaitUtils.waitForVisibility(driver, locator);
            WebElement element = driver.findElement(locator);
            scrollIntoViewIfNeeded(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed in element: " + locator);
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on first attempt, handling overlays and retrying");
            prepareForInteraction();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
            WaitUtils.waitForVisibility(driver, locator);
            WebElement element = driver.findElement(locator);
            scrollIntoViewIfNeeded(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Successfully typed after handling overlays: " + text);
        }
    }

    /**
     * Safe sendKeys on WebElement
     */
    public void sendKeys(WebElement element, String text) {
        try {
            prepareForInteraction();
            element = waitForElementVisible(element);
            scrollIntoViewIfNeeded(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed in WebElement");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element not interactable on first attempt, handling overlays and retrying");
            prepareForInteraction();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
            element = waitForElementVisible(element);
            scrollIntoViewIfNeeded(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Successfully typed in WebElement after handling overlays");
        }
    }

    /**
     * Get text from element
     */
    public String getText(By locator) {
        WaitUtils.waitForVisibility(driver, locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Check if element is displayed
     */
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scroll element into view if needed
     * Helps with ElementNotInteractableException
     */
    private void scrollIntoViewIfNeeded(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.debug("Scrolled element into view");
        } catch (Exception e) {
            logger.debug("Could not scroll element into view: " + e.getMessage());
        }
    }

    /**
     * Wait for WebElement to be visible
     */
    private WebElement waitForElementVisible(WebElement element) {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(element));
            return element;
        } catch (Exception e) {
            logger.warn("Element visibility timeout: " + e.getMessage());
            return element;
        }
    }

    /**
     * Wait for WebElement to be clickable
     */
    private WebElement waitForElementClickable(WebElement element) {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(element));
            return element;
        } catch (Exception e) {
            logger.warn("Element clickability timeout: " + e.getMessage());
            return element;
        }
    }
}