package lab2.domain;

import lab2.system.SystemFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SystemDomainTest {

  private final SystemFunction system = new SystemFunction();
  private final BigDecimal eps = new BigDecimal("0.000001");

  @Test
  void system_throws_at_x_equals_1() {
    assertThrows(ArithmeticException.class, () -> system.calculate(new BigDecimal("1.00"), eps));
  }
}