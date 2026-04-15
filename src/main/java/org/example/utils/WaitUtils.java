package org.example.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class WaitUtils {

    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        return getWait(driver).until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    public static WebElement waitForClickability(WebDriver driver, By locator) {
        return getWait(driver).until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    // ✅ FIXED OVERLAY HANDLER
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
            } catch (Exception ignored) {}
        }
    }


}