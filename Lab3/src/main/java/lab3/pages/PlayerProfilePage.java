package lab3.pages;

import org.openqa.selenium.WebDriver;

public class PlayerProfilePage extends BasePage {

    public static final String URL =
            "https://worldoftanks.eu/en/community/accounts/724828929-S_374808/?utm_source=global-nav&utm_medium=link&utm_campaign=wgnr";

    private static final String PLAYER_NICKNAME = "S_374808";

    private static final String XPATH_MY_PROFILE_ENTRY =
            "//*[self::a or self::button][normalize-space()='My Profile' " +
            "or normalize-space()='Мой профиль' " +
            "or contains(normalize-space(.),'My Profile') " +
            "or contains(normalize-space(.),'Мой профиль')]";

    private static final String XPATH_LOGOUT_ENTRY =
            "//*[self::a or self::button][normalize-space()='Log out' " +
            "or normalize-space()='Выйти' " +
            "or normalize-space()='Выход' " +
            "or contains(normalize-space(.),'Log out') " +
            "or contains(normalize-space(.),'Выйти') " +
            "or contains(normalize-space(.),'Выход') " +
            "or contains(@href,'logout') " +
            "or contains(@href,'signout')]";

    private static final String XPATH_ACCOUNT_MANAGEMENT_ENTRY =
        "(" +
        "//*[self::a or self::button][" +
        "contains(normalize-space(.),'Account Management') " +
        "or contains(normalize-space(.),'Управление аккаунтом') " +
        "or contains(normalize-space(.),'Личный кабинет') " +
        "or contains(normalize-space(.),'Мой аккаунт') " +
        "or contains(normalize-space(.),'Аккаунт') " +
        "or contains(@href,'account') " +
        "or contains(@href,'personal') " +
        "or contains(@href,'users.wargaming.net') " +
        "or contains(@href,'id.wargaming.net')" +
        "]" +
        ")[1]";

    private static final String XPATH_SEARCH_PLAYERS_ENTRY =
        "(" +
        "//*[self::a or self::button][" +
        "contains(normalize-space(.),'Search Players') " +
        "or contains(normalize-space(.),'Player Search') " +
        "or contains(normalize-space(.),'Поиск игроков') " +
        "or contains(normalize-space(.),'Поиск игрока') " +
        "or contains(normalize-space(.),'Найти игрока') " +
        "or normalize-space(.)='Игроки'" +
        "]" +
        " | " +
        "//a[(contains(@href,'/community/accounts/') or contains(@href,'/ru/community/accounts/')) " +
        "and not(contains(@href,'" + PLAYER_NICKNAME + "'))]" +
        ")[1]";

    private static final String XPATH_PLAYER_NICKNAME =
            "//*[self::h1 or self::h2 or self::div or self::span][normalize-space()='S_374808']";

    private static final String XPATH_STATISTICS_SECTION =
            "//*[self::h1 or self::h2 or self::h3 or self::section or self::div][contains(normalize-space(.),'Statistics') " +
            "or contains(normalize-space(.),'Статистика')]";

    private static final String XPATH_JOIN_CLAN_BLOCK =
            "//*[contains(normalize-space(.),'Join a clan!') " +
            "or contains(normalize-space(.),'Вступите в клан')]";

    private static final String XPATH_ACCOUNT_DATES_BLOCK =
        "//*[contains(normalize-space(.),'Account created') " +
        "or contains(normalize-space(.),'Created') " +
        "or contains(normalize-space(.),'Registered') " +
        "or contains(normalize-space(.),'Registration date') " +
        "or contains(normalize-space(.),'Member since') " +
        "or contains(normalize-space(.),'Last battle') " +
        "or contains(normalize-space(.),'Last battle fought') " +
        "or contains(normalize-space(.),'Last seen') " +
        "or contains(normalize-space(.),'Personal Rating') " +
        "or contains(normalize-space(.),'Battles') " +
        "or contains(normalize-space(.),'Victories') " +
        "or contains(normalize-space(.),'Win Rate') " +
        "or contains(normalize-space(.),'Average damage') " +
        "or contains(normalize-space(.),'Аккаунт создан') " +
        "or contains(normalize-space(.),'Дата регистрации') " +
        "or contains(normalize-space(.),'Зарегистрирован') " +
        "or contains(normalize-space(.),'Последний бой') " +
        "or contains(normalize-space(.),'Последняя активность') " +
        "or contains(normalize-space(.),'Личный рейтинг') " +
        "or contains(normalize-space(.),'Бои') " +
        "or contains(normalize-space(.),'Победы') " +
        "or contains(normalize-space(.),'Процент побед') " +
        "or contains(normalize-space(.),'Средний урон')]";

