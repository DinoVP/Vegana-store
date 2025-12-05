package com.java.automation.tests.admin;

import com.java.automation.base.BaseTest;
import com.java.automation.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test cases for Admin Functions - Products, Orders, Customers, Categories, Suppliers
 */
public class AdminFunctionsTest extends BaseTest {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123123";

    /**
     * Helper method to login as admin
     */
    private void loginAsAdmin() {
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        
        // Wait for redirect
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ==================== PRODUCTS TESTS ====================

    @Test(priority = 1, description = "Test navigation đến trang Products Management")
    public void testNavigateToProductsPage() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigation đến trang Products Management");

        loginAsAdmin();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        
        Assert.assertTrue(productsPage.isOnProductsPage(), 
            "Không ở trang Products Management");
        Assert.assertTrue(productsPage.isPageTitleDisplayed(), 
            "Không hiển thị page title");
        Assert.assertTrue(productsPage.isCardTitleDisplayed(), 
            "Không hiển thị card title");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Navigation đến trang Products Management thành công");
    }

    @Test(priority = 2, description = "Test hiển thị các thành phần trên trang Products")
    public void testProductsPageElements() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test các thành phần trên trang Products");

        loginAsAdmin();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        
        Assert.assertTrue(productsPage.isAddProductButtonDisplayed(), 
            "Không hiển thị nút Add Product");
        Assert.assertTrue(productsPage.isProductsTableDisplayed(), 
            "Không hiển thị bảng danh sách sản phẩm");
        
