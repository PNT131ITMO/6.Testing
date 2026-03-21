package lab3.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final String XPATH_EMAIL_INPUT =
            "//input[@type='email' or @name='login' or @name='email' or contains(@autocomplete,'username')]";
    private static final String XPATH_PASSWORD_INPUT =
            "//input[@type='password' or @name='password' or contains(@autocomplete,'current-password')]";
    private static final String XPATH_SUBMIT_BTN =
            "//button[@type='submit'] | //input[@type='submit']";
    private static final String XPATH_FORGOT_LINK =
            "//a[contains(translate(normalize-space(.),'FORGOTPASSWORDЗАБЫЛИПАРОЛЬ','forgotpasswordзабылыпароль'),'forgot') or contains(normalize-space(.),'Забыли') or contains(@href,'password')]";
    private static final String XPATH_LEGAL_LINKS =
            "//a[contains(normalize-space(.),'License') or contains(normalize-space(.),'Agreement') or contains(normalize-space(.),'Privacy')]";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterEmail(String email) {
        typeByXPath(XPATH_EMAIL_INPUT, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        typeByXPath(XPATH_PASSWORD_INPUT, password);
        return this;
    }

    public void clickSubmit() {
        clickByXPath(XPATH_SUBMIT_BTN);
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
    }

    public boolean isOnLoginPage() {
        return waitForUrlContains("auth/oid", "wargaming.net")
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

    public boolean isForgotPasswordLinkVisible() {
        return isVisibleByXPath(XPATH_FORGOT_LINK);
    }

    public boolean hasLegalLinks() {
        return isVisibleByXPath(XPATH_LEGAL_LINKS);
    }
}