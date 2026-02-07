package lab1.part2;

import lab1.part2.structure.BucketSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java App \"<numbers separated by comma or space>\" [bucketCount]");
            return;
        }

        double[] a = parseArray(args[0]);
        int bucketCount = (args.length >= 2) ? Integer.parseInt(args[1]) : Math.max(1, a.length);

        List<BucketSort.Point> trace = new ArrayList<>();

        System.out.println("Input : " + Arrays.toString(a));
        System.out.println("k     : " + bucketCount);

        BucketSort.sort(a, bucketCount, trace);

        System.out.println("Sorted: " + Arrays.toString(a));
        System.out.println("Trace : " + trace);
    }

    private static double[] parseArray(String s) {
        String cleaned = s.trim().replace(",", " ");
        String[] parts = cleaned.split("\\s+");

        double[] a = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            a[i] = Double.parseDouble(parts[i]);
        }
        return a;
    }
}
