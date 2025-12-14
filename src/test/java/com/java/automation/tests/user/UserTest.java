package com.java.automation.tests.user;

import com.java.automation.base.BaseTest;
import com.java.automation.config.TestConfig;
import com.java.automation.pages.*;
import com.java.automation.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebElement;

/**
 * Các Test Case dành cho người dùng (Customer/User Flow)
 * Gồm: Đăng nhập, Đăng ký, Điều hướng cơ bản.
 */
public class UserTest extends BaseTest {

    // --- DỮ LIỆU CỐ ĐỊNH THEO YÊU CẦU ---
    // TC1 & TC2: Tài khoản mẫu
    private static final String USER_ID = "khai00";
    private static final String USER_PASSWORD = "123456";

    // Dữ liệu dùng cho TC4 (Đăng ký thành công)
    private final String TC4_FULLNAME = "Nguyen Van Test";
    private final String TC_PASSWORD = "MatKhauTest1";

    // --- BIẾN TOÀN CỤC CHO CÁC TC ĐĂNG KÝ (Đảm bảo dữ liệu duy nhất) ---
    // Tạo ID/Email duy nhất cho TC4, được tái sử dụng trong TC5
    private static final String UNIQUE_TEST_ID = TestDataGenerator.generateUniqueCustomerId();
    private static final String UNIQUE_TEST_EMAIL = UNIQUE_TEST_ID + "@example.com";

    // Dữ liệu mới cho TC6/TC7
    private static final String TC6_ID = TestDataGenerator.generateUniqueCustomerId();
    private static final String TC7_EMAIL = TestDataGenerator.generateUniqueEmail();


    // === PHƯƠNG THỨC HỖ TRỢ (Nếu cần) ===
    private void loginAsUser() {
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(USER_ID, USER_PASSWORD);
    }

    // =======================================================
    // TC1, TC2 (Sign In)
    // =======================================================

    @Test(priority = 1, description = "TC1: Successful Customer Login (ID: khai00)")
    public void testTC1_LoginSuccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC1: Bắt đầu Đăng nhập thành công với Customer ID: " + USER_ID);

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();

        loginPage.login(USER_ID, USER_PASSWORD);

