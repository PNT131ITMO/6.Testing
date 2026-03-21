package lab3.pages;

import org.openqa.selenium.WebDriver;

public class NewsPage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/news/";

    private static final String XPATH_PAGE_HEADER =
            "//h1[contains(normalize-space(.),'НОВОСТИ')]";
    private static final String XPATH_FILTER_BLOCK =
            "//*[contains(normalize-space(.),'Фильтр') and contains(normalize-space(.),'Категории')] | //*[contains(normalize-space(.),'Категории')]";
    private static final String XPATH_FIRST_NEWS_LINK =
            "(//a[contains(@href,'/ru/news/') and string-length(normalize-space(.)) > 15 and not(contains(normalize-space(.),'Все новости')) and not(contains(normalize-space(.),'RSS'))])[1]";
    private static final String XPATH_NEWS_CARDS =
            XPATH_FIRST_NEWS_LINK;
    private static final String XPATH_ARTICLE_DATE =
            "//time | //*[@datetime] | //*[contains(@class,'date')]";
    private static final String XPATH_RSS_LABEL =
            "//*[contains(normalize-space(.),'RSS')]";

    public NewsPage(WebDriver driver) {
        super(driver);
    }

    public NewsPage open() {
        driver.get(URL);
        waitForUrlContains("/ru/news/");
        return this;
    }

    public ArticlePage clickFirstNews() {
        clickByXPath(XPATH_FIRST_NEWS_LINK);
        return new ArticlePage(driver);
    }

    public boolean isPageHeaderVisible() { return isVisibleByXPath(XPATH_PAGE_HEADER); }
    public boolean isFilterBlockVisible() { return isVisibleByXPath(XPATH_FILTER_BLOCK); }
    public boolean areNewsCardsVisible() { return isVisibleByXPath(XPATH_NEWS_CARDS); }
    public boolean isDateVisible() { return isVisibleByXPath(XPATH_ARTICLE_DATE); }
    public boolean isRssLabelVisible() { return isVisibleByXPath(XPATH_RSS_LABEL); }
}