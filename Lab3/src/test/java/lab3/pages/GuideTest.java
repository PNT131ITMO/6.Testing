package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-06: Guides")
class GuideTest extends BaseTest {

    @Test
    @DisplayName("TC-35: Guides page loads successfully")
    void testGuidePageLoads() {
        GuidePage guidePage = new GuidePage(driver).open();
        assertTrue(guidePage.getCurrentUrl().contains("/content/guide/"),
                "Guides page must load successfully. URL: " + guidePage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-36: Guides page header and main categories are visible")
    void testGuideHeaderAndSectionsVisible() {
        GuidePage guidePage = new GuidePage(driver).open();
        assertAll(
                () -> assertTrue(guidePage.isPageHeaderVisible(), "Guides header must be visible"),
                () -> assertTrue(guidePage.isNewbieSectionVisible(), "Newbie section must be visible"),
                () -> assertTrue(guidePage.isMainSectionVisible(), "Main section must be visible")
        );
    }

    @Test
    @DisplayName("TC-37: Economy and rules sections are visible on the guides page")
    void testGuideAdditionalSectionsVisible() {
        GuidePage guidePage = new GuidePage(driver).open();
        assertAll(
                () -> assertTrue(guidePage.isEconomySectionVisible(), "Economy section must be visible"),
                () -> assertTrue(guidePage.isRulesSectionVisible(), "Rules section must be visible")
        );
    }

    @Test
    @DisplayName("TC-38: Guide article links are displayed on the guides page")
    void testGuideLinksVisible() {
        GuidePage guidePage = new GuidePage(driver).open();
        assertTrue(guidePage.areGuideLinksVisible(),
                "Guide article links must be displayed on the guides page");
    }

    @Test
    @DisplayName("TC-39: Clicking a guide article opens the guide detail page")
    void testOpenGuideArticle() {
        GuidePage guidePage = new GuidePage(driver).open();
        GuideArticlePage articlePage = guidePage.clickGettingStartedArticle();

        assertAll(
                () -> assertTrue(articlePage.isArticleTitleVisible(), "Guide article must have a title"),
                () -> assertTrue(articlePage.isArticleBodyVisible(), "Guide article must have body content")
        );
    }

    @Test
    @DisplayName("TC-40: Guide detail page shows breadcrumb or subsection")
    void testGuideArticleMetadataVisible() {
        GuidePage guidePage = new GuidePage(driver).open();
        GuideArticlePage articlePage = guidePage.clickGettingStartedArticle();

        assertTrue(articlePage.isBreadcrumbVisible() || articlePage.isFirstSubsectionVisible(),
                "Guide article must show breadcrumb or at least one subsection");
    }
}