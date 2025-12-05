package com.java.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductDetailPage {
    private WebDriver driver;

    // --- Locators Trang Chi tiết Sản phẩm ---
    @FindBy(xpath = "//h1[@class='product-name']") private WebElement productName;
    @FindBy(xpath = "//span[@class='price-value']") private WebElement productPrice;
    @FindBy(xpath = "//h1[@class='product-title']") private WebElement productTitle; // Bổ sung cho TC24

    // ... (Các locators khác như Description, Add to Cart, v.v.) ...

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Lấy tên sản phẩm trên trang chi tiết
     */
    public String getProductName() {
        return productName.getText();
    }

    public String getProductTitle() { // Bổ sung cho TC24
        // Dùng productName hoặc locator khác tùy theo cấu trúc HTML của trang Detail
        return productTitle.getText();
    }

    /**
     * Lấy giá sản phẩm trên trang chi tiết
     */
    public String getProductPrice() {
        return productPrice.getText();
    }
}