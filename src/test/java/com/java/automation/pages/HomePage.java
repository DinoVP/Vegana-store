package com.java.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException; // Cần import cho custom exception

import java.time.Duration;
import java.util.List;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
    private final Duration TIMEOUT = Duration.ofSeconds(10);

    // --- Locators chung (Header) ---
    @FindBy(xpath = "//a[contains(text(), 'Categories')]") private WebElement categoriesMenu;
    @FindBy(xpath = "//a[contains(text(), 'Suppliers')]") private WebElement suppliersMenu;

    @FindBy(xpath = "//input[@placeholder='Search anything...']")
    private WebElement searchInputHeader;

    // FIX LOCATOR: Nút Search (Sử dụng Locator dựa trên form Search để an toàn hơn)
    // Giả định: Form search có attribute action='search' hoặc button có text 'Search'
    @FindBy(xpath = "//button[normalize-space()='Search']")
    private WebElement searchButtonHeader;

    // --- Locators Trang Sản phẩm (Product List) ---
    @FindBy(xpath = "//div[@class='product-item']") private List<WebElement> productItems;

    // FIX/RELIABILITY: Locator tìm tất cả tên sản phẩm
    @FindBy(xpath = "//div[@class='product-item']//h3/a") private List<WebElement> productTitles;

    // FIX/RELIABILITY: Locator tìm tên sản phẩm đầu tiên
    @FindBy(xpath = "(//div[@class='product-item']//h3/a)[1]") private WebElement firstProductNameLink;

    @FindBy(xpath = "(//div[@class='product-item']//i[@class='far fa-heart'])[1]") private WebElement firstProductWishlistButton;

    // --- Locators cho TC8: Compare ---
    @FindBy(xpath = "(//div[@class='product-item']//i[@class='fas fa-exchange-alt'])[1]") private WebElement firstProductCompareButton;
    @FindBy(xpath = "(//div[@class='product-item']//i[@class='fas fa-exchange-alt'])[2]") private WebElement secondProductCompareButton;
    @FindBy(xpath = "//a[contains(@href, '/compare')]") private WebElement compareLink;

    // --- Locators cho TC11: Banner ---
    @FindBy(xpath = "//section[@class='main-banner']") private WebElement mainBannerSection;
    @FindBy(xpath = "//a[contains(@class, 'carousel-control-next')]") private WebElement nextSlideButton;
    @FindBy(xpath = "//div[@class='carousel-indicators']/li[contains(@data-slide-to, '1')]") private WebElement secondSlideIndicator;
    @FindBy(xpath = "//a[contains(@class, 'btn') and contains(@href, 'products')]") private WebElement bannerCTAButton;

    // --- Locators cho TC25: Giá gốc gạch ngang (Mới) ---
    @FindBy(xpath = "(//del)[1]") private WebElement firstProductOriginalPrice;

    // --- Locators Message ---
    @FindBy(xpath = "//div[contains(@class, 'toast-body')]") private WebElement toastMessage;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    // --- Hàm Getters ---
    public List<WebElement> getProductTitles() {
        // FIX: Chờ các sản phẩm hiển thị trước khi trả về danh sách
        wait.until(ExpectedConditions.visibilityOfAllElements(productTitles));
        return productTitles;
    }

    public String getFirstProductName() {
        // FIX: Chờ cho link tên sản phẩm hiển thị trước khi lấy text
        return wait.until(ExpectedConditions.visibilityOf(firstProductNameLink)).getText();
    }


    // --- Hàm cho TC5 & TC10: Lọc sản phẩm ---

    private void filterByDropdown(WebElement menuElement, String filterName) {
        actions.moveToElement(menuElement).perform();
        WebElement filterLink = wait.until(ExpectedConditions.elementToBeClickable(
                driver.findElement(org.openqa.selenium.By.xpath(
                        "//div[contains(@class, 'dropdown-menu show')]//a[normalize-space()='" + filterName + "']"
                ))
        ));
        filterLink.click();
    }

    public void filterByCategory(String categoryName) {
        filterByDropdown(categoriesMenu, categoryName);
    }

    public void filterBySupplier(String supplierName) {
        filterByDropdown(suppliersMenu, supplierName);
    }

    // --- Hàm cho TC4 & TC23: Tìm kiếm Header (FIX LỖI) ---
    public void searchOnHeader(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInputHeader)).sendKeys(keyword);
        // FIX: Click nút Search sau khi nhập từ khóa
        wait.until(ExpectedConditions.elementToBeClickable(searchButtonHeader)).click();
    }

    // --- Hàm cho TC6 & TC24: Xem chi tiết sản phẩm ---

    public ProductDetailPage clickFirstProductTitle() {
        // FIX: Hàm getProductTitles() đã đảm bảo sản phẩm hiển thị
        if (!productTitles.isEmpty()) {
            productTitles.get(0).click();
            return new ProductDetailPage(driver);
        }
        // Ném ngoại lệ an toàn nếu không tìm thấy
        throw new NoSuchElementException("Không tìm thấy sản phẩm nào để click. Product list is empty.");
    }

    // --- Hàm cho TC7: Add to Wishlist ---

    public void addFirstProductToWishlist() {
        actions.moveToElement(productItems.get(0)).perform();
        wait.until(ExpectedConditions.elementToBeClickable(firstProductWishlistButton)).click();
    }

    public String getToastMessageText() {
        wait.until(ExpectedConditions.visibilityOf(toastMessage));
        return toastMessage.getText();
    }

    // --- Hàm cho TC8: Compare ---

    public void addProductToCompare(int index) {
        WebElement compareButton = null;
        if (index == 1) {
            compareButton = firstProductCompareButton;
        } else if (index == 2) {
            compareButton = secondProductCompareButton;
        } else {
            throw new IllegalArgumentException("Chỉ hỗ trợ thêm 2 sản phẩm đầu tiên.");
        }
        actions.moveToElement(productItems.get(index - 1)).perform();
        wait.until(ExpectedConditions.elementToBeClickable(compareButton)).click();
    }

    public void openComparePopup() {
        wait.until(ExpectedConditions.elementToBeClickable(compareLink)).click();
    }

    // --- Hàm cho TC11: Banner ---

    public boolean isBannerDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(mainBannerSection)).isDisplayed();
    }

    public void clickNextSlide() {
        wait.until(ExpectedConditions.elementToBeClickable(nextSlideButton)).click();
    }

    public boolean isSecondSlideActive() {
        return wait.until(ExpectedConditions.attributeContains(secondSlideIndicator, "class", "active"));
    }

    public void clickBannerCTA() {
        wait.until(ExpectedConditions.elementToBeClickable(bannerCTAButton)).click();
    }

    // --- Hàm cho TC25: Giá gốc ---
    public boolean isOriginalPriceDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(firstProductOriginalPrice)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}