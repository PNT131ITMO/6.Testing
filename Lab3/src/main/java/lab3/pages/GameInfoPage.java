package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class GameInfoPage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/game/";

    private static final String XPATH_PAGE_HEADER =
            "//*[contains(normalize-space(.),'Легендарная танковая онлайн-игра')]";

    private static final String XPATH_REGISTER_BTN =
            "//a[contains(normalize-space(.),'Зарегистрироваться') or contains(@href,'registration')]";

    private static final String XPATH_VIDEO_LINK =
            "//a[contains(normalize-space(.),'Промовидео') or contains(normalize-space(.),'Игровой процесс')]";

    // Bám theo text thật trên trang, không ép phải là h2/h3
    private static final String XPATH_HISTORY_SECTION =
            "//*[normalize-space(.)='Игра с историей' or contains(normalize-space(.),'Игра с историей')]";

    private static final String XPATH_SCREENSHOTS_SECTION =
            "//*[normalize-space(.)='Скриншоты' or contains(normalize-space(.),'Скриншоты')]";

    private static final String XPATH_TANK_CLASSES_SECTION =
            "//*[normalize-space(.)='Пять уникальных классов техники' or contains(normalize-space(.),'Пять уникальных классов техники')]";

    private static final String XPATH_VEHICLES_SECTION =
            "//*[normalize-space(.)='Более 800 боевых машин' or contains(normalize-space(.),'Более 800 боевых машин')]";

    public GameInfoPage(WebDriver driver) {
        super(driver);
    }

    public GameInfoPage open() {
        driver.get(URL);
        waitForUrlContains("/ru/game/");
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

    public boolean isRegisterButtonVisible() {
        return hasVisibleElement(XPATH_REGISTER_BTN);
    }

    public boolean isVideoLinkVisible() {
        return hasVisibleElement(XPATH_VIDEO_LINK);
    }

    public boolean isHistorySectionVisible() {
        return hasVisibleElement(XPATH_HISTORY_SECTION);
    }

    public boolean isScreenshotsSectionVisible() {
        return hasVisibleElement(XPATH_SCREENSHOTS_SECTION);
    }

    public boolean isTankClassesSectionVisible() {
        return hasVisibleElement(XPATH_TANK_CLASSES_SECTION);
    }

    public boolean isVehiclesSectionVisible() {
        return hasVisibleElement(XPATH_VEHICLES_SECTION);
    }

    public String getHeaderText() {
        List<WebElement> headers = driver.findElements(By.xpath(XPATH_PAGE_HEADER));
        for (WebElement header : headers) {
            try {
                if (header.isDisplayed()) {
                    return header.getText().trim();
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return "";
    }
}