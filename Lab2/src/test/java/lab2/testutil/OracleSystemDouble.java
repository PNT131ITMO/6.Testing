package lab2.testutil;

public final class OracleSystemDouble {
  private OracleSystemDouble() {}

  public static double eval(double x) {
    if (x <= 0.0) {
      double sin = Math.sin(x);
      double cos = Math.cos(x);
      double tan = Math.tan(x);
      double sec = 1.0 / cos;
      double csc = 1.0 / sin;
      double cot = cos / sin;

      double tanMinusCsc = tan - csc;
      double part1Left = Math.pow(Math.pow(tanMinusCsc, 2), 3) - csc; 
      double cotMulSin = cot * sin;
      double part1Right = ((Math.pow(cotMulSin, 2) / sec) / cot);
      double part1 = part1Left * part1Right;

      double part2Inner = (cot * cot) + (cos - (cos / tan));
      double part2 = (cotMulSin * part2Inner) / tan;

      return part1 + part2;
    } else {
      double ln = Math.log(x);
      double log2 = Math.log(x) / Math.log(2.0);
      double log5 = Math.log(x) / Math.log(5.0);
      double log10 = Math.log10(x);

      double denomA = (log10 / log10);
      double partA = ((log2 * ln) * (log2 + log2)) / denomA;
      double partB = (log10 + (log10 + log10));
      double partC = (log5 / ln);

      return partA + partB - partC;
    }
  }
}