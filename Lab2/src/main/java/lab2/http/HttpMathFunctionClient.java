package lab2.http;

import lab2.core.AbstractMathFunction;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpMathFunctionClient extends AbstractMathFunction {

  private static final Pattern STRING_VALUE = Pattern.compile("\"value\"\\s*:\\s*\"([^\"]+)\"");
  private static final Pattern NUMBER_VALUE = Pattern.compile(
      "\"value\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?(?:[eE][+-]?\\d+)?)"
  );

  private final String functionName;
  private final URI endpoint;
  private final HttpClient httpClient;
  private final Duration timeout;

  public HttpMathFunctionClient(String functionName, URI endpoint, Duration timeout) {
    this(functionName, endpoint, timeout, HttpClient.newBuilder()
        .connectTimeout(Objects.requireNonNull(timeout, "timeout is null"))
        .build());
  }

  public HttpMathFunctionClient(String functionName,
                                URI endpoint,
                                Duration timeout,
                                HttpClient httpClient) {
    this.functionName = Objects.requireNonNull(functionName, "functionName is null");
    this.endpoint = Objects.requireNonNull(endpoint, "endpoint is null");
    this.timeout = Objects.requireNonNull(timeout, "timeout is null");
    this.httpClient = Objects.requireNonNull(httpClient, "httpClient is null");
  }

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);

    String requestJson = "{\"x\":\"" + x.toPlainString()
        + "\",\"eps\":\"" + eps.toPlainString() + "\"}";

    HttpRequest request = HttpRequest.newBuilder(endpoint)
        .timeout(timeout)
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
        .build();

    try {
      HttpResponse<String> response = httpClient.send(
          request,
          HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
      );

      int status = response.statusCode();
      if (status < 200 || status >= 300) {
        throw new ArithmeticException(
            "Remote function " + functionName + " returned HTTP " + status
        );
      }

      BigDecimal value = extractValue(response.body());
      return roundToEps(value, eps);

    } catch (HttpTimeoutException e) {
      throw new ArithmeticException(
          "Remote function " + functionName + " timed out after " + timeout.toMillis() + " ms"
      );
    } catch (IOException e) {
      throw new ArithmeticException(
          "Remote function " + functionName + " I/O error: " + e.getMessage()
      );
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ArithmeticException("Remote function " + functionName + " was interrupted");
    }
  }

  private BigDecimal extractValue(String responseBody) {
    if (responseBody == null || responseBody.isBlank()) {
      throw new ArithmeticException("Remote function " + functionName + " returned empty body");
    }

    Matcher stringMatcher = STRING_VALUE.matcher(responseBody);
    if (stringMatcher.find()) {
      return new BigDecimal(stringMatcher.group(1).trim());
    }

    Matcher numberMatcher = NUMBER_VALUE.matcher(responseBody);
    if (numberMatcher.find()) {
      return new BigDecimal(numberMatcher.group(1).trim());
    }

    throw new ArithmeticException(
        "Remote function " + functionName + " returned invalid JSON: " + responseBody
    );
  }
}