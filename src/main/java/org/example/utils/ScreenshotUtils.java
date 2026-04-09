package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtils - Handles screenshot capture functionality
 */
public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "screenshots";

    /**
     * Take screenshot and save to file
     *
     * @param driver WebDriver instance
     * @param testCaseName Name of the test case
     * @param pageName Name of the page (Home/Products/Cart/SignupLogin)
     * @return File path of the screenshot
     */
    public static String takeScreenshot(WebDriver driver, String testCaseName, String pageName) {
        try {
            // Create directory if it doesn't exist
            String directoryPath = SCREENSHOT_DIR + File.separator + pageName;
            createDirectoryIfNotExists(directoryPath);

            // Generate timestamp
            String timestamp = getTimestamp();

            // Create filename
            String fileName = testCaseName + "_" + timestamp + ".png";
            String filePath = directoryPath + File.separator + fileName;

            // Take screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);

            // Save screenshot to file
            Files.write(Paths.get(filePath), screenshotBytes);

            logger.info("Screenshot saved: " + filePath);
            return filePath;

        } catch (IOException e) {
            logger.error("Failed to take screenshot", e);
            return null;
        }
    }

    /**
     * Create screenshots directory if it doesn't exist
     *
     * @param directoryPath Directory path to create
     */
    private static void createDirectoryIfNotExists(String directoryPath) {
        try {
            Files.createDirectories(Paths.get(directoryPath));
            logger.debug("Directory created: " + directoryPath);
        } catch (IOException e) {
            logger.error("Failed to create directory: " + directoryPath, e);
        }
    }

    /**
     * Get current timestamp for screenshot naming
     *
     * @return Timestamp string
     */
    private static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return LocalDateTime.now().format(formatter);
    }
}
