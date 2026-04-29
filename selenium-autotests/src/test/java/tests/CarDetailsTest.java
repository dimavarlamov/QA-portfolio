package tests;

import base.AuthenticatedBaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CarDetailsPage;
import pages.FavoritesPage;

public class CarDetailsTest extends AuthenticatedBaseTest {
    private static final Integer CAMRY_ID = 115;
    private static final String CAMRY_TITLE = "Toyota Camry";
    private static final Integer OUT_OF_STOCK_CAR_ID = 118;
    private static final String OUT_OF_STOCK_TITLE = "Lexus LS460";

    private CarDetailsPage carDetailsPage;
    private FavoritesPage favoritesPage;

    @BeforeMethod
    public void setUpDetailsTest() {
        carDetailsPage = new CarDetailsPage(driver);
        favoritesPage = new FavoritesPage(driver);
        ensureCamryNotInFavorites();
    }

    @AfterMethod
    public void cleanUpFavorites() {
        ensureCamryNotInFavorites();
    }

    private void ensureCamryNotInFavorites() {
        favoritesPage.openFavoritesPage();
        if (favoritesPage.containsFavoriteTitle(CAMRY_TITLE)) {
            carDetailsPage.openCarDetailsPage(CAMRY_ID);
            carDetailsPage.clickFavoriteToggle();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            favoritesPage = new FavoritesPage(driver);
            favoritesPage.openFavoritesPage();
        }
    }

//    @Test(description = "Открытие карточки автомобиля")
//    public void openCarDetailsTest() {
//        CarDetailsPage page = carDetailsPage.openCarDetailsPage(CAMRY_ID);
//        Assert.assertTrue(page.isOpened());
//        Assert.assertTrue(page.getCarTitleText().contains(CAMRY_TITLE));
//        Assert.assertTrue(page.getPriceText().contains("₽"));
//        Assert.assertTrue(page.isMainImageDisplayed());
//        Assert.assertTrue(page.isInStockDisplayed());
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ignored) {}
//
//
//        boolean favoritesButtonVisible = page.isAddToFavoritesButtonDisplayed();
//        if (!favoritesButtonVisible) {
//            driver.navigate().refresh();
//            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
//            favoritesButtonVisible = page.isAddToFavoritesButtonDisplayed();
//        }
//        Assert.assertTrue(favoritesButtonVisible, "Кнопка добавления в избранное не отображается");
//
//        boolean buyButtonVisible = page.isBuyButtonDisplayed();
//        if (!buyButtonVisible) {
//            driver.navigate().refresh();
//            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
//            buyButtonVisible = page.isBuyButtonDisplayed();
//        }
//        Assert.assertTrue(buyButtonVisible, "Кнопка покупки не отображается");
//    }

    @Test(description = "Добавление в избранное из карточки")
    public void addCarToFavoritesFromDetailsTest() {
        carDetailsPage.openCarDetailsPage(CAMRY_ID).clickAddToFavorites();
        favoritesPage.openFavoritesPage();
        Assert.assertTrue(favoritesPage.containsFavoriteTitle(CAMRY_TITLE));
    }

    @Test(description = "Удаление из избранного из карточки")
    public void removeCarFromFavoritesFromDetailsTest() {
        favoritesPage.openFavoritesPage();
        if (!favoritesPage.containsFavoriteTitle(CAMRY_TITLE)) {
            carDetailsPage.openCarDetailsPage(CAMRY_ID).clickAddToFavorites();
            favoritesPage.openFavoritesPage();
        }
        carDetailsPage.openCarDetailsPage(CAMRY_ID).clickFavoriteToggle();
        favoritesPage.openFavoritesPage();
        Assert.assertFalse(favoritesPage.containsFavoriteTitle(CAMRY_TITLE));
    }

    @Test(description = "Карточка авто без наличия")
    public void outOfStockCarDetailsTest() {
        CarDetailsPage page = carDetailsPage.openCarDetailsPage(OUT_OF_STOCK_CAR_ID);
        Assert.assertTrue(page.isOpened());
        Assert.assertTrue(page.getCarTitleText().contains(OUT_OF_STOCK_TITLE));
        Assert.assertTrue(page.isOutOfStockDisplayed());
        Assert.assertTrue(page.isAddToFavoritesButtonDisplayed());
    }
}