package dop.splay.actions;

import dop.splay.World;

public final class DeleteAction extends SplayActions {
    private final int x;

    public DeleteAction(int x) {
        this.x = x;
    }

    @Override
    public boolean precondition(World w) {
        return w.model.contains(x);
    }

    @Override
    public World run(World w) {
        w.sut.delete(x);
        w.model.remove(x);
        w.assertAgreement();
        return w;
    }

    @Override
    public String toString() {
        return "delete(" + x + ")";
    }
}
