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

    // Add Product Button - using data-target attribute
    @FindBy(xpath = "//button[@data-target='#addRowModal']")
    private WebElement addProductButton;

    // Table elements
    @FindBy(xpath = "//table[@id='add-row']")
    private WebElement productsTable;

    @FindBy(xpath = "//table[@id='add-row']//tbody//tr")
    private java.util.List<WebElement> productRows;

    // Add Product Modal elements
    @FindBy(xpath = "//div[@id='addRowModal']")
    private WebElement addProductModal;

    @FindBy(xpath = "//form[@action='/addProduct']//input[@name='name']")
    private WebElement productNameInput;

    @FindBy(xpath = "//form[@action='/addProduct']")
    private WebElement addProductForm;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
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
            // Try alternative xpath if first one fails
            try {
                WebElement altButton = driver.findElement(
                    org.openqa.selenium.By.xpath("//button[contains(@class, 'btn-primary') and contains(., 'Add Product')]"));
                return altButton.isDisplayed();
            } catch (Exception e2) {
                return false;
            }
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
        try {
            wait.until(ExpectedConditions.visibilityOf(addProductButton));
            // Try to click, if fails use JavaScript
            try {
                addProductButton.click();
            } catch (Exception e) {
                // Fallback to JavaScript click
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", addProductButton);
            }
        } catch (Exception e) {
            // Button might not be visible, that's OK for test
            throw new RuntimeException("Cannot click Add Product button: " + e.getMessage(), e);
        }
    }

    public int getProductCount() {
        try {
            wait.until(ExpectedConditions.visibilityOf(productsTable));
            return productRows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isAddProductModalDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addProductModal));
            return addProductModal.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddProductFormDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addProductForm));
            return addProductForm.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

