package dop.splay.actions;

import dop.splay.World;

public final class SearchAction extends SplayActions {
    private final int x;

    public SearchAction(int x) {
        this.x = x;
    }

    @Override
    public World run(World w) {
        boolean got = w.sut.contains(x);
        boolean exp = w.model.contains(x);

        if (got != exp) {
            throw new AssertionError("contains(" + x + ") mismatch: sut=" + got + " model=" + exp);
        }

        w.assertAgreement();
        return w;
    }

    @Override
    public String toString() {
        return "search(" + x +")";
    }
}
