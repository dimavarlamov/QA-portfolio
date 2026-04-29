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
        catalogPage.openCatalogPage();
        favoritesPage.openFavoritesPage();
        int beforeCount = favoritesPage.getFavoritesCount();

        catalogPage.openCatalogPage();
        favoritesPage.toggleFirstFavorite();

        favoritesPage.openFavoritesPage();
        Assert.assertTrue(favoritesPage.getFavoritesCount() > beforeCount,
                "Количество избранных автомобилей должно увеличиться");
    }

    @Test(description = "Удаление автомобиля из избранного")
    public void removeCarFromFavoritesTest() {
        favoritesPage.openFavoritesPage();
        if (favoritesPage.getFavoritesCount() == 0) {
            catalogPage.openCatalogPage();
            favoritesPage.toggleFirstFavorite();
            favoritesPage.openFavoritesPage();
        }
        int beforeRemoveCount = favoritesPage.getFavoritesCount();
        favoritesPage.removeFirstFavorite();

        favoritesPage.openFavoritesPage();
        Assert.assertTrue(favoritesPage.isEmptyMessageDisplayed() || favoritesPage.getFavoritesCount() < beforeRemoveCount,
                "После удаления список избранного должен измениться");
    }

    @Test(description = "Быстрые клики по кнопке избранного не создают дубли")
    public void rapidFavoriteClicksNoDuplicatesTest() {
        favoritesPage.openFavoritesPage();
        while (favoritesPage.getFavoritesCount() > 0) {
            favoritesPage.removeFirstFavorite();
            favoritesPage.openFavoritesPage();
        }
        Assert.assertEquals(favoritesPage.getFavoritesCount(), 0, "Избранное не очистилось");

        catalogPage.openCatalogPage();
        for (int i = 0; i < 3; i++) {
            favoritesPage.toggleFirstFavorite();
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }

        favoritesPage.openFavoritesPage();
        int count = favoritesPage.getFavoritesCount();
        Assert.assertEquals(count, 1, "Ожидался 1 автомобиль в избранном, но получено " + count);
    }
}