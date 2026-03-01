package dop.splay.actions;

import dop.splay.World;
import net.jqwik.api.stateful.Action;

public abstract class SplayActions implements Action<World> {
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
