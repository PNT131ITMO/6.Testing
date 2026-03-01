package lab2.unit;

import lab2.mathfunctions.log.LnFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LnFunctionTest {

  private final LnFunction ln = new LnFunction();
  private final BigDecimal eps = new BigDecimal("0.000001");

  @Test
  void ln_matches_math_log_on_valid_points() {
    double[] xs = {0.1, 0.5, 0.9, 1.1, 2.0, 10.0};
    for (double x : xs) {
      double expected = Math.log(x);
      double actual = ln.calculate(BigDecimal.valueOf(x), eps).doubleValue();
      assertEquals(expected, actual, 1e-4, "x=" + x);
    }
  }

  @Test
  void ln_throws_on_non_positive() {
    assertThrows(ArithmeticException.class, () -> ln.calculate(new BigDecimal("0"), eps));
    assertThrows(ArithmeticException.class, () -> ln.calculate(new BigDecimal("-1"), eps));
  }
}