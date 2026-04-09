package org.example.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DriverFactory - Manages WebDriver initialization using Singleton and ThreadLocal pattern
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    /**
     * Initialize the WebDriver based on browser type
     *
     * @param browserType Browser type (chrome/edge)
     */
    public static void initializeDriver(String browserType) {
        WebDriver driver = null;

        switch (browserType.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (Boolean.parseBoolean(ConfigReader.getProperty("headless.mode"))) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(chromeOptions);
                logger.info("Chrome driver initialized");
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (Boolean.parseBoolean(ConfigReader.getProperty("headless.mode"))) {
                    edgeOptions.addArguments("--headless");
                }
                edgeOptions.addArguments("--disable-gpu");
                edgeOptions.addArguments("--window-size=1920,1080");
                driver = new EdgeDriver(edgeOptions);
                logger.info("Edge driver initialized");
                break;

            default:
                logger.error("Unsupported browser: " + browserType);
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }

        driver.manage().window().maximize();
        driverThreadLocal.set(driver);
    }

    /**
     * Get the WebDriver instance
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            logger.error("Driver not initialized. Call initializeDriver() first.");
            throw new IllegalStateException("Driver not initialized. Call initializeDriver() first.");
        }
        return driver;
    }

    /**
     * Quit the WebDriver instance and remove from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            logger.info("Driver quit and removed from ThreadLocal");
        }
    }
}
