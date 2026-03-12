package lab2.io.csv;

import lab2.api.MathFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvFunctionExporterTest {

  @TempDir
  Path tempDir;

  @Test
  void export_createsCsvWithDelimiter_andWritesValues() throws Exception {
    MathFunction fn = (x, eps) -> x.multiply(new BigDecimal("2"));

    CsvFunctionExporter exporter = new CsvFunctionExporter(fn, tempDir, ";");

    exporter.export(
        "test.csv",
        new BigDecimal("0.00"),
        new BigDecimal("0.10"),
        new BigDecimal("0.05"),
        new BigDecimal("0.000001")
    );

    Path out = tempDir.resolve("test.csv");
    assertTrue(Files.exists(out));

    List<String> lines = Files.readAllLines(out);
    assertTrue(lines.size() >= 2);

    assertTrue(lines.get(0).contains(";") || lines.get(1).contains(";"));

    boolean has00 = lines.stream().anyMatch(s -> s.startsWith("0.00;0.00") || s.startsWith("0.0;0.0"));
    assertTrue(has00);
  }

  @Test
  void export_writesEmptyValue_whenFunctionThrows() throws Exception {
    MathFunction fn = (x, eps) -> {
      if (x.compareTo(new BigDecimal("0.05")) == 0) {
        throw new ArithmeticException("boom");
      }
      return BigDecimal.ONE;
    };

    CsvFunctionExporter exporter = new CsvFunctionExporter(fn, tempDir, ";");

    exporter.export(
        "throw.csv",
        new BigDecimal("0.00"),
        new BigDecimal("0.10"),
        new BigDecimal("0.05"),
        new BigDecimal("0.000001")
    );

    List<String> lines = Files.readAllLines(tempDir.resolve("throw.csv"));

    boolean hasEmpty = lines.stream().anyMatch(s -> s.startsWith("0.05;") && (s.equals("0.05;") || s.endsWith(";")));
    assertTrue(hasEmpty);
  }
}