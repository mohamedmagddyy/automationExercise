package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * AlertHandler — Pure utility for handling browser alerts and UI overlays.
 *
 * Rules:
 *  - Never called inside click() or sendKeys() — only called ONCE per page load
 *  - All methods are safe (never throw exceptions to caller)
 *  - Short timeouts intentionally to avoid slowing down tests
 */
public class AlertHandler {

    private static final Logger logger = LogManager.getLogger(AlertHandler.class);

    // ── Consent button locators (ordered by priority) ──────────────────────
    private static final By[] CONSENT_BUTTONS = {
            By.cssSelector("button.fc-button.fc-cta-consent.fc-primary-button"),
            By.cssSelector("button[class*='accept-cookie']"),
            By.cssSelector("button[class*='accept-consent']"),
            By.id("acceptCookie"),
            By.xpath("//button[contains(text(), 'Accept All')]"),
            By.xpath("//button[contains(text(), 'Accept')]"),
            By.xpath("//button[contains(text(), 'Agree')]"),
            By.xpath("//button[contains(@aria-label, 'Accept')]")
    };

    // ── Blocking overlay locators ──────────────────────────────────────────
    private static final By[] BLOCKING_OVERLAYS = {
            By.cssSelector("div.fc-consent-root"),
            By.cssSelector("[class*='cookie-banner']"),
            By.cssSelector("[class*='modal-backdrop']"),
            By.cssSelector("[class*='overlay-active']"),
            By.cssSelector("[class*='popup-overlay']"),
            By.xpath("//div[@class='modal-backdrop' and contains(@style,'display')]")
    };

    // ── Wait factories ─────────────────────────────────────────────────────

    /** Standard wait — used for browser alert detection. */
    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * Short wait (1s) — used for consent/overlay checks.
     * Intentionally low to prevent test slowdown.
     */
    private static WebDriverWait getShortWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(1));
    }

    // =========================================================================
    // BROWSER ALERTS
    // =========================================================================

    public static void acceptAlert(WebDriver driver) {
        try {
            Alert alert = getWait(driver).until(ExpectedConditions.alertIsPresent());
            logger.info("Alert text: {}", alert.getText());
            alert.accept();
            logger.info("Alert accepted");
        } catch (Exception e) {
            logger.debug("No alert to accept: {}", e.getMessage());
        }
    }

    public static void dismissAlert(WebDriver driver) {
        try {
            Alert alert = getWait(driver).until(ExpectedConditions.alertIsPresent());
            logger.info("Alert text: {}", alert.getText());
            alert.dismiss();
            logger.info("Alert dismissed");
        } catch (Exception e) {
            logger.debug("No alert to dismiss: {}", e.getMessage());
        }
    }

    public static String getAlertText(WebDriver driver) {
        try {
            Alert alert = getWait(driver).until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            logger.info("Alert text: {}", text);
            return text;
        } catch (Exception e) {
            logger.debug("No alert present: {}", e.getMessage());
            return null;
        }
    }

    public static boolean isAlertPresent(WebDriver driver) {
        try {
            getShortWait(driver).until(ExpectedConditions.alertIsPresent());
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

    // =========================================================================
    // UI OVERLAY HANDLING
    // =========================================================================

    /**
     * Finds and clicks the first visible consent/cookie button.
     * Uses short 1-second timeout per button to stay fast.
     */
    public static void closeConsentPopupIfPresent(WebDriver driver) {
        try {
            for (By buttonLocator : CONSENT_BUTTONS) {
                try {
                    WebElement button = getShortWait(driver)
                            .until(ExpectedConditions.elementToBeClickable(buttonLocator));
                    if (button.isDisplayed()) {
                        button.click();
                        logger.info("Consent popup closed via: {}", buttonLocator);
                        WaitUtils.waitForOverlayToDisappear(driver);
                        return;
                    }
                } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException ignored) {
                    // not found — try next locator
                }
            }
            logger.debug("No consent popup found");
        } catch (Exception e) {
            logger.debug("closeConsentPopupIfPresent error (safe): {}", e.getMessage());
        }
    }

    /**
     * Tries to close any blocking overlay using Escape key.
     */
    public static void closeBlockingOverlayIfPresent(WebDriver driver) {
        try {
            for (By overlayLocator : BLOCKING_OVERLAYS) {
                try {
                    WebElement overlay = driver.findElement(overlayLocator);
                    if (overlay.isDisplayed()) {
                        overlay.sendKeys(Keys.ESCAPE);
                        logger.info("Closed overlay via Escape: {}", overlayLocator);
                        return;
                    }
                } catch (NoSuchElementException | StaleElementReferenceException ignored) {
                    // not found — try next locator
                }
            }
            logger.debug("No blocking overlay found");
        } catch (Exception e) {
            logger.debug("closeBlockingOverlayIfPresent error (safe): {}", e.getMessage());
        }
    }

    /**
     * Hides all ad iframes via JavaScript so they don't intercept clicks.
     */
    public static void hideAdIframes(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "var iframes = document.querySelectorAll('iframe');" +
                            "iframes.forEach(function(f){ f.style.display='none'; });"
            );
            logger.debug("Ad iframes hidden");
        } catch (Exception e) {
            logger.debug("hideAdIframes error (safe): {}", e.getMessage());
        }
    }

    /**
     * Master method — handles all types of blockers in sequence.
     *
     * Call this ONCE after page navigation, never before every interaction.
     */
    public static void handleAllBlockersIfPresent(WebDriver driver) {
        try {
            logger.debug("Handling UI blockers...");
            acceptAlertIfPresent(driver);
            closeConsentPopupIfPresent(driver);
            closeBlockingOverlayIfPresent(driver);
            hideAdIframes(driver);
            logger.debug("UI blocker handling complete");
        } catch (Exception e) {
            logger.warn("Unexpected error in handleAllBlockersIfPresent (safe): {}", e.getMessage());
        }
    }
}