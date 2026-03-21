package lab3;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {

    private DriverFactory() {}

    private static final String EDGE_DRIVER_PATH = "drivers/msedgedriver.exe";

    public static WebDriver createEdgeDriver(boolean headless) {
        System.setProperty("webdriver.edge.driver", EDGE_DRIVER_PATH);

        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--lang=ru");
        options.setExperimentalOption("excludeSwitches", java.util.List.of("enable-automation"));

        return new EdgeDriver(options);
    }

    public static WebDriver createEdgeDriver() {
        return createEdgeDriver(false);
    }
}