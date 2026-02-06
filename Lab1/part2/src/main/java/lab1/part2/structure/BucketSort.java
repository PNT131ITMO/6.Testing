package lab1.part2.structure;

import java.util.ArrayList;
import java.util.List;

public class BucketSort {
    private enum Point{
        START,
        EARLY_RETURN,
        FIND_MIN_MAX,
        RANGE_ZERO,
        CREATE_BUCKETS,
        MAP_TO_BUCKET,
        PUT_IN_BUCKET,
        ITER_BUCKET,
        SKIP_EMPTY,
        SKIP_SINGLE,
        SORT_BUCKET,
        GATHER,
        END
    }
    public static void sort(double[] a) {
        sort(a, Math.max(1, a == null ? 1 : a.length ), null);
    }

    public static void sort(double[] a, int bucketCount) {
        sort(a, bucketCount, null);
    }

    public static void sort(double[] a, int bucketCount, List<Point> trace) {
        add(trace, Point.START);

        if (a == null) {
            add(trace, Point.END);
            throw new IllegalArgumentException("array must not be null");
        }
        if (bucketCount <= 0) {
            add(trace, Point.END);
            throw new IllegalArgumentException("bucketCount must be > 0");
        }

        int n = a.length;
        if (n <= 1) {
            add(trace, Point.EARLY_RETURN);
            add(trace, Point.END);
            return;
        }

        add(trace, Point.FIND_MIN_MAX);
        double min = a[0], max=a[0];
        for (double v : a) {
            if(!Double.isFinite(v)) {
                add(trace, Point.END);
                throw new IllegalArgumentException("all values must be finite");
            }
            if (v < min) min = v;
            if (v > max) max = v;
        }

        double range = max - min;
        if(range == 0.0) {
            add(trace, Point.RANGE_ZERO);
            add(trace, Point.END);
            return;
        }

        add(trace, Point.CREATE_BUCKETS);
        List<List<Double>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i< bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        for (double v: a) {
            add(trace, Point.MAP_TO_BUCKET);
            int idx = mapToBucket(v, min, range, bucketCount);
            add(trace, Point.PUT_IN_BUCKET);
            buckets.get(idx).add(v);
        }

        for (int i = 0; i < bucketCount; i++) {
            add(trace, Point.ITER_BUCKET);
            List<Double> b = buckets.get(i);

            if (b.isEmpty()) {
                add(trace, Point.SKIP_EMPTY);
                continue;
            }
            if (b.size() == 1) {
                add(trace, Point.SKIP_SINGLE);
                continue;
            }
            add(trace, Point.SORT_BUCKET);
            insertionSort(b);
        }

    }
}
