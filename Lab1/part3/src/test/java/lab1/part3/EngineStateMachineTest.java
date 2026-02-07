package lab1.part3;

import lab1.part3.enums.SoundStage;
import lab1.part3.models.Engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineStateMachineTest {

    @Test
    @DisplayName("Engine advances: BUZZ -> WHISTLE -> ROAR")
    void advancesInOrder() {
        Engine e = new Engine(SoundStage.BUZZ);

        assertEquals(SoundStage.BUZZ, e.getStage());

        e.advance();
        assertEquals(SoundStage.WHISTLE, e.getStage());

        e.advance();
        assertEquals(SoundStage.ROAR, e.getStage());
    }

    @Test
    @DisplayName("Engine at ROAR is idempotent (ROAR -> ROAR)")
    void roarIsIdempotent() {
        Engine e = new Engine(SoundStage.ROAR);
        assertEquals(SoundStage.ROAR, e.getStage());

        e.advance();
        assertEquals(SoundStage.ROAR, e.getStage());

        e.advance();
        assertEquals(SoundStage.ROAR, e.getStage());
    }
}
