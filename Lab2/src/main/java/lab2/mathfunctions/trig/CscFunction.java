package lab2.mathfunctions.trig;

import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class CscFunction extends AbstractMathFunction {

  private final SinFunction sinFunction = new SinFunction();

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);
    MathContext mc = mathCtx(eps, 16);

    BigDecimal internalEps = eps.setScale(eps.scale() + 8, ROUNDING);
    BigDecimal sinValue = sinFunction.calculate(x, internalEps);

    if (isNearZero(sinValue, eps)) {
      throw new ArithmeticException("csc(x) undefined when sin(x)=0 at x=" + x);
    }

    return roundToEps(BigDecimal.ONE.divide(sinValue, mc), eps);
  }
}