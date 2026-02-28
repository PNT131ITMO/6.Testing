package lab2.api;

import java.math.BigDecimal;

@FunctionalInterface
public interface MathFunction {
  BigDecimal calculate(BigDecimal x, BigDecimal eps);
}