package lab2.testutil;

import java.math.BigDecimal;
import java.util.List;

public final class TestPoints {
  private TestPoints() {}

  public static final List<BigDecimal> TRIG_OK = List.of(
      new BigDecimal("-0.70"),
      new BigDecimal("-1.00"),
      new BigDecimal("-2.30"),
      new BigDecimal("-3.00")
  );

  public static final List<BigDecimal> LOG_OK = List.of(
      new BigDecimal("0.51"), 
      new BigDecimal("2.01"), 
      new BigDecimal("9.96"), 
      new BigDecimal("0.91"), 
      new BigDecimal("1.11"), 
      new BigDecimal("0.96"),
      new BigDecimal("1.01") 
  );

  public static final List<BigDecimal> SHOULD_THROW = List.of(
      new BigDecimal("1.00")
  );
}