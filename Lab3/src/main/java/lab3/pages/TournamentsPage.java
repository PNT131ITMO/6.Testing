package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class TournamentsPage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/tournaments/";

    private static final String XPATH_PAGE_HEADER =
            "//*[self::h1 or self::h2][normalize-space()='Турниры' or contains(normalize-space(.),'Турниры')]";

    private static final String XPATH_ACTIVE_SECTION =
            "//*[self::h2 or self::h3][normalize-space()='Актуальные' or contains(normalize-space(.),'Актуальные')]";

    private static final String XPATH_SEARCH_TOURNAMENTS_LINK =
            "//a[contains(normalize-space(.),'Поиск турниров')]";

    private static final String XPATH_GUIDE_LINK =
            "//a[contains(normalize-space(.),'Турниры: руководство')]";

    private static final String XPATH_STATUS_SOON =
            "//*[contains(normalize-space(.),'Скоро')]";

    private static final String XPATH_STATUS_REG_OPEN =
            "//*[contains(normalize-space(.),'Регистрация открыта')]";

    private static final String XPATH_ACTION_JOIN =
            "//*[contains(normalize-space(.),'Участвовать')]";

    private static final String XPATH_DETAILS_LINK =
            "//*[contains(normalize-space(.),'Подробнее')]";

    public TournamentsPage(WebDriver driver) {
        super(driver);
    }

    public TournamentsPage open() {
        driver.get(URL);
        waitForUrlContains("/tournaments/");
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

    public boolean isPageHeaderVisible() {
        return hasVisibleElement(XPATH_PAGE_HEADER);
    }

    public boolean isActiveSectionVisible() {
        return hasVisibleElement(XPATH_ACTIVE_SECTION);
    }

    public boolean isSearchTournamentsLinkVisible() {
        return hasVisibleElement(XPATH_SEARCH_TOURNAMENTS_LINK);
    }

    public boolean isGuideLinkVisible() {
        return hasVisibleElement(XPATH_GUIDE_LINK);
    }

    public boolean hasSoonStatusVisible() {
        return hasVisibleElement(XPATH_STATUS_SOON);
    }

    public boolean hasRegistrationOpenStatusVisible() {
        return hasVisibleElement(XPATH_STATUS_REG_OPEN);
    }

    public boolean hasJoinActionVisible() {
        return hasVisibleElement(XPATH_ACTION_JOIN);
    }

    public boolean hasDetailsVisible() {
        return hasVisibleElement(XPATH_DETAILS_LINK);
    }
}