package lab2.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import lab2.api.MathFunction;
import lab2.http.HttpMathFunctionClient;
import lab2.system.SystemFunction;
import org.junit.jupiter.api.Test;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
public class SystemWireMockIT {

  private static final BigDecimal EPS = new BigDecimal("0.000001");

  @Test
  void remoteSinSuccess(WireMockRuntimeInfo wm) {
    WireMock wireMock = wm.getWireMock();

    wireMock.register(post(urlEqualTo("/math/sin"))
        .withRequestBody(containing("\"x\":\"-1.00\""))
        .willReturn(okJson("{\"value\":\"2\"}")));

    SystemFunction system = trigSystemWithRemoteSin(wm, Duration.ofMillis(500));

    BigDecimal actual = system.calculate(new BigDecimal("-1.00"), EPS);

    assertEquals(new BigDecimal("504.175000"), actual);
  }

  @Test
  void remoteSinServerError(WireMockRuntimeInfo wm) {
    WireMock wireMock = wm.getWireMock();

    wireMock.register(post(urlEqualTo("/math/sin"))
        .willReturn(aResponse()
            .withStatus(500)
            .withBody("boom")));

    SystemFunction system = trigSystemWithRemoteSin(wm, Duration.ofMillis(500));

    ArithmeticException ex = assertThrows(
        ArithmeticException.class,
        () -> system.calculate(new BigDecimal("-1.00"), EPS)
    );

    assertTrue(
        ex.getMessage().contains("500"),
        "Actual message: " + ex.getMessage()
    );
  }

  @Test
  void remoteSinTimeout(WireMockRuntimeInfo wm) {
    WireMock wireMock = wm.getWireMock();

    wireMock.register(post(urlEqualTo("/math/sin"))
        .willReturn(okJson("{\"value\":\"2\"}").withFixedDelay(1_000)));

    SystemFunction system = trigSystemWithRemoteSin(wm, Duration.ofMillis(100));

    ArithmeticException ex = assertThrows(
        ArithmeticException.class,
        () -> system.calculate(new BigDecimal("-1.00"), EPS)
    );

    assertTrue(
        ex.getMessage().contains("timed out"),
        "Actual message: " + ex.getMessage()
    );
  }

  private static SystemFunction trigSystemWithRemoteSin(WireMockRuntimeInfo wm, Duration timeout) {
    MathFunction remoteSin = new HttpMathFunctionClient(
        "sin",
        URI.create(wm.getHttpBaseUrl() + "/math/sin"),
        timeout
    );

    return new SystemFunction(
        remoteSin,
        fixed("3"),
        fixed("4"),
        fixed("5"),
        fixed("6"),
        fixed("7"),
        fixed("1"),
        fixed("1"),
        fixed("1"),
        fixed("1")
    );
  }

  private static MathFunction fixed(String value) {
    BigDecimal v = new BigDecimal(value);
    return (x, eps) -> v.setScale(eps.scale(), HALF_EVEN);
  }
}