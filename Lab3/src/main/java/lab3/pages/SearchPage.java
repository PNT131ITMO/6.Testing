package lab3.pages;

import org.openqa.selenium.WebDriver;

public class SearchPage extends BasePage {

    private static final String XPATH_SEARCH_INPUT =
            "//input[@type='search' or @name='q' or @name='query' or contains(@class,'search') and @type='text']";
    private static final String XPATH_SEARCH_SUBMIT =
            "//button[@type='submit' and (contains(@class,'search') or ancestor::*[contains(@class,'search')])]"
          + " | //input[@type='submit' and ancestor::*[contains(@class,'search')]]";
    private static final String XPATH_SEARCH_RESULTS =
            "//*[contains(@class,'search-result') or contains(@class,'search-item') or contains(@class,'result-item')]";
    private static final String XPATH_RESULT_TITLES =
            "//*[contains(@class,'search-result')]//h2 | //*[contains(@class,'search-result')]//h3"
          + " | //*[contains(@class,'result-item')]//a";
    private static final String XPATH_NO_RESULTS =
            "//*[contains(text(),'не найдено') or contains(text(),'ничего не найдено') or contains(text(),'No results')]";

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public SearchPage typeQuery(String query) {
        typeByXPath(XPATH_SEARCH_INPUT, query);
        return this;
    }

    public SearchPage submitSearch() {
        clickByXPath(XPATH_SEARCH_SUBMIT);
        return this;
    }

    public SearchPage search(String query) {
        typeQuery(query);
        submitSearch();
        return this;
    }

    public String getFirstResultTitle() {
        return getTextByXPath(XPATH_RESULT_TITLES);
    }

    public boolean hasResults()          { return isVisibleByXPath(XPATH_SEARCH_RESULTS); }
    public boolean hasNoResultsMessage() { return isVisibleByXPath(XPATH_NO_RESULTS); }
    public boolean isSearchInputVisible(){ return isVisibleByXPath(XPATH_SEARCH_INPUT); }
}