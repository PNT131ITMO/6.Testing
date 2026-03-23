package lab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CommunityPage extends BasePage {

    public static final String URL = "https://worldoftanks.eu/ru/community/";

    private static final String XPATH_WELCOME_SECTION =
            "//*[self::h1 or self::h2][contains(normalize-space(.),'Добро пожаловать, бойцы!')]";

    private static final String XPATH_DISCORD_BLOCK =
            "//*[contains(normalize-space(.),'Discord')]";

    private static final String XPATH_YOUTUBE_BLOCK =
            "//*[contains(normalize-space(.),'YOUTUBE') or contains(normalize-space(.),'YouTube')]";

    private static final String XPATH_TWITCH_BLOCK =
            "//*[contains(normalize-space(.),'TWITCH') or contains(normalize-space(.),'Twitch')]";

    private static final String XPATH_CONTENT_CREATORS_BLOCK =
            "//*[contains(normalize-space(.),'Создатели контента')]";

    private static final String XPATH_REFERRAL_BLOCK =
            "//*[contains(normalize-space(.),'РЕФЕРАЛЬНАЯ ПРОГРАММА') or contains(normalize-space(.),'Реферальная программа')]";

    private static final String XPATH_FACEBOOK_BLOCK =
            "//*[contains(normalize-space(.),'FACEBOOK')]";

    private static final String XPATH_TIKTOK_BLOCK =
            "//*[contains(normalize-space(.),'TIKTOK')]";

    public CommunityPage(WebDriver driver) {
        super(driver);
    }

    public CommunityPage open() {
        driver.get(URL);
        waitForUrlContains("/community/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_WELCOME_SECTION)));
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

    public boolean isWelcomeSectionVisible() {
        return hasVisibleElement(XPATH_WELCOME_SECTION);
    }

    public boolean isDiscordBlockVisible() {
        return hasVisibleElement(XPATH_DISCORD_BLOCK);
    }

    public boolean isYoutubeBlockVisible() {
        return hasVisibleElement(XPATH_YOUTUBE_BLOCK);
    }

    public boolean isTwitchBlockVisible() {
        return hasVisibleElement(XPATH_TWITCH_BLOCK);
    }

    public boolean isContentCreatorsBlockVisible() {
        return hasVisibleElement(XPATH_CONTENT_CREATORS_BLOCK);
    }

    public boolean isReferralBlockVisible() {
        return hasVisibleElement(XPATH_REFERRAL_BLOCK);
    }

    public boolean isFacebookBlockVisible() {
        return hasVisibleElement(XPATH_FACEBOOK_BLOCK);
    }

    public boolean isTikTokBlockVisible() {
        return hasVisibleElement(XPATH_TIKTOK_BLOCK);
    }
}