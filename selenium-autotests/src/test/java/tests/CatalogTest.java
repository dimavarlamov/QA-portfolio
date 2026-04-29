package tests;

import base.AuthenticatedBaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CarDetailsPage;

import java.math.BigDecimal;
import java.util.List;

public class CatalogTest extends AuthenticatedBaseTest {

    @Test(description = "Поиск по несуществующему запросу")
    public void emptySearchTest() {
        catalogPage.searchByText("zzzzzzzzzz12345");
        Assert.assertTrue(catalogPage.isNoProductsDisplayed(), "Сообщение 'Автомобили не найдены' не отображается");
        Assert.assertEquals(catalogPage.getProductCount(), 0, "Список товаров должен быть пустым");
    }

    @Test(description = "Сброс фильтров возвращает на каталог без параметров")
    public void resetFiltersTest() {
        catalogPage.searchByText("zzzzzzzzzz12345");
        catalogPage.resetFilters();
        Assert.assertEquals(catalogPage.getCurrentUrl(), "http://localhost:8080/catalog", "URL не чистый");
        Assert.assertTrue(catalogPage.isProductGridDisplayed(), "Сетка товаров не отображается");
    }

    @Test(description = "Некорректный диапазон цены вызывает ошибку валидации")
    public void invalidPriceFilterShowsAlertTest() {
        catalogPage.openFilters();
        catalogPage.setPriceRange(new BigDecimal("1000"), new BigDecimal("1"));
        String urlBefore = catalogPage.getCurrentUrl();
        catalogPage.applyFilters();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        String urlAfter = catalogPage.getCurrentUrl();
        Assert.assertEquals(urlAfter, urlBefore, "Страница не должна была обновиться");
        boolean hasError = driver.getPageSource().contains("Минимальная цена не может быть больше максимальной");
        Assert.assertTrue(hasError, "Сообщение об ошибке не отображается");
    }

    @Test(description = "Поиск существующего автомобиля")
    public void searchExistingCarTest() {
        catalogPage.searchByText("Camry");
        Assert.assertTrue(catalogPage.getProductCount() > 0, "Поиск не вернул результатов");
        Assert.assertTrue(catalogPage.getProductTitles().stream().anyMatch(t -> t.toLowerCase().contains("camry")),
                "В результатах отсутствует Toyota Camry");
    }

    @Test(description = "Поиск с XSS-строкой (безопасность)")
    public void searchWithXssTest() {
        String xssPayload = "<script>alert(1)</script>";
        catalogPage.searchByText(xssPayload);
        boolean alertPresent;
        try {
            driver.switchTo().alert();
            alertPresent = true;
        } catch (Exception e) {
            alertPresent = false;
        }
        Assert.assertFalse(alertPresent, "XSS уязвимость: alert выполнен!");
        Assert.assertTrue(catalogPage.isNoProductsDisplayed() || catalogPage.getProductCount() == 0,
                "Поиск XSS вернул результаты");
    }

    @Test(description = "Поиск с невалидными символами (B@M,W)")
    public void searchWithInvalidSymbolsTest() {
        catalogPage.searchByText("B@M,W");
        Assert.assertTrue(catalogPage.isNoProductsDisplayed() || catalogPage.getProductCount() == 0,
                "Поиск с невалидными символами вернул результаты");
    }

    @Test(description = "Фильтр по цене: валидный диапазон 1-2 млн")
    public void filterByPriceRangeTest() {
        BigDecimal min = new BigDecimal("1000000");
        BigDecimal max = new BigDecimal("2000000");
        catalogPage.openFilters()
                .setPriceRange(min, max)
                .applyFilters();

        List<BigDecimal> prices = catalogPage.getProductPrices();
        for (BigDecimal price : prices) {
            Assert.assertTrue(price.compareTo(min) >= 0 && price.compareTo(max) <= 0,
                    "Цена " + price + " вне диапазона");
        }
    }

    @Test(description = "Фильтр по марке (BMW)")
    public void filterByBrandTest() {
        catalogPage.openFilters();
        new Select(driver.findElement(By.id("brandId"))).selectByVisibleText("BMW");
        catalogPage.applyFilters();

        List<String> titles = catalogPage.getProductTitles();
        Assert.assertFalse(titles.isEmpty(), "Нет результатов для BMW");
        for (String title : titles) {
            Assert.assertTrue(title.contains("BMW"), "Найден автомобиль не BMW: " + title);
        }
    }

