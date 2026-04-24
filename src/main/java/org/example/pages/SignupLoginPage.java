package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.AlertHandler;
import org.openqa.selenium.By;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SignupLoginPage — Page Object for Signup, Login, Registration, Contact Us pages.
 *
 * All interactions go through BasePage → ActionsHelper. No direct driver access.
 */
public class SignupLoginPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(SignupLoginPage.class);

    // ── Login ──────────────────────────────────────────────────────────────
    private static final By LOGIN_EMAIL    = By.cssSelector("input[data-qa='login-email']");
    private static final By LOGIN_PASSWORD = By.cssSelector("input[placeholder='Password']");
    private static final By LOGIN_BTN      = By.cssSelector("button[data-qa='login-button']");
    private static final By LOGIN_ERROR    = By.cssSelector("p[style='color: red;']");

    // ── Signup ─────────────────────────────────────────────────────────────
    private static final By SIGNUP_NAME    = By.cssSelector("input[placeholder='Name']");
    private static final By SIGNUP_EMAIL   = By.cssSelector("input[data-qa='signup-email']");
    private static final By SIGNUP_BTN     = By.cssSelector("button[data-qa='signup-button']");
    private static final By SIGNUP_ERROR   = By.cssSelector("p[style='color: red;']");

    // ── Registration Form ──────────────────────────────────────────────────
    private static final By TITLE_MR       = By.id("id_gender1");
    private static final By TITLE_MRS      = By.id("id_gender2");
    private static final By REG_PASSWORD   = By.id("password");
    private static final By DOB_DAY        = By.id("days");
    private static final By DOB_MONTH      = By.id("months");
    private static final By DOB_YEAR       = By.id("years");
    private static final By NEWSLETTER_CB  = By.id("newsletter");
    private static final By OPTIN_CB       = By.id("optin");
    private static final By FIRST_NAME     = By.id("first_name");
    private static final By LAST_NAME      = By.id("last_name");
    private static final By COMPANY        = By.id("company");
    private static final By ADDRESS1       = By.id("address1");
    private static final By ADDRESS2       = By.id("address2");
    private static final By COUNTRY        = By.id("country");
    private static final By STATE          = By.id("state");
    private static final By CITY           = By.id("city");
    private static final By ZIPCODE        = By.id("zipcode");
    private static final By MOBILE         = By.id("mobile_number");
    private static final By CREATE_ACCOUNT = By.cssSelector("button[data-qa='create-account']");

    // ── Account Created ────────────────────────────────────────────────────
    private static final By ACCOUNT_CREATED_MSG = By.cssSelector("h2[data-qa='account-created']");
    private static final By CONTINUE_BTN        = By.cssSelector("a[data-qa='continue-button']");

    // ── Logged In ──────────────────────────────────────────────────────────
    private static final By LOGGED_IN_USER = By.cssSelector("a>b");
    private static final By LOGOUT_BTN     = By.cssSelector("a[href='/logout']");

    // ── Delete Account ─────────────────────────────────────────────────────
    private static final By DELETE_ACCOUNT_BTN = By.cssSelector("a[href='/delete_account']");
    private static final By ACCOUNT_DELETED_MSG = By.cssSelector("h2[data-qa='account-deleted']");

    // ── Contact Us ─────────────────────────────────────────────────────────
    private static final By CONTACT_NAME    = By.cssSelector("input[placeholder='Name']");
    private static final By CONTACT_EMAIL   = By.cssSelector("input[placeholder='Email']");
    private static final By CONTACT_SUBJECT = By.cssSelector("input[placeholder='Subject']");
    private static final By CONTACT_MESSAGE = By.cssSelector("textarea[placeholder='Your Message Here']");
    private static final By UPLOAD_FILE     = By.cssSelector("input[name='upload_file']");
    private static final By CONTACT_SUBMIT  = By.cssSelector("input[value='Submit']");
    private static final By CONTACT_SUCCESS = By.cssSelector("div.status.alert.alert-success");
    private static final By HOME_BTN        = By.cssSelector("a.btn.btn-success");

    // ── Subscription ───────────────────────────────────────────────────────
    private static final By SUBSCRIPTION_EMAIL   = By.id("susbscribe_email");
    private static final By SUBSCRIBE_BTN        = By.id("subscribe");

    // ── Constructor ────────────────────────────────────────────────────────

    public SignupLoginPage() {
        super();
    }

    // =========================================================================
    // LOGIN
    // =========================================================================

    public void enterLoginEmail(String email) {
        sendKeys(LOGIN_EMAIL, email);
        logger.info("Entered login email: {}", email);
    }

    public void enterLoginPassword(String password) {
        sendKeys(LOGIN_PASSWORD, password);
        logger.info("Entered login password");
    }

    public void clickLoginButton() {
        click(LOGIN_BTN);
        logger.info("Clicked login button");
    }

    public String getLoginErrorMessage() {
        return getText(LOGIN_ERROR);
    }

    // =========================================================================
    // SIGNUP
    // =========================================================================

    public void enterSignupName(String name) {
        sendKeys(SIGNUP_NAME, name);
        logger.info("Entered signup name: {}", name);
    }

    public void enterSignupEmail(String email) {
        sendKeys(SIGNUP_EMAIL, email);
        logger.info("Entered signup email: {}", email);
    }

    public void clickSignupButton() {
        click(SIGNUP_BTN);
        logger.info("Clicked signup button");
    }

    public String getSignupErrorMessage() {
        return getText(SIGNUP_ERROR);
    }

    // =========================================================================
    // REGISTRATION FORM
    // =========================================================================

    /**
     * Fills the full registration form after clicking signup.
     * Uses BasePage select methods — no direct driver access.
     */
    public void fillRegistrationForm(
            String title,
            String password,
            String day,
            String month,
            String year,
            String firstName,
            String lastName,
            String company,
            String address1,
            String address2,
            String country,
            String state,
            String city,
            String zipcode,
            String mobile
    ) {
        // Title radio button
        if ("Mr".equalsIgnoreCase(title)) {
            click(TITLE_MR);
        } else if ("Mrs".equalsIgnoreCase(title)) {
            click(TITLE_MRS);
        }

        // Password
        sendKeys(REG_PASSWORD, password);

        // Date of birth — using BasePage.selectByValue (no direct new Select())
        selectByValue(DOB_DAY, day);
        selectByValue(DOB_MONTH, month);
        selectByValue(DOB_YEAR, year);

        // Checkboxes
        click(NEWSLETTER_CB);
        click(OPTIN_CB);

        // Personal details
        sendKeys(FIRST_NAME, firstName);
        sendKeys(LAST_NAME, lastName);
        sendKeys(COMPANY, company);
        sendKeys(ADDRESS1, address1);
        sendKeys(ADDRESS2, address2);

        // Country dropdown — using BasePage.selectByVisibleText (no direct new Select())
        selectByVisibleText(COUNTRY, country);

        // Address details
        sendKeys(STATE, state);
        sendKeys(CITY, city);
        sendKeys(ZIPCODE, zipcode);
        sendKeys(MOBILE, mobile);

        logger.info("Registration form filled");
    }

    public void clickCreateAccountButton() {
        click(CREATE_ACCOUNT);
        logger.info("Clicked Create Account");
    }

    public String getAccountCreatedMessage() {
        return getText(ACCOUNT_CREATED_MSG);
    }

    public void clickContinueAfterAccount() {
        click(CONTINUE_BTN);
        logger.info("Clicked Continue after account creation");
    }

    // =========================================================================
    // LOGGED IN STATE
    // =========================================================================

    public String getLoggedInUsername() {
        return getText(LOGGED_IN_USER);
    }

    public void clickLogoutButton() {
        click(LOGOUT_BTN);
        logger.info("Clicked Logout");
    }

    // =========================================================================
    // DELETE ACCOUNT
    // =========================================================================

    public void clickDeleteAccountButton() {
        click(DELETE_ACCOUNT_BTN);
        logger.info("Clicked Delete Account");
    }

    public String getAccountDeletedMessage() {
        return getText(ACCOUNT_DELETED_MSG);
    }

    // =========================================================================
    // CONTACT US
    // =========================================================================

    public void fillContactForm(String name, String email, String subject, String message) {
        sendKeys(CONTACT_NAME, name);
        sendKeys(CONTACT_EMAIL, email);
        sendKeys(CONTACT_SUBJECT, subject);
        sendKeys(CONTACT_MESSAGE, message);
        logger.info("Contact form filled");
    }

    public void uploadFile(String filePath) {
        sendKeys(UPLOAD_FILE, filePath);
        logger.info("File uploaded: {}", filePath);
    }

    public void clickSubmitContactForm() {
        click(CONTACT_SUBMIT);
        // Browser alert appears after submit on this site
        AlertHandler.acceptAlertIfPresent(driver);
        logger.info("Contact form submitted");
    }

    public String getContactSuccessMessage() {
        return getText(CONTACT_SUCCESS);
    }

    public void clickHomeAfterContact() {
        click(HOME_BTN);
        logger.info("Clicked Home after contact");
    }

    // =========================================================================
    // SUBSCRIPTION
    // =========================================================================

    public void enterSubscriptionEmail(String email) {
        sendKeys(SUBSCRIPTION_EMAIL, email);
        logger.info("Entered subscription email: {}", email);
    }

    public void clickSubscribeButton() {
        click(SUBSCRIBE_BTN);
        logger.info("Clicked Subscribe");
    }

    public String getSubscriptionSuccessMessage() {
        return getText(By.cssSelector("div.alert-success"));
    }
}