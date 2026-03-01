package lab2.unit;

import lab2.mathfunctions.trig.SinFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SinFunctionTest {

  private final SinFunction sin = new SinFunction();
  private final BigDecimal eps = new BigDecimal("0.000001");

  @Test
  void sin_matches_math_sin_on_points() {
    double[] xs = {-6.0, -3.0, -1.0, -0.5, 0.0, 0.5, 1.0, 3.0, 6.0};
    for (double x : xs) {
      double expected = Math.sin(x);
      double actual = sin.calculate(BigDecimal.valueOf(x), eps).doubleValue();
      assertEquals(expected, actual, 1e-4, "x=" + x);
    }
  }
}