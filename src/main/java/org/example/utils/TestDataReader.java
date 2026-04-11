package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestDataReader - Reads test data from properties file
 * Provides generic property access with logging and dynamic data generation
 */
public class TestDataReader {

    private static final Logger logger = LogManager.getLogger(TestDataReader.class);
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    /**
     * Loads the properties file from src/test/resources/testdata.properties
     */
    private static void loadProperties() {
        try (FileInputStream fis = new FileInputStream("src/test/resources/testdata.properties")) {
            properties.load(fis);
            logger.info("Test data properties loaded successfully from src/test/resources/testdata.properties");
        } catch (IOException e) {
            logger.error("Failed to load test data properties from src/test/resources/testdata.properties", e);
            throw new RuntimeException("Failed to load test data properties", e);
        }
    }

    /**
     * Get property value from testdata.properties
     *
     * @param key Property key
     * @return Property value or null if not found
     */
    public static String getProperty(String key) {
        logger.debug("Requesting property: " + key);
        String value = properties.getProperty(key);
        if (value != null) {
            logger.debug("Property '" + key + "' found with value: " + value);
        } else {
            logger.warn("Property '" + key + "' not found in testdata.properties");
        }
        return value;
    }

    /**
     * Get required property value - throws RuntimeException if property is missing
     *
     * @param key Property key
     * @return Property value
     * @throws RuntimeException if property is not found
     */
    public static String getRequiredProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            String errorMessage = "Required property '" + key + "' is missing from testdata.properties";
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        return value;
    }

    /**
     * Generate a dynamic email using timestamp for unique test data
     *
     * @return Dynamic email in format: test{timestamp}@example.com
     */
    public static String generateDynamicEmail() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String dynamicEmail = "test" + timestamp + "@example.com";
        logger.info("Generated dynamic email: " + dynamicEmail);
        return dynamicEmail;
    }
}
