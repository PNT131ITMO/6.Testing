package dop.splay;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class World {
    public final SplayTree sut = new SplayTree();
    public final TreeSet<Integer> model = new TreeSet<>();

    private int stepCount;
    private SplayTreeMetrics.Snapshot lastSnapshot = SplayTreeMetrics.snapshot(sut);

    public void assertAgreement() {
        List<Integer> expected = new ArrayList<>(model);
        List<Integer> actual = sut.inOrder();

        if (!actual.equals(expected)) {
            throw new AssertionError(
                "Content mismatch.\nSUT   =" + actual + "\nMODEL =" + expected
            );
        }

        SplayTreeInvariants.assertAll(sut);
        stepCount++;
        lastSnapshot = SplayTreeMetrics.snapshot(sut);
    }

    public int stepCount() {
        return stepCount;
    }

    public SplayTreeMetrics.Snapshot lastSnapshot() {
        return lastSnapshot;
    }
}