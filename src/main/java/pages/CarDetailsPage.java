package pages;

import common.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.base.BasePage;

import java.time.Duration;
import java.util.List;

public class CarDetailsPage extends BasePage {

    private final WebDriverWait wait;

    @FindBy(css = ".car-info h1")
    private WebElement carTitle;

    @FindBy(css = ".car-info .price")
    private WebElement priceBlock;

    @FindBy(id = "mainCarImage")
    private WebElement mainCarImage;

    @FindBy(css = ".car-info p")
    private List<WebElement> infoParagraphs;

    @FindBy(css = "button.favorite-btn-large")
    private WebElement addToFavoritesButton;

    @FindBy(css = ".car-info form[action='/favorites/toggle'] button")
    private WebElement favoriteToggleButton;

    @FindBy(css = ".car-info .btn.btn-success")
    private WebElement buyButton;

    public CarDetailsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public CarDetailsPage openCarDetailsPage(Integer carId) {
        if (carId == null) {
            throw new IllegalArgumentException("carId cannot be null");
        }
        open(Config.BASE_URL + "/cars/" + carId);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(carTitle),
                ExpectedConditions.visibilityOf(mainCarImage)
        ));
        return this;
    }

    public boolean isOpened() {
        return urlContains("/cars/");
    }

    public String getCarTitleText() {
        wait.until(ExpectedConditions.visibilityOf(carTitle));
        return carTitle.getText().trim();
    }

    public String getPriceText() {
        wait.until(ExpectedConditions.visibilityOf(priceBlock));
        return priceBlock.getText().trim();
    }

    public boolean isMainImageDisplayed() {
        return !driver.findElements(By.id("mainCarImage")).isEmpty() && mainCarImage.isDisplayed();
    }

    public String getMainImageSource() {
        wait.until(ExpectedConditions.visibilityOf(mainCarImage));
        return mainCarImage.getAttribute("src");
    }

    public String getDescriptionText() {
        for (WebElement paragraph : infoParagraphs) {
            String text = paragraph.getText().trim();
            if (text.isEmpty()) continue;
            if (text.equals(getCarTitleText())) continue;
            if (text.equals(getPriceText())) continue;
            if (text.startsWith("В наличии") || text.startsWith("Нет в наличии") || text.startsWith("Ожидается")) continue;
            if (text.startsWith("Страна производитель")) continue;
            return text;
        }
        return "";
    }

    public boolean isInStockDisplayed() {
        return !driver.findElements(By.xpath("//p[contains(.,'В наличии')]")).isEmpty();
    }

    public boolean isOutOfStockDisplayed() {
        return !driver.findElements(By.xpath("//p[contains(.,'Нет в наличии')]")).isEmpty();
    }

    public boolean isAddToFavoritesButtonDisplayed() {
        return !driver.findElements(By.cssSelector("button.favorite-btn-large")).isEmpty()
                && addToFavoritesButton.isDisplayed();
    }

    public CarDetailsPage clickAddToFavorites() {
        wait.until(ExpectedConditions.elementToBeClickable(addToFavoritesButton)).click();
     
        wait.until(ExpectedConditions.visibilityOf(carTitle));
        return this;
    }

    public boolean isFavoriteToggleButtonDisplayed() {
        return !driver.findElements(By.cssSelector(".car-info form[action='/favorites/toggle'] button")).isEmpty()
                && favoriteToggleButton.isDisplayed();
    }

    // Аналогично для toggle
    public CarDetailsPage clickFavoriteToggle() {
        wait.until(ExpectedConditions.elementToBeClickable(favoriteToggleButton)).click();
        wait.until(ExpectedConditions.visibilityOf(carTitle));
        return this;
    }

    public boolean isBuyButtonDisplayed() {
        return !driver.findElements(By.cssSelector(".car-info .btn.btn-success")).isEmpty()
                && buyButton.isDisplayed();
    }

    public void clickBuyButton() {
        wait.until(ExpectedConditions.elementToBeClickable(buyButton)).click();
    }
}