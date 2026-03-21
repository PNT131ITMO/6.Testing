package lab3.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class NewsPage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/news/";

    private static final String XPATH_PAGE_HEADER =
            "//h1[contains(normalize-space(.),'НОВОСТИ')]";

    private static final String XPATH_FILTER_BLOCK =
            "//*[contains(normalize-space(.),'Фильтр') and contains(normalize-space(.),'Категории')] | //*[contains(normalize-space(.),'Категории')]";

    // Bám từ header NEWS trở xuống, không phụ thuộc vào <main>
    private static final String XPATH_NEWS_CARD_LINKS =
            "//h1[contains(normalize-space(.),'НОВОСТИ')]/following::a[" +
            "contains(@href,'/ru/news/') " +
            "and normalize-space(.) != '' " +
            "and string-length(normalize-space(.)) > 10 " +
            "and not(contains(@href,'discord.gg')) " +
            "and not(contains(normalize-space(.),'RSS')) " +
            "and not(contains(normalize-space(.),'Все новости'))" +
            "]";

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
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAGE_HEADER)));
        return this;
    }

    public ArticlePage clickFirstNews() {
        wait.until(d -> !d.findElements(By.xpath(XPATH_NEWS_CARD_LINKS)).isEmpty());

        List<WebElement> links = driver.findElements(By.xpath(XPATH_NEWS_CARD_LINKS));

        for (WebElement link : links) {
            try {
                if (link.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});", link);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                    return new ArticlePage(driver);
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }

        throw new NoSuchElementException("No visible news card link found on News page.");
    }

    public boolean isPageHeaderVisible() {
        return isVisibleByXPath(XPATH_PAGE_HEADER);
    }

    public boolean isFilterBlockVisible() {
        return isVisibleByXPath(XPATH_FILTER_BLOCK);
    }

    public boolean areNewsCardsVisible() {
        try {
            wait.until(d -> !d.findElements(By.xpath(XPATH_NEWS_CARD_LINKS)).isEmpty());
        } catch (TimeoutException e) {
            return false;
        }

        List<WebElement> links = driver.findElements(By.xpath(XPATH_NEWS_CARD_LINKS));
        System.out.println("News links found: " + links.size());

        for (WebElement link : links) {
            try {
                if (link.isDisplayed()) {
                    System.out.println("First visible news title: " + link.getText());
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    public boolean isDateVisible() {
        return isVisibleByXPath(XPATH_ARTICLE_DATE);
    }

    public boolean isRssLabelVisible() {
        return isVisibleByXPath(XPATH_RSS_LABEL);
    }
}