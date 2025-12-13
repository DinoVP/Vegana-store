package com.java.automation.base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.java.automation.pages.*; // Cần import tất cả Page Objects
import com.java.automation.utils.ExtentReportManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * BaseTest chuẩn cho Selenium 4 + TestNG + Extent Report năm 2025 (ĐÃ FIX LỖI INIT PO)
 */
public class BaseTest {

    protected WebDriver driver;
    protected ExtentTest extentTest;

    // KHAI BÁO TẤT CẢ PAGE OBJECTS (protected) - FIX LỖI DRIVER NULL
    protected LoginOrRegisterPage loginPage;
    protected CategoriesPage categoriesPage;
    protected EditCategoryPage editCategoryPage;
    protected SuppliersPage suppliersPage;
    protected EditSupplierPage editSupplierPage;
    protected ProductsPage productsPage;
    protected EditProductPage editProductPage;
    // Thêm các PO khác nếu cần cho AdminTest
    // Ví dụ:
    // protected AdminPage adminDashboardPage;
    // protected OrdersPage ordersPage;
    // protected CustomersPage customersPage;


    // ============================
    // CONFIG MẶC ĐỊNH
    // ============================
    private final String BASE_URL = "http://localhost:9090";
    private final int IMPLICIT_WAIT = 2;
    private final int PAGELOAD_WAIT = 5;

    private final boolean IS_GITHUB =
            System.getenv("GITHUB_ACTIONS") != null;   // auto detect pipeline

    @BeforeSuite
    public void setupSuite() {
        ExtentReportManager.getInstance();
        System.out.println(">>> TEST SUITE STARTED");
    }

    @BeforeMethod
    public void setup(Method method) {

        String testName = method.getName();
        extentTest = ExtentReportManager.createTest(testName, "Running test: " + testName);

        driver = setupBrowser("chrome");

        // Timeouts
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(PAGELOAD_WAIT));

        // KHỞI TẠO PAGE OBJECTS SAU KHI DRIVER ĐÃ SẴN SÀNG (FIX LỖI)
        initializePageObjects();

        driver.get(BASE_URL);
        extentTest.log(Status.INFO, "Navigate to: " + BASE_URL);
    }

    // PHƯƠNG THỨC KHỞI TẠO PAGE OBJECTS
    private void initializePageObjects() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver must be initialized before initializing Page Objects.");
        }
        // Khởi tạo tất cả PO cần thiết
        loginPage = new LoginOrRegisterPage(driver);
        categoriesPage = new CategoriesPage(driver);
        editCategoryPage = new EditCategoryPage(driver);
        suppliersPage = new SuppliersPage(driver);
        editSupplierPage = new EditSupplierPage(driver);
        productsPage = new ProductsPage(driver);
        editProductPage = new EditProductPage(driver);
        // Khởi tạo các PO admin khác nếu có
        // adminDashboardPage = new AdminPage(driver);
        // ordersPage = new OrdersPage(driver);
        // customersPage = new CustomersPage(driver);
    }

    // ============================
    // BROWSER FACTORY – AUTO HEADLESS CHO GITHUB ACTIONS
    // ============================
    private WebDriver setupBrowser(String browserName) {

        switch (browserName.toLowerCase()) {

            case "firefox":
                FirefoxOptions ff = new FirefoxOptions();
                if (IS_GITHUB) {
                    ff.addArguments("--headless");
                } else {
                    ff.addArguments("--start-maximized");
                }
                return new FirefoxDriver(ff);

            case "chrome":
            default:
                ChromeOptions co = new ChromeOptions();

                if (IS_GITHUB) {
                    co.addArguments("--headless=new");
                    co.addArguments("--no-sandbox");
                    co.addArguments("--disable-dev-shm-usage");
                    co.addArguments("--disable-gpu");
                } else {
                    co.addArguments("--start-maximized");
                    co.addArguments("--disable-notifications");
                    co.addArguments("--disable-infobars");
                }

                return new ChromeDriver(co);
        }
    }

    // ============================
    // TEARDOWN
    // ============================
    @AfterMethod
    public void tearDown(ITestResult result) {

        String testName = result.getName();

        if (result.getStatus() == ITestResult.FAILURE) {

            extentTest.log(Status.FAIL, "FAILED: " + result.getThrowable());
            String screenshotPath = takeScreenshot(testName);

            if (screenshotPath != null) {
                try {
                    extentTest.addScreenCaptureFromPath(screenshotPath);
                } catch (Exception ignored) {}
            }

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS, "PASSED");
        } else {
            extentTest.log(Status.SKIP, "SKIPPED");
        }

        if (driver != null) driver.quit();
        ExtentReportManager.flush();
    }

    // ============================
    // SCREENSHOT UTILITY
    // ============================
    private String takeScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String folder = "test-output/screenshots/";
            Files.createDirectories(Paths.get(folder));

            String path = folder + name + ".png";
            Files.copy(src.toPath(), Paths.get(path));

            return path;
        } catch (Exception e) {
            return null;
        }
    }
}