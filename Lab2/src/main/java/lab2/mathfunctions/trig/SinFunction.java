package lab2.mathfunctions.trig;

import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

import static lab2.math.trig.TrigConstants.*;

public class SinFunction extends AbstractMathFunction {

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);
    MathContext mc = mathCtx(eps, 14);

    BigDecimal xReduced = normalizeToMinusPiPi(x, mc);

    BigDecimal seriesTerm = xReduced;
    BigDecimal seriesSum = seriesTerm;

    BigDecimal x2 = xReduced.multiply(xReduced, mc);

    int k = 0;
    while (true) {
      long a = 2L * k + 2;
      long b = 2L * k + 3;
      BigDecimal denom = new BigDecimal(a * b);

      seriesTerm = seriesTerm.multiply(x2, mc).divide(denom, mc).negate();
      seriesSum = seriesSum.add(seriesTerm, mc);

      if (seriesTerm.abs().compareTo(eps) <= 0) break;
      if (++k > 100_000) break;
    }

    return roundToEps(seriesSum, eps);
  }

  private BigDecimal normalizeToMinusPiPi(BigDecimal x, MathContext mc) {
    BigDecimal r = x.remainder(TWO_PI, mc);
    if (r.compareTo(PI) > 0) r = r.subtract(TWO_PI, mc);
    if (r.compareTo(PI.negate()) < 0) r = r.add(TWO_PI, mc);
    return r;
  }
}