package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-08: Tournaments")
class TournamentsTest extends BaseTest {

    @Test
    @DisplayName("TC-46: Tournaments page loads successfully")
    void testTournamentsPageLoads() {
        TournamentsPage tournamentsPage = new TournamentsPage(driver).open();
        assertTrue(tournamentsPage.getCurrentUrl().contains("/tournaments/"),
                "Tournaments page must load successfully. URL: " + tournamentsPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-47: Tournaments header and active section are visible")
    void testTournamentsHeaderVisible() {
        TournamentsPage tournamentsPage = new TournamentsPage(driver).open();
        assertAll(
                () -> assertTrue(tournamentsPage.isPageHeaderVisible(), "Tournaments header must be visible"),
                () -> assertTrue(tournamentsPage.isActiveSectionVisible(), "Active tournaments section must be visible")
        );
    }

    @Test
    @DisplayName("TC-48: Search tournaments link and guide link are visible")
    void testTournamentsControlsVisible() {
        TournamentsPage tournamentsPage = new TournamentsPage(driver).open();
        assertAll(
                () -> assertTrue(tournamentsPage.isSearchTournamentsLinkVisible(), "Search tournaments link must be visible"),
                () -> assertTrue(tournamentsPage.isGuideLinkVisible(), "Tournament guide link must be visible")
        );
    }

    @Test
    @DisplayName("TC-49: Tournament status labels are visible")
    void testTournamentsStatusesVisible() {
        TournamentsPage tournamentsPage = new TournamentsPage(driver).open();
        assertTrue(
                tournamentsPage.hasSoonStatusVisible() || tournamentsPage.hasRegistrationOpenStatusVisible(),
                "At least one tournament status label must be visible"
        );
    }

    @Test
    @DisplayName("TC-50: Tournament actions or details are visible")
    void testTournamentsActionsVisible() {
        TournamentsPage tournamentsPage = new TournamentsPage(driver).open();
        assertTrue(
                tournamentsPage.hasJoinActionVisible() || tournamentsPage.hasDetailsVisible(),
                "Join action or details link must be visible on tournaments page"
        );
    }
}