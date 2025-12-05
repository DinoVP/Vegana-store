package com.java.automation.tests.admin;

import com.java.automation.base.BaseTest;
import com.java.automation.config.TestConfig;
import com.java.automation.pages.AdminDashboardPage;
import com.java.automation.pages.LoginOrRegisterPage;
import com.java.automation.pages.ManagementTablePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminManagementTest extends BaseTest {

    private LoginOrRegisterPage loginPage;
    private AdminDashboardPage adminPage;
    private ManagementTablePage tablePage;

    private final String ADMIN_ID = TestConfig.getProperty("test.admin.id");
    private final String ADMIN_PASS = TestConfig.getProperty("test.admin.password");

    // FIX: SỬ DỤNG @BeforeMethod để setup Page Objects
    @BeforeMethod
    public void setupAdminPages() {
        loginPage = new LoginOrRegisterPage(driver);
        adminPage = new AdminDashboardPage(driver);
        tablePage = new ManagementTablePage(driver);
    }

    // --- TC14: Đăng nhập thành công Admin ---
    @Test(priority = 1, description = "TC14: Đăng nhập Admin thành công")
    public void TC14_verifyAdminLoginSuccess() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);

        Assert.assertTrue(loginPage.isAdminDashboardDisplayed(),
                "Lỗi TC14: Không chuyển hướng đến Admin Dashboard.");

        // YÊU CẦU: GIỮ TRÌNH DUYỆT (set shouldQuit = false)
        if (driver != null) {
            setShouldQuit(false);
        }
    }

    // --- TC15: Đăng nhập thất bại Admin ---
    @Test(priority = 2, description = "TC15: Đăng nhập Admin thất bại (Sai pass)")
    public void TC15_verifyAdminLoginFailure() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, "sai_mat_khau_ne");

        Assert.assertTrue(loginPage.isErrorAlertDisplayed(),
                "Lỗi TC15: Không hiển thị thông báo lỗi.");

        String errorText = loginPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("không chính xác"), "Thông báo lỗi không đúng.");
    }

    // --- TC16: Tìm kiếm Customer ID hợp lệ ---
    @Test(priority = 3, description = "TC16: Tìm kiếm Customer ID hợp lệ ('admin')", dependsOnMethods = {"TC14_verifyAdminLoginSuccess"})
    public void TC16_verifyCustomerSearch() {
        // LƯU Ý: Vì @BeforeMethod mở lại trang BASE_URL, ta cần đăng nhập lại Admin
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);
        setShouldQuit(false); // Giữ lại trang nếu test pass

        adminPage.navigateToCustomerManagement();
        String searchId = "admin";

        tablePage.search(searchId);

        Assert.assertEquals(tablePage.getFirstResultText(), searchId,
                "Lỗi TC16: Kết quả tìm kiếm Customer không khớp.");
    }

    // --- TC17: Tìm kiếm Supplier tên hợp lệ ---
    @Test(priority = 4, description = "TC17: Tìm kiếm Supplier tên hợp lệ ('Vinamilk')", dependsOnMethods = {"TC14_verifyAdminLoginSuccess"})
    public void TC17_verifySupplierSearch() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);
        setShouldQuit(false);

        adminPage.navigateToSupplierManagement();
        String searchName = "Vinamilk";

        tablePage.search(searchName);

        Assert.assertTrue(tablePage.getFirstResultText().contains(searchName),
                "Lỗi TC17: Kết quả tìm kiếm Supplier không khớp.");
    }

    // --- TC18: Tìm kiếm Category không tồn tại ---
    @Test(priority = 5, description = "TC18: Tìm kiếm Category không tồn tại ('XYZ_nonexist')", dependsOnMethods = {"TC14_verifyAdminLoginSuccess"})
    public void TC18_verifyCategorySearchNoResult() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);
        setShouldQuit(false);

        adminPage.navigateToCategoryManagement();

        tablePage.search("XYZ_nonexist");

        Assert.assertTrue(tablePage.isNoRecordsFoundDisplayed(),
                "Lỗi TC18: Không hiển thị thông báo 'No matching records found'.");
    }

    // --- TC19: Kiểm tra nút Next trên phân trang Supplier ---
    @Test(priority = 6, description = "TC19: Kiểm tra phân trang Next (Supplier)", dependsOnMethods = {"TC14_verifyAdminLoginSuccess"})
    public void TC19_verifyPaginationNext() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);
        setShouldQuit(false);

        adminPage.navigateToSupplierManagement();

        tablePage.clickNext();

        Assert.assertTrue(tablePage.isPageActive("2"),
                "Lỗi TC19: Click Next không chuyển sang trang 2.");
    }

    // --- TC20: Kiểm tra click số 2 trên phân trang Order ---
    @Test(priority = 7, description = "TC20: Kiểm tra phân trang click trang 2 (Order)", dependsOnMethods = {"TC14_verifyAdminLoginSuccess"})
    public void TC20_verifyPaginationPageTwo() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);
        setShouldQuit(false);

        adminPage.navigateToOrderManagement();

        tablePage.clickPageTwo();

        Assert.assertTrue(tablePage.isPageActive("2"),
                "Lỗi TC20: Click số 2 không chuyển sang trang 2.");
    }

    // --- TC21: Kiểm tra nút Previous trên phân trang Customer ---
    @Test(priority = 8, description = "TC21: Kiểm tra phân trang Previous (Customer)", dependsOnMethods = {"TC14_verifyAdminLoginSuccess"})
    public void TC21_verifyPaginationPrevious() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);
        setShouldQuit(false);

        adminPage.navigateToCustomerManagement();

        // Đảm bảo đang ở trang 2 trước khi click Previous
        tablePage.clickNext();

        tablePage.clickPrevious();

        Assert.assertTrue(tablePage.isPageActive("1"),
                "Lỗi TC21: Click Previous không quay về trang 1.");
    }

    // --- TC22: Kiểm tra nút Export To Excel trong Order Management ---
    @Test(priority = 9, description = "TC22: Kiểm tra nút Export To Excel (Order)", dependsOnMethods = {"TC14_verifyAdminLoginSuccess"})
    public void TC22_verifyExportToExcel() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);
        setShouldQuit(false);

        adminPage.navigateToOrderManagement();

        tablePage.clickExportToExcel();

        Assert.assertTrue(driver.getCurrentUrl().contains("Order Management"),
                "Lỗi TC22: Sau khi click Export, trang web chuyển hướng không đúng.");
    }

    @Test(priority = 10, description = "TC26: Kiểm tra tiêu đề 'Home Page' trong menu Admin", dependsOnMethods = {"TC14_verifyAdminLoginSuccess"})
    public void TC26_verifyAdminHomePageTitle() {
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_ID, ADMIN_PASS);
        setShouldQuit(false);

        Assert.assertTrue(adminPage.isHomePageMenuTitleDisplayed(),
                "Lỗi TC26: Tiêu đề 'Home Page' trong menu bên trái Admin không hiển thị.");
    }

}