package lab2.mathfunctions.log;

import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class LnFunction extends AbstractMathFunction {

  private static final BigDecimal ZERO = BigDecimal.ZERO;
  private static final BigDecimal ONE  = BigDecimal.ONE;
  private static final BigDecimal TWO  = new BigDecimal("2");

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);

    if (x.compareTo(ZERO) <= 0) {
      throw new ArithmeticException("ln(x) undefined for x <= 0: " + x);
    }
    if (x.compareTo(ONE) == 0) {
      return ZERO.setScale(eps.scale(), ROUNDING);
    }

    MathContext mc = mathCtx(eps, 10);

    BigDecimal z = x.subtract(ONE, mc).divide(x.add(ONE, mc), mc);
    BigDecimal z2 = z.multiply(z, mc);

    BigDecimal seriesTerm = z;
    BigDecimal seriesSum  = z;
    int denom = 1;

    while (true) {
      denom += 2;
      seriesTerm = seriesTerm.multiply(z2, mc);
      BigDecimal addend = seriesTerm.divide(new BigDecimal(denom), mc);

      seriesSum = seriesSum.add(addend, mc);

      if (addend.abs().compareTo(eps) <= 0) break;
      if (denom > 200_000) break;
    }

    BigDecimal resultValue = TWO.multiply(seriesSum, mc);
    return roundToEps(resultValue, eps);
  }
}