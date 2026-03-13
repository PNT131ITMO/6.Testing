package lab2.system;

import lab2.mathfunctions.log.LnFunction;
import lab2.mathfunctions.log.LogBaseFunction;
import lab2.mathfunctions.trig.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SystemModulesSpyTest {

  private static final BigDecimal EPS = new BigDecimal("0.000001");

  @Test
  void whenXLessOrEqualZero_calls_trig_modules_not_log_modules() {
    SinFunction sin = spy(new SinFunction());
    CosFunction cos = spy(new CosFunction(sin));
    TanFunction tan = spy(new TanFunction(sin, cos));
    SecFunction sec = spy(new SecFunction(cos));
    CscFunction csc = spy(new CscFunction(sin));
    CotFunction cot = spy(new CotFunction(sin, cos));

    LnFunction ln = spy(new LnFunction());
    LogBaseFunction log2 = spy(new LogBaseFunction(2));
    LogBaseFunction log5 = spy(new LogBaseFunction(5));
    LogBaseFunction log10 = spy(new LogBaseFunction(10));

    SystemFunction system = new SystemFunction(
        sin, cos, tan, sec, csc, cot,
        ln, log2, log5, log10
    );

    system.calculate(new BigDecimal("-1.00"), EPS);

    verify(sin, atLeastOnce()).calculate(any(), any());
    verify(cos, atLeastOnce()).calculate(any(), any());

    verify(ln, never()).calculate(any(), any());
    verify(log2, never()).calculate(any(), any());
    verify(log5, never()).calculate(any(), any());
    verify(log10, never()).calculate(any(), any());
  }

  @Test
  void whenXGreaterZero_calls_log_modules_not_trig_modules() {
    SinFunction sin = spy(new SinFunction());
    CosFunction cos = spy(new CosFunction(sin));
    TanFunction tan = spy(new TanFunction(sin, cos));
    SecFunction sec = spy(new SecFunction(cos));
    CscFunction csc = spy(new CscFunction(sin));
    CotFunction cot = spy(new CotFunction(sin, cos));

    LnFunction ln = spy(new LnFunction());
    LogBaseFunction log2 = spy(new LogBaseFunction(2));
    LogBaseFunction log5 = spy(new LogBaseFunction(5));
    LogBaseFunction log10 = spy(new LogBaseFunction(10));

    SystemFunction system = new SystemFunction(
        sin, cos, tan, sec, csc, cot,
        ln, log2, log5, log10
    );

    system.calculate(new BigDecimal("2.00"), EPS);

    verify(ln, atLeastOnce()).calculate(any(), any());
    verify(log2, atLeastOnce()).calculate(any(), any());
    verify(log5, atLeastOnce()).calculate(any(), any());
    verify(log10, atLeastOnce()).calculate(any(), any());

    verify(sin, never()).calculate(any(), any());
    verify(cos, never()).calculate(any(), any());
    verify(tan, never()).calculate(any(), any());
    verify(sec, never()).calculate(any(), any());
    verify(csc, never()).calculate(any(), any());
    verify(cot, never()).calculate(any(), any());
  }
}