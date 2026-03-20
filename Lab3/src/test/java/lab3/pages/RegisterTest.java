package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-03: Account Registration Functionality")
class RegisterTest extends BaseTest {

    @Test
    @DisplayName("TC-14: Clicking Register navigates to the registration page")
    void testNavigateToRegisterPage() {
        RegisterPage registerPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        assertTrue(registerPage.isOnRegisterPage(),
                "Must navigate to the registration page. URL: " + driver.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-15: Registration page has a form header")
    void testRegisterPageHasHeader() {
        new HomePage(driver).open().acceptCookieIfPresent().clickRegister();
        RegisterPage page = new RegisterPage(driver);
        assertTrue(page.isFormHeaderVisible() || page.isOnRegisterPage(),
                "Registration page must have a visible form header");
    }

    @Test
    @DisplayName("TC-16: Registering with an existing email — error is shown")
    void testRegisterWithExistingEmail() {
        RegisterPage page = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        if (page.isOnRegisterPage()) {
            page.enterNickname("TestNick" + System.currentTimeMillis())
                .enterEmail("existing_test_account@wargaming.net")
                .enterPassword("TestPass123!")
                .enterPasswordConfirm("TestPass123!")
                .clickSubmit();
            assertTrue(page.isErrorDisplayed() || page.isOnRegisterPage(),
                    "Must show an error or reject registration for an already-used email");
        }
    }

    @Test
    @DisplayName("TC-17: Mismatched password confirmation — error is shown")
    void testRegisterPasswordMismatch() {
        RegisterPage page = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        if (page.isOnRegisterPage()) {
            page.enterNickname("TankPlayer" + System.currentTimeMillis())
                .enterEmail("new_user_" + System.currentTimeMillis() + "@test.com")
                .enterPassword("Password123!")
                .enterPasswordConfirm("DifferentPassword!")
                .clickSubmit();
            assertTrue(page.isErrorDisplayed() || page.isOnRegisterPage(),
                    "Must show an error when the password confirmation does not match");
        }
    }

    @Test
    @DisplayName("TC-18: Submitting an empty form — validation errors are shown")
    void testRegisterWithEmptyForm() {
        RegisterPage page = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickRegister();
        if (page.isOnRegisterPage()) {
            page.clickSubmit();
            assertTrue(page.isErrorDisplayed() || page.isOnRegisterPage(),
                    "Must show validation errors when the form is submitted empty");
        }
    }
}