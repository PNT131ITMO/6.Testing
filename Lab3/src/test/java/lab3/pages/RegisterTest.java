package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-11: Registration Entry Point")
class RegisterTest extends BaseTest {

    @Test
    @DisplayName("TC-61: Clicking Register navigates to the Wargaming registration page")
    void testNavigateToRegisterPage() {
        RegisterPage registerPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        assertTrue(registerPage.isOnRegisterPage(),
                "Must navigate to the registration page. URL: " + driver.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-62: Registration page URL belongs to the Wargaming registration domain")
    void testRegisterPageUrl() {
        RegisterPage registerPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        String url = registerPage.getCurrentUrl();
        assertTrue(url.contains("registration") || url.contains("wargaming.net"),
                "Registration page must open on Wargaming registration. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-63: Registration page title is not empty")
    void testRegisterPageTitleNotEmpty() {
        RegisterPage registerPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        assertFalse(registerPage.getPageTitle().isEmpty(),
                "Registration page title must not be empty");
    }

    @Test
    @DisplayName("TC-64: Registration page renders email/password controls or submit button")
    void testRegisterFormElements() {
        RegisterPage registerPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        assertTrue(registerPage.isEmailInputVisible() || registerPage.isPasswordInputVisible() || registerPage.isSubmitButtonVisible(),
                "Registration controls must be rendered");
    }

    @Test
    @DisplayName("TC-65: Registration page shows legal links")
    void testRegisterPageLegalLinks() {
        RegisterPage registerPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        assertAll(
                () -> assertTrue(registerPage.isEulaLinkVisible(), "EULA link must be visible"),
                () -> assertTrue(registerPage.isPrivacyLinkVisible(), "Privacy Policy link must be visible")
        );
    }
}