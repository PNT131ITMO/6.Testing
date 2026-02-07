package lab1.part2.structure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BucketSortTest {

    private static void assertSortedNonDecreasing(double[] a) {
        for (int i = 1; i < a.length; i++) {
            assertTrue(a[i - 1] <= a[i], "Array not sorted at i=" + i);
        }
    }

    @Test
    @DisplayName("Correctness: empty and single-element arrays")
    void correctnessTrivial() {
        double[] a0 = {};
        BucketSort.sort(a0, 5);
        assertArrayEquals(new double[]{}, a0);

        double[] a1 = {42.0};
        BucketSort.sort(a1, 5);
        assertArrayEquals(new double[]{42.0}, a1);
    }

    @Test
    @DisplayName("Correctness: all elements equal")
    void correctnessAllEqual() {
        double[] a = {7, 7, 7, 7, 7};
        BucketSort.sort(a, 3);
        assertSortedNonDecreasing(a);
        assertArrayEquals(new double[]{7, 7, 7, 7, 7}, a);
    }

    @Test
    @DisplayName("Correctness: typical mixed values")
    void correctnessMixed() {
        double[] a = {5, 4, 3, 2, 1};
        BucketSort.sort(a, 5);
        assertSortedNonDecreasing(a);
        assertArrayEquals(new double[]{1, 2, 3, 4, 5}, a);

        double[] b = {-10, 0, 5, -3, 2};
        BucketSort.sort(b, 5);
        assertSortedNonDecreasing(b);
        assertArrayEquals(new double[]{-10, -3, 0, 2, 5}, b);
    }

    @Test
    @DisplayName("Trace test: matches etalon sequence for a controlled dataset (min=0, max=1, k=10)")
    void traceMatchesEtalon() {
        double[] a = {
                0.78, 0.17, 0.39, 0.26, 0.72, 0.94, 0.21, 0.12, 0.23, 0.68,
                0.0, 1.0
        };
        int k = 10;

        List<BucketSort.Point> trace = new ArrayList<>();
        BucketSort.sort(a, k, trace);

        List<BucketSort.Point> expected = new ArrayList<>();
        expected.add(BucketSort.Point.START);
        expected.add(BucketSort.Point.FIND_MIN_MAX);
        expected.add(BucketSort.Point.CREATE_BUCKETS);

        for (int i = 0; i < 12; i++) {
            expected.add(BucketSort.Point.MAP_TO_BUCKET);
            expected.add(BucketSort.Point.PUT_IN_BUCKET);
        }

        for (int i = 0; i < k; i++) {
            expected.add(BucketSort.Point.ITER_BUCKET);
            switch (i) {
                case 0 -> expected.add(BucketSort.Point.SKIP_SINGLE);
                case 1 -> expected.add(BucketSort.Point.SORT_BUCKET);
                case 2 -> expected.add(BucketSort.Point.SORT_BUCKET);
                case 3 -> expected.add(BucketSort.Point.SKIP_SINGLE);
                case 4 -> expected.add(BucketSort.Point.SKIP_EMPTY);
                case 5 -> expected.add(BucketSort.Point.SKIP_EMPTY);
                case 6 -> expected.add(BucketSort.Point.SKIP_SINGLE);
                case 7 -> expected.add(BucketSort.Point.SORT_BUCKET);
                case 8 -> expected.add(BucketSort.Point.SKIP_EMPTY);
                case 9 -> expected.add(BucketSort.Point.SORT_BUCKET);
            }
        }

        expected.add(BucketSort.Point.GATHER);
        expected.add(BucketSort.Point.END);

        assertEquals(expected, trace);

        assertSortedNonDecreasing(a);
    }

    @Test
    @DisplayName("Trace test: early return for empty array")
    void traceEarlyReturnEmpty() {
        double[] a = {};
        List<BucketSort.Point> trace = new ArrayList<>();
        BucketSort.sort(a, 5, trace);

        List<BucketSort.Point> expected = List.of(
            BucketSort.Point.START,
            BucketSort.Point.EARLY_RETURN,
            BucketSort.Point.END
        );

        assertEquals(expected, trace);
    }

    @Test
    @DisplayName("Trace test: range zero (all equal)")
    void traceRangeZeroAllEqual() {
        double[] a = {7, 7, 7, 7};
        List<BucketSort.Point> trace = new ArrayList<>();
        BucketSort.sort(a, 3, trace);

        List<BucketSort.Point> expected = List.of( 
            BucketSort.Point.START,
            BucketSort.Point.FIND_MIN_MAX,
            BucketSort.Point.RANGE_ZERO,
            BucketSort.Point.END
        );

        assertEquals(expected, trace);
    }


    @Test
    @DisplayName("Invalid inputs: null array, non-finite values, invalid bucketCount")
    void invalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> BucketSort.sort(null, 5, new ArrayList<>()));

        double[] hasNaN = {0.1, Double.NaN, 0.2};
        assertThrows(IllegalArgumentException.class, () -> BucketSort.sort(hasNaN, 3, new ArrayList<>()));

        double[] ok = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> BucketSort.sort(ok, 0, new ArrayList<>()));
    }
}
