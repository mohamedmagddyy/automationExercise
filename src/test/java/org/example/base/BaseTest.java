package org.example.base;

import org.example.driver.DriverFactory;
import org.example.utils.AlertHandler;
import org.example.utils.ConfigReader;
import org.example.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class BaseTest {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void clearAllureResults() {
        try {
            Path allureResultsPath = Paths.get("allure-results");
            if (Files.exists(allureResultsPath)) {
                Files.walk(allureResultsPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try { Files.delete(file); }
                        catch (IOException e) { logger.warn("Could not delete: " + file); }
                    });
                logger.info("Allure results cleared successfully");
            } else {
                Files.createDirectories(allureResultsPath);
                logger.info("Allure results folder created");
            }
        } catch (IOException e) {
            logger.error("Failed to clear allure results", e);
        }
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {

        DriverFactory.initializeDriver(browser);
        driver = DriverFactory.getDriver();

        driver.manage().window().maximize();

        driver.get(ConfigReader.getBaseUrl());

        AlertHandler.closeConsentPopupIfPresent(driver);
        WaitUtils.waitForOverlayToDisappear(driver);

        logger.info("Setup completed - browser: " + browser);
    }

    @AfterMethod(alwaysRun = true)
    public void teardown(ITestResult result) {

        logger.info("Tearing down test: " + result.getMethod().getMethodName());

        if (result.getStatus() == ITestResult.FAILURE) {

            logger.error("Test failed: " + result.getThrowable());


        } else {
            logger.info("Test passed");
        }

        DriverFactory.quitDriver();
        logger.info("Driver closed");
    }

    protected WebDriver getDriver() {
        return driver;
    }
}