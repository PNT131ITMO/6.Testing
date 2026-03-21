package lab3.pages;

import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    private static final String XPATH_EMAIL_INPUT =
            "//input[@type='email' or @name='email' or contains(@autocomplete,'email')]";
    private static final String XPATH_PASSWORD_INPUT =
            "//input[@type='password']";
    private static final String XPATH_SUBMIT_BTN =
            "//button[@type='submit'] | //input[@type='submit']";
    private static final String XPATH_EULA_LINK =
            "//a[contains(normalize-space(.),'End User License Agreement') or contains(normalize-space(.),'Лицензионное соглашение')]";
    private static final String XPATH_PRIVACY_LINK =
            "//a[contains(normalize-space(.),'Privacy Policy') or contains(normalize-space(.),'Политика конфиденциальности')]";

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnRegisterPage() {
        return waitForUrlContains("registration", "wargaming.net")
                || isVisibleByXPath(XPATH_EMAIL_INPUT)
                || isVisibleByXPath(XPATH_PASSWORD_INPUT);
    }

    public boolean isEmailInputVisible() {
        return isVisibleByXPath(XPATH_EMAIL_INPUT);
    }

    public boolean isPasswordInputVisible() {
        return isVisibleByXPath(XPATH_PASSWORD_INPUT);
    }

    public boolean isSubmitButtonVisible() {
        return isVisibleByXPath(XPATH_SUBMIT_BTN);
    }

    public boolean isEulaLinkVisible() {
        return isVisibleByXPath(XPATH_EULA_LINK);
    }

    public boolean isPrivacyLinkVisible() {
        return isVisibleByXPath(XPATH_PRIVACY_LINK);
    }
}