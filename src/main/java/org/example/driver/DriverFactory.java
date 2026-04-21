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

    private static WebDriver driver;
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    public static void initializeDriver(String browserType) {

        if (browserType == null || browserType.isEmpty()) {
            throw new IllegalArgumentException("Browser type cannot be null");
        }

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
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            logger.info("Driver closed");
        }
    }
}