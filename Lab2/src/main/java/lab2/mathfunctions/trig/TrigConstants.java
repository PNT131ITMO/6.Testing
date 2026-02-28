package lab2.mathfunctions.trig;

import java.math.BigDecimal;

public final class TrigConstants {
    private TrigConstants() {}

    public static final BigDecimal PI = new BigDecimal(
      "3.141592653589793238462643383279502884197169399375105820974944592307816406286"
    );

    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal TWO = new BigDecimal("2");
    public static final BigDecimal HALF = new BigDecimal("0.5");

    public static final BigDecimal TWO_PI = PI.multiply(TWO);
    public static final BigDecimal HALF_PI = PI.multiply(HALF);
}
