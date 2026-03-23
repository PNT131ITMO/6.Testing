package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ArticlePage extends BasePage {

    private static final String XPATH_ARTICLE_TITLES =
            "//h1[string-length(normalize-space(.)) > 8]";

    private static final String XPATH_ARTICLE_BODY =
            "(//h1[string-length(normalize-space(.)) > 8])[1]/following::*[" +
            "(self::p or self::div or self::section or self::span or self::h2 or self::h3 or self::h4)" +
            " and string-length(normalize-space(.)) > 40" +
            "][1]";

    private static final String XPATH_PUBLISH_DATE =
            "//*[contains(normalize-space(.),'Дата и время')] | //time | //*[@datetime]";

    private static final String XPATH_FIRST_SECTION =
            "(//h1[string-length(normalize-space(.)) > 8])[1]/following::*[" +
            "(self::h2 or self::h3 or self::h4)" +
            " and string-length(normalize-space(.)) > 3" +
            "][1]";

    private static final String XPATH_NEWS_LINK =
            "//a[contains(@href,'/ru/news/')]";

    public ArticlePage(WebDriver driver) {
        super(driver);
    }

    public boolean isArticleTitleVisible() {
        List<WebElement> titles = driver.findElements(By.xpath(XPATH_ARTICLE_TITLES));
        for (WebElement title : titles) {
            try {
                if (title.isDisplayed() && !title.getText().trim().isEmpty()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    public boolean isArticleBodyVisible() {
        return isVisibleByXPath(XPATH_ARTICLE_BODY);
    }

    public boolean isPublishDateVisible() {
        return isVisibleByXPath(XPATH_PUBLISH_DATE);
    }

    public boolean isBreadcrumbVisible() {
        return isVisibleByXPath(XPATH_NEWS_LINK);
    }

    public boolean isSectionVisible() {
        return isVisibleByXPath(XPATH_FIRST_SECTION);
    }

    public String getArticleTitle() {
        List<WebElement> titles = driver.findElements(By.xpath(XPATH_ARTICLE_TITLES));
        for (WebElement title : titles) {
            try {
                if (title.isDisplayed()) {
                    return title.getText().trim();
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return "";
    }
}