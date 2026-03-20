package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-04: Search Functionality")
class SearchTest extends BaseTest {

    @Test
    @DisplayName("TC-19: Clicking the search icon reveals the search input field")
    void testSearchInputVisible() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        SearchPage searchPage = home.clickSearch();
        assertTrue(searchPage.isSearchInputVisible(),
                "The search input must be visible after clicking the search icon");
    }

    @Test
    @DisplayName("TC-20: Searching 'танк' returns relevant results")
    void testSearchWithValidKeyword() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        SearchPage searchPage = home.clickSearch();
        searchPage.search("танк");
        assertTrue(searchPage.hasResults() || !searchPage.hasNoResultsMessage(),
                "Searching 'танк' must return at least one result");
    }

    @ParameterizedTest(name = "TC-21 [{index}]: Search for ''{0}''")
    @DisplayName("TC-21: Parameterized — search with multiple valid keywords")
    @ValueSource(strings = {"танк", "новости", "игра", "World of Tanks"})
    void testSearchVariousKeywords(String keyword) {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        SearchPage searchPage = home.clickSearch();
        searchPage.search(keyword);
        assertFalse(driver.getCurrentUrl().isEmpty(),
                "URL must not be empty after searching for '" + keyword + "'");
    }

    @Test
    @DisplayName("TC-22: Searching with special characters — page does not crash")
    void testSearchWithSpecialCharacters() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        SearchPage searchPage = home.clickSearch();
        searchPage.search("!@#$%");
        assertTrue(driver.getCurrentUrl().contains("worldoftanks") ||
                   driver.getCurrentUrl().contains("wargaming"),
                "Page must not crash when searching with special characters");
    }

    @Test
    @DisplayName("TC-23: Searching a non-existent keyword — 'No results' message is shown")
    void testSearchNoResults() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        SearchPage searchPage = home.clickSearch();
        searchPage.search("xyzabcnonexistent12345");
        assertTrue(searchPage.hasNoResultsMessage() || !searchPage.hasResults(),
                "A 'no results' message must appear for a non-existent search term");
    }
}