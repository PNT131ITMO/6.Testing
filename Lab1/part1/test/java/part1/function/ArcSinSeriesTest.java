package lab1.part1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ArcSinSeriesTest {

    private static final double EPS = 1e-10;
    private static final int MAX_TERMS = 1_000_000;

    private static double tolFor(double x) {
        return (Math.abs(x) <= 0.9) ? 1e-8 : 1e-6;
    }

    @Test
    @DisplayName("Invalid domain throws IllegalArgumentException")
    void invalidDomainThrows() {
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(1.0001, EPS));
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(-1.5, EPS));
    }

    @Test
    @DisplayName("NaN/Infinity inputs throw IllegalArgumentException")
    void nanAndInfinityThrow() {
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(Double.NaN, EPS));
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(Double.POSITIVE_INFINITY, EPS));
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(Double.NEGATIVE_INFINITY, EPS));
    }

    @Test
    @DisplayName("Invalid eps throws IllegalArgumentException")
    void invalidEpsThrows() {
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(0.5, 0.0));
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(0.5, -1e-6));
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(0.5, Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(0.5, Double.POSITIVE_INFINITY));
    }

    @ParameterizedTest(name = "corner x = {0}")
    @DisplayName("Corner & boundary values (inside compare to Math.asin; outside => throw)")
    @ValueSource(doubles = {
            -999.9,
            -1.0000001,
            -1.0,
            -0.999999,
            -0.99,
            -0.5,
            -0.000001,
            -0.0001,
            -0.0,
            0.0,
            0.0001,
            0.000001,
            0.5,
            0.99,
            0.999999,
            1.0,
            1.0000001,
            999.9,
            Double.MIN_VALUE
    })
    void checkCornerValues(double x) {
        if (!Double.isFinite(x) || Math.abs(x) > 1.0) {
            assertThrows(IllegalArgumentException.class, () -> ArcSinSeries.asin(x, EPS));
            return;
        }

        double actual = ArcSinSeries.asin(x, EPS, MAX_TERMS);
        double expected = Math.asin(x);

        assertEquals(expected, actual, tolFor(x));
    }

    @Test
    @DisplayName("Preserves -0.0 sign")
    void preservesNegativeZero() {
        double x = -0.0d;
        double y = ArcSinSeries.asin(x, EPS, MAX_TERMS);

        assertEquals(0.0, y, 0.0);
        assertEquals(Double.doubleToRawLongBits(x), Double.doubleToRawLongBits(y));
    }


    @ParameterizedTest
    @DisplayName("Matches Math.asin for typical points |x| <= 0.9")
    @ValueSource(doubles = {-0.9, -0.5, -0.1, 0.0, 0.1, 0.5, 0.9})
    void matchesMathAsinInsideInterval(double x) {
        double actual = ArcSinSeries.asin(x, EPS, MAX_TERMS);
        double expected = Math.asin(x);
        assertEquals(expected, actual, 1e-8);
    }

    @ParameterizedTest
    @DisplayName("Odd property: asin(-x) = -asin(x)")
    @ValueSource(doubles = {-0.8, -0.3, 0.2, 0.7})
    void oddProperty(double x) {
        double a = ArcSinSeries.asin(x, EPS, MAX_TERMS);
        double b = ArcSinSeries.asin(-x, EPS, MAX_TERMS);
        assertEquals(a, -b, 1e-8);
    }

    @Test
    @DisplayName("Monotonic sample: asin(x) increases with x")
    void monotonicSample() {
        double a = ArcSinSeries.asin(-0.4, EPS, MAX_TERMS);
        double b = ArcSinSeries.asin(0.2, EPS, MAX_TERMS);
        double c = ArcSinSeries.asin(0.9, EPS, MAX_TERMS);
        assertTrue(a < b);
        assertTrue(b < c);
    }

    @Test
    @DisplayName("Near 1 still reasonable (slower convergence => looser tol / bigger maxTerms)")
    void nearOneStillReasonable() {
        double x = 0.999999;
        double actual = ArcSinSeries.asin(x, EPS, 2_000_000);
        double expected = Math.asin(x);
        assertEquals(expected, actual, 1e-6);
    }

    @Test
    @DisplayName("First terms sanity: asin(x) ≈ x + x^3/6 + 3x^5/40 (3 terms)")
    void firstTermsSanityCheck() {
        double x = 0.5;
        double expected = x + Math.pow(x, 3) / 6.0 + 3.0 * Math.pow(x, 5) / 40.0;
        double actual = ArcSinSeries.asinNTerms(x, 3);
        assertEquals(expected, actual, 1e-15);
    }

    @ParameterizedTest(name = "csv x={0}, y={1}")
    @DisplayName("Check values from CSV (x;y) with adaptive tolerance")
    @CsvFileSource(resources = "/check_values.csv", numLinesToSkip = 1, delimiter = ';')
    void checkFromCsv(double x, double y) {
        double actual = ArcSinSeries.asin(x, EPS, MAX_TERMS);
        assertEquals(y, actual, tolFor(x));
    }

    @Test
    @DisplayName("Fuzzy testing (seeded, limited iterations, reproducible)")
    void fuzzyTestingSeeded() {
        Random rnd = new Random(1234567L);
        int iterations = 20_000;

        for (int i = 0; i < iterations; i++) {
            double x = -0.9999 + (1.9998 * rnd.nextDouble());
            double actual = ArcSinSeries.asin(x, EPS, MAX_TERMS);
            double expected = Math.asin(x);
            assertEquals(expected, actual, tolFor(x));
        }
    }
}
