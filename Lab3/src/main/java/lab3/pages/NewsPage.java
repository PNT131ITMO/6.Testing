package lab3.pages;

import org.openqa.selenium.WebDriver;

public class NewsPage extends BasePage {

    public static final String URL = "https://worldoftanks.ru/ru/news/";

    private static final String XPATH_PAGE_HEADER =
            "//h1 | //h2[contains(translate(text(),'НОВОСТИ','новости'),'новости')]";
    private static final String XPATH_NEWS_CARDS =
            "//*[contains(@class,'news-item') or contains(@class,'article-item') or contains(@class,'card')]";
    private static final String XPATH_FIRST_NEWS_LINK =
            "(//*[contains(@class,'news-item') or contains(@class,'article-item') or contains(@class,'card')]//a)[1]";
    private static final String XPATH_FIRST_NEWS_TITLE =
            "(//*[contains(@class,'news-item') or contains(@class,'article-item')]//h2 | "
          + "//*[contains(@class,'news-item')]//h3)[1]";
    private static final String XPATH_LOAD_MORE_BTN =
            "//button[contains(text(),'Показать ещё') or contains(text(),'Загрузить') or contains(@class,'load-more')]";
    private static final String XPATH_PAGINATION =
            "//*[contains(@class,'pagination')]";
    private static final String XPATH_ARTICLE_DATE =
            "//*[contains(@class,'date') or @datetime]";

    public NewsPage(WebDriver driver) {
        super(driver);
    }

    public NewsPage open() {
        driver.get(URL);
        return this;
    }

    public ArticlePage clickFirstNews() {
        clickByXPath(XPATH_FIRST_NEWS_LINK);
        return new ArticlePage(driver);
    }

    public NewsPage clickLoadMore() {
        if (isVisibleByXPath(XPATH_LOAD_MORE_BTN)) {
            clickByXPath(XPATH_LOAD_MORE_BTN);
        }
        return this;
    }

    public boolean isPageHeaderVisible()  { return isVisibleByXPath(XPATH_PAGE_HEADER); }
    public boolean areNewsCardsVisible()  { return isVisibleByXPath(XPATH_NEWS_CARDS); }
    public boolean isPaginationVisible()  { return isVisibleByXPath(XPATH_PAGINATION); }
    public boolean isDateVisible()        { return isVisibleByXPath(XPATH_ARTICLE_DATE); }

    public String getFirstNewsTitle() {
        return getTextByXPath(XPATH_FIRST_NEWS_TITLE);
    }
}