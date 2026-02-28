package lab2.mathfunctions.trig;

import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class TanFunction extends AbstractMathFunction {

  private final SinFunction sinFunction = new SinFunction();
  private final CosFunction cosFunction = new CosFunction();

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);
    MathContext mc = mathCtx(eps, 16);

    BigDecimal internalEps = eps.setScale(eps.scale() + 8, ROUNDING);

    BigDecimal sinValue = sinFunction.calculate(x, internalEps);
    BigDecimal cosValue = cosFunction.calculate(x, internalEps);

    if (isNearZero(cosValue, eps)) {
      throw new ArithmeticException("tan(x) undefined when cos(x)=0 at x=" + x);
    }

    return roundToEps(sinValue.divide(cosValue, mc), eps);
  }
}