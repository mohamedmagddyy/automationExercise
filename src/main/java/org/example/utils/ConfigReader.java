package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ConfigReader - Reads configuration from properties file
 */
public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);
            logger.info("Config properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load config properties", e);
            throw new RuntimeException("Failed to load config properties", e);
        }
    }

    /**
     * Get property value from config.properties
     *
     * @param key Property key
     * @return Property value
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: " + key);
        }
        return value;
    }

    /**
     * Get browser name from config
     *
     * @return Browser name
     */
    public static String getBrowser() {
        return getProperty("browser");
    }

    /**
     * Get base URL from config
     *
     * @return Base URL
     */
    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    /**
     * Get explicit wait timeout from config
     *
     * @return Wait timeout in seconds
     */
    public static int getWaitTimeout() {
        return Integer.parseInt(getProperty("explicit.wait.timeout"));
    }

    /**
     * Check if headless mode is enabled
     *
     * @return true if headless mode is enabled, false otherwise
     */
    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(getProperty("headless.mode"));
    }

    /**
     * Check if screenshot on failure is enabled
     *
     * @return true if screenshot on failure is enabled, false otherwise
     */
    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure"));
    }

    /**
     * Get report path from config
     *
     * @return Report path
     */
    public static String getReportPath() {
        return getProperty("report.path");
    }
}
