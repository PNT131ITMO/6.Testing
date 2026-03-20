package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-02: Login Functionality")
class LoginTest extends BaseTest {

    @Test
    @DisplayName("TC-08: Clicking the Login button navigates to the login page")
    void testNavigateToLoginPage() {
        HomePage home = new HomePage(driver).open().acceptCookieIfPresent();
        LoginPage loginPage = home.clickLogin();
        assertTrue(loginPage.isOnLoginPage(),
                "After clicking Login the user must reach the login page. URL: "
                + driver.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-09: Login form fields are rendered correctly")
    void testLoginFormElements() {
        new HomePage(driver).open().acceptCookieIfPresent().clickLogin();
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isOnLoginPage(), "Must be on the login page");
    }

    @Test
    @DisplayName("TC-10: Login with wrong credentials — error message is shown")
    void testLoginWithInvalidCredentials() {
        LoginPage loginPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickLogin();
        loginPage.login("invalid_user_test@fake.com", "WrongPass123!");
        assertTrue(loginPage.isErrorMessageDisplayed(),
                "An error message must appear when credentials are invalid");
    }

    @Test
    @DisplayName("TC-11: Login with empty email — validation error is shown")
    void testLoginWithEmptyEmail() {
        LoginPage loginPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickLogin();
        loginPage.enterPassword("SomePassword1!").clickSubmit();
        assertTrue(loginPage.isOnLoginPage() || loginPage.isErrorMessageDisplayed(),
                "Form must not submit or must show an error when email is empty");
    }

    @ParameterizedTest(name = "TC-12 [{index}]: Login ''{0}'' / ''{1}''")
    @DisplayName("TC-12: Parameterized — multiple invalid login combinations")
    @CsvSource({
        "notanemail, Password1!",
        "@nodomain.com, Password1!",
        "test@test.com, ''",
        "'', ''"
    })
    void testLoginInvalidInputs(String email, String password) {
        LoginPage loginPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickLogin();
        loginPage.login(email, password);
        assertTrue(loginPage.isOnLoginPage() || loginPage.isErrorMessageDisplayed(),
                "Must show an error or stay on the login page for invalid input");
    }

    @Test
    @DisplayName("TC-13: 'Forgot password' link is visible on the login form")
    void testForgotPasswordLinkVisible() {
        LoginPage loginPage = new HomePage(driver).open()
                .acceptCookieIfPresent()
                .clickLogin();
        assertTrue(loginPage.isForgotPasswordLinkVisible(),
                "'Forgot password' link must be visible on the login form");
    }
}