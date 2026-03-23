package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class GuidePage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/content/guide/";

    private static final String XPATH_PAGE_HEADER =
            "//*[self::h1 or self::h2][contains(normalize-space(.),'Руководства')]";

    private static final String XPATH_NEWBIE_SECTION =
            "//*[normalize-space(.)='Руководство для новичков' or contains(normalize-space(.),'Руководство для новичков')]";

    private static final String XPATH_MAIN_SECTION =
            "//*[normalize-space(.)='Основное' or contains(normalize-space(.),'Основное')]";

    private static final String XPATH_ECONOMY_SECTION =
            "//*[normalize-space(.)='Экономика World of Tanks' or contains(normalize-space(.),'Экономика World of Tanks')]";

    private static final String XPATH_RULES_SECTION =
            "//*[normalize-space(.)='Играем по правилам' or contains(normalize-space(.),'Играем по правилам')]";

    private static final String XPATH_GUIDE_ARTICLE_LINKS =
            "//a[contains(@href,'/content/guide/') and normalize-space(.) != '' " +
            "and not(contains(normalize-space(.),'Руководства')) " +
            "and not(contains(normalize-space(.),'Главная'))]";

    private static final String XPATH_GETTING_STARTED_LINK =
            "//a[contains(@href,'/content/guide/') and contains(normalize-space(.),'Знакомство с игрой')]";

    public GuidePage(WebDriver driver) {
        super(driver);
    }

    public GuidePage open() {
        driver.get(URL);
        waitForUrlContains("/content/guide/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAGE_HEADER)));
        return this;
    }

    private boolean hasVisibleElement(String xpath) {
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        for (WebElement element : elements) {
            try {
                if (element.isDisplayed() && !element.getText().trim().isEmpty()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    public GuideArticlePage clickGettingStartedArticle() {
        String oldUrl = driver.getCurrentUrl();
        List<WebElement> links = driver.findElements(By.xpath(XPATH_GETTING_STARTED_LINK));

        for (WebElement link : links) {
            try {
                if (link.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});", link);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

                    wait.until(d -> !d.getCurrentUrl().equals(oldUrl));
                    wait.until(d -> d.getCurrentUrl().contains("/content/guide/"));
                    return new GuideArticlePage(driver);
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }

        throw new IllegalStateException("Visible guide article link 'Знакомство с игрой' not found.");
    }

    public boolean isPageHeaderVisible() {
        return hasVisibleElement(XPATH_PAGE_HEADER);
    }

    public boolean isNewbieSectionVisible() {
        return hasVisibleElement(XPATH_NEWBIE_SECTION);
    }

    public boolean isMainSectionVisible() {
        return hasVisibleElement(XPATH_MAIN_SECTION);
    }

    public boolean isEconomySectionVisible() {
        return hasVisibleElement(XPATH_ECONOMY_SECTION);
    }

    public boolean isRulesSectionVisible() {
        return hasVisibleElement(XPATH_RULES_SECTION);
    }

    public boolean areGuideLinksVisible() {
        return hasVisibleElement(XPATH_GUIDE_ARTICLE_LINKS);
    }
}