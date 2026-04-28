package base;

import common.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.CatalogPage;
import pages.FavoritesPage;
import pages.LoginPage;

public abstract class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;
    protected CatalogPage catalogPage;
    protected FavoritesPage favoritesPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverManager.getDriver();

        loginPage = new LoginPage(driver);
        catalogPage = new CatalogPage(driver);
        favoritesPage = new FavoritesPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}