package org.example.base;

import org.example.driver.DriverFactory;
import org.example.utils.ConfigReader;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BaseTest - Base class for all test classes
 */
public class BaseTest {

    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    /**
     * Setup method - Runs before each test
     *
     * @param browser Browser parameter from testng.xml
     */
    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        logger.info("Setting up test with browser: " + browser);
        DriverFactory.initializeDriver(browser);
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getBaseUrl());
        ExtentReportManager.initReport();
        logger.info("Test setup completed");
    }

    /**
     * Teardown method - Runs after each test
     *
     * @param result Test result
     */
    @AfterMethod
    public void teardown(ITestResult result) {
        logger.info("Tearing down test: " + result.getMethod().getMethodName());

        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test failed: " + result.getThrowable());
            ExtentReportManager.getTest().fail("Test Failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed");
            ExtentReportManager.getTest().pass("Test Passed");
        }

        ExtentReportManager.flushReport();
        DriverFactory.quitDriver();
        logger.info("Test teardown completed");
    }

    /**
     * Get WebDriver instance for test classes
     *
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
}
