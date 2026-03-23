package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-04: Game Information Page")
class GameInfoTest extends BaseTest {

    @Test
    @DisplayName("TC-24: Game page loads successfully")
    void testGamePageLoads() {
        GameInfoPage gamePage = new GameInfoPage(driver).open();
        assertTrue(gamePage.getCurrentUrl().contains("/ru/game/"),
                "Game page must load successfully. URL: " + gamePage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-25: Game page has a visible header")
    void testGamePageHasHeader() {
        GameInfoPage gamePage = new GameInfoPage(driver).open();
        assertTrue(gamePage.isPageHeaderVisible(),
                "Game page must have a visible header");
    }

    @Test
    @DisplayName("TC-26: Game page contains history and screenshots sections")
    void testGamePageMainSections() {
        GameInfoPage gamePage = new GameInfoPage(driver).open();
        assertAll(
                () -> assertTrue(gamePage.isHistorySectionVisible(), "History section must be visible"),
                () -> assertTrue(gamePage.isScreenshotsSectionVisible(), "Screenshots section must be visible")
        );
    }

    @Test
    @DisplayName("TC-27: Game page contains tank classes and vehicles sections")
    void testGamePageVehicleSections() {
        GameInfoPage gamePage = new GameInfoPage(driver).open();
        assertAll(
                () -> assertTrue(gamePage.isTankClassesSectionVisible(), "Tank classes section must be visible"),
                () -> assertTrue(gamePage.isVehiclesSectionVisible(), "Vehicles section must be visible")
        );
    }

    @Test
    @DisplayName("TC-28: Game page shows register/download entry points")
    void testRegisterOrVideoVisible() {
        GameInfoPage gamePage = new GameInfoPage(driver).open();
        assertTrue(gamePage.isRegisterButtonVisible() || gamePage.isVideoLinkVisible(),
                "Game page must expose a register/download or promo/video entry point");
    }
}