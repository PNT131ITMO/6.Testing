package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-06: News & Article Pages")
class NewsTest extends BaseTest {

    @Test
    @DisplayName("TC-18: News page loads successfully")
    void testNewsPageLoads() {
        NewsPage newsPage = new NewsPage(driver).open();
        assertTrue(newsPage.getCurrentUrl().contains("/ru/news/"),
                "News page must load successfully. URL: " + newsPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-19: News page header and RSS label are visible")
    void testNewsHeaderVisible() {
        NewsPage newsPage = new NewsPage(driver).open();
        assertAll(
                () -> assertTrue(newsPage.isPageHeaderVisible(), "News header must be visible"),
                () -> assertTrue(newsPage.isRssLabelVisible(), "RSS label must be visible")
        );
    }

    @Test
    @DisplayName("TC-20: Filter block is visible on the news page")
    void testFilterBlockVisible() {
        NewsPage newsPage = new NewsPage(driver).open();
        assertTrue(newsPage.isFilterBlockVisible(),
                "Filter block must be visible on the news page");
    }

    @Test
    @DisplayName("TC-21: News cards are displayed on the listing page")
    void testNewsCardsVisible() {
        NewsPage newsPage = new NewsPage(driver).open();
        assertTrue(newsPage.areNewsCardsVisible(),
                "News cards must be displayed on the listing page");
    }

    @Test
    @DisplayName("TC-22: Clicking the first news card opens the article detail page")
    void testClickFirstArticle() {
        NewsPage newsPage = new NewsPage(driver).open();
        ArticlePage articlePage = newsPage.clickFirstNews();
        assertAll(
                () -> assertTrue(articlePage.isArticleTitleVisible(), "The article page must have a title"),
                () -> assertTrue(articlePage.isArticleBodyVisible(), "The article page must have body content")
        );
    }

    @Test
    @DisplayName("TC-23: Article detail page shows a publish date or breadcrumb")
    void testArticleMetadataVisible() {
        NewsPage newsPage = new NewsPage(driver).open();
        ArticlePage articlePage = newsPage.clickFirstNews();
        assertTrue(articlePage.isPublishDateVisible() || articlePage.isBreadcrumbVisible(),
                "Article page must have a publish date or breadcrumb");
    }
}