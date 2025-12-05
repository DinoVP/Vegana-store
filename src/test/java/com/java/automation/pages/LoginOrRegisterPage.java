package com.java.automation.pages;

import com.java.automation.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model for Login/Register page - Optimized version
 */
public class LoginOrRegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Duration TIMEOUT = Duration.ofSeconds(10);

    // Login Tab
    @FindBy(xpath = "//a[contains(@href, '#signin')]")
    private WebElement signInTab;

    // Login Form Elements
    @FindBy(name = "customerId")
    private WebElement loginCustomerIdInput;

    @FindBy(name = "password")
    private WebElement loginPasswordInput;

    @FindBy(xpath = "//form[@action='/doLogin']//button[@type='submit']")
    private WebElement signInButton;

    // Register Tab
    @FindBy(xpath = "//a[contains(@href, '#signup')]")
    private WebElement signUpTab;

    // Register Form Elements
    @FindBy(xpath = "//form[@action='/registered']//input[@placeholder='ID Login']")
    private WebElement registerCustomerIdInput;

    @FindBy(xpath = "//form[@action='/registered']//input[@placeholder='Full Name']")
    private WebElement registerFullnameInput;

    @FindBy(xpath = "//form[@action='/registered']//input[@type='email']")
    private WebElement registerEmailInput;

    @FindBy(xpath = "//form[@action='/registered']//input[@type='password']")
    private WebElement registerPasswordInput;

    @FindBy(xpath = "//form[@action='/registered']//button[@type='submit']")
    private WebElement signUpButton;

    // Alert Messages
    @FindBy(xpath = "//div[contains(@class, 'alert-danger')]")
    private WebElement errorAlert;
    @FindBy(xpath = "//div[contains(@class, 'alert-success')]")
    private WebElement successAlert;

    // LOCATOR CHO TRANG ĐÍCH (TRANG MY ACCOUNT - TC1)
    @FindBy(xpath = "//h2[contains(text(), 'Your Timeline')]")
    private WebElement myAccountTimelineTitle; // Tiêu đề đặc trưng của trang My Account

    // Remember me checkbox
    @FindBy(id = "signin-check")
    private WebElement rememberMeCheckbox;

    // Forgot password link
    @FindBy(xpath = "//a[contains(text(), 'Forgot password')]")
    private WebElement forgotPasswordLink;

    public LoginOrRegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    // --- Navigation methods ---

    public void navigateToLoginPage() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.urlContains("/login"));
        wait.until(ExpectedConditions.visibilityOf(signInTab));
    }

    public void clickSignInTab() {
        wait.until(ExpectedConditions.elementToBeClickable(signInTab));
        signInTab.click();
        wait.until(ExpectedConditions.visibilityOf(loginCustomerIdInput));
    }

    public void clickSignUpTab() {
        wait.until(ExpectedConditions.elementToBeClickable(signUpTab));
        signUpTab.click();
        wait.until(ExpectedConditions.visibilityOf(registerCustomerIdInput));
    }

    // --- Login methods ---

    public void enterLoginCustomerId(String customerId) {
        wait.until(ExpectedConditions.visibilityOf(loginCustomerIdInput));
        loginCustomerIdInput.clear();
        loginCustomerIdInput.sendKeys(customerId);
    }

    public void enterLoginPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(loginPasswordInput));
        loginPasswordInput.clear();
        loginPasswordInput.sendKeys(password);
    }

    public void clickSignInButton() {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton));
        signInButton.click();

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.not(ExpectedConditions.urlContains("/login")),
                    ExpectedConditions.visibilityOf(errorAlert)
            ));
        } catch (Exception e) {
            // Pass
        }
    }

    public void login(String customerId, String password) {
        clickSignInTab();
        enterLoginCustomerId(customerId);
        enterLoginPassword(password);
        clickSignInButton();
    }

    public void checkRememberMe() {
        if (!rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
    }

    // --- Registration methods (FIX LỖI BIÊN DỊCH) ---

    public void enterRegisterCustomerId(String customerId) {
        wait.until(ExpectedConditions.visibilityOf(registerCustomerIdInput));
        registerCustomerIdInput.clear();
        registerCustomerIdInput.sendKeys(customerId);
    }

    public void enterRegisterFullname(String fullname) {
        wait.until(ExpectedConditions.visibilityOf(registerFullnameInput));
        registerFullnameInput.clear();
        registerFullnameInput.sendKeys(fullname);
    }

    public void enterRegisterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(registerEmailInput));
        registerEmailInput.clear();
        registerEmailInput.sendKeys(email);
    }

    public void enterRegisterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(registerPasswordInput));
        registerPasswordInput.clear();
        registerPasswordInput.sendKeys(password);
    }

    public void clickSignUpButton() {
        wait.until(ExpectedConditions.elementToBeClickable(signUpButton));
        signUpButton.click();
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(successAlert),
                    ExpectedConditions.visibilityOf(errorAlert)
            ));
        } catch (Exception e) {
            // Pass
        }
    }

    // Phương thức tổng hợp cho Register
    public void register(String customerId, String fullname, String email, String password) {
        clickSignUpTab();
        enterRegisterCustomerId(customerId);
        enterRegisterFullname(fullname);
        enterRegisterEmail(email);
        enterRegisterPassword(password);
        clickSignUpButton();
    }


    // --- Validation methods ---

    public boolean isErrorAlertDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorAlert));
            return errorAlert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSuccessAlertDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successAlert));
            return successAlert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorAlertText() {
        if (isErrorAlertDisplayed()) {
            return errorAlert.getText();
        }
        return "";
    }

    public String getSuccessAlertText() {
        if (isSuccessAlertDisplayed()) {
            return successAlert.getText();
        }
        return "";
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/login");
    }

    // --- SỬA LỖI XÁC THỰC TC1 ---
    public boolean isCustomerAccountDisplayed() {
        if (driver.getCurrentUrl().contains("/account")) {
            try {
                // Chờ tiêu đề "Your Timeline" hiển thị (Locator đã thêm)
                return wait.until(ExpectedConditions.visibilityOf(myAccountTimelineTitle)).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Kiểm tra chuyển hướng thành công đến Dashboard (Admin - TC14)
     */
    public boolean isAdminDashboardDisplayed() {
        return driver.getCurrentUrl().contains("/admin/dashboard");
    }
}