package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-05: Main Menu Navigation")
class NavigationTest extends BaseTest {

    @Test
    @DisplayName("TC-29: Clicking News navigates to the news page")
    void testNavigateToNews() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        NewsPage newsPage = home.clickNavNews();
        assertTrue(newsPage.getCurrentUrl().contains("/ru/news/"),
                "Must navigate to the news page. Actual URL: " + newsPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-30: Clicking Game navigates to the game page")
    void testNavigateToGame() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        GameInfoPage gamePage = home.clickNavGame();
        assertTrue(gamePage.getCurrentUrl().contains("/ru/game/"),
                "Must navigate to the game page. Actual URL: " + gamePage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-31: Clicking Media navigates to the media page")
    void testNavigateToMedia() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavMedia();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("/ru/media/") || url.contains("media"),
                "Must navigate to the media page. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-32: Clicking Community navigates to the community area")
    void testNavigateToCommunity() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavCommunity();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("/ru/community/") || url.contains("community"),
                "Must navigate to the community area. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-33: Browser back button returns to the RU home page")
    void testBrowserBackNavigation() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavNews();
        driver.navigate().back();
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("worldoftanks.eu/ru/") || url.contains("worldoftanks.ru"),
                "Back button must return to the RU home page. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-34: Tested pages have non-empty titles")
    void testPageTitlesNotEmpty() {
        String[] urls = {
                "https://worldoftanks.eu/ru/",
                "https://worldoftanks.eu/ru/news/",
                "https://worldoftanks.eu/ru/game/",
                "https://worldoftanks.eu/ru/community/accounts/"
        };

        for (String url : urls) {
            driver.get(url);
            assertFalse(driver.getTitle().isEmpty(),
                    "Page title must not be empty for: " + url);
        }
    }
}