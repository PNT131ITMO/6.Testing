package lab2.mathfunctions.trig;

import lab2.api.MathFunction;
import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

import static lab2.mathfunctions.trig.TrigConstants.HALF_PI;

public class CosFunction extends AbstractMathFunction {

  private final MathFunction sin;

  public CosFunction() {
    this(new SinFunction());
  }

  public CosFunction(MathFunction sin) {
    this.sin = sin;
  }

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);

    BigDecimal internalEps = eps.setScale(eps.scale() + 8, ROUNDING);
    MathContext mc = mathCtx(eps, 16);

    BigDecimal shifted = HALF_PI.subtract(x, mc);

    BigDecimal resultValue = sin.calculate(shifted, internalEps);
    return roundToEps(resultValue, eps);
  }
}