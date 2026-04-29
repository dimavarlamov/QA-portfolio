package tests;

import base.AuthenticatedBaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CarDetailsPage;
import pages.OrderDetailsPage;
import pages.OrdersPage;

import java.math.BigDecimal;
import java.time.Duration;

public class PurchaseTest extends AuthenticatedBaseTest {

    private static final Integer CAR_IN_STOCK_ID = 102; // LADA Vesta, stock=3, цена 1 350 000
    private static final String CAR_IN_STOCK_NAME = "LADA Vesta";
    private static final Integer CAR_OUT_OF_STOCK_ID = 118; // Lexus LS460, stock=0

    @Test(description = "Покупка автомобиля, который есть в наличии")
    public void purchaseCarInStockTest() {
        CarDetailsPage carDetailsPage = new CarDetailsPage(driver);
        carDetailsPage.openCarDetailsPage(CAR_IN_STOCK_ID);

        String priceText = carDetailsPage.getPriceText().replace("₽", "").trim();
        BigDecimal price = new BigDecimal(priceText);

        OrdersPage ordersPage = new OrdersPage(driver);
        ordersPage.openOrdersPage();
        int ordersBefore = ordersPage.getOrdersCount();

        carDetailsPage.openCarDetailsPage(CAR_IN_STOCK_ID);
        carDetailsPage.clickBuyButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(alert.getText(), "Подтвердите покупку");
        alert.accept();

        By successMessageLocator = By.xpath("//div[contains(@class, 'alert-success') and contains(text(), 'Поздравляем с покупкой')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageLocator));
        String successText = driver.findElement(successMessageLocator).getText();
        Assert.assertTrue(successText.contains("ждёт вас в автосалоне"), "Текст сообщения не соответствует");

        ordersPage.openOrdersPage();
        int ordersAfter = ordersPage.getOrdersCount();
        Assert.assertEquals(ordersAfter, ordersBefore + 1, "Количество заказов не увеличилось");

        String totalAmountText = ordersPage.getOrderTotalAmount(0);
        BigDecimal displayedTotal = new BigDecimal(totalAmountText.replace("₽", "").trim());
        Assert.assertEquals(displayedTotal, price, "Сумма заказа не совпадает с ценой авто");

        ordersPage.clickOrderDetails(0);
        OrderDetailsPage detailsPage = new OrderDetailsPage(driver);
        String carModel = detailsPage.getCarBrandModel();
        Assert.assertTrue(carModel.contains(CAR_IN_STOCK_NAME), "Модель в деталях заказа не совпадает");
        String detailPriceText = detailsPage.getCarPrice();
        BigDecimal detailPrice = new BigDecimal(detailPriceText.replace("₽", "").trim());
        Assert.assertEquals(detailPrice, price, "Цена в деталях заказа не совпадает");
    }

    @Test(description = "Покупка автомобиля, которого нет в наличии")
    public void purchaseCarOutOfStockTest() {
        CarDetailsPage carDetailsPage = new CarDetailsPage(driver);
        carDetailsPage.openCarDetailsPage(CAR_OUT_OF_STOCK_ID);
        carDetailsPage.clickBuyButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(alert.getText(), "Подтвердите покупку");
        alert.accept();

        By errorMessageLocator = By.xpath("//div[contains(@class, 'alert-danger') and contains(text(), 'отсутствует на складе')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
        String errorText = driver.findElement(errorMessageLocator).getText();
        Assert.assertTrue(errorText.contains("отсутствует на складе"), "Текст ошибки не соответствует");

        Assert.assertFalse(driver.getCurrentUrl().contains("/profile/orders"), "Не должно быть редиректа на заказы");
    }
}