package dop.splay.actions;

import dop.splay.World;

public final class RotateRightAction extends SplayActions {
    private final int x;
    
    public RotateRightAction(int x) {
        this.x = x;
    }

    @Override
    public boolean precondition(World w) {
        return w.model.contains(x) && w.sut.canRotateRight(x);
    }

    @Override
    public World run(World w) {
        w.sut.rotateRight(x);
        w.assertAgreement();
        return w;
    }

    @Override
    public String toString() {
        return "rotateRight(" + x +")";
    }
}
