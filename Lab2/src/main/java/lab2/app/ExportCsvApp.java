package lab2.app;

import lab2.io.csv.CsvFunctionExporter;
import lab2.mathfunctions.log.LnFunction;
import lab2.mathfunctions.log.LogBaseFunction;
import lab2.mathfunctions.trig.*;
import lab2.system.SystemFunction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import static java.math.RoundingMode.HALF_EVEN;

public class ExportCsvApp {

  private static final BigDecimal EPS = new BigDecimal("0.000001");
  private static final BigDecimal STEP = new BigDecimal("0.05").setScale(2, HALF_EVEN);

  private static final BigDecimal X_FROM = new BigDecimal("-10").setScale(2, HALF_EVEN);
  private static final BigDecimal X_TO   = new BigDecimal("10").setScale(2, HALF_EVEN);

  private static final BigDecimal X_POS_FROM = new BigDecimal("0.01").setScale(2, HALF_EVEN);

  private static final String DELIM = ";";

  public static void main(String[] args) throws IOException {
    Path outDir = Path.of("output");

    var trig = List.of(
        new NamedFn("sin", new SinFunction()),
        new NamedFn("cos", new CosFunction()),
        new NamedFn("tan", new TanFunction()),
        new NamedFn("sec", new SecFunction()),
        new NamedFn("csc", new CscFunction()),
        new NamedFn("cot", new CotFunction())
    );

    var logs = List.of(
        new NamedFn("ln", new LnFunction()),
        new NamedFn("log2", new LogBaseFunction(2)),
        new NamedFn("log5", new LogBaseFunction(5)),
        new NamedFn("log10", new LogBaseFunction(10))
    );

    var system = new NamedFn("system", new SystemFunction());

    for (NamedFn fn : trig) {
      new CsvFunctionExporter(fn.fn, outDir, DELIM)
          .export(fn.name + ".csv", X_FROM, X_TO, STEP, EPS);
    }

    for (NamedFn fn : logs) {
      new CsvFunctionExporter(fn.fn, outDir, DELIM)
          .export(fn.name + ".csv", X_POS_FROM, X_TO, STEP, EPS);
    }

    new CsvFunctionExporter(system.fn, outDir, DELIM)
        .export(system.name + ".csv", X_FROM, X_TO, STEP, EPS);
  }

  private record NamedFn(String name, lab2.api.MathFunction fn) {}
}