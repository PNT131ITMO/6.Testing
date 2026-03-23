package lab3.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-09: Community")
class CommunityTest extends BaseTest {

    @Test
    @DisplayName("TC-51: Community page loads successfully")
    void testCommunityPageLoads() {
        CommunityPage communityPage = new CommunityPage(driver).open();
        assertTrue(communityPage.getCurrentUrl().contains("/community/"),
                "Community page must load successfully. URL: " + communityPage.getCurrentUrl());
    }

    @Test
    @DisplayName("TC-52: Community welcome section is visible")
    void testCommunityWelcomeVisible() {
        CommunityPage communityPage = new CommunityPage(driver).open();
        assertTrue(communityPage.isWelcomeSectionVisible(),
                "Community welcome section must be visible");
    }

    @Test
    @DisplayName("TC-53: Discord, YouTube and Twitch blocks are visible")
    void testCommunitySocialBlocksVisible() {
        CommunityPage communityPage = new CommunityPage(driver).open();
        assertAll(
                () -> assertTrue(communityPage.isDiscordBlockVisible(), "Discord block must be visible"),
                () -> assertTrue(communityPage.isYoutubeBlockVisible(), "YouTube block must be visible"),
                () -> assertTrue(communityPage.isTwitchBlockVisible(), "Twitch block must be visible")
        );
    }

    @Test
    @DisplayName("TC-54: Content creators and referral program blocks are visible")
    void testCommunityProgramBlocksVisible() {
        CommunityPage communityPage = new CommunityPage(driver).open();
        assertAll(
                () -> assertTrue(communityPage.isContentCreatorsBlockVisible(), "Content creators block must be visible"),
                () -> assertTrue(communityPage.isReferralBlockVisible(), "Referral program block must be visible")
        );
    }

    @Test
    @DisplayName("TC-55: Additional social follow blocks are visible")
    void testCommunityAdditionalSocialBlocksVisible() {
        CommunityPage communityPage = new CommunityPage(driver).open();
        assertTrue(
                communityPage.isFacebookBlockVisible() || communityPage.isTikTokBlockVisible(),
                "At least one additional social follow block must be visible"
        );
    }
}