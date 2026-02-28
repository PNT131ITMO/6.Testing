package lab2.system;

import lab2.core.AbstractMathFunction;
import lab2.mathfunctions.log.LnFunction;
import lab2.mathfunctions.log.LogBaseFunction;
import lab2.mathfunctions.trig.*;

import java.math.BigDecimal;
import java.math.MathContext;

public class SystemFunction extends AbstractMathFunction {

  private final SinFunction sinFunction = new SinFunction();
  private final CosFunction cosFunction = new CosFunction();
  private final TanFunction tanFunction = new TanFunction();
  private final SecFunction secFunction = new SecFunction();
  private final CscFunction cscFunction = new CscFunction();
  private final CotFunction cotFunction = new CotFunction();

  private final LnFunction lnFunction = new LnFunction();
  private final LogBaseFunction log2Function  = new LogBaseFunction(2);
  private final LogBaseFunction log5Function  = new LogBaseFunction(5);
  private final LogBaseFunction log10Function = new LogBaseFunction(10);

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);
    MathContext mc = mathCtx(eps, 18);

    BigDecimal internalEps = eps.setScale(eps.scale() + 10, ROUNDING);

    if (x.compareTo(BigDecimal.ZERO) <= 0) {

      BigDecimal sinValue = sinFunction.calculate(x, internalEps);
      BigDecimal cosValue = cosFunction.calculate(x, internalEps);
      BigDecimal tanValue = tanFunction.calculate(x, internalEps);
      BigDecimal secValue = secFunction.calculate(x, internalEps);
      BigDecimal cscValue = cscFunction.calculate(x, internalEps);
      BigDecimal cotValue = cotFunction.calculate(x, internalEps);

      // (((((tan-csc)^2)^3)-csc) * ((((cot*sin)^2)/sec)/cot))
      BigDecimal tanMinusCsc = tanValue.subtract(cscValue, mc);
      BigDecimal pow2 = powInt(tanMinusCsc, 2, mc);
      BigDecimal pow6 = powInt(pow2, 3, mc); // ((...)^2)^3
      BigDecimal leftBracket = pow6.subtract(cscValue, mc);

      BigDecimal cotMulSin = cotValue.multiply(sinValue, mc);
      BigDecimal cotMulSinSquared = powInt(cotMulSin, 2, mc);

      BigDecimal frac1 = cotMulSinSquared.divide(secValue, mc);
      BigDecimal frac2 = frac1.divide(cotValue, mc);

      BigDecimal part1 = leftBracket.multiply(frac2, mc);

      // ((cot*sin)*((cot*cot)+(cos-(cos/tan))))/tan
      BigDecimal cotMulCot = cotValue.multiply(cotValue, mc);
      BigDecimal cosDivTan = cosValue.divide(tanValue, mc);
      BigDecimal cosMinusCosDivTan = cosValue.subtract(cosDivTan, mc);

      BigDecimal innerSum = cotMulCot.add(cosMinusCosDivTan, mc);
      BigDecimal numerator2 = cotMulSin.multiply(innerSum, mc);
      BigDecimal part2 = numerator2.divide(tanValue, mc);

      BigDecimal resultValue = part1.add(part2, mc);
      return roundToEps(resultValue, eps);

    } else {

      BigDecimal lnValue    = lnFunction.calculate(x, internalEps);
      BigDecimal log2Value  = log2Function.calculate(x, internalEps);
      BigDecimal log5Value  = log5Function.calculate(x, internalEps);
      BigDecimal log10Value = log10Function.calculate(x, internalEps);

      // (((log2*ln)*(log2+log2)) / (log10/log10))
      BigDecimal log2MulLn = log2Value.multiply(lnValue, mc);

      // (log2 + log2) 
      BigDecimal log2PlusLog2 = log2Value.add(log2Value, mc);

      BigDecimal numeratorA = log2MulLn.multiply(log2PlusLog2, mc);

      // (log10/log10)
      BigDecimal denomA = log10Value.divide(log10Value, mc);

      BigDecimal partA = numeratorA.divide(denomA, mc);

      // log10 + (log10 + log10) 
      BigDecimal log10PlusLog10 = log10Value.add(log10Value, mc);
      BigDecimal partB = log10Value.add(log10PlusLog10, mc);

      // log5/ln
      BigDecimal partC = log5Value.divide(lnValue, mc);

      BigDecimal resultValue = partA.add(partB, mc).subtract(partC, mc);
      return roundToEps(resultValue, eps);
    }
  }

  private BigDecimal powInt(BigDecimal base, int exp, MathContext mc) {
    if (exp < 0) throw new IllegalArgumentException("exp must be >= 0");
    if (exp == 0) return BigDecimal.ONE;
    BigDecimal r = BigDecimal.ONE;
    for (int i = 0; i < exp; i++) {
      r = r.multiply(base, mc);
    }
    return r;
  }
}