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
 * Page Object Model for Admin Products Management page
 */
public class ProductsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page Title
    @FindBy(xpath = "//h4[contains(@class, 'page-title') and contains(text(), 'Product Management')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//h4[contains(@class, 'card-title') and contains(text(), 'Product Management')]")
    private WebElement cardTitle;

    // Add Product Button
    @FindBy(xpath = "//button[contains(@class, 'btn-primary') and contains(text(), 'Add Product')]")
    private WebElement addProductButton;

    // Table elements
    @FindBy(xpath = "//table[@id='add-row']")
    private WebElement productsTable;

    @FindBy(xpath = "//table[@id='add-row']//tbody//tr")
    private java.util.List<WebElement> productRows;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void navigateToProductsPage() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/products");
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public boolean isOnProductsPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/admin/products");
    }

    public boolean isPageTitleDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCardTitleDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cardTitle));
            return cardTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddProductButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addProductButton));
            return addProductButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProductsTableDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(productsTable));
            return productsTable.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddProductButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addProductButton));
        addProductButton.click();
    }

    public int getProductCount() {
        try {
            wait.until(ExpectedConditions.visibilityOf(productsTable));
            return productRows.size();
        } catch (Exception e) {
            return 0;
        }
    }
}

