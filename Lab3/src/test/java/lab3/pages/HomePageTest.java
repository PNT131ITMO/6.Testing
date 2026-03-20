package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-01: worldoftanks.ru Home Page")
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
    @DisplayName("TC-02: Home page URL contains worldoftanks.ru")
    void testHomePageUrl() {
        new HomePage(driver).open();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("worldoftanks.ru"),
                "URL must contain 'worldoftanks.ru', actual: " + url);
    }

    @Test
    @DisplayName("TC-03: Logo is visible in the header")
    void testLogoIsVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertTrue(home.isLogoVisible(), "Logo must be visible in the header");
    }

    @Test
    @DisplayName("TC-04: Login button (Войти) is visible")
    void testLoginButtonVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertTrue(home.isLoginBtnVisible(), "Login button must be visible");
    }

    @Test
    @DisplayName("TC-05: Hero banner / promo block is visible")
    void testHeroBannerVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertTrue(home.isHeroBannerVisible(), "Hero banner must be visible");
    }

    @Test
    @DisplayName("TC-06: Footer is visible at the bottom of the page")
    void testFooterVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertTrue(home.isFooterVisible(), "Footer must be visible");
    }

    @Test
    @DisplayName("TC-07: News menu item is visible in the navigation bar")
    void testNavNewsVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        assertTrue(home.isNavNewsVisible(), "'News' item must be visible in the nav bar");
    }
}