package pages;

import common.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.*;
import pages.base.BasePage;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogPage extends BasePage {

    private final WebDriverWait wait;

    @FindBy(css = ".product-grid")
    private WebElement productGrid;

    @FindBy(css = ".search-input")
    private WebElement searchInput;

    @FindBy(css = ".search-button")
    private WebElement searchButton;

    @FindBy(css = ".filter-button")
    private WebElement filterButton;

    @FindBy(css = ".filter-form")
    private WebElement filterForm;

    @FindBy(id = "brandId")
    private WebElement brandSelect;

    @FindBy(id = "modelId")
    private WebElement modelSelect;

    @FindBy(id = "country")
    private WebElement countrySelect;

    @FindBy(id = "steeringSide")
    private WebElement steeringSideSelect;

    @FindBy(id = "engineType")
    private WebElement engineTypeSelect;

    @FindBy(id = "minPrice")
    private WebElement minPriceInput;

    @FindBy(id = "maxPrice")
    private WebElement maxPriceInput;

    @FindBy(id = "yearFrom")
    private WebElement yearFromInput;

    @FindBy(id = "yearTo")
    private WebElement yearToInput;

    @FindBy(id = "mileageFrom")
    private WebElement mileageFromInput;

    @FindBy(id = "mileageTo")
    private WebElement mileageToInput;

    @FindBy(id = "hpFrom")
    private WebElement hpFromInput;

    @FindBy(id = "hpTo")
    private WebElement hpToInput;

    @FindBy(id = "bodyType")
    private WebElement bodyTypeSelect;

    @FindBy(id = "color")
    private WebElement colorSelect;

    @FindBy(id = "airConditioning")
    private WebElement airConditioningSelect;

    @FindBy(css = ".apply-btn")
    private WebElement applyFiltersButton;

    @FindBy(css = ".reset-btn")
    private WebElement resetButton;

    @FindBy(css = ".product-card h3")
    private List<WebElement> productTitles;

    @FindBy(css = ".no-products")
    private List<WebElement> noProductsBlocks;

    public CatalogPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public CatalogPage openCatalogPage() {
        open("/catalog");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(productGrid),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-grid"))
        ));
        return this;
    }

    public boolean isOpened() {
        return urlContains("/catalog");
    }

    public boolean isProductGridDisplayed() {
        return !driver.findElements(By.cssSelector(".product-grid")).isEmpty()
                && productGrid.isDisplayed();
    }

    public int getProductCount() {
        return driver.findElements(By.cssSelector(".product-card")).size();
    }

    public List<String> getProductTitles() {
        return productTitles.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isNoProductsDisplayed() {
        return !noProductsBlocks.isEmpty() && noProductsBlocks.get(0).isDisplayed();
    }

    public CatalogPage searchByText(String query) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.clear();
        searchInput.sendKeys(query);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        waitForResultsToLoad();
        return this;
    }

    public CatalogPage openFilters() {
        wait.until(ExpectedConditions.elementToBeClickable(filterButton)).click();
        wait.until(driver -> filterForm.isDisplayed());
        return this;
    }

    public CatalogPage selectBrandByVisibleText(String brandName) {
        new Select(brandSelect).selectByVisibleText(brandName);
        return this;
    }

    public CatalogPage selectModelByVisibleText(String modelName) {
        new Select(modelSelect).selectByVisibleText(modelName);
        return this;
    }

    public CatalogPage selectCountryByVisibleText(String country) {
        new Select(countrySelect).selectByVisibleText(country);
        return this;
    }

    public CatalogPage selectSteeringSideByVisibleText(String side) {
        new Select(steeringSideSelect).selectByVisibleText(side);
        return this;
    }

    public CatalogPage selectEngineTypeByVisibleText(String engineType) {
        new Select(engineTypeSelect).selectByVisibleText(engineType);
        return this;
    }

    public CatalogPage setPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        wait.until(ExpectedConditions.visibilityOf(minPriceInput));
        minPriceInput.clear();
        minPriceInput.sendKeys(minPrice.toPlainString());
        maxPriceInput.clear();
        maxPriceInput.sendKeys(maxPrice.toPlainString());
        return this;
    }

    public CatalogPage setYearRange(Integer from, Integer to) {
        yearFromInput.clear();
        yearFromInput.sendKeys(String.valueOf(from));
        yearToInput.clear();
        yearToInput.sendKeys(String.valueOf(to));
        return this;
    }

    public CatalogPage setMileageRange(Integer from, Integer to) {
        mileageFromInput.clear();
        mileageFromInput.sendKeys(String.valueOf(from));
        mileageToInput.clear();
        mileageToInput.sendKeys(String.valueOf(to));
        return this;
    }

    public CatalogPage setHorsepowerRange(Integer from, Integer to) {
        hpFromInput.clear();
        hpFromInput.sendKeys(String.valueOf(from));
        hpToInput.clear();
        hpToInput.sendKeys(String.valueOf(to));
        return this;
    }

    public CatalogPage selectBodyTypeByVisibleText(String bodyType) {
        new Select(bodyTypeSelect).selectByVisibleText(bodyType);
        return this;
    }

    public CatalogPage selectColorByVisibleText(String color) {
        new Select(colorSelect).selectByVisibleText(color);
        return this;
    }

    public CatalogPage selectAirConditioningByVisibleText(String value) {
        new Select(airConditioningSelect).selectByVisibleText(value);
        return this;
    }

    public CatalogPage applyFilters() {
        try {
            applyFiltersButton.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", applyFiltersButton);
        }
        waitForResultsToLoad();
        return this;
    }

    public CatalogPage resetFilters() {
        openFilters();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", resetButton);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", resetButton);
        wait.until(ExpectedConditions.urlToBe(Config.BASE_URL + "/catalog"));
        wait.until(ExpectedConditions.visibilityOf(productGrid));
        return this;
    }

    public LoginPage logout() {
        WebElement logoutButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(normalize-space(),'Выйти')]")
                )
        );
        logoutButton.click();
        wait.until(ExpectedConditions.urlContains("/auth/login"));
        return new LoginPage(driver);
    }

    private void waitForResultsToLoad() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            shortWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(productGrid),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".no-products"))
            ));
        } catch (TimeoutException ignored) {}
    }

    public List<BigDecimal> getProductPrices() {
        return driver.findElements(By.cssSelector(".product-card .price"))
                .stream()
                .map(el -> new BigDecimal(el.getText().replace("₽", "").trim()))
                .collect(Collectors.toList());
    }

    public CarDetailsPage clickFirstCar() {
        WebElement firstCarLink = driver.findElement(By.cssSelector(".product-card a"));
        firstCarLink.click();
        return new CarDetailsPage(driver);
    }
}