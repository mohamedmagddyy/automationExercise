package org.example.utils;

import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * AllureUtils - Clean utility class for Allure attachments
 * Keep it simple and production-ready
 */
public class AllureUtils {

    private static final Logger logger = LogManager.getLogger(AllureUtils.class);

    /**
     * Attach screenshot to Allure report
     *
     * @param driver WebDriver instance
     * @param name attachment name
     * @return screenshot in byte[]
     */
    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachScreenshot(WebDriver driver, String name) {
        logger.info("Attaching screenshot: {}", name);
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Attach page source to Allure report
     *
     * @param driver WebDriver instance
     * @param name attachment name
     * @return page source HTML
     */
    @Attachment(value = "{name}", type = "text/html")
    public static String attachPageSource(WebDriver driver, String name) {
        logger.info("Attaching page source: {}", name);
        return driver.getPageSource();
    }

    /**
     * Attach plain text to Allure report
     *
     * @param content text content
     * @param name attachment name
     * @return text content
     */
    @Attachment(value = "{name}", type = "text/plain")
    public static String attachText(String content, String name) {
        return content;
    }




}