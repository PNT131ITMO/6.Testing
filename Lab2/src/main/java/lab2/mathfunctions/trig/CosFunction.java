package lab2.mathfunctions.trig;

import java.math.BigDecimal;
import java.math.MathContext;

import static lab2.mathfunctions.trig.TrigConstants.HALF_PI;

public class CosFunction extends SinFunction {

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);

    BigDecimal internalEps = eps.setScale(eps.scale() + 8, ROUNDING);
    MathContext mc = mathCtx(eps, 16);

    BigDecimal shifted = HALF_PI.subtract(x, mc);

    BigDecimal resultValue = super.calculate(shifted, internalEps);
    return roundToEps(resultValue, eps);
  }
}