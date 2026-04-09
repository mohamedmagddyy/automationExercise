package org.example.utils;

import org.example.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * AlertHandler - Handles all alert interactions using WebDriverWait
 */
public class AlertHandler {

    private static final Logger logger = LogManager.getLogger(AlertHandler.class);

    /**
     * Accept alert
     *
     * @param driver WebDriver instance
     */
    public static void acceptAlert(WebDriver driver) {
        try {
            Alert alert = WaitUtils.waitForAlertPresent(driver);
            String alertText = alert.getText();
            alert.accept();
            logger.info("Alert accepted with text: " + alertText);
        } catch (Exception e) {
            logger.error("Failed to accept alert", e);
            throw e;
        }
    }

    /**
     * Dismiss alert
     *
     * @param driver WebDriver instance
     */
    public static void dismissAlert(WebDriver driver) {
        try {
            Alert alert = WaitUtils.waitForAlertPresent(driver);
            String alertText = alert.getText();
            alert.dismiss();
            logger.warn("Alert dismissed with text: " + alertText);
        } catch (Exception e) {
            logger.error("Failed to dismiss alert", e);
            throw e;
        }
    }

    /**
     * Get alert text
     *
     * @param driver WebDriver instance
     * @return Alert text
     */
    public static String getAlertText(WebDriver driver) {
        try {
            Alert alert = WaitUtils.waitForAlertPresent(driver);
            String alertText = alert.getText();
            logger.info("Alert text retrieved: " + alertText);
            return alertText;
        } catch (Exception e) {
            logger.error("Failed to get alert text", e);
            throw e;
        }
    }

    /**
     * Check if alert is present
     *
     * @param driver WebDriver instance
     * @return true if alert is present, false otherwise
     */
    public static boolean isAlertPresent(WebDriver driver) {
        try {
            WaitUtils.waitForAlertPresent(driver);
            logger.info("Alert is present");
            return true;
        } catch (Exception e) {
            logger.debug("Alert is not present");
            return false;
        }
    }

    /**
     * Accept alert if present (safe method)
     *
     * @param driver WebDriver instance
     */
    public static void acceptAlertIfPresent(WebDriver driver) {
        if (isAlertPresent(driver)) {
            acceptAlert(driver);
        } else {
            logger.debug("No alert present to accept");
        }
    }
}
