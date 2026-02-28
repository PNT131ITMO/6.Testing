package lab2.mathfunctions.log;

import lab2.core.AbstractMathFunction;

import java.math.BigDecimal;
import java.math.MathContext;

public class LogBaseFunction extends AbstractMathFunction {

    private final int logBase;
    private final LnFunction lnFunction;

    public LogBaseFunction(int logBase) {
        if (logBase <= 0 || logBase == 1) {
            throw new IllegalArgumentException("Invalid log base!");
        }
        this.logBase = logBase;
        this.lnFunction = new LnFunction();
    }

    public int getLogBase() {
        return logBase;
    }

    @Override
    public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
        validateInputs(x, eps);
        
        if (x.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ArithmeticException("log_b(x) undefined for x <= 0");
        }

        MathContext mc = mathCtx(eps, 12);

        BigDecimal lnX = lnFunction.calculate(x, eps);
        BigDecimal lnB = lnFunction.calculate(new BigDecimal(logBase), eps);

        if (isNearZero(lnB, eps)) {
            throw new ArithmeticException("ln(base) is ~0!");
        }

        BigDecimal resultValue = lnX.divide(lnB, mc);
    }

}
