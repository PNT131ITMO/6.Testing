package lab2.mathfunctions.log;

import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class LogBaseFunction extends AbstractMathFunction {

  private final int base;
  private final LnFunction lnFunction = new LnFunction();

  public LogBaseFunction(int base) {
    if (base <= 0 || base == 1) throw new IllegalArgumentException("Invalid base: " + base);
    this.base = base;
  }

  public int getBase() {
    return base;
  }

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);

    if (x.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ArithmeticException("log_b(x) undefined for x <= 0: " + x);
    }

    MathContext mc = mathCtx(eps, 12);

    BigDecimal lnX = lnFunction.calculate(x, eps);
    BigDecimal lnB = lnFunction.calculate(new BigDecimal(base), eps);

    if (isNearZero(lnB, eps)) {
      throw new ArithmeticException("ln(base) ~ 0 for base=" + base);
    }

    BigDecimal resultValue = lnX.divide(lnB, mc);
    return roundToEps(resultValue, eps);
  }
}