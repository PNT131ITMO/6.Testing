package lab3.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final String XPATH_EMAIL_INPUT =
            "//input[@type='email' or @name='login' or @name='email' or @id='id_login']";
    private static final String XPATH_PASSWORD_INPUT =
            "//input[@type='password' or @name='password']";
    private static final String XPATH_SUBMIT_BTN =
            "//button[@type='submit' or contains(@class,'submit') or contains(text(),'Войти')]";
    private static final String XPATH_ERROR_MSG =
            "//*[contains(@class,'error') or contains(@class,'alert') or contains(@class,'notification')]";
    private static final String XPATH_FORGOT_LINK =
            "//a[contains(text(),'забыли') or contains(text(),'Forgot') or contains(@href,'forgot')]";
    private static final String XPATH_REGISTER_LINK =
            "//a[contains(text(),'Регистрация') or contains(text(),'Register') or contains(@href,'register')]";

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

    public RegisterPage clickRegisterLink() {
        clickByXPath(XPATH_REGISTER_LINK);
        return new RegisterPage(driver);
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("login") ||
               isVisibleByXPath(XPATH_EMAIL_INPUT) ||
               isVisibleByXPath(XPATH_PASSWORD_INPUT);
    }

    public boolean isErrorMessageDisplayed() {
        return isVisibleByXPath(XPATH_ERROR_MSG);
    }

    public String getErrorMessage() {
        return getTextByXPath(XPATH_ERROR_MSG);
    }

    public boolean isForgotPasswordLinkVisible() {
        return isVisibleByXPath(XPATH_FORGOT_LINK);
    }
}