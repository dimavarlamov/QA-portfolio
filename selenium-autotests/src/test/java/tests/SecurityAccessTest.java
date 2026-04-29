package tests;

import base.AuthenticatedBaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SecurityAccessTest extends AuthenticatedBaseTest {

    @Test(description = "Пользователь без прав (покупатель) не может зайти в админ-панель")
    public void userCannotAccessAdminPageTest() {
        driver.get("http://localhost:8080/admin/dashboard");

        String pageSource = driver.getPageSource();
        boolean isForbidden = pageSource.contains("Forbidden") || pageSource.contains("403");
        Assert.assertTrue(isForbidden, "Найден баг! Админ-панель доступна обычному пользователю! Ожидалась ошибка 403.");
    }
}