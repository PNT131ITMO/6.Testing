package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GuideArticlePage extends BasePage {

    private static final String XPATH_BREADCRUMB =
            "//a[normalize-space()='Главная'] | //a[contains(@href,'/content/guide/')]";

    private static final String XPATH_ARTICLE_TITLES =
            "//h1[string-length(normalize-space(.)) > 5]";

    private static final String XPATH_ARTICLE_BODY =
            "(//h1[string-length(normalize-space(.)) > 5])[1]/following::*[" +
            "(self::p or self::div or self::section or self::li or self::span or self::h2 or self::h3)" +
            " and string-length(normalize-space(.)) > 40" +
            "][1]";

    private static final String XPATH_FIRST_SUBSECTION =
            "(//h1[string-length(normalize-space(.)) > 5])[1]/following::*[" +
            "(self::h2 or self::h3) and string-length(normalize-space(.)) > 2" +
            "][1]";

    public GuideArticlePage(WebDriver driver) {
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

    public boolean isBreadcrumbVisible() {
        return isVisibleByXPath(XPATH_BREADCRUMB);
    }

    public boolean isFirstSubsectionVisible() {
        return isVisibleByXPath(XPATH_FIRST_SUBSECTION);
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