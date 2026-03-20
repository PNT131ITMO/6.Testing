package lab3.pages;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    public static final String URL = "https://worldoftanks.ru/";

    private static final String XPATH_LOGO =
            "//a[contains(@class,'logo') or ./img[contains(@alt,'World of Tanks')]]";
    private static final String XPATH_LOGIN_BTN =
            "//a[contains(@href,'login') or contains(text(),'Войти') or contains(text(),'Вход')]";
    private static final String XPATH_REGISTER_BTN =
            "//a[contains(@href,'register') or contains(text(),'Регистрация') or contains(text(),'Зарегистрироваться')]";
    private static final String XPATH_SEARCH_ICON =
            "//button[contains(@class,'search') or @aria-label='search' or @aria-label='Поиск']";
    private static final String XPATH_NAV_NEWS =
            "//nav//a[contains(translate(text(),'НОВОСТИ','новости'),'новости')]";
    private static final String XPATH_NAV_GAME =
            "//nav//a[contains(translate(text(),'ИГРА','игра'),'игра') or contains(@href,'/game/')]";
    private static final String XPATH_NAV_MEDIA =
            "//nav//a[contains(translate(text(),'МЕДИА','медиа'),'медиа') or contains(@href,'/media/')]";
    private static final String XPATH_NAV_COMMUNITY =
            "//nav//a[contains(translate(text(),'СООБЩЕСТВО','сообщество'),'сообщество') or contains(@href,'/community/')]";
    private static final String XPATH_HERO_BANNER =
            "//section[contains(@class,'banner') or contains(@class,'hero') or contains(@class,'promo')]";
    private static final String XPATH_COOKIE_ACCEPT =
            "//button[contains(text(),'Принять') or contains(text(),'Accept') or contains(@class,'cookie') and contains(@class,'accept')]";
    private static final String XPATH_FOOTER =
            "//footer | //*[contains(@class,'footer')]";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(URL);
        return this;
    }

    public HomePage acceptCookieIfPresent() {
        try {
            Thread.sleep(1500);
            if (isVisibleByXPath(XPATH_COOKIE_ACCEPT)) {
                clickByXPath(XPATH_COOKIE_ACCEPT);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        clickByXPath(XPATH_SEARCH_ICON);
        return new SearchPage(driver);
    }

    public NewsPage clickNavNews() {
        clickByXPath(XPATH_NAV_NEWS);
        return new NewsPage(driver);
    }

    public GameInfoPage clickNavGame() {
        clickByXPath(XPATH_NAV_GAME);
        return new GameInfoPage(driver);
    }

    public void clickNavMedia() {
        clickByXPath(XPATH_NAV_MEDIA);
    }

    public void clickNavCommunity() {
        clickByXPath(XPATH_NAV_COMMUNITY);
    }

    public boolean isLogoVisible()       { return isVisibleByXPath(XPATH_LOGO); }
    public boolean isLoginBtnVisible()   { return isVisibleByXPath(XPATH_LOGIN_BTN); }
    public boolean isHeroBannerVisible() { return isVisibleByXPath(XPATH_HERO_BANNER); }
    public boolean isFooterVisible()     { return isVisibleByXPath(XPATH_FOOTER); }
    public boolean isNavNewsVisible()    { return isVisibleByXPath(XPATH_NAV_NEWS); }
}