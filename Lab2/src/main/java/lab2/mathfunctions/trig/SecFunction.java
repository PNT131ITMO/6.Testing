package lab2.mathfunctions.trig;

import lab2.api.MathFunction;
import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class SecFunction extends AbstractMathFunction {

  private final MathFunction cos;

  public SecFunction() {
    this(new CosFunction());
  }

  public SecFunction(MathFunction cos) {
    this.cos = cos;
  }

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);
    MathContext mc = mathCtx(eps, 16);

    BigDecimal internalEps = eps.setScale(eps.scale() + 8, ROUNDING);
    BigDecimal cosValue = cos.calculate(x, internalEps);

    if (isNearZero(cosValue, eps)) {
      throw new ArithmeticException("sec(x) undefined when cos(x)=0 at x=" + x);
    }

    return roundToEps(BigDecimal.ONE.divide(cosValue, mc), eps);
  }
}