package lab2.system;

import lab2.core.AbstractMathFunction;
import lab2.mathfunctions.log.LnFunction;
import lab2.mathfunctions.log.LogBaseFunction;
import lab2.mathfunctions.trig.*;

import java.math.BigDecimal;
import java.math.MathContext;

public class SystemFunction extends AbstractMathFunction {

  private SinFunction sinFunction;
  private CosFunction cosFunction;
  private TanFunction tanFunction;
  private SecFunction secFunction;
  private CscFunction cscFunction;
  private CotFunction cotFunction;

  private LnFunction lnFunction;
  private LogBaseFunction log2Function;
  private LogBaseFunction log5Function;
  private LogBaseFunction log10Function;

  public SystemFunction() {
    SinFunction baseSin = new SinFunction();
    CosFunction baseCos = new CosFunction(baseSin);

    this.sinFunction = baseSin;
    this.cosFunction = baseCos;

    this.tanFunction = new TanFunction(baseSin, baseCos);
    this.cotFunction = new CotFunction(baseSin, baseCos);
    this.secFunction = new SecFunction(baseCos);
    this.cscFunction = new CscFunction(baseSin);

    this.lnFunction = new LnFunction();
    this.log2Function = new LogBaseFunction(2);
    this.log5Function = new LogBaseFunction(5);
    this.log10Function = new LogBaseFunction(10);
  }

  public SystemFunction(
      SinFunction sinFunction,
      CosFunction cosFunction,
      TanFunction tanFunction,
      SecFunction secFunction,
      CscFunction cscFunction,
      CotFunction cotFunction,
      LnFunction lnFunction,
      LogBaseFunction log2Function,
      LogBaseFunction log5Function,
      LogBaseFunction log10Function
  ) {
    this.sinFunction = sinFunction;
    this.cosFunction = cosFunction;
    this.tanFunction = tanFunction;
    this.secFunction = secFunction;
    this.cscFunction = cscFunction;
    this.cotFunction = cotFunction;

    this.lnFunction = lnFunction;
    this.log2Function = log2Function;
    this.log5Function = log5Function;
    this.log10Function = log10Function;
  }

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

      BigDecimal tanMinusCsc = tanValue.subtract(cscValue, mc);
      BigDecimal pow2 = powInt(tanMinusCsc, 2, mc);
      BigDecimal pow6 = powInt(pow2, 3, mc);
      BigDecimal leftBracket = pow6.subtract(cscValue, mc);

      BigDecimal cotMulSin = cotValue.multiply(sinValue, mc);
      BigDecimal cotMulSinSquared = powInt(cotMulSin, 2, mc);

      BigDecimal frac1 = cotMulSinSquared.divide(secValue, mc);
      BigDecimal frac2 = frac1.divide(cotValue, mc);

      BigDecimal part1 = leftBracket.multiply(frac2, mc);

      BigDecimal cotMulCot = cotValue.multiply(cotValue, mc);
      BigDecimal cosDivTan = cosValue.divide(tanValue, mc);
      BigDecimal cosMinusCosDivTan = cosValue.subtract(cosDivTan, mc);

      BigDecimal innerSum = cotMulCot.add(cosMinusCosDivTan, mc);
      BigDecimal numerator2 = cotMulSin.multiply(innerSum, mc);
      BigDecimal part2 = numerator2.divide(tanValue, mc);

      BigDecimal resultValue = part1.add(part2, mc);
      return roundToEps(resultValue, eps);

    } else {
      BigDecimal lnValue = lnFunction.calculate(x, internalEps);
      BigDecimal log2Value = log2Function.calculate(x, internalEps);
      BigDecimal log5Value = log5Function.calculate(x, internalEps);
      BigDecimal log10Value = log10Function.calculate(x, internalEps);

      BigDecimal log2MulLn = log2Value.multiply(lnValue, mc);
      BigDecimal log2PlusLog2 = log2Value.add(log2Value, mc);
      BigDecimal numeratorA = log2MulLn.multiply(log2PlusLog2, mc);

      BigDecimal denomA = log10Value.divide(log10Value, mc);
      BigDecimal partA = numeratorA.divide(denomA, mc);

      BigDecimal log10PlusLog10 = log10Value.add(log10Value, mc);
      BigDecimal partB = log10Value.add(log10PlusLog10, mc);

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