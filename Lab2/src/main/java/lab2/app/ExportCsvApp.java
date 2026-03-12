package lab2.app;

import lab2.api.MathFunction;
import lab2.io.csv.CsvFunctionExporter;
import lab2.mathfunctions.log.LnFunction;
import lab2.mathfunctions.log.LogBaseFunction;
import lab2.mathfunctions.trig.*;
import lab2.system.SystemFunction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_EVEN;

public class ExportCsvApp {

  public static void main(String[] args) throws IOException {
    Args cfg = Args.parse(args);

    Path outDir = Path.of(cfg.outDir);

    Map<String, MathFunction> registry = new LinkedHashMap<>();
    registry.put("sin", new SinFunction());
    registry.put("cos", new CosFunction());
    registry.put("tan", new TanFunction());
    registry.put("sec", new SecFunction());
    registry.put("csc", new CscFunction());
    registry.put("cot", new CotFunction());

    registry.put("ln", new LnFunction());
    registry.put("log2", new LogBaseFunction(2));
    registry.put("log5", new LogBaseFunction(5));
    registry.put("log10", new LogBaseFunction(10));

    registry.put("system", new SystemFunction());

    List<String> toExport = cfg.modules.isEmpty()
        ? new ArrayList<>(registry.keySet())
        : cfg.modules;

    for (String name : toExport) {
      MathFunction fn = registry.get(name);
      if (fn == null) {
        throw new IllegalArgumentException("Unknown module: " + name + ". Known: " + registry.keySet());
      }

      boolean isLogOnly = name.equals("ln") || name.startsWith("log");
      BigDecimal from = isLogOnly ? cfg.xPosFrom : cfg.xFrom;
      BigDecimal to = cfg.xTo;

      new CsvFunctionExporter(fn, outDir, cfg.delim)
          .export(name + ".csv", from, to, cfg.step, cfg.eps);
    }

    System.out.println("Exported: " + toExport + " -> " + outDir.toAbsolutePath());
  }

  private static final class Args {
    String outDir = "output";
    String delim = ";";
    BigDecimal eps = new BigDecimal("0.000001");
    BigDecimal step = new BigDecimal("0.05").setScale(2, HALF_EVEN);

    BigDecimal xFrom = new BigDecimal("-10").setScale(2, HALF_EVEN);
    BigDecimal xTo   = new BigDecimal("10").setScale(2, HALF_EVEN);
    BigDecimal xPosFrom = new BigDecimal("0.01").setScale(2, HALF_EVEN);

    List<String> modules = new ArrayList<>();

    static Args parse(String[] args) {
      Args cfg = new Args();
      Map<String, String> kv = parseKv(args);

      if (kv.containsKey("help") || kv.containsKey("h")) {
        printHelpAndExit();
      }

      cfg.outDir = kv.getOrDefault("out", cfg.outDir);
      cfg.delim  = kv.getOrDefault("delim", cfg.delim);

      if (kv.containsKey("eps")) {
        cfg.eps = new BigDecimal(kv.get("eps"));
      }
      if (kv.containsKey("step")) {
        BigDecimal s = new BigDecimal(kv.get("step"));
        int scale = Math.max(0, s.stripTrailingZeros().scale());
        cfg.step = s.setScale(scale, HALF_EVEN);
      }
      if (kv.containsKey("from")) {
        cfg.xFrom = new BigDecimal(kv.get("from")).setScale(cfg.step.scale(), HALF_EVEN);
      }
      if (kv.containsKey("to")) {
        cfg.xTo = new BigDecimal(kv.get("to")).setScale(cfg.step.scale(), HALF_EVEN);
      }
      if (kv.containsKey("posFrom")) {
        cfg.xPosFrom = new BigDecimal(kv.get("posFrom")).setScale(cfg.step.scale(), HALF_EVEN);
      }

      if (cfg.eps.compareTo(BigDecimal.ZERO) <= 0 || cfg.eps.compareTo(BigDecimal.ONE) >= 0) {
        throw new IllegalArgumentException("--eps must be in (0,1)");
      }
      if (cfg.step.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("--step must be > 0");
      }
      if (cfg.xFrom.compareTo(cfg.xTo) > 0) {
        throw new IllegalArgumentException("--from must be <= --to");
      }
      if (cfg.xPosFrom.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("--posFrom must be > 0 for log range");
      }

      if (kv.containsKey("modules")) {
        cfg.modules = Arrays.stream(kv.get("modules").split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
      }

      return cfg;
    }

    private static Map<String, String> parseKv(String[] args) {
      Map<String, String> kv = new HashMap<>();
      for (int i = 0; i < args.length; i++) {
        String a = args[i];
        if (!a.startsWith("--")) continue;

        String key = a.substring(2).trim();
        String val = "true";

        if (key.contains("=")) {
          String[] parts = key.split("=", 2);
          key = parts[0];
          val = parts[1];
        } else if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
          val = args[++i];
        }

        kv.put(key, val);
      }
      return kv;
    }

    private static void printHelpAndExit() {
      System.out.println("""
          Usage:
            --out outputDir
            --delim ; | , | \\t | ...
            --step 0.05
            --eps 0.000001
            --from -10
            --to 10
            --posFrom 0.01
            --modules sin,cos,tan,sec,csc,cot,ln,log2,log5,log10,system

          Examples:
            gradle run --args="--step 0.1 --delim , --out out --modules system"
            gradle run --args="--eps 0.00001 --from -5 --to 5 --modules sin,cos,tan"
          """);
      System.exit(0);
    }
  }
}