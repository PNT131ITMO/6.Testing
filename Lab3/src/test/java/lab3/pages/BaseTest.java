package lab3.pages;

import io.qameta.allure.Allure;
import lab3.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public abstract class BaseTest {

    protected WebDriver driver;

    protected static final boolean HEADLESS = false;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.createEdgeDriver(HEADLESS);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            try {
                Allure.addAttachment(
                        "Current URL",
                        "text/plain",
                        driver.getCurrentUrl()
                );

                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);

                Allure.addAttachment(
                        "Final screenshot",
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png"
                );
            } catch (Exception ignored) {
            }

            driver.quit();
        }
    }
}