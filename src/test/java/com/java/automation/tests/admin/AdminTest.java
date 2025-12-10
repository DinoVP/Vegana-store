package com.java.automation.tests.admin;

import com.java.automation.base.BaseTest;
import com.java.automation.pages.AdminPage;
import com.java.automation.pages.LoginOrRegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test cases for Admin Dashboard functionality
 */
public class AdminTest extends BaseTest {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123123";

    @Test(priority = 1, description = "Test đăng nhập admin thành công và chuyển đến trang dashboard")
    public void testAdminLoginSuccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng nhập admin với username: " + ADMIN_USERNAME);

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        
        // Đăng nhập với tài khoản admin
        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        
        // Verify redirect to admin dashboard
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/admin/home"), 
            "Đăng nhập admin thành công nhưng không redirect về trang admin dashboard. URL hiện tại: " + currentUrl);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Đăng nhập admin thành công và đã redirect đến trang dashboard");
    }

    @Test(priority = 2, description = "Test hiển thị các thành phần chính trên trang admin dashboard")
    public void testAdminDashboardElements() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test kiểm tra các thành phần trên trang admin dashboard");

        // Đăng nhập admin trước
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        
        // Khởi tạo AdminPage
        AdminPage adminPage = new AdminPage(driver);
        
        // Verify đang ở trang admin dashboard
        Assert.assertTrue(adminPage.isOnAdminDashboard(), 
            "Không ở trang admin dashboard");
        
        // Verify các thành phần chính
        Assert.assertTrue(adminPage.isDashboardTitleDisplayed(), 
            "Không hiển thị tiêu đề Dashboard");
        
        Assert.assertTrue(adminPage.isAdminDashboardSubtitleDisplayed(), 
            "Không hiển thị subtitle 'Admin Dashboard'");
        
        Assert.assertTrue(adminPage.isOverallStatisticsCardDisplayed(), 
            "Không hiển thị card 'Overall statistics'");
        
        Assert.assertTrue(adminPage.isTotalIncomeCardDisplayed(), 
            "Không hiển thị card 'Total income & spend statistics'");
        
        Assert.assertTrue(adminPage.isUserStatisticsCardDisplayed(), 
            "Không hiển thị card 'User Statistics'");
        
        Assert.assertTrue(adminPage.isDailySalesCardDisplayed(), 
            "Không hiển thị card 'Daily Sales'");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Tất cả các thành phần chính trên trang admin dashboard đều hiển thị đúng");
    }

    @Test(priority = 3, description = "Test hiển thị role Administrator trên navbar")
    public void testAdminRoleDisplay() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test kiểm tra hiển thị role Administrator");

        // Đăng nhập admin
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        
        AdminPage adminPage = new AdminPage(driver);
        
        // Verify role Administrator được hiển thị
        Assert.assertTrue(adminPage.isAdministratorRoleDisplayed(), 
            "Không hiển thị role Administrator trên navbar");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Role Administrator được hiển thị đúng trên navbar");
    }

    @Test(priority = 4, description = "Test đăng nhập admin với mật khẩu sai")
    public void testAdminLoginWithWrongPassword() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng nhập admin với mật khẩu sai");

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        
        // Đăng nhập với mật khẩu sai
        loginPage.login(ADMIN_USERNAME, "wrong_password");
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorAlertDisplayed(), 
            "Không hiển thị thông báo lỗi khi đăng nhập với mật khẩu sai");
        
        String errorText = loginPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("không chính xác") || 
                         errorText.contains("Tài khoản") ||
                         errorText.contains("sai"), 
            "Thông báo lỗi không đúng: " + errorText);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Đăng nhập với mật khẩu sai đã hiển thị thông báo lỗi đúng");
    }
}

