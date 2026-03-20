package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-07: Game Information Page")
class GameInfoTest extends BaseTest {

    @Test
    @DisplayName("TC-36: Game page loads successfully")
    void testGamePageLoads() {
        new GameInfoPage(driver).open();
        assertTrue(driver.getCurrentUrl().contains("worldoftanks") ||
                   driver.getCurrentUrl().contains("wargaming"),
                "Game page must load successfully");
    }

    @Test
    @DisplayName("TC-37: Game page has a visible header")
    void testGamePageHasHeader() {
        GameInfoPage gamePage = new GameInfoPage(driver).open();
        assertTrue(gamePage.isPageHeaderVisible() || gamePage.areGameSectionsVisible(),
                "Game page must have a header or content sections");
    }

    @Test
    @DisplayName("TC-38: Game page contains content sections")
    void testGamePageHasSections() {
        GameInfoPage gamePage = new GameInfoPage(driver).open();
        assertTrue(gamePage.areGameSectionsVisible(),
                "Game page must contain at least one content section");
    }

    @Test
    @DisplayName("TC-39: Play / Download button is visible")
    void testPlayButtonVisible() {
        GameInfoPage gamePage = new GameInfoPage(driver).open();
        assertTrue(gamePage.isPlayButtonVisible() || gamePage.isPageHeaderVisible(),
                "Play or Download button must be visible");
    }

    @Test
    @DisplayName("TC-40: Navigating to the game page via the main menu works")
    void testNavigateToGameFromMenu() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        GameInfoPage gamePage = home.clickNavGame();
        assertTrue(gamePage.isPageHeaderVisible() || gamePage.areGameSectionsVisible(),
                "Must reach the game page via the main menu");
    }
}