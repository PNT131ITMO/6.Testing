package lab2.integration;

import lab2.system.SystemFunction;
import lab2.testutil.OracleSystemDouble;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static lab2.testutil.TestPoints.LOG_OK;
import static lab2.testutil.TestPoints.TRIG_OK;
import static org.junit.jupiter.api.Assertions.*;

public class FullRealIT {

  private static final BigDecimal EPS = new BigDecimal("0.000001");
  private final SystemFunction system = new SystemFunction();

  @Test
  void full_real_matches_oracle_on_trig_points() {
    for (BigDecimal x : TRIG_OK) {
      double expected = OracleSystemDouble.eval(x.doubleValue());
      double actual = system.calculate(x, EPS).doubleValue();

      assertEquals(expected, actual, 1e-2, "x=" + x);
    }
  }

  @Test
  void full_real_matches_oracle_on_log_points_except_x1() {
    for (BigDecimal x : LOG_OK) {
      if (x.compareTo(BigDecimal.ONE) == 0) continue;
      double expected = OracleSystemDouble.eval(x.doubleValue());
      double actual = system.calculate(x, EPS).doubleValue();

      assertEquals(expected, actual, 1e-3, "x=" + x);
    }
  }
}