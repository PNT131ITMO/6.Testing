package lab2.system;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SystemStubIT {

  @Test
  void systemStubReadsFromTable() throws Exception {
    var stub = new SystemStubFunction(Path.of("output/system.csv"));
    BigDecimal eps = new BigDecimal("0.000001");

    BigDecimal y = stub.calculate(new BigDecimal("-10.00"), eps);
    assertNotNull(y);
  }
}