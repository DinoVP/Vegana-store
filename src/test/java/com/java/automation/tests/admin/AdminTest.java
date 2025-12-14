package com.java.automation.tests.admin;

import com.java.automation.base.BaseTest;
import com.java.automation.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Test Suite cho các chức năng Quản trị (TC11 - TC22) - KHẮC PHỤC LỖI TƯƠNG TÁC BẰNG JAVASCRIPT EXECUTOR.
 */
public class AdminTest extends BaseTest {

    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PASSWORD = "123123";
    private final Duration WAIT_TIME = Duration.ofSeconds(5);

    // Helper: Thực hiện click bằng JavaScript để khắc phục lỗi phần tử bị che/click không hiệu quả.
    private void clickElementByJs(By locator) {
        WebElement element = new WebDriverWait(driver, WAIT_TIME).until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Phương thức helper để thực hiện Đăng nhập Admin
    private void loginAsAdmin() {
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASSWORD);

        new WebDriverWait(driver, WAIT_TIME).until(
                ExpectedConditions.urlContains("/admin")
        );
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Đã thực hiện đăng nhập Admin thành công.");
    }

    // =======================================================
    // TC11, TC12 (Admin Authentication) - GIỮ NGUYÊN
    // =======================================================

    @Test(priority = 11, description = "TC11: Failed Admin Login (Sai mật khẩu)")
    public void testTC11FailedAdminLogin() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC11: Đăng nhập Admin thất bại.");
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, "saiadmin");
        Assert.assertTrue(loginPage.isErrorAlertDisplayed(), "TC11 FAILED: Không hiển thị thông báo lỗi khi Admin nhập sai mật khẩu.");
        String errorText = loginPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("không chính xác") || errorText.contains("sai") || errorText.contains("Invalid"), "TC11 FAILED: Thông báo lỗi không đúng.");
        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC11 PASSED: Đăng nhập sai mật khẩu Admin hiển thị lỗi đúng.");
    }

    @Test(priority = 12, description = "TC12: Successful Admin Login")
    public void testTC12SuccessfulAdminLogin() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC12: Đăng nhập Admin thành công.");
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASSWORD);
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin"), "TC12 FAILED: Đăng nhập Admin thành công nhưng không chuyển hướng đến Dashboard.");
        AdminPage adminPage = new AdminPage(driver);
        Assert.assertTrue(adminPage.isDashboardTitleDisplayed(), "TC12 FAILED: Không thấy tiêu đề Dashboard sau khi đăng nhập.");
        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC12 PASSED: Đăng nhập Admin thành công.");
    }

    // =======================================================
    // TC13 - TC17 (Truy cập các trang quản lý) - DÙNG JS ĐỂ CLICK CHẮC CHẮN
    // =======================================================

    /**
     * Helper để click vào liên kết menu trong thanh điều hướng Admin
     * Sử dụng JS Executor để vượt qua vấn đề bị che khuất.
     */
    private void navigateToAdminPage(String menuText, String expectedPageTitle, String expectedUrlSegment) {
        // XPath tìm liên kết dựa trên Text của menu
        String menuLinkXpath = String.format("//a[contains(., '%s')]", menuText);

        // DÙNG JS CLICK để đảm bảo thao tác được thực hiện
        clickElementByJs(By.xpath(menuLinkXpath));

        // Chờ tiêu đề trang tải xong
        new WebDriverWait(driver, WAIT_TIME).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[contains(@class, 'page-title') and contains(text(), '" + expectedPageTitle + "')]"))
        );

        // Kiểm tra URL ngay lập tức
        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrlSegment),
                "FAILED: URL không đúng sau khi chuyển hướng đến " + menuText);
    }

    @Test(priority = 13, description = "TC13: Category Management Access")
    public void testTC13CategoryManagementAccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC13: Truy cập Quản lý Danh mục.");
        loginAsAdmin();
        navigateToAdminPage("Category Management", "Category Management", "/admin/categories");
        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC13 PASSED: Truy cập Quản lý Danh mục thành công.");
    }

    @Test(priority = 14, description = "TC14: Supplier Management Access")
    public void testTC14SupplierManagementAccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC14: Truy cập Quản lý Nhà cung cấp.");
        loginAsAdmin();
        navigateToAdminPage("Supplier Management", "Supplier Management", "/admin/suppliers");
        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC14 PASSED: Truy cập Quản lý Nhà cung cấp thành công.");
    }

    @Test(priority = 15, description = "TC15: Product Management Access")
    public void testTC15ProductManagementAccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC15: Truy cập Quản lý Sản phẩm.");
        loginAsAdmin();
        navigateToAdminPage("Product Management", "Product Management", "/admin/products");
        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC15 PASSED: Truy cập Quản lý Sản phẩm thành công.");
    }

    @Test(priority = 16, description = "TC16: Order Management Access")
    public void testTC16OrderManagementAccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC16: Truy cập Quản lý Đơn hàng.");
        loginAsAdmin();
        navigateToAdminPage("Order Management", "Order Management", "/admin/orders");
        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC16 PASSED: Truy cập Quản lý Đơn hàng thành công.");
    }

    @Test(priority = 17, description = "TC17: Customer Management Access")
    public void testTC17CustomerManagementAccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC17: Truy cập Quản lý Khách hàng.");
        loginAsAdmin();
        navigateToAdminPage("Customer Management", "Customer Management", "/admin/customers");
        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC17 PASSED: Truy cập Quản lý Khách hàng thành công.");
    }

    // =======================================================
    // TC18 - TC22 (Kiểm thử Phân trang / Pagination) - DÙNG JS ĐỂ CLICK CHẮC CHẮN
    // =======================================================

    /**
     * Helper để thực hiện kiểm thử phân trang trên các trang Admin.
     */
    private void testPagination(String menuText, String navigationHref, String pageTitle, String pageIdentifierToClick, String expectedPageNumber) {
        loginAsAdmin();

        // 1. Điều hướng đến trang quản lý (Dùng Helper mới)
        navigateToAdminPage(menuText, pageTitle, navigationHref);

        // B2: Tìm và click vào số trang/nút mong muốn
        String xpathToPage = "";

        // Dựa trên cấu trúc DataTables: #add-row_paginate
        String paginationBaseXpath = "//div[@id='add-row_paginate']";

        if (pageIdentifierToClick.equalsIgnoreCase("Next")) {
            // XPath cho nút 'Next'
            xpathToPage = paginationBaseXpath + "//li[contains(@class, 'next') and not(contains(@class, 'disabled'))]/a";
        } else {
            // XPath cho số trang cụ thể
            xpathToPage = paginationBaseXpath + "//a[text()='" + pageIdentifierToClick + "']";
        }

        // DÙNG JS CLICK để đảm bảo thao tác được thực hiện
        clickElementByJs(By.xpath(xpathToPage));

        // B3: Chờ 1 giây để AJAX/JS load lại DataTable (rất quan trọng)
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // B4: Xác minh số trang đang active
        String activePageXpath = paginationBaseXpath + "//li[contains(@class, 'active')]/a[text()='" + expectedPageNumber + "']";

        // Chờ số trang mong muốn trở nên active
        new WebDriverWait(driver, WAIT_TIME).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(activePageXpath))
        );

        boolean isPageActive = driver.findElements(By.xpath(activePageXpath)).size() > 0;

        Assert.assertTrue(isPageActive,
                "FAILED: Phân trang không hoạt động đúng. Trang " + expectedPageNumber + " không được highlight là active.");
    }

    // TC18: Product Listing Pagination (Page 3)
    @Test(priority = 18, description = "TC18: Product Listing Pagination (Page 3)")
    public void testTC18ProductListingPagination() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC18: Phân trang Sản phẩm (click trang 3).");

        testPagination("Product Management", "/admin/products", "Product Management", "3", "3");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC18 PASSED: Phân trang Sản phẩm (trang 3) thành công.");
    }

    // TC19: Order Listing Pagination (Next) - Kỳ vọng chuyển sang trang 2
    @Test(priority = 19, description = "TC19: Order Listing Pagination (Next)")
    public void testTC19OrderListingPagination() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC19: Phân trang Đơn hàng (click Next).");

        testPagination("Order Management", "/admin/orders", "Order Management", "Next", "2");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC19 PASSED: Phân trang Đơn hàng (Next) thành công.");
    }

    // TC20: Customer Listing Pagination (Page 4)
    @Test(priority = 20, description = "TC20: Customer Listing Pagination (Page 4)")
    public void testTC20CustomerListingPagination() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC20: Phân trang Khách hàng (click trang 4).");

        testPagination("Customer Management", "/admin/customers", "Customer Management", "4", "4");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC20 PASSED: Phân trang Khách hàng (trang 4) thành công.");
    }

    // TC21: Category Listing Pagination (Page 2)
    @Test(priority = 21, description = "TC21: Category Listing Pagination (Page 2)")
    public void testTC21CategoryListingPagination() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC21: Phân trang Danh mục (click trang 2).");

        testPagination("Category Management", "/admin/categories", "Category Management", "2", "2");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC21 PASSED: Phân trang Danh mục (trang 2) thành công.");
    }

    // TC22: Supplier Listing Pagination (Next) - Kỳ vọng chuyển sang trang 2
    @Test(priority = 22, description = "TC22: Supplier Listing Pagination (Next)")
    public void testTC22SupplierListingPagination() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu test TC22: Phân trang Nhà cung cấp (click Next).");

        testPagination("Supplier Management", "/admin/suppliers", "Supplier Management", "Next", "2");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "TC22 PASSED: Phân trang Nhà cung cấp (Next) thành công.");
    }
}


