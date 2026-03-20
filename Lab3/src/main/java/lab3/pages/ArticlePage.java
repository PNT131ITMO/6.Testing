package lab3.pages;

import org.openqa.selenium.WebDriver;

public class ArticlePage extends BasePage {

    private static final String XPATH_ARTICLE_TITLE =
            "//h1 | //article//h1";
    private static final String XPATH_ARTICLE_BODY =
            "//article | //*[contains(@class,'article-body') or contains(@class,'content-body')]";
    private static final String XPATH_PUBLISH_DATE =
            "//*[contains(@class,'date') or @datetime or contains(@class,'publish')]";
    private static final String XPATH_SHARE_BTNS =
            "//*[contains(@class,'share') or contains(@class,'social')]//a | "
          + "//*[contains(@class,'share')]//button";
    private static final String XPATH_COMMENTS_SECTION =
            "//*[contains(@class,'comments') or contains(@id,'comments')]";
    private static final String XPATH_BREADCRUMB =
            "//*[contains(@class,'breadcrumb')]//a | //nav[@aria-label='breadcrumb']//a";
    private static final String XPATH_BACK_TO_NEWS =
            "//a[contains(text(),'Все новости') or contains(text(),'Назад') or contains(@href,'/news/')]";

    public ArticlePage(WebDriver driver) {
        super(driver);
    }

    public NewsPage clickBackToNews() {
        clickByXPath(XPATH_BACK_TO_NEWS);
        return new NewsPage(driver);
    }

    public boolean isArticleTitleVisible()    { return isVisibleByXPath(XPATH_ARTICLE_TITLE); }
    public boolean isArticleBodyVisible()     { return isVisibleByXPath(XPATH_ARTICLE_BODY); }
    public boolean isPublishDateVisible()     { return isVisibleByXPath(XPATH_PUBLISH_DATE); }
    public boolean isShareButtonsVisible()    { return isVisibleByXPath(XPATH_SHARE_BTNS); }
    public boolean isBreadcrumbVisible()      { return isVisibleByXPath(XPATH_BREADCRUMB); }
    public boolean isCommentsSectionVisible() { return isVisibleByXPath(XPATH_COMMENTS_SECTION); }

    public String getArticleTitle() {
        return getTextByXPath(XPATH_ARTICLE_TITLE);
    }
}