package lab2.io.csv;

import lab2.api.MathFunction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.math.RoundingMode.HALF_EVEN;

public class CsvFunctionExporter {

  private final MathFunction function;
  private final Path outputDir;
  private final String delimiter;

  public CsvFunctionExporter(MathFunction function, Path outputDir, String delimiter) {
    this.function = function;
    this.outputDir = outputDir;
    this.delimiter = delimiter;
  }

  public void export(String fileName,
                     BigDecimal xStart,
                     BigDecimal xEnd,
                     BigDecimal xStep,
                     BigDecimal eps) throws IOException {

    Files.createDirectories(outputDir);
    Path out = outputDir.resolve(fileName);

    try (BufferedWriter bw = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
      bw.write("x" + delimiter + "value");
      bw.newLine();

      BigDecimal x = xStart;
      int guard = 0;

      while (x.compareTo(xEnd) <= 0) {
        try {
          BigDecimal y = function.calculate(x, eps);
          bw.write(x.toPlainString() + delimiter + y.toPlainString());
        } catch (ArithmeticException ex) {
          bw.write(x.toPlainString() + delimiter);
        }
        bw.newLine();

        x = x.add(xStep).setScale(xStep.scale(), HALF_EVEN);
        if (++guard > 5_000_000) break;
      }
    }
  }
}