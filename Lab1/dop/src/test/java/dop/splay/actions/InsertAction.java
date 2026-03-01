package dop.splay.actions;

import dop.splay.World;

public final class InsertAction extends SplayActions {
    private final int x;

    public InsertAction(int x) {
        this.x = x;
    }

    @Override
    public boolean precondition(World w) {
        return !w.model.contains(x);
    }

    @Override
    public World run(World w) {
        w.sut.insert(x);
        w.model.add(x);
        w.assertAgreement();
        return w;
    }
    @Override
    public String toString() {
        return "insert (" + x + ")";
    }
}
