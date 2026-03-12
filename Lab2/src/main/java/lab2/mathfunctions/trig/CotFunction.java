package lab2.mathfunctions.trig;

import lab2.api.MathFunction;
import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class CotFunction extends AbstractMathFunction {

  private final MathFunction sin;
  private final MathFunction cos;

  public CotFunction() {
    SinFunction baseSin = new SinFunction();
    this.sin = baseSin;
    this.cos = new CosFunction(baseSin);
  }

  public CotFunction(MathFunction sin, MathFunction cos) {
    this.sin = sin;
    this.cos = cos;
  }

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);
    MathContext mc = mathCtx(eps, 16);

    BigDecimal internalEps = eps.setScale(eps.scale() + 8, ROUNDING);

    BigDecimal sinValue = sin.calculate(x, internalEps);
    BigDecimal cosValue = cos.calculate(x, internalEps);

    if (isNearZero(sinValue, eps)) {
      throw new ArithmeticException("cot(x) undefined when sin(x)=0 at x=" + x);
    }

    return roundToEps(cosValue.divide(sinValue, mc), eps);
  }
}