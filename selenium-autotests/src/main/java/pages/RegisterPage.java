package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.base.BasePage;

import java.time.Duration;

public class RegisterPage extends BasePage {

    private final WebDriverWait wait;

    @FindBy(css = "input[name='lastName']")
    private WebElement lastNameInput;

    @FindBy(css = "input[name='firstName']")
    private WebElement firstNameInput;

    @FindBy(css = "input[name='patronymic']")
    private WebElement patronymicInput;

    @FindBy(css = "input[name='email']")
    private WebElement emailInput;

    @FindBy(css = "input[name='password']")
    private WebElement passwordInput;

    @FindBy(css = "input[name='matchingPassword']")
    private WebElement matchingPasswordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    public RegisterPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public RegisterPage openRegisterPage() {
        open("/register");
        return this;
    }

    public RegisterPage enterLastName(String lastName) {
        wait.until(ExpectedConditions.visibilityOf(lastNameInput));
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        return this;
    }

    public RegisterPage enterFirstName(String firstName) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        return this;
    }

    public RegisterPage enterPatronymic(String patronymic) {
        patronymicInput.clear();
        patronymicInput.sendKeys(patronymic);
        return this;
    }

    public RegisterPage enterEmail(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    public RegisterPage enterMatchingPassword(String matchingPassword) {
        matchingPasswordInput.clear();
        matchingPasswordInput.sendKeys(matchingPassword);
        return this;
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }
}