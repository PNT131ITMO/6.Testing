package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-05: Main Menu Navigation")
class NavigationTest extends BaseTest {

    @Test
    @DisplayName("TC-24: Clicking News navigates to the news page")
    void testNavigateToNews() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavNews();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("news") || url.contains("новости"),
                "Must navigate to the news page. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-25: Clicking Game navigates to the game info page")
    void testNavigateToGame() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavGame();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("game") || url.contains("игра") || url.contains("content"),
                "Must navigate to the game page. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-26: Clicking Media navigates to the media page")
    void testNavigateToMedia() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavMedia();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("media") || url.contains("медиа"),
                "Must navigate to the media page. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-27: Clicking Community navigates to the community page")
    void testNavigateToCommunity() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavCommunity();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("community") || url.contains("сообщество") || url.contains("clan"),
                "Must navigate to the community page. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-28: Browser back button returns to the previous page")
    void testBrowserBackNavigation() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavNews();
        driver.navigate().back();
        assertTrue(driver.getCurrentUrl().contains("worldoftanks.ru"),
                "Back button must return to the previous page");
    }

    @Test
    @DisplayName("TC-29: Every tested page has a non-empty title")
    void testPageTitlesNotEmpty() {
        String[] urls = {
            "https://worldoftanks.ru/",
            "https://worldoftanks.ru/ru/news/",
        };
        for (String url : urls) {
            driver.get(url);
            assertFalse(driver.getTitle().isEmpty(),
                    "Page title must not be empty for: " + url);
        }
    }
}