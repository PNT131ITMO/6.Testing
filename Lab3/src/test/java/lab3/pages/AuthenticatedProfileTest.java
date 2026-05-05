package lab3.pages;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-12..UC-15: Authenticated Profile Flow")
class AuthenticatedProfileTest extends BaseTest {

    private String requireEnv(String key) {
        String value = System.getenv(key);
        Assumptions.assumeTrue(value != null && !value.isBlank(),
                "Environment variable " + key + " must be set");
        return value;
    }

    private void authenticateSession() {
        String email = requireEnv("WG_EMAIL");
        String password = requireEnv("WG_PASSWORD");

        LoginPage loginPage = new HomePage(driver)
                .open()
                .acceptCookieIfPresent()
                .clickLogin();

        assertTrue(loginPage.isOnLoginPage(),
                "Login page must open before submitting credentials. URL: " + driver.getCurrentUrl());

        loginPage.login(email, password);

        assertTrue(loginPage.waitForAuthFlowToFinish(),
                "Auth flow did not finish as expected. URL after submit: " + driver.getCurrentUrl());
    }

    private PlayerProfilePage loginAndOpenProfile() {
        authenticateSession();

        PlayerProfilePage profilePage = new PlayerProfilePage(driver).open();

        assertTrue(profilePage.waitUntilProfileLoaded(),
                "Profile page did not load expected content. URL: " + driver.getCurrentUrl());

        return profilePage;
    }

    @Test
    @DisplayName("TC-66: Valid credentials allow access to the personal player profile")
    void testAuthorizedUserCanOpenOwnProfile() {
        PlayerProfilePage profilePage = loginAndOpenProfile();

        assertAll(
                () -> assertTrue(profilePage.isOnProfilePage(),
                        "Authorized user must reach player profile page"),
                () -> assertTrue(profilePage.isNicknameVisible(),
                        "Player nickname must be visible on profile page")
        );
    }

    @Test
    @DisplayName("TC-67: Profile page exposes account navigation entries")
    void testAuthorizedHeaderControlsVisible() {
        PlayerProfilePage profilePage = loginAndOpenProfile();

        assertTrue(profilePage.hasMyProfileEntry() || profilePage.hasLogoutEntry(),
                "Profile page must expose at least one authenticated account entry");
    }

    @Test
    @DisplayName("TC-68: Profile page shows account management and player search links")
    void testProfileQuickLinksVisible() {
        PlayerProfilePage profilePage = loginAndOpenProfile();

        assertAll(
                () -> assertTrue(profilePage.hasAccountManagementEntry(),
                        "Profile page must contain 'Account Management' entry"),
                () -> assertTrue(profilePage.hasSearchPlayersEntry(),
                        "Profile page must contain 'Search Players' entry")
        );
    }

    @Test
    @DisplayName("TC-69: Profile page shows statistics section")
    void testStatisticsSectionVisible() {
        PlayerProfilePage profilePage = loginAndOpenProfile();

        assertTrue(profilePage.hasStatisticsSection(),
                "Profile page must display the statistics section");
    }

    @Test
    @DisplayName("TC-70: Profile page shows clan-related block")
    void testClanBlockVisible() {
        PlayerProfilePage profilePage = loginAndOpenProfile();

        assertTrue(profilePage.hasJoinClanBlock(),
                "Profile page must contain clan-related information block");
    }

    @Test
    @DisplayName("TC-71: Profile page shows account dates or profile metadata")
    void testProfileMetadataVisible() {
        PlayerProfilePage profilePage = loginAndOpenProfile();

        assertTrue(profilePage.hasAccountDatesBlock(),
                "Profile page must contain account metadata such as creation date or last battle");
    }
}   