        Assert.assertTrue(loginPage.isOnHomePage(),
                "TC1 FAILED: Đăng nhập thành công nhưng không redirect về trang chủ.");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC1 PASSED: Đăng nhập thành công.");
    }

    @Test(priority = 2, description = "TC2: Failed Login - Invalid Password (sai123)")
    public void testTC2_LoginWithInvalidPassword() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC2: Bắt đầu Đăng nhập với mật khẩu sai (sai123).");

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();

        loginPage.login(USER_ID, "sai123");

        Assert.assertTrue(loginPage.isErrorAlertDisplayed(),
                "TC2 FAILED: Không hiển thị thông báo lỗi khi đăng nhập với mật khẩu sai.");

        String errorText = loginPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("không chính xác") || errorText.contains("sai") || errorText.contains("Invalid"),
                "TC2 FAILED: Thông báo lỗi không đúng (dự kiến lỗi thông tin đăng nhập). Text: " + errorText);

        Assert.assertTrue(loginPage.isOnLoginPage(),
                "TC2 FAILED: Đã thoát khỏi trang login sau khi nhập mật khẩu sai.");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC2 PASSED: Đăng nhập sai mật khẩu hiển thị lỗi đúng.");
    }

    // =======================================================
    // TC3 (Sign Up Navigation)
    // =======================================================

    @Test(priority = 3, description = "TC3: 'Sign Up' Link Navigation")
    public void testTC3_SignUpLinkNavigation() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC3: Kiểm tra chuyển hướng đến tab Đăng ký.");

        LoginOrRegisterPage page = new LoginOrRegisterPage(driver);
        page.navigateToLoginPage();

        page.clickSignUpTab();

        // Khắc phục lỗi: Kiểm tra form Đăng ký đã hiển thị (dựa vào trường ID Login)
        boolean isSignUpFormDisplayed = false;
        try {
            isSignUpFormDisplayed = driver.findElement(By.xpath("//form[@action='/registered']//input[@placeholder='ID Login']")).isDisplayed();
        } catch (Exception e) {
            isSignUpFormDisplayed = false;
        }

        Assert.assertTrue(page.isOnLoginPage() && isSignUpFormDisplayed,
                "TC3 FAILED: Không chuyển đến tab Đăng ký (Sign Up) hoặc form không hiển thị.");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC3 PASSED: Chuyển hướng đến tab Đăng ký thành công.");
    }

    // =======================================================
    // TC4 (Registration Success)
    // =======================================================

    @Test(priority = 4, description = "TC4: Successful Account Registration")
    public void testTC4_RegisterSuccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC4: Bắt đầu test đăng ký thành công.");

        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
        registerPage.navigateToLoginPage();

        // Dùng dữ liệu UNIQUE_TEST_ID, TC4_FULLNAME, UNIQUE_TEST_EMAIL, TC_PASSWORD
        registerPage.register(UNIQUE_TEST_ID, TC4_FULLNAME, UNIQUE_TEST_EMAIL, TC_PASSWORD);

        Assert.assertTrue(registerPage.isSuccessAlertDisplayed(),
                "TC4 FAILED: Không hiển thị thông báo thành công khi đăng ký.");

        String successText = registerPage.getSuccessAlertText();
        Assert.assertTrue(successText.contains("thành công") || successText.contains("Đăng kí"),
                "TC4 FAILED: Thông báo thành công không đúng. Text: " + successText);

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC4 PASSED: Đăng ký thành công với ID: " + UNIQUE_TEST_ID);
    }

    // =======================================================
    // TC5 (Registration Duplicate) - Đăng ký thất bại
    // =======================================================

    @Test(priority = 5, description = "TC5: Failed Registration - Duplicate Account (Lặp lại data TC4)")
    public void testTC5_RegisterDuplicateAccount() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC5: Bắt đầu test đăng ký trùng ID/Email (sử dụng lại data TC4).");

        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
        registerPage.navigateToLoginPage();

        // Sử dụng lại ID và Email đã đăng ký thành công ở TC4
        String tempFullName = TestDataGenerator.generateUniqueFullname();
        registerPage.register(UNIQUE_TEST_ID, tempFullName, UNIQUE_TEST_EMAIL, TC_PASSWORD);

        Assert.assertTrue(registerPage.isErrorAlertDisplayed(),
                "TC5 FAILED: Không hiển thị thông báo lỗi khi đăng ký trùng ID/Email.");

        String errorText = registerPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("ID Login") || errorText.contains("Email") || errorText.contains("đã được sử dụng") || errorText.contains("exists"),
                "TC5 FAILED: Thông báo lỗi không đúng (dự kiến lỗi trùng lặp): " + errorText);

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC5 PASSED: Đăng ký trùng lặp hiển thị lỗi đúng (thất bại).");
    }

    // =======================================================
    // TC6 (Missing Field - Full Name) - Đăng ký thất bại
    // =======================================================

    @Test(priority = 6, description = "TC6: Failed Registration - Missing Full Name")
    public void testTC6_RegisterMissingFullname() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC6: Bắt đầu test đăng ký thiếu trường Full Name.");

        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
        registerPage.navigateToLoginPage();
        registerPage.clickSignUpTab();

        // Bước: Bỏ trống trường Full Name (không gọi enterRegisterFullname)
        registerPage.enterRegisterCustomerId(TC6_ID); // Dùng ID mới để tránh lỗi trùng lặp
        // registerPage.enterRegisterFullname(""); // Bỏ qua bước nhập để trường trống
        registerPage.enterRegisterEmail(TestDataGenerator.generateUniqueEmail());
        registerPage.enterRegisterPassword(TC_PASSWORD);

        registerPage.clickSignUpButton();

        // Kết quả mong đợi: Validation hoạt động, trang vẫn ở form Đăng ký.
        // Nếu có lỗi Alert từ server do thiếu trường, nó sẽ được check ở Assert.assertTrue(registerPage.isErrorAlertDisplayed())

        // Kiểm tra validation: trang vẫn ở lại form Đăng ký
        Assert.assertTrue(registerPage.isOnLoginPage(),
                "TC6 FAILED: Đã thoát khỏi trang login/register mặc dù thiếu trường Full Name.");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC6 PASSED: Validation hoạt động, ngăn form submit khi thiếu Full Name (thất bại).");
    }

    // =======================================================
    // TC7 (Missing Field - ID Login) - Đăng ký thất bại
    // =======================================================

    @Test(priority = 7, description = "TC7: Failed Registration - Missing ID Login")
    public void testTC7_RegisterMissingCustomerId() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC7: Bắt đầu test đăng ký thiếu trường ID Login.");

        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
        registerPage.navigateToLoginPage();
        registerPage.clickSignUpTab();

        // Bước: Bỏ trống trường ID Login (không gọi enterRegisterCustomerId)
        // registerPage.enterRegisterCustomerId(""); // Bỏ qua bước nhập
        registerPage.enterRegisterFullname(TC4_FULLNAME);
        registerPage.enterRegisterEmail(TC7_EMAIL); // Dùng email mới để tránh lỗi trùng lặp
        registerPage.enterRegisterPassword(TC_PASSWORD);

        registerPage.clickSignUpButton();

        // Kết quả mong đợi: Validation lỗi hoạt động, ngăn form submit.
        Assert.assertTrue(registerPage.isOnLoginPage(),
                "TC7 FAILED: Đã thoát khỏi trang login/register mặc dù thiếu trường ID Login.");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC7 PASSED: Validation hoạt động, ngăn form submit khi thiếu ID Login (thất bại).");
    }

    // =======================================================
    // TC8, TC9, TC10 (Header Navigation)
    // =======================================================

    @Test(priority = 8, description = "TC8: Direct Product Listing Page Access (All Products)")
    public void testTC8_AllProductsLink() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC8: Kiểm tra nút 'All Products' trên Header.");

        driver.findElement(By.xpath("//a[contains(@class, 'navbar-link') and contains(text(), 'All Products')]")).click();

        ShopPage shopPage = new ShopPage(driver);

        Assert.assertTrue(shopPage.isOnShopPage(),
                "TC8 FAILED: Không chuyển hướng thành công đến trang Products.");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC8 PASSED: Chuyển đến trang Products thành công.");
    }

    @Test(priority = 9, description = "TC9: Login & Register Link Navigation")
    public void testTC9_LoginRegisterLink() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC9: Kiểm tra nút 'Login & Register' trên Header.");

        driver.findElement(By.xpath("//a[contains(@class, 'navbar-link') and contains(text(), 'Login & Register')]")).click();

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);

        Assert.assertTrue(loginPage.isOnLoginPage(),
                "TC9 FAILED: Không chuyển hướng thành công đến trang Đăng nhập.");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC9 PASSED: Chuyển đến trang Đăng nhập thành công.");
    }

    @Test(priority = 10, description = "TC10: Contact Link Navigation")
    public void testTC10_ContactLink() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "TC10: Kiểm tra nút 'Contact' trên Header.");

        driver.findElement(By.xpath("//a[contains(@class, 'navbar-link') and contains(text(), 'Contact')]")).click();

        // Sử dụng kiểm tra URL cho TC10
        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains("/contact"),
                "TC10 FAILED: Không chuyển hướng thành công đến trang Liên hệ. URL: " + currentUrl);

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC10 PASSED: Chuyển đến trang Liên hệ thành công.");
    }
}


