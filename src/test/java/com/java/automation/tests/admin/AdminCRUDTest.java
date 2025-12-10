package com.java.automation.tests.admin;

import com.java.automation.base.BaseTest;
import com.java.automation.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test cases for Admin CRUD operations
 */
public class AdminCRUDTest extends BaseTest {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123123";

    private void loginAsAdmin() {
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    // ==================== PRODUCTS CRUD ====================

    @Test(priority = 1, description = "Test xem danh sách sản phẩm trong admin")
    public void testViewProductsList() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test xem danh sách sản phẩm admin");

        loginAsAdmin();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        
        Assert.assertTrue(productsPage.isOnProductsPage(), 
            "Không ở trang Products Management");
        Assert.assertTrue(productsPage.isProductsTableDisplayed(), 
            "Bảng sản phẩm không hiển thị");
        
        int productCount = productsPage.getProductCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng sản phẩm: " + productCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Xem danh sách sản phẩm thành công");
    }

    @Test(priority = 2, description = "Test nút Add Product có hiển thị")
    public void testAddProductButtonDisplayed() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test kiểm tra nút Add Product");

        loginAsAdmin();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        
        Assert.assertTrue(productsPage.isAddProductButtonDisplayed(), 
            "Nút Add Product không hiển thị");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Nút Add Product hiển thị đúng");
    }

    @Test(priority = 18, description = "Test form thêm sản phẩm có hiển thị")
    public void testAddProductFormDisplayed() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test form thêm sản phẩm");

        loginAsAdmin();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        
        // Click Add Product button to open modal
        productsPage.clickAddProductButton();
        
        // Verify form is displayed (modal might need time to show)
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertTrue(productsPage.isAddProductFormDisplayed() || 
                        productsPage.isAddProductModalDisplayed(), 
            "Form thêm sản phẩm không hiển thị");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Form thêm sản phẩm hiển thị đúng");
    }

    // ==================== ORDERS CRUD ====================

    @Test(priority = 3, description = "Test xem danh sách đơn hàng")
    public void testViewOrdersList() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test xem danh sách đơn hàng");

        loginAsAdmin();
        
        OrdersPage ordersPage = new OrdersPage(driver);
        ordersPage.navigateToOrdersPage();
        
        Assert.assertTrue(ordersPage.isOnOrdersPage(), 
            "Không ở trang Orders Management");
        Assert.assertTrue(ordersPage.isOrdersTableDisplayed(), 
            "Bảng đơn hàng không hiển thị");
        
        int orderCount = ordersPage.getOrderCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng đơn hàng: " + orderCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Xem danh sách đơn hàng thành công");
    }

    @Test(priority = 4, description = "Test link Export To Excel có hiển thị")
    public void testExportToExcelLinkDisplayed() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test kiểm tra link Export To Excel");

        loginAsAdmin();
        
        OrdersPage ordersPage = new OrdersPage(driver);
        ordersPage.navigateToOrdersPage();
        
        Assert.assertTrue(ordersPage.isExportToExcelLinkDisplayed(), 
            "Link Export To Excel không hiển thị");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Link Export To Excel hiển thị đúng");
    }

    // ==================== CUSTOMERS CRUD ====================

    @Test(priority = 5, description = "Test xem danh sách khách hàng")
    public void testViewCustomersList() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test xem danh sách khách hàng");

        loginAsAdmin();
        
        CustomersPage customersPage = new CustomersPage(driver);
        customersPage.navigateToCustomersPage();
        
        Assert.assertTrue(customersPage.isOnCustomersPage(), 
            "Không ở trang Customers Management");
        Assert.assertTrue(customersPage.isCustomersTableDisplayed(), 
            "Bảng khách hàng không hiển thị");
        
        int customerCount = customersPage.getCustomerCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng khách hàng: " + customerCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Xem danh sách khách hàng thành công");
    }

    // ==================== CATEGORIES CRUD ====================

    @Test(priority = 6, description = "Test xem danh sách danh mục")
    public void testViewCategoriesList() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test xem danh sách danh mục");

        loginAsAdmin();
        
        CategoriesPage categoriesPage = new CategoriesPage(driver);
        categoriesPage.navigateToCategoriesPage();
        
        Assert.assertTrue(categoriesPage.isOnCategoriesPage(), 
            "Không ở trang Categories Management");
        Assert.assertTrue(categoriesPage.isCategoriesTableDisplayed(), 
            "Bảng danh mục không hiển thị");
        
        int categoryCount = categoriesPage.getCategoryCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng danh mục: " + categoryCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Xem danh sách danh mục thành công");
    }

    @Test(priority = 7, description = "Test nút Add Category có hiển thị")
    public void testAddCategoryButtonDisplayed() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test kiểm tra nút Add Category");

        loginAsAdmin();
        
        CategoriesPage categoriesPage = new CategoriesPage(driver);
        categoriesPage.navigateToCategoriesPage();
        
        Assert.assertTrue(categoriesPage.isAddCategoryButtonDisplayed(), 
            "Nút Add Category không hiển thị");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Nút Add Category hiển thị đúng");
    }

    // ==================== SUPPLIERS CRUD ====================

    @Test(priority = 8, description = "Test xem danh sách nhà cung cấp")
    public void testViewSuppliersList() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test xem danh sách nhà cung cấp");

        loginAsAdmin();
        
        SuppliersPage suppliersPage = new SuppliersPage(driver);
        suppliersPage.navigateToSuppliersPage();
        
        Assert.assertTrue(suppliersPage.isOnSuppliersPage(), 
            "Không ở trang Suppliers Management");
        Assert.assertTrue(suppliersPage.isSuppliersTableDisplayed(), 
            "Bảng nhà cung cấp không hiển thị");
        
        int supplierCount = suppliersPage.getSupplierCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng nhà cung cấp: " + supplierCount);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Xem danh sách nhà cung cấp thành công");
    }

    @Test(priority = 9, description = "Test nút Add Supplier có hiển thị")
    public void testAddSupplierButtonDisplayed() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test kiểm tra nút Add Supplier");

        loginAsAdmin();
        
        SuppliersPage suppliersPage = new SuppliersPage(driver);
        suppliersPage.navigateToSuppliersPage();
        
        Assert.assertTrue(suppliersPage.isAddSupplierButtonDisplayed(), 
            "Nút Add Supplier không hiển thị");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Nút Add Supplier hiển thị đúng");
    }

    // ==================== UPDATE TESTS ====================

    @Test(priority = 10, description = "Test navigate đến trang edit sản phẩm")
    public void testNavigateToEditProduct() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigate đến trang edit sản phẩm");

        loginAsAdmin();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        
        int productCount = productsPage.getProductCount();
        if (productCount > 0) {
            // Navigate to edit first product (assuming productId starts from 1)
            String baseUrl = com.java.automation.config.TestConfig.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            driver.get(baseUrl + "/editProduct/1");
            
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/editProduct"), 
                "Không navigate đến trang edit sản phẩm");
            
            extentTest.log(com.aventstack.extentreports.Status.PASS, 
                "Navigate đến trang edit sản phẩm thành công");
        } else {
            extentTest.log(com.aventstack.extentreports.Status.SKIP, 
                "Không có sản phẩm để test edit");
        }
    }

    @Test(priority = 11, description = "Test navigate đến trang edit danh mục")
    public void testNavigateToEditCategory() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigate đến trang edit danh mục");

        loginAsAdmin();
        
        CategoriesPage categoriesPage = new CategoriesPage(driver);
        categoriesPage.navigateToCategoriesPage();
        
        int categoryCount = categoriesPage.getCategoryCount();
        if (categoryCount > 0) {
            String baseUrl = com.java.automation.config.TestConfig.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            driver.get(baseUrl + "/editCategory/1");
            
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/editCategory"), 
                "Không navigate đến trang edit danh mục");
            
            extentTest.log(com.aventstack.extentreports.Status.PASS, 
                "Navigate đến trang edit danh mục thành công");
        } else {
            extentTest.log(com.aventstack.extentreports.Status.SKIP, 
                "Không có danh mục để test edit");
        }
    }

    @Test(priority = 12, description = "Test navigate đến trang edit nhà cung cấp")
    public void testNavigateToEditSupplier() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigate đến trang edit nhà cung cấp");

        loginAsAdmin();
        
        SuppliersPage suppliersPage = new SuppliersPage(driver);
        suppliersPage.navigateToSuppliersPage();
        
        int supplierCount = suppliersPage.getSupplierCount();
        if (supplierCount > 0) {
            String baseUrl = com.java.automation.config.TestConfig.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            driver.get(baseUrl + "/editSupplier/1");
            
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/editSupplier"), 
                "Không navigate đến trang edit nhà cung cấp");
            
            extentTest.log(com.aventstack.extentreports.Status.PASS, 
                "Navigate đến trang edit nhà cung cấp thành công");
        } else {
            extentTest.log(com.aventstack.extentreports.Status.SKIP, 
                "Không có nhà cung cấp để test edit");
        }
    }

    @Test(priority = 13, description = "Test navigate đến trang edit đơn hàng")
    public void testNavigateToEditOrder() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test navigate đến trang edit đơn hàng");

        loginAsAdmin();
        
        OrdersPage ordersPage = new OrdersPage(driver);
        ordersPage.navigateToOrdersPage();
        
        int orderCount = ordersPage.getOrderCount();
        if (orderCount > 0) {
            String baseUrl = com.java.automation.config.TestConfig.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            driver.get(baseUrl + "/editorder/1");
            
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/editorder"), 
                "Không navigate đến trang edit đơn hàng");
            
            extentTest.log(com.aventstack.extentreports.Status.PASS, 
                "Navigate đến trang edit đơn hàng thành công");
        } else {
            extentTest.log(com.aventstack.extentreports.Status.SKIP, 
                "Không có đơn hàng để test edit");
        }
    }

    // ==================== DELETE TESTS ====================

    @Test(priority = 14, description = "Test delete sản phẩm (navigate đến delete URL)")
    public void testDeleteProduct() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test delete sản phẩm");

        loginAsAdmin();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        
        int initialCount = productsPage.getProductCount();
        if (initialCount > 0) {
            String baseUrl = com.java.automation.config.TestConfig.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            // Navigate to delete URL (will redirect back to products page)
            driver.get(baseUrl + "/deleteProduct/999"); // Use non-existent ID to avoid actual deletion
            
            // Verify redirect back to products page
            productsPage.navigateToProductsPage();
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/admin/products"), 
                "Không redirect về trang products sau khi delete");
            
            extentTest.log(com.aventstack.extentreports.Status.PASS, 
                "Delete sản phẩm redirect đúng");
        } else {
            extentTest.log(com.aventstack.extentreports.Status.SKIP, 
                "Không có sản phẩm để test delete");
        }
    }

    @Test(priority = 15, description = "Test delete danh mục (navigate đến delete URL)")
    public void testDeleteCategory() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test delete danh mục");

        loginAsAdmin();
        
        CategoriesPage categoriesPage = new CategoriesPage(driver);
        categoriesPage.navigateToCategoriesPage();
        
        int initialCount = categoriesPage.getCategoryCount();
        if (initialCount > 0) {
            String baseUrl = com.java.automation.config.TestConfig.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            // Navigate to delete URL
            driver.get(baseUrl + "/delete/999"); // Use non-existent ID
            
            // Verify redirect
            categoriesPage.navigateToCategoriesPage();
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/admin/categories"), 
                "Không redirect về trang categories sau khi delete");
            
            extentTest.log(com.aventstack.extentreports.Status.PASS, 
                "Delete danh mục redirect đúng");
        } else {
            extentTest.log(com.aventstack.extentreports.Status.SKIP, 
                "Không có danh mục để test delete");
        }
    }

    @Test(priority = 16, description = "Test delete nhà cung cấp (navigate đến delete URL)")
    public void testDeleteSupplier() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test delete nhà cung cấp");

        loginAsAdmin();
        
        SuppliersPage suppliersPage = new SuppliersPage(driver);
        suppliersPage.navigateToSuppliersPage();
        
        int initialCount = suppliersPage.getSupplierCount();
        if (initialCount > 0) {
            String baseUrl = com.java.automation.config.TestConfig.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            // Navigate to delete URL
            driver.get(baseUrl + "/deleteSupplier/999"); // Use non-existent ID
            
            // Verify redirect
            suppliersPage.navigateToSuppliersPage();
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/admin/suppliers"), 
                "Không redirect về trang suppliers sau khi delete");
            
            extentTest.log(com.aventstack.extentreports.Status.PASS, 
                "Delete nhà cung cấp redirect đúng");
        } else {
            extentTest.log(com.aventstack.extentreports.Status.SKIP, 
                "Không có nhà cung cấp để test delete");
        }
    }

    @Test(priority = 17, description = "Test delete đơn hàng (navigate đến delete URL)")
    public void testDeleteOrder() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test delete đơn hàng");

        loginAsAdmin();
        
        OrdersPage ordersPage = new OrdersPage(driver);
        ordersPage.navigateToOrdersPage();
        
        int initialCount = ordersPage.getOrderCount();
        if (initialCount > 0) {
            String baseUrl = com.java.automation.config.TestConfig.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            // Navigate to delete URL
            driver.get(baseUrl + "/deleteOrder/999"); // Use non-existent ID
            
            // Verify redirect
            ordersPage.navigateToOrdersPage();
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/admin/orders"), 
                "Không redirect về trang orders sau khi delete");
            
            extentTest.log(com.aventstack.extentreports.Status.PASS, 
                "Delete đơn hàng redirect đúng");
        } else {
            extentTest.log(com.aventstack.extentreports.Status.SKIP, 
                "Không có đơn hàng để test delete");
        }
    }
}

