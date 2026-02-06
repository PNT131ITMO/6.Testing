package lab1.part1;

public class ArcSinSeries {
    public static double asin(double x, double eps) {
        return asin(x, eps, 1_000_000);
    }

    public static double asin(double x, double eps, double maxTerms) {
        validateInputs(x, eps, maxTerms);

        if (x == 1.0) return Math.PI / 2.0;
        if (x == -1.0) return -Math.PI / 2.0;
        
        double sum = 0.0;
        double term = x;
        double x2 = x * x;
        
        for (int n= 0; n < maxTerms; n++) {
            sum += term;

            if (Math.abs(term) < eps) {
                return sum;
            }

            double num = 2.0 * n + 1.0;
            double den1 = 2.0 * n + 2.0;
            double den2 = 2.0 * n + 3.0;

            term *= x2 * (num * num) / (den1 * den2);
        }

        throw new ArithmeticException(
            "Series did not converge within " + maxTerms + " terms."
        );
    }

    public static double asinNTerms(double x, int nTerms) {
        if(!Double.isFinite(x)) throw new IllegalArgumentException("x must be finite");
        if(x < -1.0 || x > 1.0) throw new IllegalArgumentException("x must be in [-1, 1]");
        if (nTerms <= 0) throw new IllegalArgumentException("nTerms must be >= 1");

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

    private static void validateInputs(double x, double eps, double maxTerms) {
        if(!Double.isFinite(x)) throw new IllegalArgumentException("x must be finite");
        if(x < -1.0 || x > 1.0) throw new IllegalArgumentException("x must be in [-1, 1]");

        if (!Double.isFinite(eps) || eps <= 0.0) throw new IllegalArgumentException("eps must be finite and > 0");

        if (maxTerms <= 0) throw new IllegalArgumentException("maxTerms must be > 0");
    }
}
