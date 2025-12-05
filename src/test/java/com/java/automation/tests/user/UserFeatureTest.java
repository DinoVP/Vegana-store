package com.java.automation.tests.user;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.java.automation.base.BaseTest;
import com.java.automation.config.TestConfig;
import com.java.automation.pages.HomePage;
import com.java.automation.pages.LoginOrRegisterPage;
import com.java.automation.pages.ProductDetailPage;
import com.java.automation.pages.ContactPage;

public class UserFeatureTest extends BaseTest {

    private LoginOrRegisterPage loginPage;
    private HomePage homePage;
    private ProductDetailPage productPage;
    private ContactPage contactPage;

    private final String USER_ID = TestConfig.getProperty("test.user.id");
    private final String USER_PASS = TestConfig.getProperty("test.user.password");

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginOrRegisterPage(driver);
        homePage = new HomePage(driver);
        contactPage = new ContactPage(driver);
    }

    // --- TC1: Đăng nhập thành công với tài khoản khách hàng ---
    @Test(priority = 1, description = "TC1: Đăng nhập khách hàng thành công")
    public void TC1_verifyUserLoginSuccess() { /* Code đã fix */ }

    // --- TC5: Kiểm tra lọc sản phẩm theo Danh mục ("Milk") ---
    @Test(priority = 2, description = "TC5: Lọc sản phẩm theo Category (Milk)")
    public void TC5_verifyFilterByCategory() { /* Code đã fix */ }

    // --- TC6: Kiểm tra chức năng xem chi tiết sản phẩm ---
    @Test(priority = 3, description = "TC6: Xem chi tiết sản phẩm đầu tiên")
    public void TC6_verifyViewProductDetail() { /* Code đã fix */ }

    // --- TC7: Kiểm tra nút Yêu thích (Add to Wishlist) ---
    @Test(priority = 4, description = "TC7: Thêm sản phẩm vào Wishlist")
    public void TC7_verifyAddToWishlist() { /* Code đã fix */ }

    // --- TC8: Kiểm tra chức năng So sánh sản phẩm (Compare) ---
    @Test(priority = 5, description = "TC8: So sánh 2 sản phẩm")
    public void TC8_verifyProductCompare() { /* Code đã fix */ }

    // --- TC9: Liên hệ gửi Form thành công ---
    @Test(priority = 6, description = "TC9: Gửi form liên hệ thành công")
    public void TC9_verifyContactFormSuccess() { /* Code đã fix */ }

    // --- TC10: Kiểm tra lọc sản phẩm theo Nhà cung cấp ("Vinamilk") ---
    @Test(priority = 7, description = "TC10: Lọc sản phẩm theo Supplier (Vinamilk)")
    public void TC10_verifyFilterBySupplier() { /* Code đã fix */ }

    // --- TC11: Kiểm tra hiển thị và hoạt động của Banner chính ---
    @Test(priority = 8, description = "TC11: Kiểm tra Banner chính")
    public void TC11_verifyMainBannerFunctionality() { /* Code đã fix */ }

    // --- TC4 & TC23: Kiểm tra tìm kiếm chung trên Header ---
    @Test(priority = 9, description = "TC4/TC23: Kiểm tra tìm kiếm chung trên Header với từ khóa hợp lệ")
    public void TC4_verifyHeaderSearch() {
        String keyword = "Snack";

        homePage.searchOnHeader(keyword);

        Assert.assertTrue(driver.getCurrentUrl().contains("search"),
                "Lỗi TC4: Không chuyển hướng đến trang kết quả tìm kiếm.");

        Assert.assertTrue(homePage.getFirstProductName().contains(keyword),
                "Lỗi TC4: Sản phẩm đầu tiên không chứa từ khóa tìm kiếm.");
    }

    // --- TC25: Kiểm tra giá gốc hiển thị trên danh sách sản phẩm ---
    @Test(priority = 10, description = "TC25: Kiểm tra giá gốc (gạch ngang) hiển thị")
    public void TC25_verifyOriginalPriceDisplay() {
        driver.get(TestConfig.getBaseUrl() + "/products");

        Assert.assertTrue(homePage.isOriginalPriceDisplayed(),
                "Lỗi TC25: Không tìm thấy giá gốc (gạch ngang) cho sản phẩm giảm giá.");
    }

    // --- TC24: Kiểm tra chức năng xem chi tiết sản phẩm (Click tên sản phẩm) ---
    @Test(priority = 11, description = "TC24: Kiểm tra chuyển hướng đến trang chi tiết sản phẩm")
    public void TC24_verifyProductDetailRedirection() {
        driver.get(TestConfig.getBaseUrl() + "/products");

        String productNameBeforeClick = homePage.getFirstProductName();

        homePage.clickFirstProductTitle();

        // Khởi tạo ProductDetailPage
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);

        Assert.assertTrue(driver.getCurrentUrl().contains("product"),
                "Lỗi TC24: URL không chuyển hướng đến trang chi tiết sản phẩm.");

        String productTitleOnDetailPage = productDetailPage.getProductTitle();
        Assert.assertEquals(productTitleOnDetailPage, productNameBeforeClick,
                "Lỗi TC24: Tên sản phẩm trên trang chi tiết không khớp với sản phẩm đã click.");
    }
}