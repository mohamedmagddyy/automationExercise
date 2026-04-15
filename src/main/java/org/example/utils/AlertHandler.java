package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * AlertHandler - Handles browser alerts and UI overlays safely
 */
public class AlertHandler {

    private static final Logger logger = LogManager.getLogger(AlertHandler.class);

    // Cookie consent button locators
    private static final By[] CONSENT_BUTTONS = {
            By.cssSelector("button.fc-button.fc-cta-consent.fc-primary-button"),
            By.cssSelector("button[class*='accept-cookie']"),
            By.cssSelector("button[class*='accept-consent']"),
            By.id("acceptCookie"),
            By.xpath("//button[contains(text(), 'Accept')]"),
            By.xpath("//button[contains(text(), 'Accept All')]"),
            By.xpath("//button[contains(text(), 'Agree')]"),
            By.xpath("//button[contains(@aria-label, 'Accept')]")
    };

    // Blocking overlay locators
    private static final By[] BLOCKING_OVERLAYS = {
            By.cssSelector("div.fc-consent-root"),
            By.cssSelector("[class*='cookie-banner']"),
            By.cssSelector("[class*='modal-backdrop']"),
            By.cssSelector("[class*='overlay-active']"),
            By.cssSelector("[class*='popup-overlay']"),
            By.xpath("//div[@class='modal-backdrop' and contains(@style, 'display')]")
    };

    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    private static Alert waitForAlert(WebDriver driver) {
        return getWait(driver).until(ExpectedConditions.alertIsPresent());
    }

    // ==================== Browser Alerts ====================

    public static void acceptAlert(WebDriver driver) {
        try {
            Alert alert = waitForAlert(driver);
            logger.info("Alert text: " + alert.getText());
            alert.accept();
            logger.info("Alert accepted");
        } catch (Exception e) {
            logger.debug("Failed to accept alert: " + e.getMessage());
        }
    }

    public static void dismissAlert(WebDriver driver) {
        try {
            Alert alert = waitForAlert(driver);
            logger.info("Alert text: " + alert.getText());
            alert.dismiss();
            logger.warn("Alert dismissed");
        } catch (Exception e) {
            logger.debug("Failed to dismiss alert: " + e.getMessage());
        }
    }

    public static String getAlertText(WebDriver driver) {
        try {
            Alert alert = waitForAlert(driver);
            String text = alert.getText();
            logger.info("Alert text retrieved: " + text);
            return text;
        } catch (Exception e) {
            logger.debug("Failed to get alert text: " + e.getMessage());
            return null;
        }
    }

    public static boolean isAlertPresent(WebDriver driver) {
        try {
            getWait(driver).until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void acceptAlertIfPresent(WebDriver driver) {
        if (isAlertPresent(driver)) {
            acceptAlert(driver);
        }
    }

    // ==================== UI Overlay Handling ====================

    /**
     * Close cookie consent popup if present
     * Safe - does not throw exceptions if not found
     */
    public static void closeConsentPopupIfPresent(WebDriver driver) {
        try {
            for (By button : CONSENT_BUTTONS) {
                try {
                    WebElement element = driver.findElement(button);
                    if (element.isDisplayed()) {
                        logger.debug("Found consent button, closing: " + button);
                        element.click();
                        logger.info("Closed consent popup");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ignored) {
                        }
                        return;
                    }
                } catch (NoSuchElementException | StaleElementReferenceException ignored) {
                    logger.debug("Consent button not found: " + button);
                }
            }
        } catch (Exception e) {
            logger.debug("Error closing consent popup: " + e.getMessage());
        }
    }

    /**
     * Close generic blocking overlays
     * Safe - attempts Escape key if close button not found
     */
    public static void closeBlockingOverlayIfPresent(WebDriver driver) {
        try {
            for (By overlay : BLOCKING_OVERLAYS) {
                try {
                    WebElement element = driver.findElement(overlay);
                    if (element.isDisplayed()) {
                        logger.debug("Found blocking overlay: " + overlay);
                        try {
                            element.sendKeys(Keys.ESCAPE);
                            logger.info("Closed overlay using Escape key");
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ignored) {
                            }
                            return;
                        } catch (Exception e) {
                            logger.debug("Escape key didn't work, ignoring: " + e.getMessage());
                        }
                    }
                } catch (NoSuchElementException | StaleElementReferenceException ignored) {
                    logger.debug("Overlay not found: " + overlay);
                }
            }
        } catch (Exception e) {
            logger.debug("Error closing overlay: " + e.getMessage());
        }
    }

    /**
     * Handle all UI blockers (alerts + overlays)
     * Safe operation - always completes without throwing exceptions
     */
    public static void handleAllBlockersIfPresent(WebDriver driver) {
        try {
            logger.debug("Checking for UI blockers...");
            acceptAlertIfPresent(driver);
            closeConsentPopupIfPresent(driver);
            closeBlockingOverlayIfPresent(driver);
            logger.debug("UI blocker check complete");
        } catch (Exception e) {
            logger.warn("Unexpected error during blocker handling: " + e.getMessage());
        }
    }
}