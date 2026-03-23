package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-07: Clans")
class ClansTest extends BaseTest {

    @Test
    @DisplayName("TC-41: Clans page loads successfully")
    void testClansPageLoads() {
        ClansPage clansPage = new ClansPage(driver).open();
        assertTrue(clansPage.getCurrentUrl().contains("/clanwars/"),
                "Clans page must load successfully. URL: " + clansPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-42: Clans page header and main action buttons are visible")
    void testClansHeaderAndActionsVisible() {
        ClansPage clansPage = new ClansPage(driver).open();
        assertAll(
                () -> assertTrue(clansPage.isPageHeaderVisible(), "Clans header must be visible"),
                () -> assertTrue(clansPage.isFindClanButtonVisible(), "Find clan button must be visible"),
                () -> assertTrue(clansPage.isCreateClanButtonVisible(), "Create clan button must be visible")
        );
    }

    @Test
    @DisplayName("TC-43: Clan portal link is visible")
    void testClanPortalLinkVisible() {
        ClansPage clansPage = new ClansPage(driver).open();
        assertTrue(clansPage.isClanPortalLinkVisible(),
                "Clan portal link must be visible");
    }

    @Test
    @DisplayName("TC-44: Stronghold and Global Map sections are visible")
    void testClansMainSectionsVisible() {
        ClansPage clansPage = new ClansPage(driver).open();
        assertAll(
                () -> assertTrue(clansPage.isStrongholdSectionVisible(), "Stronghold section must be visible"),
                () -> assertTrue(clansPage.isGlobalMapSectionVisible(), "Global Map section must be visible")
        );
    }

    @Test
    @DisplayName("TC-45: Top clans and clan rating blocks are visible")
    void testClansRankingBlocksVisible() {
        ClansPage clansPage = new ClansPage(driver).open();
        assertAll(
                () -> assertTrue(clansPage.isTopClansSectionVisible(), "Top clans section must be visible"),
                () -> assertTrue(clansPage.isClanRatingBlockVisible(), "Clan rating block must be visible")
        );
    }
}