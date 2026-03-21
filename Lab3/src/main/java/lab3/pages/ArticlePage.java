package lab3.pages;

import org.openqa.selenium.WebDriver;

public class ArticlePage extends BasePage {

    private static final String XPATH_ARTICLE_TITLE =
            "//h1[normalize-space()]";
    private static final String XPATH_ARTICLE_BODY =
            "//article | //main//*[self::p or self::div][string-length(normalize-space(.)) > 80]";
    private static final String XPATH_PUBLISH_DATE =
            "//time | //*[@datetime] | //*[contains(@class,'date')]";
    private static final String XPATH_BREADCRUMB =
            "//a[contains(@href,'/ru/news/') or normalize-space()='Главная']";
    private static final String XPATH_BACK_TO_NEWS =
            "//a[contains(normalize-space(.),'Все новости') or contains(@href,'/ru/news/')]";

    public ArticlePage(WebDriver driver) {
        super(driver);
    }

    public NewsPage clickBackToNews() {
        clickByXPath(XPATH_BACK_TO_NEWS);
        return new NewsPage(driver);
    }

    public boolean isArticleTitleVisible() { return isVisibleByXPath(XPATH_ARTICLE_TITLE); }
    public boolean isArticleBodyVisible() { return isVisibleByXPath(XPATH_ARTICLE_BODY); }
    public boolean isPublishDateVisible() { return isVisibleByXPath(XPATH_PUBLISH_DATE); }
    public boolean isBreadcrumbVisible() { return isVisibleByXPath(XPATH_BREADCRUMB); }

    public String getArticleTitle() {
        return getTextByXPath(XPATH_ARTICLE_TITLE);
    }
}