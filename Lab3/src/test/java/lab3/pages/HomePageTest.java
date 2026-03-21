package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-01: World of Tanks Home Page")
class HomePageTest extends BaseTest {

    @Test
    @DisplayName("TC-01: Home page loads successfully — title contains 'World of Tanks'")
    void testHomePageTitle() {
        HomePage home = new HomePage(driver).open();
        String title = home.getPageTitle();
        assertTrue(title.contains("World of Tanks"),
                "Page title must contain 'World of Tanks', actual: " + title);
    }

    @Test
    @DisplayName("TC-02: Home page opens on the EU RU portal")
    void testHomePageUrl() {
        HomePage home = new HomePage(driver).open();
        String url = home.getCurrentUrl();
        assertTrue(url.contains("worldoftanks.eu/ru/") || url.contains("worldoftanks.ru"),
                "URL must open the RU portal, actual: " + url);
    }

    @Test
    @DisplayName("TC-03: Logo is visible in the header")
    void testLogoIsVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertTrue(home.isLogoVisible(), "Logo must be visible in the header");
    }

    @Test
    @DisplayName("TC-04: Login and Register buttons are visible")
    void testAuthButtonsVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertAll(
                () -> assertTrue(home.isLoginBtnVisible(), "Login button must be visible"),
                () -> assertTrue(home.isRegisterBtnVisible(), "Register button must be visible")
        );
    }

    @Test
    @DisplayName("TC-05: Hero banner / promo block is visible")
    void testHeroBannerVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertTrue(home.isHeroBannerVisible(), "Hero banner must be visible");
    }

    @Test
    @DisplayName("TC-06: News preview block is visible on the home page")
    void testNewsBlockVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertTrue(home.isNewsBlockVisible(), "News preview block must be visible");
    }

    @Test
    @DisplayName("TC-07: Footer and region selector are visible")
    void testFooterAndRegionBlockVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertAll(
                () -> assertTrue(home.isFooterVisible(), "Footer must be visible"),
                () -> assertTrue(home.isRegionBlockVisible(), "Region selector block must be visible")
        );
    }
}