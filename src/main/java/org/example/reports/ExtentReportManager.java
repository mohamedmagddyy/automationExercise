package org.example.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ExtentReportManager - Manages ExtentReports setup and test logging
 */
public class ExtentReportManager {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Initialize ExtentReports
     */
    public static void initReport() {
        if (extentReports == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(ConfigReader.getReportPath());

            // Configure report
            sparkReporter.config().setDocumentTitle("Automation Exercise Test Report");
            sparkReporter.config().setReportName("Test Execution Report");
            sparkReporter.config().setTheme(Theme.DARK);

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);

            // Add system information
            extentReports.setSystemInfo("Environment", "QA");
            extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User", System.getProperty("user.name"));

            logger.info("ExtentReports initialized");
        }
    }

    /**
     * Create a new test
     *
     * @param testName Test name
     * @return ExtentTest instance
     */
    public static ExtentTest createTest(String testName) {
        ExtentTest test = extentReports.createTest(testName);
        extentTest.set(test);
        logger.debug("Created test: " + testName);
        return test;
    }

    /**
     * Get current test instance
     *
     * @return ExtentTest instance
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Flush the report
     */
    public static void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed");
        }
    }

    /**
     * Remove test from ThreadLocal
     */
    public static void removeTest() {
        extentTest.remove();
    }
}
