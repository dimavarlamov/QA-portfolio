package tests;

import base.BaseTest;
import common.Config;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Успешная авторизация")
    public void validLoginTest() {
        catalogPage = loginPage.loginAs(Config.TEST_EMAIL, Config.TEST_PASSWORD);

        Assert.assertTrue(
                catalogPage.isOpened(),
                "После авторизации не произошёл переход в каталог"
        );
        Assert.assertTrue(
                catalogPage.isProductGridDisplayed(),
                "Каталог автомобилей не отображается"
        );
    }

    @Test(description = "Авторизация с неверным паролем")
    public void invalidPasswordLoginTest() {
        loginPage.loginWithInvalidCredentials(Config.TEST_EMAIL, "wrongPassword123");

        Assert.assertTrue(
                loginPage.isErrorMessageDisplayed(),
                "Сообщение об ошибке не отображается"
        );
        Assert.assertEquals(
                loginPage.getErrorMessageText(),
                "Неверное имя пользователя или пароль",
                "Некорректный текст ошибки"
        );
        Assert.assertTrue(
                loginPage.isOpened(),
                "Пользователь был перенаправлен со страницы логина"
        );
    }

    @Test(description = "Успешный выход из аккаунта")
    public void logoutTest() {
        catalogPage = loginPage.loginAs(Config.TEST_EMAIL, Config.TEST_PASSWORD);

        Assert.assertTrue(
                catalogPage.isOpened(),
                "Не удалось залогиниться перед logout"
        );

        loginPage = catalogPage.logout();

        Assert.assertTrue(
                loginPage.isOpened(),
                "После logout не произошёл возврат на страницу логина"
        );
    }
}