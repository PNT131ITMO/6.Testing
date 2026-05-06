package extra.protocol;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.File;
import java.net.URL;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebDriverProtocolCaptureTest {

    private static final int EDGEDRIVER_PORT =
            Integer.getInteger("webdriver.port", 9516);

    private static final long PAUSE_MS =
            Long.getLong("protocol.pause.ms", 1500L);

    private static final boolean HEADLESS =
            Boolean.getBoolean("webdriver.headless");

    private static final String EDGE_DRIVER_PATH =
            System.getProperty("edge.driver.path", "").trim();

    @Test
    @DisplayName("Capture full Edge WebDriver cycle: /session -> /url -> /element -> /click -> delete session")
    void captureEdgeWebDriverProtocolCycle() throws Exception {
        setupEdgeDriver();

        EdgeDriverService service = createEdgeDriverService();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--window-size=1200,800");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        if (HEADLESS) {
            options.addArguments("--headless=new");
        }

        EdgeDriver driver = null;

        try {
            step("Before creating EdgeDriver. Expected HTTP command: POST /session");
            driver = new EdgeDriver(service, options);

            step("Session created. Session ID = " + driver.getSessionId());
            pauseForCapture();

            URL page = getClass()
                    .getClassLoader()
                    .getResource("site/index.html");

            assertNotNull(
                    page,
                    "Test HTML page must exist in src/test/resources/site/index.html"
            );

            step("Opening local page. Expected HTTP command: POST /session/{sessionId}/url");
            driver.get(page.toExternalForm());
            pauseForCapture();

            step("Finding button. Expected HTTP command: POST /session/{sessionId}/element");
            WebElement button = driver.findElement(By.cssSelector("#go-button"));
            assertNotNull(button);

            step("Element found. Selenium object = " + button);
            pauseForCapture();

            step("Clicking button. Expected HTTP command: POST /session/{sessionId}/element/{elementId}/click");
            button.click();
            pauseForCapture();

        } finally {
            if (driver != null) {
                step("Closing browser. Expected HTTP command: DELETE /session/{sessionId}");
                driver.quit();
            }

            service.stop();
        }
    }

    private static void setupEdgeDriver() {
        if (!EDGE_DRIVER_PATH.isBlank()) {
            File driverFile = new File(EDGE_DRIVER_PATH);

            if (!driverFile.exists()) {
                throw new IllegalArgumentException(
                        "EdgeDriver file does not exist: " + EDGE_DRIVER_PATH
                );
            }

            System.setProperty("webdriver.edge.driver", driverFile.getAbsolutePath());
            step("Using local msedgedriver: " + driverFile.getAbsolutePath());
        } else {
            step("edge.driver.path is not provided. WebDriverManager will try to resolve msedgedriver.");
            WebDriverManager.edgedriver().setup();
        }
    }

    private static EdgeDriverService createEdgeDriverService() {
        EdgeDriverService.Builder builder = new EdgeDriverService.Builder()
                .usingPort(EDGEDRIVER_PORT)
                .withLogOutput(System.out);

        if (!EDGE_DRIVER_PATH.isBlank()) {
            builder.usingDriverExecutable(new File(EDGE_DRIVER_PATH));
        }

        return builder.build();
    }

    private static void step(String message) {
        System.out.printf("%n[%s] %s%n", LocalTime.now(), message);
    }

    private static void pauseForCapture() throws InterruptedException {
        Thread.sleep(PAUSE_MS);
    }
}