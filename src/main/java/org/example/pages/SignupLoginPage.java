package org.example.pages;

import org.example.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SignupLoginPage - Page Object for Signup/Login page
 */
public class SignupLoginPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(SignupLoginPage.class);

    // Login locators
    private static final By emailAddressLogin = By.cssSelector("input[data-qa='login-email']");
    private static final By password = By.cssSelector("input[placeholder='Password']");
    private static final By loginButton = By.cssSelector("button[data-qa='login-button']");

    // Signup locators
    private static final By nameSignup = By.cssSelector("input[placeholder='Name']");
    private static final By emailAddressSignup = By.cssSelector("input[data-qa='signup-email']");
    private static final By signupButton = By.cssSelector("button[data-qa='signup-button']");

    // Registration locators
    private static final By titleMr = By.id("id_gender1");
    private static final By titleMrs = By.id("id_gender2");
    private static final By nameReg = By.id("name");
    private static final By emailReg = By.id("email");
    private static final By passwordReg = By.id("password");
    private static final By daySelect = By.id("days");
    private static final By monthSelect = By.id("months");
    private static final By yearSelect = By.id("years");
    private static final By newsletterCheckbox = By.id("newsletter");
    private static final By optinCheckbox = By.id("optin");
    private static final By firstName = By.id("first_name");
    private static final By lastName = By.id("last_name");
    private static final By company = By.id("company");
    private static final By address1 = By.id("address1");
    private static final By address2 = By.id("address2");
    private static final By countrySelect = By.id("country");
    private static final By state = By.id("state");
    private static final By city = By.id("city");
    private static final By zipcode = By.id("zipcode");
    private static final By mobileNumber = By.id("mobile_number");
    private static final By createAccountBtn = By.cssSelector("button[data-qa='create-account']");

    // Account created locators
    private static final By accountCreatedMessage = By.cssSelector("h2[data-qa='account-created']");
    private static final By continueBtn = By.cssSelector("a[data-qa='continue-button']");

    // Logged in locators
    private static final By loggedInUsername = By.cssSelector("a>b");
    private static final By logoutBtn = By.cssSelector("a[href='/logout']");

    // Delete account locators
    private static final By deleteAccountBtn = By.cssSelector("a[href='/delete_account']");
    private static final By accountDeletedMessage = By.cssSelector("h2[data-qa='account-deleted']");

    // Contact us locators
    private static final By contactUsLink = By.cssSelector("a[href='/contact_us']");
    private static final By contactName = By.cssSelector("input[placeholder='Name']");
    private static final By contactEmail = By.cssSelector("input[placeholder='Email']");
    private static final By contactSubject = By.cssSelector("input[placeholder='Subject']");
    private static final By contactMessage = By.cssSelector("textarea[placeholder='Your Message Here']");
    private static final By uploadFile = By.cssSelector("input[name='upload_file']");
    private static final By submitBtn = By.cssSelector("input[value='Submit']");
    private static final By contactSuccessMessage = By.cssSelector("div.status.alert.alert-success");
    private static final By homeBtn = By.cssSelector("a.btn.btn-success");

    // Subscription locators
    private static final By subscriptionEmail = By.id("susbscribe_email");
    private static final By subscribeBtn = By.id("subscribe");

    // Error messages
    private static final By loginErrorMessage = By.cssSelector("p[style='color: red;']");
    private static final By signupErrorMessage = By.cssSelector("p[style='color: red;']");

    public SignupLoginPage() {
        super();
    }
    /**
     * Enter login email
     *
     * @param email Email address
     */
    public void enterLoginEmail(String email) {
        sendKeys(emailAddressLogin, email);
        logger.info("Entered login email: " + email);
    }

    /**
     * Enter login password
     *
     * @param password Password
     */
    public void enterLoginPassword(String password) {
        sendKeys(this.password, password);
        logger.info("Entered login password");
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        click(loginButton);
        logger.info("Clicked login button");
    }

    /**
     * Get login error message
     *
     * @return Error message
     */
    public String getLoginErrorMessage() {
        return getText(loginErrorMessage);
    }

    /**
     * Enter signup name
     *
     * @param name Name
     */
    public void enterSignupName(String name) {
        sendKeys(nameSignup, name);
        logger.info("Entered signup name: " + name);
    }

    /**
     * Enter signup email
     *
     * @param email Email address
     */
    public void enterSignupEmail(String email) {
        sendKeys(emailAddressSignup, email);
        logger.info("Entered signup email: " + email);
    }

    /**
     * Click signup button
     */
    public void clickSignupButton() {
        click(signupButton);
        logger.info("Clicked signup button");
    }

    /**
     * Get signup error message
     *
     * @return Error message
     */
    public String getSignupErrorMessage() {
        return getText(signupErrorMessage);
    }

    /**
     * Fill registration form
     *
     * @param title Title (Mr/Mrs)
     * @param password Password
     * @param day Day of birth
     * @param month Month of birth
     * @param year Year of birth
     * @param firstName First name
     * @param lastName Last name
     * @param company Company
     * @param address1 Address line 1
     * @param address2 Address line 2
     * @param country Country
     * @param state State
     * @param city City
     * @param zipcode Zipcode
     * @param mobile Mobile number
     */
    public void fillRegistrationForm(String title, String password, String day, String month, String year,
                                   String firstName, String lastName, String company, String address1,
                                   String address2, String country, String state, String city,
                                   String zipcode, String mobile) {
        // Select title
        if ("Mr".equalsIgnoreCase(title)) {
            click(titleMr);
        } else if ("Mrs".equalsIgnoreCase(title)) {
            click(titleMrs);
        }

        // Enter password
        sendKeys(passwordReg, password);

        // Select date of birth
        selectByValue(daySelect, day);
        selectByValue(monthSelect, month);
        selectByValue(yearSelect, year);

        // Check newsletters and offers
        click(newsletterCheckbox);
        click(optinCheckbox);

        // Enter personal details
        sendKeys(this.firstName, firstName);
        sendKeys(this.lastName, lastName);
        sendKeys(this.company, company);
        sendKeys(this.address1, address1);
        sendKeys(this.address2, address2);

        // Select country
        selectByVisibleText(countrySelect, country);

        // Enter address details
        sendKeys(this.state, state);
        sendKeys(this.city, city);
        sendKeys(this.zipcode, zipcode);
        sendKeys(mobileNumber, mobile);

        logger.info("Filled registration form");
    }

    /**
     * Click create account button
     */
    public void clickCreateAccountButton() {
        click(createAccountBtn);
        logger.info("Clicked create account button");
    }

    /**
     * Get account created message
     *
     * @return Success message
     */
    public String getAccountCreatedMessage() {
        return getText(accountCreatedMessage);
    }

    /**
     * Click continue after account creation
     */
    public void clickContinueAfterAccount() {
        click(continueBtn);
        logger.info("Clicked continue after account creation");
    }

    /**
     * Get logged in username
     *
     * @return Username
     */
    public String getLoggedInUsername() {
        return getText(loggedInUsername);
    }

    /**
     * Click logout button
     */
    public void clickLogoutButton() {
        click(logoutBtn);
        logger.info("Clicked logout button");
    }

    /**
     * Click delete account button
     */
    public void clickDeleteAccountButton() {
        click(deleteAccountBtn);
        logger.info("Clicked delete account button");
    }

    /**
     * Get account deleted message
     *
     * @return Success message
     */
    public String getAccountDeletedMessage() {
        return getText(accountDeletedMessage);
    }

    /**
     * Navigate to contact us
     */
    public void navigateToContactUs() {
        click(contactUsLink);
        logger.info("Navigated to contact us page");
    }

    /**
     * Fill contact form
     *
     * @param name Name
     * @param email Email
     * @param subject Subject
     * @param message Message
     */
    public void fillContactForm(String name, String email, String subject, String message) {
        sendKeys(contactName, name);
        sendKeys(contactEmail, email);
        sendKeys(contactSubject, subject);
        sendKeys(contactMessage, message);
        logger.info("Filled contact form");
    }

    /**
     * Upload file
     *
     * @param filePath File path
     */
    public void uploadFile(String filePath) {
        sendKeys(uploadFile, filePath);
        logger.info("Uploaded file: " + filePath);
    }

    /**
     * Click submit contact form
     */
    public void clickSubmitContactForm() {
        click(submitBtn);
        logger.info("Clicked submit contact form");
    }

    /**
     * Get contact success message
     *
     * @return Success message
     */
    public String getContactSuccessMessage() {
        return getText(contactSuccessMessage);
    }

    /**
     * Click home after contact
     */
    public void clickHomeAfterContact() {
        click(homeBtn);
        logger.info("Clicked home after contact");
    }

    /**
     * Enter subscription email
     *
     * @param email Email address
     */
    public void enterSubscriptionEmail(String email) {
        sendKeys(subscriptionEmail, email);
        logger.info("Entered subscription email: " + email);
    }

    /**
     * Click subscribe button
     */
    public void clickSubscribeButton() {
        click(subscribeBtn);
        logger.info("Clicked subscribe button");
    }

    /**
     * Get subscription success message
     *
     * @return Success message
     */
    public String getSubscriptionSuccessMessage() {
        // Assuming success message locator, may need to adjust based on actual site
        return getText(By.cssSelector("div.alert-success"));
    }

    /**
     * Helper method to select by value
     *
     * @param locator Select locator
     * @param value Value to select
     */
    private void selectByValue(By locator, String value) {
        Select select = new Select(driver.findElement(locator));
        select.selectByValue(value);
        logger.info("Selected value: " + value + " from dropdown: " + locator);
    }

    /**
     * Helper method to select by visible text
     *
     * @param locator Select locator
     * @param text Visible text to select
     */
    private void selectByVisibleText(By locator, String text) {
        Select select = new Select(driver.findElement(locator));
        select.selectByVisibleText(text);
        logger.info("Selected text: " + text + " from dropdown: " + locator);
    }
}
