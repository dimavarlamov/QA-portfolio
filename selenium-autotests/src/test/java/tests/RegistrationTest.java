package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.RegisterPage;

import java.time.Duration;

public class RegistrationTest extends BaseTest {

    private RegisterPage registerPage;
    private String uniqueEmail;

    @BeforeMethod
    public void setUpRegistration() {
        registerPage = new RegisterPage(driver);
        uniqueEmail = "qa_" + System.currentTimeMillis() + "@mail.test";
    }

    @Test(description = "Регистрация с валидными данными")
    public void validRegistrationTest() {
        registerPage.openRegisterPage()
                .enterLastName("Иванов")
                .enterFirstName("Иван")
                .enterPatronymic("Иванович")
                .enterEmail(uniqueEmail)
                .enterPassword("Test1234")
                .enterMatchingPassword("Test1234")
                .submit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/auth/login?success"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login?success"),
                "Редирект на /auth/login?success не произошёл");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
    }

    @Test(description = "Регистрация с пустыми полями (HTML5 валидация)")
    public void emptyFieldsRegistrationTest() {
        registerPage.openRegisterPage().submit();

        Assert.assertTrue(driver.getCurrentUrl().contains("/register"),
                "При пустых полях форма не должна отправляться");

        WebElement lastNameField = driver.findElement(By.cssSelector("input[name='lastName']"));
        String validationMessage = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", lastNameField);
        Assert.assertFalse(validationMessage.isEmpty(), "Должна быть показана подсказка о пустом поле");
    }

    @Test(description = "Регистрация с невалидным email (HTML5 валидация)")
    public void invalidEmailRegistrationTest() {
        registerPage.openRegisterPage()
                .enterLastName("Иванов")
                .enterFirstName("Иван")
                .enterEmail("invalid-email")
                .enterPassword("Test1234")
                .enterMatchingPassword("Test1234")
                .submit();

        Assert.assertTrue(driver.getCurrentUrl().contains("/register"),
                "Форма не должна отправляться при невалидном email");

        WebElement emailField = driver.findElement(By.cssSelector("input[name='email']"));
        String validationMessage = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", emailField);
        Assert.assertTrue(validationMessage.contains("@") || validationMessage.toLowerCase().contains("email"),
                "Подсказка должна указывать на проблему с email: " + validationMessage);
    }

    @Test(description = "Регистрация с уже существующим email (серверная ошибка)")
    public void duplicateEmailRegistrationTest() {
        registerPage.openRegisterPage()
                .enterLastName("Дубль")
                .enterFirstName("Тест")
                .enterEmail("demavarlamov@gmail.com")
                .enterPassword("Test1234")
                .enterMatchingPassword("Test1234")
                .submit();

        Assert.assertTrue(driver.getCurrentUrl().contains("/register"),
                "При дубликате email перенаправления не должно быть");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorBlock = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error")));
        Assert.assertTrue(errorBlock.isDisplayed(), "Блок ошибки не виден");
        Assert.assertTrue(errorBlock.getText().contains("уже существует"),
                "Текст ошибки должен сообщать о существующем email");
    }
}