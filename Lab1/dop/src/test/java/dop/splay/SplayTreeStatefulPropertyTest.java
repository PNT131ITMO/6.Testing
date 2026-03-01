package dop.splay;

import dop.splay.actions.*;
import net.jqwik.api.*;
import net.jqwik.api.stateful.*;

class SplayTreeStatefulPropertyTest {

    @Provide
    Arbitrary<ActionSequence<World>> splaySequences() {
        Arbitrary<Integer> keys = Arbitraries.integers().between(-50, 50);

        Arbitrary<Action<World>> actions =
        Arbitraries.oneOf(
            keys.map(InsertAction::new),
            keys.map(DeleteAction::new),
            keys.map(SearchAction::new),
            keys.map(RotateLeftAction::new),
            keys.map(RotateRightAction::new)
        );

        Arbitrary<Integer> sizes = Arbitraries.integers().between(1000, 1500);

        return sizes.flatMap(n -> Arbitraries.sequences(actions).ofSize(n));
    }

    @Property(tries = 50, shrinking = ShrinkingMode.FULL)
    void stateful_random_sequences_preserve_invariants(
        @ForAll("splaySequences") ActionSequence<World> sequence
    ) {
        World w = new World();
        sequence.run(w);
    }
}