//package com.java.automation.tests.user;
//
//import com.java.automation.base.BaseTest;
//import com.java.automation.config.TestConfig;
//import com.java.automation.pages.*;
//import com.java.automation.utils.TestDataGenerator;
//import org.testng.Assert;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
///**
// * All User Test Cases - Authentication, Shopping, Cart & Checkout
// */
//public class UserTest extends BaseTest {
//
//    private static final String USER_ID = TestConfig.getProperty("test.user.id");
//    private static final String USER_PASSWORD = TestConfig.getProperty("test.user.password");
//
//    /**
//     * Helper method to login as user
//     */
//    private void loginAsUser() {
//        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
//        loginPage.navigateToLoginPage();
//        loginPage.login(USER_ID, USER_PASSWORD);
//    }
//
//    // ==================== REGISTRATION TESTS ====================
//
//    @Test(priority = 1, description = "01. Register - Đăng ký thành công")
//    public void testRegisterSuccess() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test đăng ký thành công");
//
//        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
//        registerPage.navigateToLoginPage();
//
//        String customerId = TestDataGenerator.generateUniqueCustomerId();
//        String fullname = TestDataGenerator.generateUniqueFullname();
//        String email = TestDataGenerator.generateUniqueEmail();
//        String password = "123456";
//
//        registerPage.register(customerId, fullname, email, password);
//
//        Assert.assertTrue(registerPage.isSuccessAlertDisplayed(),
//            "Không hiển thị thông báo thành công khi đăng ký");
//
//        String successText = registerPage.getSuccessAlertText();
//        Assert.assertTrue(successText.contains("thành công") ||
//                         successText.contains("Đăng kí"),
//            "Thông báo thành công không đúng: " + successText);
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Đăng ký thành công");
//    }
//
//    @Test(priority = 2, description = "02. Register - Đăng ký với Customer ID đã tồn tại")
//    public void testRegisterWithExistingCustomerId() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test đăng ký với Customer ID đã tồn tại");
//
//        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
//        registerPage.navigateToLoginPage();
//
//        String existingCustomerId = TestConfig.getProperty("test.user.id");
//        String fullname = TestDataGenerator.generateUniqueFullname();
//        String email = TestDataGenerator.generateUniqueEmail();
//        String password = "123456";
//
//        registerPage.register(existingCustomerId, fullname, email, password);
//
//        Assert.assertTrue(registerPage.isErrorAlertDisplayed(),
//            "Không hiển thị thông báo lỗi khi đăng ký với Customer ID đã tồn tại");
//
//        String errorText = registerPage.getErrorAlertText();
//        Assert.assertTrue(errorText.contains("ID Login") ||
//                         errorText.contains("đã được sử dụng"),
//            "Thông báo lỗi không đúng: " + errorText);
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Đăng ký với Customer ID đã tồn tại đã hiển thị lỗi đúng");
//    }
//
//    @Test(priority = 3, description = "03. Register - Đăng ký với Email đã tồn tại")
//    public void testRegisterWithExistingEmail() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test đăng ký với Email đã tồn tại");
//
//        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
//        registerPage.navigateToLoginPage();
//
//        String customerId = TestDataGenerator.generateUniqueCustomerId();
//        String fullname = TestDataGenerator.generateUniqueFullname();
//        String existingEmail = TestConfig.getProperty("test.user.email");
//        String password = "123456";
//
//        registerPage.register(customerId, fullname, existingEmail, password);
//
//        Assert.assertTrue(registerPage.isErrorAlertDisplayed(),
//            "Không hiển thị thông báo lỗi khi đăng ký với Email đã tồn tại");
//
//        String errorText = registerPage.getErrorAlertText();
//        Assert.assertTrue(errorText.contains("Email") ||
//                         errorText.contains("đã được sử dụng"),
//            "Thông báo lỗi không đúng: " + errorText);
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Đăng ký với Email đã tồn tại đã hiển thị lỗi đúng");
//    }
//
//    @Test(priority = 4, description = "04. Register - Đăng ký với Customer ID trống")
//    public void testRegisterWithEmptyCustomerId() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test đăng ký với Customer ID trống");
//
//        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
//        registerPage.navigateToLoginPage();
//
//        registerPage.clickSignUpTab();
//        registerPage.enterRegisterFullname("Test User");
//        registerPage.enterRegisterEmail("test@example.com");
//        registerPage.enterRegisterPassword("123456");
//        registerPage.clickSignUpButton();
//
//        Assert.assertTrue(registerPage.isOnLoginPage(),
//            "Form không validate khi Customer ID trống");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Form validate đúng khi Customer ID trống");
//    }
//
//    // ==================== LOGIN TESTS ====================
//
//    @Test(priority = 5, description = "05. Login - Đăng nhập thành công")
//    public void testLoginSuccess() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test đăng nhập thành công");
//
//        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
//        loginPage.navigateToLoginPage();
//
//        loginPage.login(USER_ID, USER_PASSWORD);
//
//        Assert.assertTrue(loginPage.isOnHomePage(),
//            "Đăng nhập thành công nhưng không redirect về trang chủ");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Đăng nhập thành công");
//    }
//
//    @Test(priority = 6, description = "06. Login - Đăng nhập với Customer ID sai")
//    public void testLoginWithInvalidCustomerId() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test đăng nhập với Customer ID sai");
//
//        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
//        loginPage.navigateToLoginPage();
//
//        loginPage.login("invalid_user_id", "123456");
//
//        Assert.assertTrue(loginPage.isErrorAlertDisplayed(),
//            "Không hiển thị thông báo lỗi khi đăng nhập với Customer ID sai");
//
//        String errorText = loginPage.getErrorAlertText();
//        Assert.assertTrue(errorText.contains("không chính xác") ||
//                         errorText.contains("Tài khoản"),
//            "Thông báo lỗi không đúng: " + errorText);
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Đăng nhập với Customer ID sai đã hiển thị lỗi đúng");
//    }
//
//    @Test(priority = 7, description = "07. Login - Đăng nhập với mật khẩu sai")
//    public void testLoginWithInvalidPassword() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test đăng nhập với mật khẩu sai");
//
//        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
//        loginPage.navigateToLoginPage();
//
//        loginPage.login(USER_ID, "wrong_password");
//
//        Assert.assertTrue(loginPage.isErrorAlertDisplayed(),
//            "Không hiển thị thông báo lỗi khi đăng nhập với mật khẩu sai");
//
//        String errorText = loginPage.getErrorAlertText();
//        Assert.assertTrue(errorText.contains("không chính xác") ||
//                         errorText.contains("Tài khoản"),
//            "Thông báo lỗi không đúng: " + errorText);
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Đăng nhập với mật khẩu sai đã hiển thị lỗi đúng");
//    }
//
//    @DataProvider(name = "loginData")
//    public Object[][] getLoginData() {
//        return new Object[][] {
//            {USER_ID, USER_PASSWORD, "success"},
//            {"invalid_user", "123456", "error"},
//            {USER_ID, "wrong_password", "error"},
//            {"", "123456", "error"},
//            {USER_ID, "", "error"}
//        };
//    }
//
//    @Test(priority = 8, dataProvider = "loginData", description = "08. Login - Test với DataProvider")
//    public void testLoginWithDataProvider(String customerId, String password, String expectedResult) {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Testing login with Customer ID: " + customerId + ", Expected: " + expectedResult);
//
//        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
//        loginPage.navigateToLoginPage();
//        loginPage.login(customerId, password);
//
//        if ("success".equals(expectedResult)) {
//            Assert.assertTrue(loginPage.isOnHomePage(),
//                "Đăng nhập thành công nhưng không redirect về trang chủ");
//            extentTest.log(com.aventstack.extentreports.Status.PASS, "Login successful");
//        } else {
//            boolean isError = loginPage.isErrorAlertDisplayed() || loginPage.isOnLoginPage();
//            Assert.assertTrue(isError,
//                "Không hiển thị lỗi khi đăng nhập với thông tin không hợp lệ");
//            extentTest.log(com.aventstack.extentreports.Status.PASS, "Error handled correctly");
//        }
//    }
//
//    // ==================== SHOPPING TESTS ====================
//
//    @Test(priority = 9, description = "09. Shop - Xem danh sách sản phẩm")
//    public void testViewProductsList() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test xem danh sách sản phẩm");
//
//        ShopPage shopPage = new ShopPage(driver);
//        shopPage.navigateToShopPage();
//
//        Assert.assertTrue(shopPage.isOnShopPage(),
//            "Không ở trang danh sách sản phẩm");
//
//        int productCount = shopPage.getProductCount();
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Số lượng sản phẩm: " + productCount);
//
//        Assert.assertTrue(productCount > 0,
//            "Không có sản phẩm nào được hiển thị");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Xem danh sách sản phẩm thành công");
//    }
//
//    @Test(priority = 10, description = "10. Shop - Tìm kiếm sản phẩm")
//    public void testSearchProduct() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test tìm kiếm sản phẩm");
//
//        ShopPage shopPage = new ShopPage(driver);
//        shopPage.searchProduct("test");
//
//        Assert.assertTrue(shopPage.isOnShopPage(),
//            "Không ở trang kết quả tìm kiếm");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Tìm kiếm sản phẩm thành công");
//    }
//
//    @Test(priority = 11, description = "11. Shop - Xem chi tiết sản phẩm")
//    public void testViewProductDetail() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test xem chi tiết sản phẩm");
//
//        ShopPage shopPage = new ShopPage(driver);
//        shopPage.navigateToShopPage();
//
//        int productCount = shopPage.getProductCount();
//        Assert.assertTrue(productCount > 0,
//            "Không có sản phẩm để xem chi tiết");
//
//        shopPage.clickFirstProduct();
//
//        String currentUrl = driver.getCurrentUrl();
//        Assert.assertTrue(currentUrl.contains("productDetail"),
//            "Không chuyển đến trang chi tiết sản phẩm");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Xem chi tiết sản phẩm thành công");
//    }
//
//    // ==================== SHOPPING CART TESTS ====================
//
//    @Test(priority = 12, description = "12. Cart - Xem giỏ hàng")
//    public void testViewShoppingCart() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test xem giỏ hàng");
//
//        loginAsUser();
//
//        ShoppingCartPage cartPage = new ShoppingCartPage(driver);
//        cartPage.navigateToCartPage();
//
//        Assert.assertTrue(cartPage.isOnCartPage(),
//            "Không ở trang giỏ hàng");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Xem giỏ hàng thành công");
//    }
//
//    @Test(priority = 13, description = "13. Cart - Thêm sản phẩm vào giỏ hàng")
//    public void testAddProductToCart() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test thêm sản phẩm vào giỏ hàng");
//
//        loginAsUser();
//
//        ShopPage shopPage = new ShopPage(driver);
//        shopPage.navigateToShopPage();
//
//        int initialCartCount = 0;
//        try {
//            ShoppingCartPage cartPage = new ShoppingCartPage(driver);
//            cartPage.navigateToCartPage();
//            initialCartCount = cartPage.getCartItemCount();
//        } catch (Exception e) {
//            // Cart might be empty
//        }
//
//        shopPage.navigateToShopPage();
//        shopPage.addFirstProductToCart();
//
//        ShoppingCartPage cartPage = new ShoppingCartPage(driver);
//        cartPage.navigateToCartPage();
//
//        int newCartCount = cartPage.getCartItemCount();
//        Assert.assertTrue(newCartCount > initialCartCount || newCartCount > 0,
//            "Sản phẩm không được thêm vào giỏ hàng");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Thêm sản phẩm vào giỏ hàng thành công");
//    }
//
//    @Test(priority = 14, description = "14. Cart - Cập nhật số lượng sản phẩm")
//    public void testUpdateCartQuantity() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test cập nhật số lượng sản phẩm");
//
//        loginAsUser();
//
//        ShopPage shopPage = new ShopPage(driver);
//        shopPage.navigateToShopPage();
//        shopPage.addFirstProductToCart();
//
//        ShoppingCartPage cartPage = new ShoppingCartPage(driver);
//        cartPage.navigateToCartPage();
//
//        int initialCount = cartPage.getCartItemCount();
//        Assert.assertTrue(initialCount > 0,
//            "Giỏ hàng không có sản phẩm để test");
//
//        cartPage.updateQuantity(0, 2);
//
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Cập nhật số lượng sản phẩm thành công");
//    }
//
//    @Test(priority = 15, description = "15. Checkout - Xem trang checkout")
//    public void testCheckoutWithItems() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test checkout");
//
//        loginAsUser();
//
//        ShopPage shopPage = new ShopPage(driver);
//        shopPage.navigateToShopPage();
//        shopPage.addFirstProductToCart();
//
//        CheckoutPage checkoutPage = new CheckoutPage(driver);
//        checkoutPage.navigateToCheckoutPage();
//
//        Assert.assertTrue(checkoutPage.isOnCheckoutPage(),
//            "Không ở trang checkout");
//
//        int itemCount = checkoutPage.getOrderItemCount();
//        Assert.assertTrue(itemCount > 0,
//            "Không có sản phẩm trong đơn hàng");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Checkout page hiển thị đúng với sản phẩm trong giỏ hàng");
//    }
//
//    @Test(priority = 16, description = "16. Checkout - Submit checkout")
//    public void testSubmitCheckout() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//            "Bắt đầu test submit checkout");
//
//        loginAsUser();
//
//        ShopPage shopPage = new ShopPage(driver);
//        shopPage.navigateToShopPage();
//        shopPage.addFirstProductToCart();
//
//        CheckoutPage checkoutPage = new CheckoutPage(driver);
//        checkoutPage.navigateToCheckoutPage();
//
//        checkoutPage.fillCheckoutForm(
//            "Test User",
//            "123 Test Street",
//            "0123456789",
//            "Test order description"
//        );
//
//        checkoutPage.submitCheckout();
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        String currentUrl = driver.getCurrentUrl();
//        Assert.assertTrue(currentUrl.contains("checkout_success") ||
//                         currentUrl.contains("success") ||
//                         currentUrl.contains("/"),
//            "Không redirect đến trang success sau khi checkout");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//            "Submit checkout thành công");
//    }
//}
//
