package lab2.stub;

import lab2.core.AbstractMathFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CsvTableStubFunction extends AbstractMathFunction {

  private final String functionName;
  private final Map<String, BigDecimal> xToY = new HashMap<>();
  private final int xKeyScale;
  private final String delimiterRegex;

  public CsvTableStubFunction(String functionName, Path csvPath, String delimiterRegex, int xKeyScale)
      throws IOException {
    this.functionName = functionName;
    this.xKeyScale = xKeyScale;
    this.delimiterRegex = delimiterRegex;
    load(csvPath);
  }

  private void load(Path csvPath) throws IOException {
    try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) continue;
        if (line.startsWith("x")) continue; // header: x;value

        String[] parts = line.split(delimiterRegex);
        if (parts.length < 2) continue;

        BigDecimal x = new BigDecimal(parts[0].trim()).setScale(xKeyScale, ROUNDING);
        String key = x.toPlainString();

        String yStr = parts[1].trim();
        if (yStr.isEmpty() || yStr.equalsIgnoreCase("NaN")) continue;

        BigDecimal y = new BigDecimal(yStr);
        xToY.put(key, y);
      }
    }
  }

  @Override
  public BigDecimal calculate(BigDecimal x, BigDecimal eps) {
    validateInputs(x, eps);

    BigDecimal keyX = x.setScale(xKeyScale, ROUNDING);
    BigDecimal y = xToY.get(keyX.toPlainString());

    if (y == null) {
      throw new ArithmeticException("No stub value for " + functionName + " at x=" + keyX);
    }
    return y.setScale(eps.scale(), ROUNDING);
  }
}