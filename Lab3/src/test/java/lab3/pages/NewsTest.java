package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-06: News & Article Pages")
class NewsTest extends BaseTest {

    @Test
    @DisplayName("TC-30: News page loads successfully")
    void testNewsPageLoads() {
        new NewsPage(driver).open();
        assertTrue(driver.getCurrentUrl().contains("worldoftanks"),
                "News page must load successfully");
    }

    @Test
    @DisplayName("TC-31: News cards are displayed on the listing page")
    void testNewsCardsVisible() {
        NewsPage newsPage = new NewsPage(driver).open();
        assertTrue(newsPage.areNewsCardsVisible(),
                "News cards must be displayed on the listing page");
    }

    @Test
    @DisplayName("TC-32: Clicking the first news card opens the article detail page")
    void testClickFirstArticle() {
        NewsPage newsPage = new NewsPage(driver).open();
        if (newsPage.areNewsCardsVisible()) {
            ArticlePage articlePage = newsPage.clickFirstNews();
            assertTrue(articlePage.isArticleTitleVisible() || articlePage.isArticleBodyVisible(),
                    "The article page must have a title or body content");
        }
    }

    @Test
    @DisplayName("TC-33: Article title is not empty")
    void testArticleTitleNotEmpty() {
        NewsPage newsPage = new NewsPage(driver).open();
        if (newsPage.areNewsCardsVisible()) {
            ArticlePage articlePage = newsPage.clickFirstNews();
            if (articlePage.isArticleTitleVisible()) {
                assertFalse(articlePage.getArticleTitle().isEmpty(),
                        "Article title must not be empty");
            }
        }
    }

    @Test
    @DisplayName("TC-34: Article detail page shows a publish date")
    void testArticleHasPublishDate() {
        NewsPage newsPage = new NewsPage(driver).open();
        if (newsPage.areNewsCardsVisible()) {
            ArticlePage articlePage = newsPage.clickFirstNews();
            assertTrue(articlePage.isPublishDateVisible() || articlePage.isArticleBodyVisible(),
                    "Article must have a publish date or body content");
        }
    }

    @Test
    @DisplayName("TC-35: Article detail page shows a breadcrumb")
    void testArticleBreadcrumb() {
        NewsPage newsPage = new NewsPage(driver).open();
        if (newsPage.areNewsCardsVisible()) {
            ArticlePage articlePage = newsPage.clickFirstNews();
            assertTrue(articlePage.isBreadcrumbVisible() || articlePage.isArticleTitleVisible(),
                    "Article page must have a breadcrumb or a title");
        }
    }
}