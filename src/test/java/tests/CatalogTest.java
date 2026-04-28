package tests;

import base.AuthenticatedBaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class CatalogTest extends AuthenticatedBaseTest {

    @Test(description = "Поиск по несуществующему запросу")
    public void emptySearchTest() {
        catalogPage.searchByText("zzzzzzzzzz12345");

        Assert.assertTrue(
                catalogPage.isNoProductsDisplayed(),
                "Сообщение 'Автомобили не найдены' не отображается"
        );
        Assert.assertEquals(
                catalogPage.getProductCount(),
                0,
                "Список товаров должен быть пустым"
        );
    }

    @Test(description = "Сброс фильтров возвращает на каталог без параметров")
    public void resetFiltersTest() {
        catalogPage.searchByText("zzzzzzzzzz12345");
        catalogPage.resetFilters();

        Assert.assertEquals(
                catalogPage.getCurrentUrl(),
                "http://localhost:8080/catalog",
                "После сброса фильтров должен открыться чистый каталог"
        );
        Assert.assertTrue(
                catalogPage.isProductGridDisplayed(),
                "После сброса фильтров сетка товаров должна отображаться"
        );
    }

    @Test(description = "Некорректный диапазон цены вызывает ошибку валидации")
    public void invalidPriceFilterShowsAlertTest() {
        catalogPage.openFilters();
        catalogPage.setPriceRange(new BigDecimal("1000"), new BigDecimal("1"));

        String urlBefore = catalogPage.getCurrentUrl();
        catalogPage.applyFilters();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        String urlAfter = catalogPage.getCurrentUrl();
        Assert.assertEquals(urlAfter, urlBefore, "Страница не должна была обновиться из-за ошибки валидации");

        boolean hasErrorOnPage = driver.getPageSource().contains("Минимальная цена не может быть больше максимальной");
        Assert.assertTrue(hasErrorOnPage, "Сообщение об ошибке должно отображаться на странице");
    }

    @Test(description = "Поиск существующего автомобиля")
    public void searchExistingCarTest() {
        catalogPage.searchByText("Camry");

        Assert.assertTrue(
                catalogPage.getProductCount() > 0,
                "Поиск не вернул результатов"
        );
        Assert.assertTrue(
                catalogPage.getProductTitles()
                        .stream()
                        .anyMatch(title -> title.toLowerCase().contains("camry")),
                "В результатах поиска отсутствует Toyota Camry"
        );
    }
}