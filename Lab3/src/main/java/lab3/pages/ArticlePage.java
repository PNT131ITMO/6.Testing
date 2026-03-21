package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ArticlePage extends BasePage {

    // Title thật nằm ngay sau breadcrumb/news navigation
    private static final String XPATH_ARTICLE_TITLE =
            "(//a[normalize-space()='Главная']/following::h1[1])[1]";

    private static final String XPATH_BREADCRUMB =
            "//a[normalize-space()='Главная'] | //a[contains(@href,'/ru/news/')]";

    private static final String XPATH_PUBLISH_DATE =
            "//time | //*[@datetime] | //*[contains(@class,'date')]";

    // Nội dung đầu tiên đủ dài sau title
    private static final String XPATH_ARTICLE_BODY =
            "(" + XPATH_ARTICLE_TITLE + ")/following::*[" +
            "(self::p or self::div or self::section or self::span or self::h2 or self::h3)" +
            " and string-length(normalize-space(.)) > 25" +
            "][1]";

    private static final String XPATH_BACK_TO_NEWS =
            "//a[contains(normalize-space(.),'Все новости') or contains(@href,'/ru/news/')]";

    public ArticlePage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_BREADCRUMB)));
    }

    public NewsPage clickBackToNews() {
        clickByXPath(XPATH_BACK_TO_NEWS);
        return new NewsPage(driver);
    }

    public boolean isArticleTitleVisible() {
        List<WebElement> titles = driver.findElements(By.xpath(XPATH_ARTICLE_TITLE));
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
        return isVisibleByXPath(XPATH_BREADCRUMB);
    }

    public String getArticleTitle() {
        List<WebElement> titles = driver.findElements(By.xpath(XPATH_ARTICLE_TITLE));
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