//package com.java.automation.tests.admin;
//
//import com.java.automation.base.BaseTest;
//import com.java.automation.pages.CategoriesPage;
//import com.java.automation.pages.EditCategoryPage;
//import com.java.automation.pages.EditProductPage;
//import com.java.automation.pages.EditSupplierPage;
//import com.java.automation.pages.LoginOrRegisterPage;
//import com.java.automation.pages.ProductsPage;
//import com.java.automation.pages.SuppliersPage;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import java.nio.file.Paths;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
///**
// * All Admin Test Cases - Authentication, Dashboard, Navigation, CRUD Operations
// */
//public class AdminTest extends BaseTest {
//
//    private static final String ADMIN_USERNAME = "admin";
//    private static final String ADMIN_PASSWORD = "123123";
//
//    private String uniqueName(String prefix) {
//        return prefix + System.currentTimeMillis();
//    }
//
//    private void loginAsAdmin() {
//        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
//        loginPage.navigateToLoginPage();
//        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
//    }
//
//    // ==================== AUTH ====================
//
//    @Test(priority = 1, description = "01. Admin - Đăng nhập thành công")
//    public void testAdminLoginSuccess() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//                "Bắt đầu test đăng nhập admin với username: " + ADMIN_USERNAME);
//
//        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
//        loginPage.navigateToLoginPage();
//        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
//
//        String currentUrl = driver.getCurrentUrl();
//        Assert.assertTrue(currentUrl.contains("/admin/home"),
//                "Đăng nhập admin thành công nhưng không redirect về trang admin dashboard. URL hiện tại: " + currentUrl);
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//                "Đăng nhập admin thành công và đã redirect đến trang dashboard");
//    }
//
//    @Test(priority = 2, description = "02. Admin - Đăng nhập với mật khẩu sai")
//    public void testAdminLoginWithWrongPassword() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO,
//                "Bắt đầu test đăng nhập admin với mật khẩu sai");
//
//        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
//        loginPage.navigateToLoginPage();
//        loginPage.login(ADMIN_USERNAME, "wrong_password");
//
//        Assert.assertTrue(loginPage.isErrorAlertDisplayed(),
//                "Không hiển thị thông báo lỗi khi đăng nhập với mật khẩu sai");
//
//        String errorText = loginPage.getErrorAlertText();
//        Assert.assertTrue(errorText.contains("không chính xác") ||
//                        errorText.contains("Tài khoản") ||
//                        errorText.contains("sai"),
//                "Thông báo lỗi không đúng: " + errorText);
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS,
//                "Đăng nhập với mật khẩu sai đã hiển thị thông báo lỗi đúng");
//    }
//
//    // ==================== CRUD FLOWS ====================
//
//    @Test(priority = 3, description = "03. Categories - Full CRUD flow với data giả")
//    public void testCategoryCRUDFlow() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu CRUD category end-to-end");
//        loginAsAdmin();
//
//        CategoriesPage categoriesPage = new CategoriesPage(driver);
//        categoriesPage.navigateToCategoriesPage();
//
//        String categoryName = uniqueName("AutoCat-");
//        String updatedName = categoryName + "-Updated";
//
//        categoriesPage.createCategory(categoryName);
//        Assert.assertNotNull(categoriesPage.findCategoryRow(categoryName), "Không tìm thấy category vừa thêm");
//
//        categoriesPage.clickEditForCategory(categoryName);
//        EditCategoryPage editCategoryPage = new EditCategoryPage(driver);
//        editCategoryPage.updateCategoryName(updatedName);
//
//        categoriesPage = new CategoriesPage(driver);
//        categoriesPage.navigateToCategoriesPage();
//        Assert.assertNotNull(categoriesPage.findCategoryRow(updatedName), "Không thấy category sau khi update");
//
//        categoriesPage.deleteCategoryByName(updatedName);
//        Assert.assertNull(categoriesPage.findCategoryRow(updatedName), "Category chưa bị xóa");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS, "CRUD Category hoàn tất với data giả");
//    }
//
//    @Test(priority = 4, description = "04. Suppliers - Full CRUD flow với data giả")
//    public void testSupplierCRUDFlow() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu CRUD supplier end-to-end");
//        loginAsAdmin();
//
//        SuppliersPage suppliersPage = new SuppliersPage(driver);
//        suppliersPage.navigateToSuppliersPage();
//
//        String supplierName = uniqueName("AutoSupplier-");
//        String supplierEmail = supplierName.toLowerCase() + "@example.com";
//        String supplierPhone = "090" + (int) (Math.random() * 1000000);
//
//        suppliersPage.createSupplier(supplierName, supplierEmail, supplierPhone);
//        Assert.assertNotNull(suppliersPage.findSupplierRow(supplierName), "Không tìm thấy supplier vừa thêm");
//
//        suppliersPage.clickEditSupplier(supplierName);
//        EditSupplierPage editSupplierPage = new EditSupplierPage(driver);
//
//        String updatedName = supplierName + "-Updated";
//        String updatedEmail = updatedName.toLowerCase() + "@example.com";
//        String updatedPhone = "091" + (int) (Math.random() * 1000000);
//        editSupplierPage.updateSupplier(updatedName, updatedEmail, updatedPhone);
//
//        suppliersPage = new SuppliersPage(driver);
//        suppliersPage.navigateToSuppliersPage();
//        Assert.assertNotNull(suppliersPage.findSupplierRow(updatedName), "Không thấy supplier sau khi update");
//
//        suppliersPage.deleteSupplier(updatedName);
//        Assert.assertNull(suppliersPage.findSupplierRow(updatedName), "Supplier chưa bị xóa");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS, "CRUD Supplier hoàn tất với data giả");
//    }
//
//    @Test(priority = 5, description = "05. Products - Full CRUD flow với data giả")
//    public void testProductCRUDFlow() {
//        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu CRUD product end-to-end");
//        loginAsAdmin();
//
//        ProductsPage productsPage = new ProductsPage(driver);
//        productsPage.navigateToProductsPage();
//
//        String productName = uniqueName("AutoProduct-");
//        String price = "12345";  // DOUBLE
//        String quantity = "5";       // INT
//        String discount = "25";     // DOUBLE
//        String enteredDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE); // DATE format YYYY-MM-DD
//        String description = "Auto generated product for UI test"; // VARCHAR(255)
//        String imagePath = Paths.get(
//                System.getProperty("user.dir"),
//                "upload",
//                "image",
//                "coca-cola.jpg"
//        ).toString();
//
//        productsPage.createProduct(productName, price, quantity, discount, enteredDate, description, imagePath);
//        Assert.assertNotNull(productsPage.findProductRow(productName), "Không tìm thấy sản phẩm vừa thêm");
//
//        productsPage.clickEditProduct(productName);
//        EditProductPage editProductPage = new EditProductPage(driver);
//
//        String updatedName = productName + "-Updated";
//        String updatedPrice = "2222275";  // DOUBLE
//        String updatedQuantity = "7";       // INT
//        String updatedDiscount = "3";     // DOUBLE
//        String updatedDescription = "Updated auto product"; // VARCHAR(255)
//        editProductPage.updateProduct(updatedName, updatedPrice, updatedQuantity, updatedDiscount, enteredDate, updatedDescription, imagePath);
//
//        productsPage = new ProductsPage(driver);
//        productsPage.navigateToProductsPage();
//        Assert.assertNotNull(productsPage.findProductRow(updatedName), "Không thấy sản phẩm sau khi update");
//
//        productsPage.deleteProduct(updatedName);
//        Assert.assertNull(productsPage.findProductRow(updatedName), "Sản phẩm chưa bị xóa");
//
//        extentTest.log(com.aventstack.extentreports.Status.PASS, "CRUD Product hoàn tất với data giả");
//    }
//}
