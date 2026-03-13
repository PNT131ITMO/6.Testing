package lab2.mathfunctions.trig;

import lab2.api.MathFunction;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static lab2.mathfunctions.trig.TrigConstants.HALF_PI;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrigModulesSpyTest {

  private static final BigDecimal EPS = new BigDecimal("0.000001");

  @Test
  void cos_calls_sin_with_shifted_argument() {
    SinFunction realSin = new SinFunction();
    MathFunction sinSpy = spy(realSin);

    CosFunction cos = new CosFunction(sinSpy);

    BigDecimal x = new BigDecimal("1.00");
    cos.calculate(x, EPS);

    ArgumentCaptor<BigDecimal> xCaptor = ArgumentCaptor.forClass(BigDecimal.class);
    ArgumentCaptor<BigDecimal> epsCaptor = ArgumentCaptor.forClass(BigDecimal.class);

    verify(sinSpy, atLeastOnce()).calculate(xCaptor.capture(), epsCaptor.capture());

    BigDecimal expectedShifted = HALF_PI.subtract(x);
    BigDecimal usedX = xCaptor.getValue();

    BigDecimal diff = usedX.subtract(expectedShifted).abs();
    assertTrue(diff.compareTo(new BigDecimal("0.0001")) <= 0);
  }

  @Test
  void tan_calls_sin_and_cos() {
    MathFunction sinSpy = spy(new SinFunction());
    MathFunction cosSpy = spy(new CosFunction(sinSpy));

    TanFunction tan = new TanFunction(sinSpy, cosSpy);

    BigDecimal x = new BigDecimal("1.00");
    tan.calculate(x, EPS);

    verify(sinSpy, atLeastOnce()).calculate(any(), any());
    verify(cosSpy, atLeastOnce()).calculate(any(), any());
  }

  @Test
  void sec_calls_cos() {
      MathFunction sinSpy = spy(new SinFunction());
      MathFunction cosSpy = spy(new CosFunction(sinSpy));

      SecFunction sec = new SecFunction(cosSpy);

      BigDecimal x = new BigDecimal("1.00");
      sec.calculate(x, EPS);

      verify(cosSpy, atLeastOnce()).calculate(any(), any());
}

  @Test
  void csc_calls_sin() {
    MathFunction sinSpy = spy(new SinFunction());

    CscFunction csc = new CscFunction(sinSpy);

    BigDecimal x = new BigDecimal("1.00");
    csc.calculate(x, EPS);

    verify(sinSpy, atLeastOnce()).calculate(any(), any());
  }

  @Test
  void cot_calls_sin_and_cos() {
    MathFunction sinSpy = spy(new SinFunction());
    MathFunction cosSpy = spy(new CosFunction(sinSpy));

    CotFunction cot = new CotFunction(sinSpy, cosSpy);

    BigDecimal x = new BigDecimal("1.00");
    cot.calculate(x, EPS);

    verify(sinSpy, atLeastOnce()).calculate(any(), any());
    verify(cosSpy, atLeastOnce()).calculate(any(), any());
  }
}