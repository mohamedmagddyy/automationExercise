package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.ActionsHelper;
import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

/**
 * HomePage - Page Object for Home page
 */
public class HomePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    // ==================== Navigation ====================
    private static final By testCasesLink = By.cssSelector("a[href='/test_cases']");
    private static final By productsLink = By.cssSelector("a[href='/products']");
    private static final By cartLink = By.cssSelector("a[href='/view_cart']");
    private static final By contactUsLink = By.cssSelector("a[href='/contact_us']");

    // ==================== Subscription ====================
    private static final By subscriptionEmail = By.id("susbscribe_email");
    private static final By subscribeBtn = By.id("subscribe");
    private static final By subscriptionSuccess = By.cssSelector("div.alert-success");

    // ==================== Scroll Up ====================
    private static final By scrollUpArrow = By.id("scrollUp");

    // ==================== Recommended Products ====================
    private static final By recommendedProductsSection = By.cssSelector("div.recommended_items");
    private static final By firstRecommendedAddToCart = By.cssSelector("div.recommended_items div.productinfo a.add-to-cart");
    private static final By continueShoppingBtn = By.cssSelector("button[data-dismiss='modal']");

    // ==================== Footer ====================
    private static final By footer = By.cssSelector("footer");

    /**
     * Constructor
     */
    public HomePage() {
        super();
    }

    /**
     * Navigate to home page
     */
    public void navigateToHome() {
        logger.info("Navigating to home page");
        // Navigation is handled by BaseTest setup
    }

    /**
     * Enter subscription email
     *
     * @param email Email address
     */
    public void enterSubscriptionEmail(String email) {
        logger.info("Entering subscription email: " + email);
        sendKeys(subscriptionEmail, email);
    }

    /**
     * Click subscribe button
     */
    public void clickSubscribeButton() {
        logger.info("Clicking subscribe button");
        click(subscribeBtn);
    }

    /**
     * Get subscription success message
     *
     * @return Success message
     */
    public String getSubscriptionSuccessMessage() {
        logger.info("Getting subscription success message");
        return getText(subscriptionSuccess);
    }

    /**
     * Scroll down to footer
     */
    public void scrollDownToFooter() {
        ensureDriver();
        logger.info("Scrolling down to footer");
        ActionsHelper.scrollToBottom(driver);
        // Wait for footer to be visible
        WaitUtils.waitForVisibility(driver, footer);
    }

    /**
     * Click scroll up arrow button
     */
    public void clickScrollUpArrowButton() {
        logger.info("Clicking scroll up arrow button");
        // First scroll down to make the arrow visible
        scrollDownToFooter();
        // Click the scroll up arrow
        click(scrollUpArrow);
        logger.info("Scroll up arrow clicked");
    }

    /**
     * Scroll to top of the page using JavaScript
     */
    public void scrollToTop() {
        ensureDriver();
        logger.info("Scrolling to top of page");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");
        logger.info("Page scrolled to top");
    }

    /**
     * Check if scrolled to top
     *
     * @return true if at top, false otherwise
     */
    public boolean isScrolledToTop() {
        ensureDriver();
        logger.info("Checking if page is scrolled to top");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long scrollY = (Long) js.executeScript("return window.pageYOffset;");
        boolean isAtTop = scrollY == 0;
        logger.info("Page scroll position: " + scrollY + ", is at top: " + isAtTop);
        return isAtTop;
    }

    /**
     * Add first recommended item to cart
     */
    public void addFirstRecommendedItemToCart() {
        ensureDriver();
        logger.info("Adding first recommended item to cart");
        ActionsHelper.scrollToElement(driver, driver.findElement(recommendedProductsSection));
        WebElement btn = driver.findElement(firstRecommendedAddToCart);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        logger.info("Clicked add to cart for first recommended product");
        try {
            WaitUtils.waitForVisibility(driver, continueShoppingBtn);
            click(continueShoppingBtn);
            logger.info("Clicked continue shopping to close modal");
        } catch (Exception e) {
            logger.warn("Continue shopping button not found: " + e.getMessage());
        }
    }

    /**
     * Navigate to test cases page
     */
    public void navigateToTestCases() {
        logger.info("Navigating to test cases page");
        click(testCasesLink);
    }

    /**
     * Navigate to products page
     */
    public void navigateToProducts() {
        logger.info("Navigating to products page");
        click(productsLink);
    }

    /**
     * Navigate to cart
     */
    public void navigateToCart() {
        logger.info("Navigating to cart");
        click(cartLink);
    }

    /**
     * Navigate to contact us
     */
    public void navigateToContactUs() {
        logger.info("Navigating to contact us page");
        click(contactUsLink);
    }


}