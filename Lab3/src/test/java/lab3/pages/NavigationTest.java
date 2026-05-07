package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-02: Main Menu Navigation")
class NavigationTest extends BaseTest {

    private void waitForDocumentReady() {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(d ->
                "complete".equals(
                        ((JavascriptExecutor) d).executeScript("return document.readyState")
                )
        );
    }

    private String waitForNonEmptyTitle(String url) {
        driver.get(url);
        waitForDocumentReady();

        try {
            return new WebDriverWait(driver, Duration.ofSeconds(30)).until(d -> {
                String title = d.getTitle();
                if (title != null && !title.trim().isEmpty()) {
                    return title.trim();
                }
                return null;
            });
        } catch (Exception firstAttemptFailed) {
            driver.navigate().refresh();
            waitForDocumentReady();

            return new WebDriverWait(driver, Duration.ofSeconds(30)).until(d -> {
                String title = d.getTitle();
                if (title != null && !title.trim().isEmpty()) {
                    return title.trim();
                }
                return null;
            });
        }
    }

    @Test
    @DisplayName("TC-08: Clicking News navigates to the news page")
    void testNavigateToNews() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        NewsPage newsPage = home.clickNavNews();

        assertTrue(newsPage.getCurrentUrl().contains("/ru/news/"),
                "Must navigate to the news page. Actual URL: " + newsPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-09: Clicking Game navigates to the game page")
    void testNavigateToGame() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        GameInfoPage gamePage = home.clickNavGame();

        assertTrue(gamePage.getCurrentUrl().contains("/ru/game/"),
                "Must navigate to the game page. Actual URL: " + gamePage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-10: Clicking Media navigates to the media page")
    void testNavigateToMedia() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavMedia();

        String url = driver.getCurrentUrl();

        assertTrue(url.contains("/ru/media/") || url.contains("media"),
                "Must navigate to the media page. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-11: Clicking Community navigates to the community area")
    void testNavigateToCommunity() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavCommunity();

        String url = driver.getCurrentUrl();

        assertTrue(url.contains("/ru/community/") || url.contains("community"),
                "Must navigate to the community area. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-12: Browser back button returns to the RU home page")
    void testBrowserBackNavigation() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        home.clickNavNews();

        driver.navigate().back();

        new WebDriverWait(driver, Duration.ofSeconds(30)).until(d -> {
            String currentUrl = d.getCurrentUrl();
            return currentUrl.contains("worldoftanks.eu/ru/")
                    || currentUrl.contains("worldoftanks.ru");
        });

        String url = driver.getCurrentUrl();

        assertTrue(url.contains("worldoftanks.eu/ru/") || url.contains("worldoftanks.ru"),
                "Back button must return to the RU home page. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-13: Tested pages have non-empty titles")
    void testPageTitlesNotEmpty() {
        String[] urls = {
                "https://worldoftanks.eu/ru/",
                "https://worldoftanks.eu/ru/news/",
                "https://worldoftanks.eu/ru/game/",
                "https://worldoftanks.eu/ru/community/accounts/",
                "https://worldoftanks.eu/ru/content/guide/",
                "https://worldoftanks.eu/ru/clanwars/?link_place=wotp_link_main-menu",
                "https://worldoftanks.eu/ru/tournaments/",
                "https://worldoftanks.eu/ru/community/"
        };

        for (String url : urls) {
            String title = waitForNonEmptyTitle(url);

            assertFalse(title.isEmpty(),
                    "Page title must not be empty for: " + url);
        }
    }

    @Test
    @DisplayName("TC-14: Clicking Guides navigates to the guides page")
    void testNavigateToGuides() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        GuidePage guidePage = home.clickNavGuides();

        assertTrue(guidePage.getCurrentUrl().contains("/content/guide/"),
                "Must navigate to the guides page. Actual URL: " + guidePage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-15: Clicking Clans navigates to the clans page")
    void testNavigateToClans() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        ClansPage clansPage = home.clickNavClans();

        assertTrue(clansPage.getCurrentUrl().contains("/clanwars/"),
                "Must navigate to the clans page. Actual URL: " + clansPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-16: Clicking Tournaments navigates to the tournaments page")
    void testNavigateToTournaments() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        TournamentsPage tournamentsPage = home.clickNavTournaments();

        assertTrue(tournamentsPage.getCurrentUrl().contains("/tournaments/"),
                "Must navigate to the tournaments page. Actual URL: " + tournamentsPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-17: Clicking Community hub navigates to the community hub page")
    void testNavigateToCommunityHub() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        CommunityPage communityPage = home.clickNavCommunityHub();

        assertTrue(communityPage.getCurrentUrl().contains("/community/"),
                "Must navigate to the community hub page. Actual URL: " + communityPage.getCurrentUrl());
    }
}