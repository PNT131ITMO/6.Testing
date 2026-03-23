package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GuidePage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/content/guide/";

    private static final String XPATH_PAGE_HEADER =
            "//*[normalize-space()='Руководства' or contains(normalize-space(.),'Руководства')]";

    private static final String XPATH_NEWBIE_SECTION =
            "//*[normalize-space()='Руководство для новичков' or contains(normalize-space(.),'Руководство для новичков')]";

    private static final String XPATH_MAIN_SECTION =
            "//*[normalize-space()='Основное' or contains(normalize-space(.),'Основное')]";

    private static final String XPATH_ECONOMY_SECTION =
            "//*[normalize-space()='Экономика World of Tanks' or contains(normalize-space(.),'Экономика World of Tanks')]";

    private static final String XPATH_RULES_SECTION =
            "//*[normalize-space()='Играем по правилам' or contains(normalize-space(.),'Играем по правилам')]";

    private static final String XPATH_GUIDE_ARTICLE_LINKS =
            "//a[contains(@href,'/content/guide/') and normalize-space(.) != '' " +
            "and not(normalize-space()='Руководства') " +
            "and not(normalize-space()='Главная')]";

    private static final String XPATH_GETTING_STARTED_LINK =
        "//a[contains(@href,'/content/guide/newcomers-guide/getting_started/')]";

    public GuidePage(WebDriver driver) {
        super(driver);
    }

    public GuidePage open() {
        driver.get(URL);
        waitForUrlContains("/content/guide/");
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
        System.out.println("Getting started links found: " + links.size());

        for (WebElement link : links) {
            try {
                String text = link.getText().trim();
                String href = link.getAttribute("href");
                System.out.println("Guide link text: " + text + " | href: " + href);

                if (link.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", link);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

                    wait.until(d -> !d.getCurrentUrl().equals(oldUrl));
                    wait.until(d -> d.getCurrentUrl().contains("/content/guide/newcomers-guide/getting_started/"));
                    return new GuideArticlePage(driver);
                }
            } catch (StaleElementReferenceException ignored) {}
        }

        if (!links.isEmpty()) {
            WebElement first = links.get(0);
            String href = first.getAttribute("href");
            if (href != null && !href.isBlank()) {
                driver.get(href);
                wait.until(d -> d.getCurrentUrl().contains("/content/guide/newcomers-guide/getting_started/"));
                return new GuideArticlePage(driver);
            }
        }

        throw new IllegalStateException("Guide article link '/content/guide/newcomers-guide/getting_started/' not found.");
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