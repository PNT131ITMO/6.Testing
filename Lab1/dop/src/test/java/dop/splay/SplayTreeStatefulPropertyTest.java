package dop.splay;

import dop.splay.actions.DeleteAction;
import dop.splay.actions.InsertAction;
import dop.splay.actions.RotateLeftAction;
import dop.splay.actions.RotateRightAction;
import dop.splay.actions.SearchAction;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.ShrinkingMode;
import net.jqwik.api.stateful.Action;
import net.jqwik.api.stateful.ActionSequence;

class SplayTreeStatefulPropertyTest {

    @Provide
    Arbitrary<ActionSequence<World>> splaySequences() {
        Arbitrary<Integer> keys = Arbitraries.integers().between(-50, 50);

        Arbitrary<Action<World>> actions = Arbitraries.oneOf(
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
    void statefulRandomSequencesPreserveInvariants(
        @ForAll("splaySequences") ActionSequence<World> sequence
    ) {
        World world = new World();
        try {
            sequence.run(world);
        } catch (AssertionError | RuntimeException error) {
            System.err.println("\nFailing sequence reported by jqwik:");
            System.err.println(sequence);
            FailureArtifacts.writeFailureReport(sequence, world, error);
            throw error;
        }
    }
}