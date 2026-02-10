package lab1.part1.function;

public class ArcSinSeries {

    public static double asin(double x, double eps) {
        return asin(x, eps, 1_000_000);
    }

    public static double asin(double x, double eps, double maxTerms) {
        return asin(x, eps, (int) maxTerms);
    }

    public static double asin(double x, double eps, int maxTerms) {
        validateInputs(eps, maxTerms);

        if (Double.isNaN(x)) return Double.NaN;
        if (!Double.isFinite(x)) return Double.NaN;
        if (x < -1.0 || x > 1.0) return Double.NaN;

        if (x == 0.0) return x;

        if (x == 1.0) return Math.PI / 2.0;
        if (x == -1.0) return -Math.PI / 2.0;

        if (Math.abs(x) >= 0.9) {
            double sqrtTerm = Math.sqrt(1.0 - x * x);
            double inner = asin(sqrtTerm, eps, maxTerms);
            double reduced = (Math.PI / 2.0) - inner;
            return Math.copySign(reduced, x);
        }

        double sum = 0.0;
        double term = x;
        double x2 = x * x;

        for (int n = 0; n < maxTerms; n++) {
            sum += term;

            if (Math.abs(term) < eps) return sum;

            double num = 2.0 * n + 1.0;
            double den1 = 2.0 * n + 2.0;
            double den2 = 2.0 * n + 3.0;

            term *= x2 * (num * num) / (den1 * den2);
        }

        throw new ArithmeticException("Series did not converge within " + maxTerms + " terms.");
    }

    public static double asinNTerms(double x, int nTerms) {
        if (nTerms <= 0) throw new IllegalArgumentException("nTerms must be >= 1");

        if (Double.isNaN(x)) return Double.NaN;
        if(!Double.isFinite(x)) return Double.NaN;
        if (x < -1.0 || x > 1.0) return Double.NaN;

        if (x == 0.0) return x;
        if (x == 1.0) return Math.PI / 2.0;
        if (x == -1.0) return -Math.PI / 2.0;

        double sum = 0.0;
        double term = x;
        double x2 = x * x;

        for (int n = 0; n < nTerms; n++) {
            sum += term;

            double num = 2.0 * n + 1.0;
            double den1 = 2.0 * n + 2.0;
            double den2 = 2.0 * n + 3.0;

            term *= x2 * (num * num) / (den1 * den2);
        }
        return sum;
    }

    private static void validateInputs(double eps, int maxTerms) {
        if (!Double.isFinite(eps) || eps <= 0.0)
            throw new IllegalArgumentException("eps must be finite and > 0");
        if (maxTerms <= 0)
            throw new IllegalArgumentException("maxTerms must be > 0");
    }
}
