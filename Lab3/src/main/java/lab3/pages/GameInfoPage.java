package lab3.pages;

import org.openqa.selenium.WebDriver;

public class GameInfoPage extends BasePage {

    public static final String URL = "https://worldoftanks.ru/ru/content/game/";

    private static final String XPATH_PAGE_HEADER =
            "//h1 | //h2[contains(@class,'title')]";
    private static final String XPATH_GAME_SECTIONS =
            "//*[contains(@class,'section') or contains(@class,'feature') or contains(@class,'block')]";
    private static final String XPATH_PLAY_BTN =
            "//a[contains(text(),'Играть') or contains(text(),'Скачать') or contains(@href,'download')]";
    private static final String XPATH_VIDEO_BLOCK =
            "//video | //*[contains(@class,'video') or contains(@class,'player')]";
    private static final String XPATH_INNER_NAV =
            "//*[contains(@class,'tabs') or contains(@class,'subnav')]//a";

    public GameInfoPage(WebDriver driver) {
        super(driver);
    }

    public GameInfoPage open() {
        driver.get(URL);
        return this;
    }

    public boolean isPageHeaderVisible()    { return isVisibleByXPath(XPATH_PAGE_HEADER); }
    public boolean areGameSectionsVisible() { return isVisibleByXPath(XPATH_GAME_SECTIONS); }
    public boolean isPlayButtonVisible()    { return isVisibleByXPath(XPATH_PLAY_BTN); }
    public boolean isVideoBlockVisible()    { return isVisibleByXPath(XPATH_VIDEO_BLOCK); }

    public String getHeaderText() {
        return getTextByXPath(XPATH_PAGE_HEADER);
    }

    public void clickPlayButton() {
        clickByXPath(XPATH_PLAY_BTN);
    }
}