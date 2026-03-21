package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-04: Player Search")
class SearchTest extends BaseTest {

    @Test
    @DisplayName("TC-35: Player search page loads successfully")
    void testSearchPageLoads() {
        SearchPage searchPage = new SearchPage(driver).open();
        assertTrue(searchPage.getCurrentUrl().contains("/community/accounts/"),
                "Player search page must load successfully. URL: " + searchPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-36: Search header and input are visible")
    void testSearchInputVisible() {
        SearchPage searchPage = new SearchPage(driver).open();
        assertAll(
                () -> assertTrue(searchPage.isPageHeaderVisible(), "Player search header must be visible"),
                () -> assertTrue(searchPage.isSearchInputVisible(), "Search input must be visible")
        );
    }

    @Test
    @DisplayName("TC-37: Minimum character hint is displayed")
    void testMinCharacterHintVisible() {
        SearchPage searchPage = new SearchPage(driver).open();
        assertTrue(searchPage.isMinCharsHintVisible(),
                "The minimum character hint must be visible");
    }

    @Test
    @DisplayName("TC-38: Search page shows result table headers")
    void testResultsTableVisible() {
        SearchPage searchPage = new SearchPage(driver).open();
        assertTrue(searchPage.hasResultsTable(),
                "The result table headers must be visible on the player search page");
    }

    @Test
    @DisplayName("TC-39: Searching a non-existent player shows a no-results message")
    void testSearchNoResults() {
        SearchPage searchPage = new SearchPage(driver).open();
        searchPage.search("xyzabcnonexistent12345");
        assertTrue(searchPage.hasNoResultsMessage(),
                "A no-results message must appear for a non-existent player name");
    }

    @Test
    @DisplayName("TC-40: Clear control is visible on the player search page")
    void testClearButtonVisible() {
        SearchPage searchPage = new SearchPage(driver).open();
        assertTrue(searchPage.isClearButtonVisible(),
                "The clear control must be visible on the player search page");
    }
}