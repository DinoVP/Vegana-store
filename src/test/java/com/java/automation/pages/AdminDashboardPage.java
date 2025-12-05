package com.java.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminDashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Duration TIMEOUT = Duration.ofSeconds(10);

    // --- Locators Menu Sidebar (TC12) ---
    @FindBy(xpath = "//a[contains(text(), 'Management System')]")
    private WebElement managementSystemMenu;

    // Sub-menu Links
    @FindBy(xpath = "//a[contains(text(), 'Customer Management')]")
    private WebElement customerManagementLink;
    @FindBy(xpath = "//a[contains(text(), 'Supplier Management')]")
    private WebElement supplierManagementLink;
    @FindBy(xpath = "//a[contains(text(), 'Category Management')]")
    private WebElement categoryManagementLink;
    @FindBy(xpath = "//a[contains(text(), 'Order Management')]")
    private WebElement orderManagementLink;

    // --- Locators cho TC26 ---
    @FindBy(xpath = "//span[contains(text(), 'Home Page')]")
    private WebElement homePageMenuTitle;

    // --- Locators Dashboard Widgets (TC12) ---
    @FindBy(xpath = "//div[contains(text(), 'Overall statistics')]")
    private WebElement overallStatisticsWidget;
    @FindBy(xpath = "//h4[contains(text(), 'Dashboard')]")
    private WebElement dashboardTitle;

    public AdminDashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    public boolean isDashboardDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(dashboardTitle)).isDisplayed();
    }

    // TC12: Kiểm tra sự hiển thị của các module chính
    public boolean verifyMainModulesDisplay() {
        boolean titleCheck = isDashboardDisplayed();
        boolean widgetCheck = wait.until(ExpectedConditions.visibilityOf(overallStatisticsWidget)).isDisplayed();
        boolean menuCheck = managementSystemMenu.isDisplayed();

        return titleCheck && widgetCheck && menuCheck;
    }

    // Phương thức điều hướng
    private void navigateToManagementPage(WebElement link) {
        if (!managementSystemMenu.getAttribute("class").contains("active")) {
            wait.until(ExpectedConditions.elementToBeClickable(managementSystemMenu)).click();
        }
        wait.until(ExpectedConditions.elementToBeClickable(link)).click();
    }

    public void navigateToCustomerManagement() {
        navigateToManagementPage(customerManagementLink);
    }
    public void navigateToSupplierManagement() {
        navigateToManagementPage(supplierManagementLink);
    }
    public void navigateToCategoryManagement() {
        navigateToManagementPage(categoryManagementLink);
    }
    public void navigateToOrderManagement() {
        navigateToManagementPage(orderManagementLink);
    }

    // Hàm cho TC26: Kiểm tra tiêu đề Home Page trong menu
    public boolean isHomePageMenuTitleDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(homePageMenuTitle)).isDisplayed();
    }
}