package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.base.BasePage;

import java.time.Duration;

public class LoginPage extends BasePage {

    private final WebDriverWait wait;

    @FindBy(name = "email")
    private WebElement emailInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(css = ".auth-button")
    private WebElement loginButton;

    private final By errorMessageLocator = By.cssSelector(".error");

    public LoginPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage openLoginPage() {
        open("/auth/login");
        return this;
    }

    public LoginPage typeEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
        return this;
    }

    public LoginPage typePassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    public LoginPage clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return this;
    }

    public CatalogPage loginAs(String email, String password) {
        openLoginPage();
        typeEmail(email);
        typePassword(password);
        clickLoginButton();

        wait.until(ExpectedConditions.urlContains("/catalog"));
        return new CatalogPage(driver);
    }

    public LoginPage loginWithInvalidCredentials(String email, String password) {
        openLoginPage();
        typeEmail(email);
        typePassword(password);
        clickLoginButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
        return this;
    }

    public boolean isErrorMessageDisplayed() {
        return !driver.findElements(errorMessageLocator).isEmpty()
                && driver.findElement(errorMessageLocator).isDisplayed();
    }

    public String getErrorMessageText() {
        if (driver.findElements(errorMessageLocator).isEmpty()) {
            return "";
        }
        return driver.findElement(errorMessageLocator).getText().trim();
    }

    public boolean isOpened() {
        return urlContains("/auth/login");
    }
}