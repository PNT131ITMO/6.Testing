package lab3.pages;

import org.openqa.selenium.WebDriver;

public class SearchPage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/community/accounts/";

    private static final String XPATH_PAGE_HEADER =
            "//h1[contains(normalize-space(.),'Игроки') or contains(normalize-space(.),'Статистика игроков')]";
    private static final String XPATH_SEARCH_INPUT =
            "(//input[not(@type='hidden') and not(@type='checkbox') and not(@type='submit')])[1]";
    private static final String XPATH_SEARCH_SUBMIT =
            "//button[normalize-space()='Поиск'] | //input[@type='submit' and (@value='Поиск' or @value='Search')]";
    private static final String XPATH_MIN_CHARS_HINT =
            "//*[contains(normalize-space(.),'Минимальное количество символов для поиска: 3')]";
    private static final String XPATH_NO_RESULTS =
            "//*[contains(normalize-space(.),'ничего не найдено')]";
    private static final String XPATH_RESULTS_TABLE =
            "//*[contains(normalize-space(.),'Имя') and contains(normalize-space(.),'Бои') and contains(normalize-space(.),'Победы')]";
    private static final String XPATH_CLEAR_BTN =
            "//*[normalize-space()='Очистить']";

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public SearchPage open() {
        driver.get(URL);
        waitForUrlContains("/community/accounts/");
        return this;
    }

    public SearchPage typeQuery(String query) {
        typeByXPath(XPATH_SEARCH_INPUT, query);
        return this;
    }

    public SearchPage submitSearch() {
        if (isClickableByXPath(XPATH_SEARCH_SUBMIT)) {
            clickByXPath(XPATH_SEARCH_SUBMIT);
        }
        return this;
    }

    public SearchPage search(String query) {
        typeQuery(query);
        submitSearch();
        return this;
    }

    public boolean isPageHeaderVisible() { return isVisibleByXPath(XPATH_PAGE_HEADER); }
    public boolean isSearchInputVisible() { return isVisibleByXPath(XPATH_SEARCH_INPUT); }
    public boolean isMinCharsHintVisible() { return isVisibleByXPath(XPATH_MIN_CHARS_HINT); }
    public boolean hasNoResultsMessage() { return isVisibleByXPath(XPATH_NO_RESULTS); }
    public boolean hasResultsTable() { return isVisibleByXPath(XPATH_RESULTS_TABLE); }
    public boolean isClearButtonVisible() { return isVisibleByXPath(XPATH_CLEAR_BTN); }
}