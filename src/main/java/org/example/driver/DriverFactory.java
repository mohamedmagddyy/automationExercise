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

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    public static void initializeDriver(String browserType) {

        if (browserType == null || browserType.isEmpty()) {
            throw new IllegalArgumentException("Browser type cannot be null");
        }

        WebDriver driver;

        switch (browserType.toLowerCase()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();

                if (Boolean.parseBoolean(ConfigReader.getProperty("headless.mode"))) {
                    chromeOptions.addArguments("--headless=new");
                }

                chromeOptions.addArguments("--start-maximized");

                driver = new ChromeDriver(chromeOptions);
                logger.info("Chrome driver started");
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();

                EdgeOptions edgeOptions = new EdgeOptions();

                if (Boolean.parseBoolean(ConfigReader.getProperty("headless.mode"))) {
                    edgeOptions.addArguments("--headless=new");
                }

                edgeOptions.addArguments("--start-maximized");

                driver = new EdgeDriver(edgeOptions);
                logger.info("Edge driver started");
                break;

            default:
                throw new RuntimeException("Unsupported browser: " + browserType);
        }

        driverThreadLocal.set(driver);
    }

    // ✅ بيرجع null بدل ما يعمل exception
    // عشان BasePage تتعمل بدون مشاكل قبل ما الـ driver يتعمل
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();

        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            logger.info("Driver closed");
        }
    }
}