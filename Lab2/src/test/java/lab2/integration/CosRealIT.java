package lab2.integration;

import lab2.mathfunctions.trig.CosFunction;
import lab2.system.SystemFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static lab2.testutil.SystemTestKit.*;
import static lab2.testutil.TestPoints.TRIG_OK;
import static org.junit.jupiter.api.Assertions.*;

public class CosRealIT {

  private static final BigDecimal EPS = new BigDecimal("0.000001");
  private static final int X_SCALE = 2;

  @Test
  void cos_real_trig_branch() {
    Deps d = allStubDeps(X_SCALE);
    d.cos = new CosFunction();
    SystemFunction system = buildSystem(d);

    for (BigDecimal x : TRIG_OK) {
      assertNotNull(system.calculate(x, EPS), "x=" + x);
    }
  }
}