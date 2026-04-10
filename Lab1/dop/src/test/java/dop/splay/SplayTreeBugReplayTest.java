package dop.splay;

import dop.splay.actions.InsertAction;
import dop.splay.actions.SearchAction;
import org.junit.jupiter.api.Test;

class SplayTreeBugReplayTest {

    @Test
    void replayKnownFailingSequence() {
        World world = new World();

        new SearchAction(9).run(world);
        new InsertAction(-46).run(world);
        new SearchAction(3).run(world);
        new SearchAction(-6).run(world);
        new SearchAction(4).run(world);
        new SearchAction(-12).run(world);
        new InsertAction(-22).run(world);
    }
}