    @Test(description = "Комбинация фильтров: марка Toyota + цена до 2 млн")
    public void filterByBrandAndPriceTest() {
        catalogPage.openFilters();
        new Select(driver.findElement(By.id("brandId"))).selectByVisibleText("Toyota");
        catalogPage.setPriceRange(new BigDecimal("0"), new BigDecimal("2000000"));
        catalogPage.applyFilters();

        List<String> titles = catalogPage.getProductTitles();
        Assert.assertFalse(titles.isEmpty(), "Нет результатов по Toyota до 2 млн");
        for (String title : titles) {
            Assert.assertTrue(title.contains("Toyota"), "Название не содержит Toyota: " + title);
        }
    }

    @Test(description = "Фильтр по стране: Россия (должны быть LADA)")
    public void filterByCountryRussiaTest() {
        catalogPage.openFilters();
        catalogPage.selectCountryByVisibleText("Россия");
        catalogPage.applyFilters();

        Assert.assertTrue(catalogPage.getProductCount() > 0, "По стране Россия не найдено автомобилей");
        List<String> titles = catalogPage.getProductTitles();
        for (String title : titles) {
            Assert.assertTrue(title.contains("LADA") || title.contains("Niva"),
                    "Автомобиль " + title + " не является российским");
        }
    }

    @Test(description = "Фильтр по рулю: Правый (должны отсутствовать автомобили)")
    public void filterBySteeringSideRightTest() {
        catalogPage.openFilters();
        catalogPage.selectSteeringSideByVisibleText("Правый");
        catalogPage.applyFilters();

                Assert.assertTrue(catalogPage.isNoProductsDisplayed() || catalogPage.getProductCount() == 0,
                "При фильтре 'Правый руль' найдены автомобили, хотя в данных их быть не должно");
    }

    @Test(description = "Фильтр по типу двигателя: Гибрид (должен быть BMW i8)")
    public void filterByEngineTypeHybridTest() {
        catalogPage.openFilters();
        catalogPage.selectEngineTypeByVisibleText("Гибрид");
        catalogPage.applyFilters();

        List<String> titles = catalogPage.getProductTitles();
        Assert.assertFalse(titles.isEmpty(), "Гибридные автомобили не найдены");
        boolean hasI8 = titles.stream().anyMatch(t -> t.contains("i8"));
        Assert.assertTrue(hasI8, "Гибридный автомобиль BMW i8 не найден в результатах");
    }

    @Test(description = "Фильтр по кузову: Купе (проверка через детали авто)")
    public void filterByBodyTypeCoupeTest() {
        catalogPage.openFilters();
        catalogPage.selectBodyTypeByVisibleText("купе");
        catalogPage.applyFilters();

        Assert.assertTrue(catalogPage.getProductCount() > 0, "Автомобили с кузовом 'купе' не найдены");
        CarDetailsPage detailsPage = catalogPage.clickFirstCar();
        String bodyType = detailsPage.getBodyType();
        Assert.assertTrue(bodyType.contains("купе"),
                "Ожидался кузов 'купе', но в карточке получено: " + bodyType);
    }

    @Test(description = "Фильтр по цвету: Белый (проверка через детали авто)")
    public void filterByColorTest() {
        catalogPage.openFilters();
        catalogPage.selectColorByVisibleText("Белый");
        catalogPage.applyFilters();

        Assert.assertTrue(catalogPage.getProductCount() > 0, "Автомобили белого цвета не найдены");
        CarDetailsPage detailsPage = catalogPage.clickFirstCar();
        String color = detailsPage.getColor();
        Assert.assertTrue(color.contains("белый") || color.contains("white") || color.equals("белый"),
                "Ожидался белый цвет, но в карточке: " + color);
    }

    @Test(description = "Фильтр по кондиционеру: Да (проверка через детали авто)")
    public void filterByAirConditioningTest() {
        catalogPage.openFilters();
        catalogPage.selectAirConditioningByVisibleText("Да");
        catalogPage.applyFilters();

        Assert.assertTrue(catalogPage.getProductCount() > 0, "Автомобили с кондиционером не найдены");
        CarDetailsPage detailsPage = catalogPage.clickFirstCar();
        String ac = detailsPage.getAirConditioning();
        Assert.assertTrue(ac.equals("да") || ac.equals("yes") || ac.equals("true"),
                "Ожидался кондиционер 'Да', но в карточке: " + ac);
    }
}