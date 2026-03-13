package lab2.mathfunctions.log;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LogModulesSpyTest {

  private static final BigDecimal EPS = new BigDecimal("0.000001");

  @Test
  void logBase_calls_ln_for_x_and_for_base() throws Exception {
    LnFunction lnSpy = spy(new LnFunction());

    LogBaseFunction log2 = new LogBaseFunction(2);

    Field f = LogBaseFunction.class.getDeclaredField("lnFunction");
    f.setAccessible(true);
    f.set(log2, lnSpy);

    BigDecimal x = new BigDecimal("2.00");
    log2.calculate(x, EPS);

    ArgumentCaptor<BigDecimal> xCaptor = ArgumentCaptor.forClass(BigDecimal.class);
    ArgumentCaptor<BigDecimal> epsCaptor = ArgumentCaptor.forClass(BigDecimal.class);

    verify(lnSpy, atLeast(2)).calculate(xCaptor.capture(), epsCaptor.capture());
    
    boolean sawX = xCaptor.getAllValues().stream().anyMatch(v -> v.compareTo(x) == 0);
    boolean sawBase = xCaptor.getAllValues().stream().anyMatch(v -> v.compareTo(new BigDecimal("2")) == 0);

    assertEquals(true, sawX, "ln(x) was not called with x");
    assertEquals(true, sawBase, "ln(base) was not called with base");
  }
}