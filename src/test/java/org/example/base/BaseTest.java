package org.example.base;

import org.example.driver.DriverFactory;
import org.example.utils.AllureUtils;
import org.example.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseTest {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @Parameters("browser")
    @BeforeMethod
    public void setup(@Optional("chrome") String browser) {

        logger.info("Browser: " + browser);

        DriverFactory.initializeDriver(browser);

        driver = DriverFactory.getDriver();

        if (driver == null) {
            throw new RuntimeException("Driver is NULL after initialization!");
        }

        driver.manage().window().maximize();
        driver.get(ConfigReader.getBaseUrl());

        logger.info("Setup completed");
    }

    @AfterMethod
    public void teardown(ITestResult result) {

        logger.info("Tearing down test: " + result.getMethod().getMethodName());

        if (result.getStatus() == ITestResult.FAILURE) {

            logger.error("Test failed: " + result.getThrowable());

            AllureUtils.attachScreenshot(driver, "Failure Screenshot");
            AllureUtils.attachPageSource(driver, "Page Source");
            AllureUtils.attachText(
                    String.valueOf(result.getThrowable()),
                    "Failure Details"
            );

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