    public PlayerProfilePage(WebDriver driver) {
        super(driver);
    }

    public PlayerProfilePage open() {
        driver.get(URL);
        waitUntilProfileLoaded();
        return this;
    }

    public boolean waitUntilProfileLoaded() {
        if (waitForPageSourceContainsAny(
            PLAYER_NICKNAME,
            "Account Management",
            "Search Players",
            "Statistics",
            "Join a clan!",
            "Мой профиль",
            "Выйти",
            "Статистика",
            "Вступите в клан"
        )) {
                return true;
        }

        return waitForUrlContains("/community/accounts/", PLAYER_NICKNAME);
    }

    public boolean isOnProfilePage() {
        return getCurrentUrl().contains("/community/accounts/")
                && (getCurrentUrl().contains("S_374808") || isNicknameVisible());
    }

    public boolean isNicknameVisible() {
        return isVisibleByXPath(XPATH_PLAYER_NICKNAME) || pageSourceContains(PLAYER_NICKNAME);
    }

    public boolean hasMyProfileEntry() {
        return isVisibleByXPath(XPATH_MY_PROFILE_ENTRY)
                || isPresentByXPath(XPATH_MY_PROFILE_ENTRY)
                || pageSourceContainsAny("My Profile", "Мой профиль");
    }

    public boolean hasLogoutEntry() {
        return isVisibleByXPath(XPATH_LOGOUT_ENTRY)
                || isPresentByXPath(XPATH_LOGOUT_ENTRY)
                || pageSourceContainsAny("Log out", "Выйти", "Выход");
    }

    public boolean hasAccountManagementEntry() {
        return isVisibleByXPath(XPATH_ACCOUNT_MANAGEMENT_ENTRY)
            || isPresentByXPath(XPATH_ACCOUNT_MANAGEMENT_ENTRY)
            || pageSourceContainsAny(
                    "Account Management",
                    "Управление аккаунтом",
                    "Личный кабинет",
                    "Мой аккаунт",
                    "Аккаунт",
                    "users.wargaming.net",
                    "id.wargaming.net",
                    "personal",
                    "account"
            );
        }

    public boolean hasSearchPlayersEntry() {
        return isVisibleByXPath(XPATH_SEARCH_PLAYERS_ENTRY)
            || isPresentByXPath(XPATH_SEARCH_PLAYERS_ENTRY)
            || pageSourceContainsAny(
                    "Search Players",
                    "Player Search",
                    "Поиск игроков",
                    "Поиск игрока",
                    "Найти игрока",
                    "/community/accounts/"
            );
        }       

    public boolean hasStatisticsSection() {
        return isVisibleByXPath(XPATH_STATISTICS_SECTION)
                || isPresentByXPath(XPATH_STATISTICS_SECTION)
                || pageSourceContainsAny("Statistics", "Статистика");
    }

    public boolean hasJoinClanBlock() {
        return isVisibleByXPath(XPATH_JOIN_CLAN_BLOCK)
                || isPresentByXPath(XPATH_JOIN_CLAN_BLOCK)
                || pageSourceContainsAny("Join a clan!", "Вступите в клан");
    }

    public boolean hasAccountDatesBlock() {
        return isVisibleByXPath(XPATH_ACCOUNT_DATES_BLOCK)
            || isPresentByXPath(XPATH_ACCOUNT_DATES_BLOCK)
            || pageSourceContainsAny(
                    "Account created",
                    "Created",
                    "Registered",
                    "Registration date",
                    "Member since",
                    "Last battle",
                    "Last battle fought",
                    "Last seen",
                    "Personal Rating",
                    "Battles",
                    "Victories",
                    "Win Rate",
                    "Average damage",
                    "Аккаунт создан",
                    "Дата регистрации",
                    "Зарегистрирован",
                    "Последний бой",
                    "Последняя активность",
                    "Личный рейтинг",
                    "Бои",
                    "Победы",
                    "Процент побед",
                    "Средний урон"
            );
            
        }

    public HomePage logout() {
        if (!clickFirstPresentByXPath(XPATH_LOGOUT_ENTRY)) {
            throw new IllegalStateException("Logout control was not found in DOM.");
        }
        return new HomePage(driver);
    }
}