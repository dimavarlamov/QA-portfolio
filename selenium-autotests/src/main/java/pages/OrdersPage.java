package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.base.BasePage;

import java.time.Duration;
import java.util.List;

public class OrdersPage extends BasePage {

    private final WebDriverWait wait;

    public OrdersPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public OrdersPage openOrdersPage() {
        open("/profile/orders");
        return this;
    }

    public int getOrdersCount() {
        return driver.findElements(By.cssSelector(".styled-table tbody tr")).size();
    }

    public String getOrderTotalAmount(int index) {
               List<WebElement> rows = driver.findElements(By.cssSelector(".styled-table tbody tr"));
        if (rows.size() <= index) return "";
        WebElement amountCell = rows.get(index).findElement(By.cssSelector("td:nth-child(2)"));
        return amountCell.getText().trim();
    }

    public void clickOrderDetails(int index) {
        List<WebElement> rows = driver.findElements(By.cssSelector(".styled-table tbody tr"));
        if (rows.size() <= index) throw new IndexOutOfBoundsException("Нет строки с индексом " + index);
        WebElement detailsLink = rows.get(index).findElement(By.linkText("Детали"));
        detailsLink.click();
    }

    public boolean isOrdersTableEmpty() {
        return driver.findElements(By.cssSelector(".styled-table tbody tr")).isEmpty();
    }

    public boolean isEmptyMessageDisplayed() {
        try {
            WebElement emptyMsg = driver.findElement(By.xpath("//p[contains(text(),'нет покупок')]"));
            return emptyMsg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}