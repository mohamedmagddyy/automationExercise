package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

/**
 * WaitUtils — Pure explicit wait utility. No business logic here.
 *
 * All timeouts come from ConfigReader so they're configurable per environment.
 */
public class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);

    // =========================================================================
    // WAIT FACTORY
    // =========================================================================

    private static WebDriverWait getWait(WebDriver driver) {
        if (driver == null) throw new IllegalStateException("WebDriver is null in WaitUtils");
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getWaitTimeout()));
    }

    /**
     * Short wait for overlay/consent checks — 1 second max.
     * Keep this low to avoid performance hits.
     */
    private static WebDriverWait getShortWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(1));
    }

    // =========================================================================
    // VISIBILITY
    // =========================================================================

    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.visibilityOf(element));
    }

    // =========================================================================
    // CLICKABILITY
    // =========================================================================

    public static WebElement waitForClickability(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickability(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    // =========================================================================
    // INVISIBILITY
    // =========================================================================

    public static boolean waitForInvisibility(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // =========================================================================
    // TEXT PRESENT
    // =========================================================================

    public static boolean waitForTextPresent(WebDriver driver, By locator, String text) {
        return getWait(driver).until(
                ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // =========================================================================
    // ALERT
    // =========================================================================

    public static Alert waitForAlertPresent(WebDriver driver) {
        return getWait(driver).until(ExpectedConditions.alertIsPresent());
    }

    // =========================================================================
    // OVERLAY DISAPPEAR
    // =========================================================================

    /**
     * Checks common overlay locators and waits for them to disappear.
     *
     * Timeout is intentionally SHORT (1 second per locator) to avoid slowing down tests.
     * Called ONCE per page load — never inside loops or before every interaction.
     */
    public static void waitForOverlayToDisappear(WebDriver driver) {
        List<By> overlays = List.of(
                By.id("consent"),
                By.cssSelector(".consent"),
                By.cssSelector(".cookie"),
                By.cssSelector(".modal"),
                By.cssSelector(".modal-backdrop"),
                By.cssSelector(".overlay"),
                By.cssSelector(".popup")
        );

        for (By overlay : overlays) {
            try {
                getShortWait(driver).until(
                        ExpectedConditions.invisibilityOfElementLocated(overlay));
                logger.debug("Overlay cleared: {}", overlay);
            } catch (TimeoutException ignored) {
                logger.debug("Overlay not present or did not clear: {}", overlay);
            } catch (Exception ignored) {
                // safe — continue to next overlay
            }
        }
    }
}