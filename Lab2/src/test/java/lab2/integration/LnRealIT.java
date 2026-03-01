package lab2.integration;

import lab2.mathfunctions.log.LnFunction;
import lab2.system.SystemFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static lab2.testutil.SystemTestKit.*;
import static lab2.testutil.TestPoints.LOG_OK;
import static org.junit.jupiter.api.Assertions.*;

public class LnRealIT {

  private static final BigDecimal EPS = new BigDecimal("0.000001");
  private static final int X_SCALE = 2;

  @Test
  void ln_real_log_branch_except_x1() {
    Deps d = allStubDeps(X_SCALE);
    d.ln = new LnFunction(); 
    SystemFunction system = buildSystem(d);

    for (BigDecimal x : LOG_OK) {
      if (x.compareTo(BigDecimal.ONE) == 0) continue;
      assertNotNull(system.calculate(x, EPS), "x=" + x);
    }
  }

  @Test
  void ln_real_still_throws_at_x1() {
    Deps d = allStubDeps(X_SCALE);
    d.ln = new LnFunction();
    SystemFunction system = buildSystem(d);

    assertThrows(ArithmeticException.class, () -> system.calculate(new BigDecimal("1.00"), EPS));
  }
}