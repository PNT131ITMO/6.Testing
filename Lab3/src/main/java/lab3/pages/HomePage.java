package lab3.pages;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/";

    private static final String XPATH_LOGO =
            "//a[contains(@href,'/ru/') and (contains(normalize-space(.),'World of Tanks') or .//img)]";
    private static final String XPATH_LOGIN_BTN =
            "//a[normalize-space()='Войти' or contains(@href,'auth/oid')]";
    private static final String XPATH_REGISTER_BTN =
            "//a[contains(normalize-space(.),'Зарегистрироваться') or contains(@href,'registration') or contains(@href,'red.wargaming.net')]";
    private static final String XPATH_NAV_NEWS =
            "//a[normalize-space()='Новости' or contains(@href,'/ru/news/')]";
    private static final String XPATH_NAV_GAME =
            "//a[normalize-space()='Игра' or contains(@href,'/ru/game/')]";
    private static final String XPATH_NAV_MEDIA =
            "//a[normalize-space()='Медиа' or contains(@href,'/ru/media/')]";
    private static final String XPATH_NAV_COMMUNITY =
            "//a[normalize-space()='Сообщество' or normalize-space()='Поиск игроков' or contains(@href,'/ru/community/') or contains(@href,'/community/accounts/')]";
    private static final String XPATH_PLAYER_SEARCH_LINK =
            "//a[normalize-space()='Поиск игроков' or contains(@href,'/community/accounts/')]";
    private static final String XPATH_HERO_BANNER =
            "//*[contains(normalize-space(.),'ТАНКОВЫЙ ОНЛАЙН-ЭКШЕН') or contains(normalize-space(.),'Легендарная танковая онлайн-игра')]";
    private static final String XPATH_NEWS_BLOCK =
            "//*[self::h2 or self::h3][contains(normalize-space(.),'Новости')]";
    private static final String XPATH_REGION_BLOCK =
            "//*[contains(normalize-space(.),'Выберите регион')]";
    private static final String XPATH_COOKIE_ACCEPT =
            "//button[contains(normalize-space(.),'Принять') or contains(normalize-space(.),'Accept')]";
    private static final String XPATH_FOOTER =
            "//footer | //*[@role='contentinfo'] | //*[contains(normalize-space(.),'© 2009')]";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(URL);
        waitForUrlContains("worldoftanks.eu/ru", "worldoftanks.ru");
        return this;
    }

    public HomePage acceptCookieIfPresent() {
        if (isClickableByXPath(XPATH_COOKIE_ACCEPT)) {
            clickByXPath(XPATH_COOKIE_ACCEPT);
        }
        return this;
    }

    public LoginPage clickLogin() {
        clickByXPath(XPATH_LOGIN_BTN);
        return new LoginPage(driver);
    }

    public RegisterPage clickRegister() {
        clickByXPath(XPATH_REGISTER_BTN);
        return new RegisterPage(driver);
    }

    public SearchPage clickSearch() {
        if (isClickableByXPath(XPATH_PLAYER_SEARCH_LINK)) {
            clickByXPath(XPATH_PLAYER_SEARCH_LINK);
        } else {
            driver.get(SearchPage.URL);
        }
        return new SearchPage(driver);
    }

    public NewsPage clickNavNews() {
        if (isClickableByXPath(XPATH_NAV_NEWS)) {
            clickByXPath(XPATH_NAV_NEWS);
        } else {
            driver.get(NewsPage.URL);
        }
        return new NewsPage(driver);
    }

    public GameInfoPage clickNavGame() {
        if (isClickableByXPath(XPATH_NAV_GAME)) {
            clickByXPath(XPATH_NAV_GAME);
        } else {
            driver.get(GameInfoPage.URL);
        }
        return new GameInfoPage(driver);
    }

    public void clickNavMedia() {
        if (isClickableByXPath(XPATH_NAV_MEDIA)) {
            clickByXPath(XPATH_NAV_MEDIA);
        } else {
            driver.get("https://worldoftanks.eu/ru/media/");
        }
    }

    public void clickNavCommunity() {
        if (isClickableByXPath(XPATH_NAV_COMMUNITY)) {
            clickByXPath(XPATH_NAV_COMMUNITY);
        } else {
            driver.get(SearchPage.URL);
        }
    }

    public boolean isLogoVisible() { return isVisibleByXPath(XPATH_LOGO); }
    public boolean isLoginBtnVisible() { return isVisibleByXPath(XPATH_LOGIN_BTN); }
    public boolean isRegisterBtnVisible() { return isVisibleByXPath(XPATH_REGISTER_BTN); }
    public boolean isHeroBannerVisible() { return isVisibleByXPath(XPATH_HERO_BANNER); }
    public boolean isFooterVisible() { return isVisibleByXPath(XPATH_FOOTER); }
    public boolean isNavNewsVisible() { return isVisibleByXPath(XPATH_NAV_NEWS); }
    public boolean isNavGameVisible() { return isVisibleByXPath(XPATH_NAV_GAME); }
    public boolean isNewsBlockVisible() { return isVisibleByXPath(XPATH_NEWS_BLOCK); }
    public boolean isRegionBlockVisible() { return isVisibleByXPath(XPATH_REGION_BLOCK); }
    public boolean isPlayerSearchLinkVisible() { return isVisibleByXPath(XPATH_PLAYER_SEARCH_LINK); }
}