package base;

import common.Config;
import org.testng.annotations.BeforeMethod;
import pages.CatalogPage;

public abstract class AuthenticatedBaseTest extends BaseTest {

    protected CatalogPage authenticatedCatalogPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        authenticatedCatalogPage = loginPage.loginAs(Config.TEST_EMAIL, Config.TEST_PASSWORD);
        this.catalogPage = authenticatedCatalogPage;
    }
}