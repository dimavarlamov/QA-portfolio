package tests;

import base.AuthenticatedBaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.FavoritesPage;

public class FavoritesTest extends AuthenticatedBaseTest {

    private FavoritesPage favoritesPage;

    @BeforeMethod
    public void setUpFavoritesTest() {
        favoritesPage = new FavoritesPage(driver);
    }

    @Test(description = "Добавление автомобиля в избранное")
    public void addCarToFavoritesTest() {
        // Открываем каталог (после логина он уже открыт, но убедимся)
        catalogPage.openCatalogPage();

        // Запоминаем текущее количество избранного
        favoritesPage.openFavoritesPage();
        int beforeCount = favoritesPage.getFavoritesCount();

        // Возвращаемся в каталог и добавляем первый автомобиль
        catalogPage.openCatalogPage();
        favoritesPage.toggleFirstFavorite();

        // Проверяем, что количество увеличилось
        favoritesPage.openFavoritesPage();
        Assert.assertTrue(
                favoritesPage.getFavoritesCount() > beforeCount,
                "Количество избранных автомобилей должно увеличиться"
        );
    }

    @Test(description = "Удаление автомобиля из избранного")
    public void removeCarFromFavoritesTest() {
        // Убедимся, что есть хотя бы один избранный автомобиль
        favoritesPage.openFavoritesPage();
        if (favoritesPage.getFavoritesCount() == 0) {
            // Добавляем первый автомобиль из каталога
            catalogPage.openCatalogPage();
            favoritesPage.toggleFirstFavorite();
            favoritesPage.openFavoritesPage();
        }

        int beforeRemoveCount = favoritesPage.getFavoritesCount();
        favoritesPage.removeFirstFavorite();

        favoritesPage.openFavoritesPage();
        Assert.assertTrue(
                favoritesPage.isEmptyMessageDisplayed() || favoritesPage.getFavoritesCount() < beforeRemoveCount,
                "После удаления список избранного должен измениться"
        );
    }
}