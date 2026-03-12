package lab2.system;

import lab2.api.MathFunction;
import lab2.stub.CsvTableStubFunction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

public class SystemStubFunction implements MathFunction {

  private final CsvTableStubFunction table;

  public SystemStubFunction(Path csvPath, String delimiterRegex, int xKeyScale) throws IOException {
    this.table = new CsvTableStubFunction("system", csvPath, delimiterRegex, xKeyScale);
  }

  public SystemStubFunction(Path csvPath) throws IOException {
    this(csvPath, ";", 2);
  }

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    return table.calculate(x, eps);
  }
}