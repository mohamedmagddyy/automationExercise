package org.example.base;

import org.example.driver.DriverFactory;
import org.example.utils.ConfigReader;
import org.example.utils.WaitUtils;
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
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {

        DriverFactory.initializeDriver(browser);
        driver = DriverFactory.getDriver();

        driver.manage().window().maximize();

        driver.get(ConfigReader.getBaseUrl());

        // ✅ handle overlay once per test
        WaitUtils.waitForOverlayToDisappear(driver);

        logger.info("Setup completed");
    }

    @AfterMethod
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