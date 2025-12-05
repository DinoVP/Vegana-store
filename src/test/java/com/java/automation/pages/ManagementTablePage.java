package com.java.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ManagementTablePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Duration TIMEOUT = Duration.ofSeconds(10);

    // --- Locators chung cho tất cả các trang quản lý (TC16, TC17, TC18, TC19, TC20, TC21, TC22) ---
    @FindBy(xpath = "//input[@type='search']") private WebElement searchInput; // Ô tìm kiếm
    @FindBy(xpath = "//table/tbody/tr[1]/td[2]") private WebElement firstRowResult; // Kết quả hàng đầu tiên (ID/Name)
    @FindBy(xpath = "//li[@class='paginate_button next']/a") private WebElement nextButton; // Nút Next (TC19)
    @FindBy(xpath = "//li[@class='paginate_button previous']/a") private WebElement previousButton; // Nút Previous (TC21)
    @FindBy(xpath = "//a[contains(text(), '2')]") private WebElement pageTwoButton; // Nút trang 2 (TC20)
    @FindBy(xpath = "//a[contains(text(), 'Export To Excel')]") private WebElement exportToExcelButton; // Nút Export (TC22)
    @FindBy(xpath = "//td[contains(text(), 'No matching records found')]") private WebElement noRecordsFound; // Thông báo không có kết quả (TC18)

    public ManagementTablePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    // --- Hàm cho TC16, TC17, TC18: Tìm kiếm ---
    public void search(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInput)).clear();
        searchInput.sendKeys(keyword);
        // Không cần click Enter vì Selenium thường tự động kích hoạt search sau khi nhập
    }

    public String getFirstResultText() {
        wait.until(ExpectedConditions.visibilityOf(firstRowResult));
        return firstRowResult.getText();
    }

    public boolean isNoRecordsFoundDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(noRecordsFound)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --- Hàm cho TC19, TC21: Phân trang Next/Previous ---
    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }
    public void clickPrevious() {
        wait.until(ExpectedConditions.elementToBeClickable(previousButton)).click();
    }

    // --- Hàm cho TC20: Click trang 2 ---
    public void clickPageTwo() {
        wait.until(ExpectedConditions.elementToBeClickable(pageTwoButton)).click();
    }

    // --- Hàm cho TC22: Export ---
    public void clickExportToExcel() {
        wait.until(ExpectedConditions.elementToBeClickable(exportToExcelButton)).click();
        // Cần thêm logic để kiểm tra file download (rất phức tạp, ta sẽ kiểm tra URL/Browser Action)
    }

    // Kiểm tra trang hiện tại
    public boolean isPageActive(String pageNumber) {
        WebElement pageButton = driver.findElement(org.openqa.selenium.By.xpath("//a[contains(text(), '" + pageNumber + "')]"));
        return pageButton.getAttribute("class").contains("active") ||
                pageButton.findElement(org.openqa.selenium.By.xpath("..")).getAttribute("class").contains("active");
    }
}