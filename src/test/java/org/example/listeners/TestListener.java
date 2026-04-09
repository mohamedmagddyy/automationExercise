package org.example.listeners;

import org.example.utils.ConfigReader;
import org.example.utils.ExtentReportManager;
import org.example.utils.ScreenshotUtils;
import org.example.driver.DriverFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestListener - Implements TestNG ITestListener for test execution events
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    /**
     * Called when a test starts
     *
     * @param result ITestResult object
     */
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Test started: " + testName);

        // Create ExtentTest
        ExtentReportManager.createTest(testName);
        ExtentReportManager.getTest().info("Test Started: " + testName);
    }

    /**
     * Called when a test passes
     *
     * @param result ITestResult object
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Test passed: " + testName);

        ExtentReportManager.getTest().pass("Test Passed: " + testName);
    }

    /**
     * Called when a test fails
     *
     * @param result ITestResult object
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testClass = result.getTestClass().getName();
        Throwable throwable = result.getThrowable();

        logger.error("Test failed: " + testName + " - " + throwable.getMessage());

        // Log failure in ExtentReports
        ExtentReportManager.getTest().fail("Test Failed: " + testName);
        ExtentReportManager.getTest().fail(throwable);

        // Take screenshot on failure if enabled
        if (ConfigReader.isScreenshotOnFailure()) {
            try {
                String pageName = getPageNameFromTestClass(testClass);
                String screenshotPath = ScreenshotUtils.takeScreenshot(
                    DriverFactory.getDriver(),
                    testName,
                    pageName
                );

                if (screenshotPath != null) {
                    ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath);
                    logger.info("Screenshot captured for failed test: " + testName);
                }
            } catch (Exception e) {
                logger.error("Failed to capture screenshot", e);
            }
        }
    }

    /**
     * Called when a test is skipped
     *
     * @param result ITestResult object
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test skipped: " + testName);

        ExtentReportManager.getTest().skip("Test Skipped: " + testName);
    }

    /**
     * Get page name from test class name
     *
     * @param testClassName Full test class name
     * @return Page name (Home/Products/Cart/SignupLogin)
     */
    private String getPageNameFromTestClass(String testClassName) {
        if (testClassName.contains("Home")) {
            return "Home";
        } else if (testClassName.contains("Products")) {
            return "Products";
        } else if (testClassName.contains("Cart")) {
            return "Cart";
        } else if (testClassName.contains("SignupLogin")) {
            return "SignupLogin";
        }
        return "General";
    }
}
