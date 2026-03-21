package lab3.pages;

import lab3.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

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
            driver.quit();
        }
    }
}