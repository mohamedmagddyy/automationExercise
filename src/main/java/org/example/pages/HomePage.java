package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.ActionsHelper;
import org.openqa.selenium.WebDriver;

/**
 * HomePage - Page Object for Home page
 */
public class HomePage extends BasePage {

    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public HomePage() {
        super();
    }

    /**
     * Navigate to home page
     */
    public void navigateToHome() {
        // TODO: implement
    }

    /**
     * Enter subscription email
     *
     * @param email Email address
     */
    public void enterSubscriptionEmail(String email) {
        // TODO: implement
    }

    /**
     * Click subscribe button
     */
    public void clickSubscribeButton() {
        // TODO: implement
    }

    /**
     * Get subscription success message
     *
     * @return Success message
     */
    public String getSubscriptionSuccessMessage() {
        // TODO: implement
        return "";
    }

    /**
     * Click scroll up arrow button
     */
    public void clickScrollUpArrowButton() {
        // TODO: implement
    }

    /**
     * Scroll down to footer
     */
    public void scrollDownToFooter() {
        ActionsHelper.scrollToBottom(driver);
    }

    /**
     * Check if scrolled to top
     *
     * @return true if at top, false otherwise
     */
    public boolean isScrolledToTop() {
        // TODO: implement
        return false;
    }

    /**
     * Add first recommended item to cart
     */
    public void addFirstRecommendedItemToCart() {
        // TODO: implement
    }
}
