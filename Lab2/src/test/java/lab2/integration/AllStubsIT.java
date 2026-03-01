package lab2.integration;

import lab2.system.SystemFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static lab2.testutil.SystemTestKit.*;
import static lab2.testutil.TestPoints.*;
import static org.junit.jupiter.api.Assertions.*;

public class AllStubsIT {

  private static final BigDecimal EPS = new BigDecimal("0.000001");
  private static final int X_SCALE = 2;

  @Test
  void all_stubs_trig_branch() {
    SystemFunction system = buildSystem(allStubDeps(X_SCALE));
    for (BigDecimal x : TRIG_OK) {
      assertNotNull(system.calculate(x, EPS), "x=" + x);
    }
  }

  @Test
  void all_stubs_log_branch_except_x1() {
    SystemFunction system = buildSystem(allStubDeps(X_SCALE));
    for (BigDecimal x : LOG_OK) {
      if (x.compareTo(BigDecimal.ONE) == 0) continue;
      assertNotNull(system.calculate(x, EPS), "x=" + x);
    }
  }
}