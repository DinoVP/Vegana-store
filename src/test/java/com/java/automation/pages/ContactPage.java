package com.java.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ContactPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Duration TIMEOUT = Duration.ofSeconds(10);

    // --- Locators Form Liên hệ (Dựa trên hình ảnh Contact Us) ---
    @FindBy(name = "fullname") private WebElement nameInput;
    @FindBy(name = "email") private WebElement emailInput;
    @FindBy(name = "subject") private WebElement subjectInput;
    @FindBy(xpath = "//textarea[@name='message']") private WebElement messageInput;
    @FindBy(xpath = "//button[contains(text(), 'SEND MESSAGE')]") private WebElement sendButton;

    // --- Locators Thông báo ---
    @FindBy(xpath = "//div[contains(@class, 'alert-success')]") private WebElement successAlert;

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    public void navigateToContactPage(String baseUrl) {
        driver.get(baseUrl + "/contact");
        wait.until(ExpectedConditions.visibilityOf(nameInput));
    }

    public void submitContactForm(String name, String email, String subject, String message) {
        nameInput.sendKeys(name);
        emailInput.sendKeys(email);
        subjectInput.sendKeys(subject);
        messageInput.sendKeys(message);

        sendButton.click();
    }

    public boolean isSuccessAlertDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successAlert));
            return successAlert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}