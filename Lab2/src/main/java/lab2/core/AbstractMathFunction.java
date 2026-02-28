package lab2.core;

import lab2.api.MathFunction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class AbstractMathFunction implements MathFunction {

  protected static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;

  protected void validateInputs(BigDecimal x, BigDecimal eps) {
    if (x == null) throw new IllegalArgumentException("x is null");
    if (eps == null) throw new IllegalArgumentException("eps is null");
    if (eps.compareTo(BigDecimal.ZERO) <= 0 || eps.compareTo(BigDecimal.ONE) >= 0) {
      throw new IllegalArgumentException("eps must be in (0,1). Got: " + eps);
    }
  }

  protected MathContext mathCtx(BigDecimal eps, int extraDigits) {
    int sigDigits = Math.max(16, eps.scale() + extraDigits);
    return new MathContext(sigDigits, ROUNDING);
  }

  protected boolean isNearZero(BigDecimal v, BigDecimal eps) {
    return v.abs().compareTo(eps) <= 0;
  }

  protected BigDecimal roundToEps(BigDecimal v, BigDecimal eps) {
    return v.setScale(eps.scale(), ROUNDING);
  }
}