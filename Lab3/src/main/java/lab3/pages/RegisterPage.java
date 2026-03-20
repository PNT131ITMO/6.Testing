package lab3.pages;

import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    private static final String XPATH_NICKNAME_INPUT =
            "//input[@name='nickname' or @id='id_nickname' or @placeholder[contains(.,'ник') or contains(.,'nick')]]";
    private static final String XPATH_EMAIL_INPUT =
            "//input[@type='email' or @name='email' or @id='id_email']";
    private static final String XPATH_PASSWORD_INPUT =
            "//input[@type='password' and not(@name='password_confirm')]";
    private static final String XPATH_PASSWORD_CONFIRM =
            "//input[@name='password_confirm' or @id='id_password_confirm']";
    private static final String XPATH_SUBMIT_BTN =
            "//button[@type='submit' or contains(text(),'Зарегистрироваться') or contains(text(),'Создать')]";
    private static final String XPATH_ERROR_MSG =
            "//*[contains(@class,'error') or contains(@class,'field-error')]";
    private static final String XPATH_TERMS_CHECKBOX =
            "//input[@type='checkbox' and (contains(@name,'agree') or contains(@id,'agree'))]";
    private static final String XPATH_FORM_HEADER =
            "//h1 | //h2[contains(text(),'Регистрация') or contains(text(),'Создать')]";

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public RegisterPage enterNickname(String nickname) {
        typeByXPath(XPATH_NICKNAME_INPUT, nickname);
        return this;
    }

    public RegisterPage enterEmail(String email) {
        typeByXPath(XPATH_EMAIL_INPUT, email);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        typeByXPath(XPATH_PASSWORD_INPUT, password);
        return this;
    }

    public RegisterPage enterPasswordConfirm(String password) {
        typeByXPath(XPATH_PASSWORD_CONFIRM, password);
        return this;
    }

    public RegisterPage acceptTerms() {
        clickByXPath(XPATH_TERMS_CHECKBOX);
        return this;
    }

    public void clickSubmit() {
        clickByXPath(XPATH_SUBMIT_BTN);
    }

    public boolean isOnRegisterPage() {
        return driver.getCurrentUrl().contains("register") ||
               isVisibleByXPath(XPATH_NICKNAME_INPUT) ||
               isVisibleByXPath(XPATH_EMAIL_INPUT);
    }

    public boolean isErrorDisplayed() {
        return isVisibleByXPath(XPATH_ERROR_MSG);
    }

    public String getErrorMessage() {
        return getTextByXPath(XPATH_ERROR_MSG);
    }

    public boolean isFormHeaderVisible() {
        return isVisibleByXPath(XPATH_FORM_HEADER);
    }
}