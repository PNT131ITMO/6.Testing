package lab3.pages;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/";

    private static final String XPATH_LOGO =
        "(" +
        "//a[" +
        "contains(translate(@class,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'logo') " +
        "or .//*[contains(translate(@class,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'logo')] " +
        "or contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'world of tanks') " +
        "or contains(translate(@title,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'world of tanks') " +
        "or .//img[contains(translate(@alt,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'world of tanks')] " +
        "or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'world of tanks')" +
        "]" +
        ")[1]";

    private static final String XPATH_LOGIN_BTN =
            "(//a[normalize-space()='Войти' or normalize-space()='Log in' or contains(@href,'/auth/oid/')])[1]";

    private static final String XPATH_REGISTER_BTN =
            "//a[contains(normalize-space(.),'Зарегистрироваться') " +
            "or contains(normalize-space(.),'Create account') " +
            "or contains(@href,'registration') " +
            "or contains(@href,'red.wargaming.net')]";

    private static final String XPATH_NAV_NEWS =
            "//a[normalize-space()='Новости' or normalize-space()='News' or contains(@href,'/news/')]";

    private static final String XPATH_NAV_GAME =
            "//a[normalize-space()='Игра' or normalize-space()='Game' or contains(@href,'/game/')]";

    private static final String XPATH_NAV_MEDIA =
            "//a[normalize-space()='Медиа' or normalize-space()='Media' or contains(@href,'/media/')]";

    private static final String XPATH_NAV_COMMUNITY =
            "//a[normalize-space()='Сообщество' or normalize-space()='Community' or " +
            "normalize-space()='Поиск игроков' or normalize-space()='Search Players' or " +
            "contains(@href,'/community/') or contains(@href,'/community/accounts/')]";

    private static final String XPATH_PLAYER_SEARCH_LINK =
            "//a[normalize-space()='Поиск игроков' or normalize-space()='Search Players' or contains(@href,'/community/accounts/')]";

    private static final String XPATH_NAV_GUIDES =
            "//a[normalize-space()='Руководства' or normalize-space()='Guides' or contains(@href,'/content/guide/')]";

    private static final String XPATH_NAV_CLANS =
            "//a[normalize-space()='Кланы' or normalize-space()='Clans' or contains(@href,'/clanwars/')]";

    private static final String XPATH_NAV_TOURNAMENTS =
            "//a[normalize-space()='Турниры' or normalize-space()='Tournaments' or contains(@href,'/tournaments/')]";

    private static final String XPATH_NAV_COMMUNITY_HUB =
            "//a[normalize-space()='Сообщество' or normalize-space()='Community' or contains(@href,'/community/')]";

    private static final String XPATH_HERO_BANNER =
            "//*[contains(normalize-space(.),'ТАНКОВЫЙ ОНЛАЙН-ЭКШЕН') " +
            "or contains(normalize-space(.),'Легендарная танковая онлайн-игра') " +
            "or contains(normalize-space(.),'Legendary online tank action game')]";

    private static final String XPATH_NEWS_BLOCK =
            "//*[self::h2 or self::h3][contains(normalize-space(.),'Новости') or contains(normalize-space(.),'News')]";

    private static final String XPATH_REGION_BLOCK =
            "//*[contains(normalize-space(.),'Выберите регион') or contains(normalize-space(.),'Select a region')]";

    private static final String XPATH_COOKIE_ACCEPT =
            "//button[contains(normalize-space(.),'Принять') or contains(normalize-space(.),'Accept')]";

    private static final String XPATH_FOOTER =
            "//footer | //*[@role='contentinfo'] | //*[contains(normalize-space(.),'© 2009')]";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(URL);
        waitForUrlContains("worldoftanks.eu/ru", "worldoftanks.eu", "worldoftanks.ru");
        return this;
    }

    public HomePage acceptCookieIfPresent() {
        if (isClickableByXPath(XPATH_COOKIE_ACCEPT)) {
            clickByXPath(XPATH_COOKIE_ACCEPT);
        }
        return this;
    }

    public LoginPage clickLogin() {
        String oldUrl = driver.getCurrentUrl();

        waitForAnyVisible(XPATH_LOGIN_BTN);
        scrollAndClick(XPATH_LOGIN_BTN);

        wait.until(d -> !d.getCurrentUrl().equals(oldUrl));
        wait.until(d -> d.getCurrentUrl().contains("/auth/oid/") || d.getCurrentUrl().contains("wargaming.net"));

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

    public GuidePage clickNavGuides() {
        if (isClickableByXPath(XPATH_NAV_GUIDES)) {
            clickByXPath(XPATH_NAV_GUIDES);
        } else {
            driver.get(GuidePage.URL);
        }
        return new GuidePage(driver);
    }

    public ClansPage clickNavClans() {
        if (isClickableByXPath(XPATH_NAV_CLANS)) {
            clickByXPath(XPATH_NAV_CLANS);
        } else {
            driver.get(ClansPage.URL);
        }
        return new ClansPage(driver);
    }

    public TournamentsPage clickNavTournaments() {
        if (isClickableByXPath(XPATH_NAV_TOURNAMENTS)) {
            clickByXPath(XPATH_NAV_TOURNAMENTS);
        } else {
            driver.get(TournamentsPage.URL);
        }
        return new TournamentsPage(driver);
    }

    public CommunityPage clickNavCommunityHub() {
        if (isClickableByXPath(XPATH_NAV_COMMUNITY_HUB)) {
            clickByXPath(XPATH_NAV_COMMUNITY_HUB);
        } else {
            driver.get(CommunityPage.URL);
        }
        return new CommunityPage(driver);
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

    public boolean waitForGuestState() {
        return waitForAnyVisible(XPATH_LOGIN_BTN)
                || waitForPageSourceContainsAny("Log in", "Войти", "Create account", "Зарегистрироваться");
    }

    public boolean isLogoVisible() {
        return waitForAnyVisible(XPATH_LOGO);
    }
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