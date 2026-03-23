package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-10: Login Entry Point")
class LoginTest extends BaseTest {

    @Test
    @DisplayName("TC-56: Clicking Login navigates to the Wargaming auth page")
    void testNavigateToLoginPage() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        LoginPage loginPage = home.clickLogin();
        assertTrue(loginPage.isOnLoginPage(),
                "After clicking Login the user must reach the auth page. URL: " + driver.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-57: Login page URL belongs to the Wargaming auth domain")
    void testLoginPageUrl() {
        LoginPage loginPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickLogin();
        String url = loginPage.getCurrentUrl();
        assertTrue(url.contains("auth/oid") || url.contains("wargaming.net"),
                "Login page must open on Wargaming auth. Actual URL: " + url);
    }

    @Test
    @DisplayName("TC-58: Login page title is not empty")
    void testLoginPageTitleNotEmpty() {
        LoginPage loginPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickLogin();
        assertFalse(loginPage.getPageTitle().isEmpty(),
                "Login page title must not be empty");
    }

    @Test
    @DisplayName("TC-59: Login page renders auth controls or at least the submit button")
    void testLoginFormElements() {
        LoginPage loginPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickLogin();
        assertTrue(loginPage.isEmailInputVisible() || loginPage.isPasswordInputVisible() || loginPage.isSubmitButtonVisible(),
                "Auth controls must be rendered on the login page");
    }

    @Test
    @DisplayName("TC-60: Login page contains recovery or legal navigation links")
    void testLoginPageSupportLinks() {
        LoginPage loginPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickLogin();
        assertTrue(loginPage.isForgotPasswordLinkVisible() || loginPage.hasLegalLinks(),
                "Login page must expose recovery or legal links");
    }
}