package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

public class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);

    private static WebDriverWait getWait(WebDriver driver) {
        if (driver == null) {
            throw new IllegalStateException("WebDriver is NULL in WaitUtils");
        }
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getWaitTimeout()));
    }

    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        return getWait(driver).until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        return getWait(driver).until(
                ExpectedConditions.visibilityOf(element)
        );
    }

    public static WebElement waitForClickability(WebDriver driver, By locator) {
        return getWait(driver).until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    public static WebElement waitForClickability(WebDriver driver, WebElement element) {
        return getWait(driver).until(
                ExpectedConditions.elementToBeClickable(element)
        );
    }

    public static boolean waitForInvisibility(WebDriver driver, By locator) {
        return getWait(driver).until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    public static boolean waitForTextPresent(WebDriver driver, By locator, String text) {
        return getWait(driver).until(
                ExpectedConditions.textToBePresentInElementLocated(locator, text)
        );
    }

    public static Alert waitForAlertPresent(WebDriver driver) {
        return getWait(driver).until(
                ExpectedConditions.alertIsPresent()
        );
    }

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
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.invisibilityOfElementLocated(overlay));
                logger.debug("Overlay disappeared: " + overlay);
            } catch (Exception ignored) {
                // overlay مش موجود أصلاً، كمّل
            }
        }
    }
}