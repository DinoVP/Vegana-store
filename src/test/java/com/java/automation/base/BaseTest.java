package com.java.automation.base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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
 * BaseTest chuẩn cho Selenium 4 + TestNG + Extent Report
 */
public class BaseTest {

    protected WebDriver driver;
    protected ExtentTest extentTest;

    // FIX: THÊM ThreadLocal để kiểm soát việc đóng trình duyệt
    private static ThreadLocal<Boolean> shouldQuitDriver = ThreadLocal.withInitial(() -> true);

    // ============================
    // CONFIG MẶC ĐỊNH
    // ============================
    private final String BASE_URL = "http://localhost:9090";
    private final int IMPLICIT_WAIT = 10;
    private final int PAGELOAD_WAIT = 20;

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

        driver.get(BASE_URL);
        extentTest.log(Status.INFO, "Navigate to: " + BASE_URL);
    }

    // ============================
    // BROWSER FACTORY – AUTO HEADLESS CHO GITHUB ACTIONS
    // ============================
    private WebDriver setupBrowser(String browserName) {
        // ... (Code setupBrowser giữ nguyên) ...
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

    // BỔ SUNG: Hàm ra lệnh KHÔNG ĐÓNG trình duyệt
    public void setShouldQuit(boolean shouldQuit) {
        shouldQuitDriver.set(shouldQuit);
    }

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

        // FIX LOGIC: CHỈ ĐÓNG KHI shouldQuitDriver là TRUE
        if (driver != null && shouldQuitDriver.get()) {
            driver.quit();
        }

        // Luôn reset cờ về TRUE cho các test sau (trừ khi test đó lại set FALSE)
        shouldQuitDriver.set(true);

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