package dop.splay.actions;

import dop.splay.World;

public final class RotateLeftAction extends SplayActions {
    private final int x;
    
    public RotateLeftAction(int x) {
        this.x = x;
    }

    @Override
    public boolean precondition(World w) {
        return w.model.contains(x) && w.sut.canRotateLeft(x);
    }

    @Override
    public World run(World w) {
        w.sut.rotateLeft(x);
        w.assertAgreement();
        return w;
    }

    @Override
    public String toString() {
        return "rotateLeft(" + x +")";
    }
}
