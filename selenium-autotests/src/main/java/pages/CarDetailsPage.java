package pages;

import common.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.base.BasePage;

import java.math.BigDecimal;
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

    @FindBy(css = ".car-info")
    private WebElement carInfoBlock;

    public CarDetailsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public CarDetailsPage openCarDetailsPage(Integer carId) {
        if (carId == null) {
            throw new IllegalArgumentException("carId cannot be null");
        }
        open(Config.BASE_URL + "/cars/" + carId);
        waitForPageFullyLoaded();
        return this;
    }

    public void waitForPageFullyLoaded() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(carTitle),
                ExpectedConditions.visibilityOf(mainCarImage)
        ));
        wait.until(ExpectedConditions.visibilityOf(carInfoBlock));
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
            if (text.startsWith("В наличии") || text.startsWith("Нет в наличии") || text.startsWith("Ожидается"))
                continue;
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
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.favorite-btn-large")));
                        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].offsetParent !== null && arguments[0].getBoundingClientRect().height > 0;",
                    addToFavoritesButton
            );
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBuyButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".car-info .btn.btn-success")));
            return (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].offsetParent !== null && arguments[0].getBoundingClientRect().height > 0;",
                    buyButton
            );
        } catch (Exception e) {
            return false;
        }
    }

    public CarDetailsPage clickAddToFavorites() {
        wait.until(ExpectedConditions.elementToBeClickable(addToFavoritesButton)).click();
        wait.until(ExpectedConditions.urlContains("/cars/"));
        PageFactory.initElements(driver, this);
        waitForPageFullyLoaded();
        return this;
    }

    public boolean isFavoriteToggleButtonDisplayed() {
        return !driver.findElements(By.cssSelector(".car-info form[action='/favorites/toggle'] button")).isEmpty()
                && favoriteToggleButton.isDisplayed();
    }

    public CarDetailsPage clickFavoriteToggle() {
        wait.until(ExpectedConditions.elementToBeClickable(favoriteToggleButton)).click();
        wait.until(ExpectedConditions.urlContains("/cars/"));
        PageFactory.initElements(driver, this);
        waitForPageFullyLoaded();
        return this;
    }

    public void clickBuyButton() {
        wait.until(ExpectedConditions.elementToBeClickable(buyButton)).click();
    }

    public BigDecimal getCarPrice() {
        String priceText = getPriceText().replace("₽", "").replace(" ", "").trim();
        return new BigDecimal(priceText);
    }

    public String getBodyType() {
        try {
            WebElement el = driver.findElement(By.xpath("//div[@class='specs']//p[strong[contains(text(),'Тип кузова')]]/span"));
            return el.getText().trim().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    public String getColor() {
        try {
            List<WebElement> els = driver.findElements(By.xpath("//div[@class='specs']//p[strong[contains(text(),'Цвет')]]/span"));
            if (!els.isEmpty()) return els.get(0).getText().trim().toLowerCase();
        } catch (Exception ignored) {}
        return "";
    }

    public String getAirConditioning() {
        try {
            List<WebElement> els = driver.findElements(By.xpath("//div[@class='specs']//p[strong[contains(text(),'Кондиционер')]]/span"));
            if (!els.isEmpty()) return els.get(0).getText().trim().toLowerCase();
        } catch (Exception ignored) {}
        return "";
    }
}