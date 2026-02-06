package lab1.part1;


public class App {
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: java App <x> [<eps>] [<maxTerms>]");
      return;
    }

    try {
      double x = Double.parseDouble(args[0]);
      double eps = args.length >= 2 ? Double.parseDouble(args[1]) : 1e-10;
      int maxTerms = args.length >= 3 ? Integer.parseInt(args[2]) : 1_000_000;

      double result = ArcSinSeries.asin(x, eps, maxTerms);
      System.out.printf("arcsin(%.6f) = %.12f%n(with eps=%g, maxTerms=%d)%n", x, result, eps, maxTerms);

      if (x >= -1.0 && x <= 1.0 && Double.isFinite(x)) {
        double ref = Math.asin(x);
        System.out.printf("Math.asin(%.6f) = %.12f%n", x, ref);
        System.out.printf("abs err = %.3e%n", Math.abs(result - ref));
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a valid number for x, a number for eps, and an integer for maxTerms.");
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    } catch (ArithmeticException e) {
      System.out.println("Computation error: " + e.getMessage());
    }
  }
}