        int productCount = productsPage.getProductCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng sản phẩm: " + productCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Tất cả các thành phần trên trang Products đều hiển thị đúng");
    }

    @Test(priority = 3, description = "Test click nút Add Product")
    public void testClickAddProductButton() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test click nút Add Product");

        loginAsAdmin();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        
        productsPage.clickAddProductButton();
        
        // Wait for modal to appear
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Click nút Add Product thành công");
    }

    // ==================== ORDERS TESTS ====================

    @Test(priority = 4, description = "Test navigation đến trang Orders Management")
    public void testNavigateToOrdersPage() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigation đến trang Orders Management");

        loginAsAdmin();
        
        OrdersPage ordersPage = new OrdersPage(driver);
        ordersPage.navigateToOrdersPage();
        
        Assert.assertTrue(ordersPage.isOnOrdersPage(), 
            "Không ở trang Orders Management");
        Assert.assertTrue(ordersPage.isPageTitleDisplayed(), 
            "Không hiển thị page title");
        Assert.assertTrue(ordersPage.isCardTitleDisplayed(), 
            "Không hiển thị card title");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Navigation đến trang Orders Management thành công");
    }

    @Test(priority = 5, description = "Test hiển thị các thành phần trên trang Orders")
    public void testOrdersPageElements() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test các thành phần trên trang Orders");

        loginAsAdmin();
        
        OrdersPage ordersPage = new OrdersPage(driver);
        ordersPage.navigateToOrdersPage();
        
        Assert.assertTrue(ordersPage.isExportToExcelLinkDisplayed(), 
            "Không hiển thị link Export To Excel");
        Assert.assertTrue(ordersPage.isOrdersTableDisplayed(), 
            "Không hiển thị bảng danh sách đơn hàng");
        
        int orderCount = ordersPage.getOrderCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng đơn hàng: " + orderCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Tất cả các thành phần trên trang Orders đều hiển thị đúng");
    }

    // ==================== CUSTOMERS TESTS ====================

    @Test(priority = 6, description = "Test navigation đến trang Customers Management")
    public void testNavigateToCustomersPage() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigation đến trang Customers Management");

        loginAsAdmin();
        
        CustomersPage customersPage = new CustomersPage(driver);
        customersPage.navigateToCustomersPage();
        
        Assert.assertTrue(customersPage.isOnCustomersPage(), 
            "Không ở trang Customers Management");
        Assert.assertTrue(customersPage.isPageTitleDisplayed(), 
            "Không hiển thị page title");
        Assert.assertTrue(customersPage.isCardTitleDisplayed(), 
            "Không hiển thị card title");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Navigation đến trang Customers Management thành công");
    }

    @Test(priority = 7, description = "Test hiển thị các thành phần trên trang Customers")
    public void testCustomersPageElements() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test các thành phần trên trang Customers");

        loginAsAdmin();
        
        CustomersPage customersPage = new CustomersPage(driver);
        customersPage.navigateToCustomersPage();
        
        Assert.assertTrue(customersPage.isAddCustomerButtonDisplayed(), 
            "Không hiển thị nút Thêm mới");
        Assert.assertTrue(customersPage.isCustomersTableDisplayed(), 
            "Không hiển thị bảng danh sách khách hàng");
        
        int customerCount = customersPage.getCustomerCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng khách hàng: " + customerCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Tất cả các thành phần trên trang Customers đều hiển thị đúng");
    }

    // ==================== CATEGORIES TESTS ====================

    @Test(priority = 8, description = "Test navigation đến trang Categories Management")
    public void testNavigateToCategoriesPage() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigation đến trang Categories Management");

        loginAsAdmin();
        
        CategoriesPage categoriesPage = new CategoriesPage(driver);
        categoriesPage.navigateToCategoriesPage();
        
        Assert.assertTrue(categoriesPage.isOnCategoriesPage(), 
            "Không ở trang Categories Management");
        Assert.assertTrue(categoriesPage.isPageTitleDisplayed(), 
            "Không hiển thị page title");
        Assert.assertTrue(categoriesPage.isCardTitleDisplayed(), 
            "Không hiển thị card title");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Navigation đến trang Categories Management thành công");
    }

    @Test(priority = 9, description = "Test hiển thị các thành phần trên trang Categories")
    public void testCategoriesPageElements() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test các thành phần trên trang Categories");

        loginAsAdmin();
        
        CategoriesPage categoriesPage = new CategoriesPage(driver);
        categoriesPage.navigateToCategoriesPage();
        
        Assert.assertTrue(categoriesPage.isAddCategoryButtonDisplayed(), 
            "Không hiển thị nút Add Category");
        Assert.assertTrue(categoriesPage.isCategoriesTableDisplayed(), 
            "Không hiển thị bảng danh sách danh mục");
        
        int categoryCount = categoriesPage.getCategoryCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng danh mục: " + categoryCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Tất cả các thành phần trên trang Categories đều hiển thị đúng");
    }

    // ==================== SUPPLIERS TESTS ====================

    @Test(priority = 10, description = "Test navigation đến trang Suppliers Management")
    public void testNavigateToSuppliersPage() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigation đến trang Suppliers Management");

        loginAsAdmin();
        
        SuppliersPage suppliersPage = new SuppliersPage(driver);
        suppliersPage.navigateToSuppliersPage();
        
        Assert.assertTrue(suppliersPage.isOnSuppliersPage(), 
            "Không ở trang Suppliers Management");
        Assert.assertTrue(suppliersPage.isPageTitleDisplayed(), 
            "Không hiển thị page title");
        Assert.assertTrue(suppliersPage.isCardTitleDisplayed(), 
            "Không hiển thị card title");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Navigation đến trang Suppliers Management thành công");
    }

    @Test(priority = 11, description = "Test hiển thị các thành phần trên trang Suppliers")
    public void testSuppliersPageElements() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test các thành phần trên trang Suppliers");

        loginAsAdmin();
        
        SuppliersPage suppliersPage = new SuppliersPage(driver);
        suppliersPage.navigateToSuppliersPage();
        
        Assert.assertTrue(suppliersPage.isAddSupplierButtonDisplayed(), 
            "Không hiển thị nút Add Supplier");
        Assert.assertTrue(suppliersPage.isSuppliersTableDisplayed(), 
            "Không hiển thị bảng danh sách nhà cung cấp");
        
        int supplierCount = suppliersPage.getSupplierCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng nhà cung cấp: " + supplierCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Tất cả các thành phần trên trang Suppliers đều hiển thị đúng");
    }

    // ==================== NAVIGATION TESTS ====================

    @Test(priority = 12, description = "Test navigation giữa các trang admin từ dashboard")
    public void testNavigationBetweenAdminPages() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigation giữa các trang admin");

        loginAsAdmin();
        
        AdminPage adminPage = new AdminPage(driver);
        
        // Test navigation to Products
        adminPage.clickProductsLink();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin/products"), 
            "Navigation đến Products không thành công");
        
        // Test navigation to Orders
        adminPage = new AdminPage(driver);
        adminPage.clickOrdersLink();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin/orders"), 
            "Navigation đến Orders không thành công");
        
        // Test navigation to Customers
        adminPage = new AdminPage(driver);
        adminPage.clickCustomersLink();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin/customers"), 
            "Navigation đến Customers không thành công");
        
        // Test navigation to Categories
        adminPage = new AdminPage(driver);
        adminPage.clickCategoriesLink();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin/categories"), 
            "Navigation đến Categories không thành công");
        
        // Test navigation to Suppliers
        adminPage = new AdminPage(driver);
        adminPage.clickSuppliersLink();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin/suppliers"), 
            "Navigation đến Suppliers không thành công");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Navigation giữa các trang admin thành công");
    }
}

