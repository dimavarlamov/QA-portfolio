package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.base.BasePage;

import java.time.Duration;
import java.util.List;

public class FavoritesPage extends BasePage {

    private final WebDriverWait wait;

    private final By favoriteCards = By.cssSelector(".product-card");
    private final By favoriteButtons = By.cssSelector("button.favorite-btn");
    private final By activeFavoriteButtons = By.cssSelector("button.favorite-btn.active");
    private final By emptyMessage = By.cssSelector(".empty-message");

    public FavoritesPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public FavoritesPage openFavoritesPage() {
        open("/favorites");
        return this;
    }

    public boolean isOpened() {
        return urlContains("/favorites");
    }

    public int getFavoritesCount() {
        return driver.findElements(favoriteCards).size();
    }

    // ИСПРАВЛЕННЫЙ метод - добавляем ожидание редиректа после клика
    public void toggleFirstFavorite() {
        List<WebElement> buttons = driver.findElements(favoriteButtons);
        if (buttons.isEmpty()) {
            throw new IllegalStateException("На странице нет кнопок избранного");
        }
        WebElement button = buttons.get(0);
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
        // После клика происходит POST /favorites/toggle и редирект обратно на каталог
        wait.until(ExpectedConditions.urlContains("/catalog"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-grid")));
    }

    public void removeFirstFavorite() {
        List<WebElement> buttons = driver.findElements(activeFavoriteButtons);
        if (buttons.isEmpty()) {
            throw new IllegalStateException("На странице нет активных избранных автомобилей");
        }
        wait.until(ExpectedConditions.elementToBeClickable(buttons.get(0))).click();
        // После удаления также редирект на каталог? Нет, removeFirstFavorite вызывается на странице избранного,
        // и после клика редирект остаётся на избранном (Referer = /favorites). Здесь ожидание можно не добавлять,
        // потому что страница избранного перезагрузится.
        // Но для надёжности подождём, пока элемент избранного исчезнет? Не будем усложнять.
    }

    public boolean isEmptyMessageDisplayed() {
        List<WebElement> elements = driver.findElements(emptyMessage);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    private final By favoriteTitles = By.cssSelector(".product-card h3");

    public java.util.List<String> getFavoriteTitles() {
        return driver.findElements(favoriteTitles)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean containsFavoriteTitle(String expectedTitle) {
        return getFavoriteTitles().stream()
                .anyMatch(title -> title != null && title.contains(expectedTitle));
    }
}