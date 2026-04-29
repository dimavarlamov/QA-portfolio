package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.base.BasePage;

import java.time.Duration;

public class OrderDetailsPage extends BasePage {

    private final WebDriverWait wait;

    public OrderDetailsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getCarBrandModel() {
        WebElement carTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".car-info h3")));
        return carTitle.getText().trim();
    }

    public String getCarPrice() {
        WebElement price = driver.findElement(By.cssSelector(".car-price"));
        return price.getText().trim();
    }

    public String getOrderTotalAmount() {
        WebElement total = driver.findElement(By.cssSelector(".order-info p:last-child"));
        return total.getText().replace("Общая сумма:", "").trim();
    }
}