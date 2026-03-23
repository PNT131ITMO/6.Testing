package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ClansPage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/clanwars/?link_place=wotp_link_main-menu";

    private static final String XPATH_PAGE_HEADER =
            "//*[self::h1 or self::h2][normalize-space()='Кланы' or contains(normalize-space(.),'Кланы')]";

    private static final String XPATH_FIND_CLAN_BTN =
            "//a[contains(normalize-space(.),'Подобрать клан')]";

    private static final String XPATH_CREATE_CLAN_BTN =
            "//a[contains(normalize-space(.),'Создать клан')]";

    private static final String XPATH_CLAN_PORTAL_LINK =
            "//a[contains(normalize-space(.),'Перейти на клановый портал')]";

    private static final String XPATH_STRONGHOLD_SECTION =
            "//*[self::h2 or self::h3][normalize-space()='Укрепрайон' or contains(normalize-space(.),'Укрепрайон')]";

    private static final String XPATH_GLOBAL_MAP_SECTION =
            "//*[self::h2 or self::h3][normalize-space()='Глобальная карта' or contains(normalize-space(.),'Глобальная карта')]";

    private static final String XPATH_TOP_CLANS_SECTION =
            "//*[self::h2 or self::h3][normalize-space()='Лучшие кланы' or contains(normalize-space(.),'Лучшие кланы')]";

    private static final String XPATH_CLAN_RATING_BLOCK =
            "//*[contains(normalize-space(.),'Рейтинг клана') or contains(normalize-space(.),'Рейтинг кланов')]";

    public ClansPage(WebDriver driver) {
        super(driver);
    }

    public ClansPage open() {
        driver.get(URL);
        waitForUrlContains("/clanwars/");
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

    public boolean isFindClanButtonVisible() {
        return hasVisibleElement(XPATH_FIND_CLAN_BTN);
    }

    public boolean isCreateClanButtonVisible() {
        return hasVisibleElement(XPATH_CREATE_CLAN_BTN);
    }

    public boolean isClanPortalLinkVisible() {
        return hasVisibleElement(XPATH_CLAN_PORTAL_LINK);
    }

    public boolean isStrongholdSectionVisible() {
        return hasVisibleElement(XPATH_STRONGHOLD_SECTION);
    }

    public boolean isGlobalMapSectionVisible() {
        return hasVisibleElement(XPATH_GLOBAL_MAP_SECTION);
    }

    public boolean isTopClansSectionVisible() {
        return hasVisibleElement(XPATH_TOP_CLANS_SECTION);
    }

    public boolean isClanRatingBlockVisible() {
        return hasVisibleElement(XPATH_CLAN_RATING_BLOCK);
    }
}