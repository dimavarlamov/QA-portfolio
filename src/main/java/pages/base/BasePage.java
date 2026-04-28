package pages.base;

import common.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {
    protected final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open(String pathOrUrl) {
        if (pathOrUrl == null || pathOrUrl.isBlank()) {
            throw new IllegalArgumentException("pathOrUrl cannot be null or blank");
        }

        if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
            driver.get(pathOrUrl);
        } else {
            String normalizedPath = pathOrUrl.startsWith("/") ? pathOrUrl : "/" + pathOrUrl;
            driver.get(Config.BASE_URL + normalizedPath);
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean urlContains(String part) {
        return driver.getCurrentUrl().contains(part);
    }
}