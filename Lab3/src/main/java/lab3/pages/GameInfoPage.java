package lab3.pages;

import org.openqa.selenium.WebDriver;

public class GameInfoPage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/game/";

    private static final String XPATH_PAGE_HEADER =
            "//h1 | //*[contains(normalize-space(.),'Легендарная танковая онлайн-игра')]";
    private static final String XPATH_REGISTER_BTN =
            "//a[contains(normalize-space(.),'Зарегистрироваться') or contains(@href,'registration')]";
    private static final String XPATH_VIDEO_LINK =
            "//a[contains(normalize-space(.),'Промовидео') or contains(normalize-space(.),'Игровой процесс')]";
    private static final String XPATH_HISTORY_SECTION =
            "//*[self::h2 or self::h3][contains(normalize-space(.),'Игра с историей')]";
    private static final String XPATH_SCREENSHOTS_SECTION =
            "//*[self::h2 or self::h3][contains(normalize-space(.),'Скриншоты')]";
    private static final String XPATH_TANK_CLASSES_SECTION =
            "//*[self::h2 or self::h3][contains(normalize-space(.),'Пять уникальных классов техники')]";
    private static final String XPATH_VEHICLES_SECTION =
            "//*[self::h2 or self::h3][contains(normalize-space(.),'Более 800 боевых машин')]";

    public GameInfoPage(WebDriver driver) {
        super(driver);
    }

    public GameInfoPage open() {
        driver.get(URL);
        waitForUrlContains("/ru/game/");
        return this;
    }

    public boolean isPageHeaderVisible() { return isVisibleByXPath(XPATH_PAGE_HEADER); }
    public boolean isRegisterButtonVisible() { return isVisibleByXPath(XPATH_REGISTER_BTN); }
    public boolean isVideoLinkVisible() { return isVisibleByXPath(XPATH_VIDEO_LINK); }
    public boolean isHistorySectionVisible() { return isVisibleByXPath(XPATH_HISTORY_SECTION); }
    public boolean isScreenshotsSectionVisible() { return isVisibleByXPath(XPATH_SCREENSHOTS_SECTION); }
    public boolean isTankClassesSectionVisible() { return isVisibleByXPath(XPATH_TANK_CLASSES_SECTION); }
    public boolean isVehiclesSectionVisible() { return isVisibleByXPath(XPATH_VEHICLES_SECTION); }

    public String getHeaderText() {
        return getTextByXPath(XPATH_PAGE_HEADER);
    }
}