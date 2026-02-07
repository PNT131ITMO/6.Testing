package lab1.part3;

import lab1.part3.enums.AirlockState;
import lab1.part3.enums.Location;
import lab1.part3.enums.SoundStage;
import lab1.part3.models.Character;
import lab1.part3.models.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelValidationTest {

    @Test
    @DisplayName("Star: brightness must be finite and > 0")
    void starBrightnessValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Star(0.0));
        assertThrows(IllegalArgumentException.class, () -> new Star(-1.0));
        assertThrows(IllegalArgumentException.class, () -> new Star(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new Star(Double.POSITIVE_INFINITY));

        assertDoesNotThrow(() -> new Star(10.0));
    }

    @Test
    @DisplayName("SpaceEnvironment: voidColor must be BLACK and stars non-empty")
    void spaceEnvironmentValidation() {
        List<Star> stars = List.of(new Star(100.0), new Star(200.0));

        assertThrows(IllegalArgumentException.class,
                () -> new SpaceEnvironment("WHITE", stars));

        assertThrows(IllegalArgumentException.class,
                () -> new SpaceEnvironment("BLACK", List.of()));

        assertDoesNotThrow(() -> new SpaceEnvironment("BLACK", stars));
    }

    @Test
    @DisplayName("Character: name non-empty, location non-null")
    void characterValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("   ", Location.INSIDE_SHIP));

        assertThrows(IllegalArgumentException.class,
                () -> new Character("Ford", null));

        assertDoesNotThrow(() -> new Character("Ford", Location.INSIDE_SHIP));
    }

    @Test
    @DisplayName("Scene: must contain engine/space/airlock and non-empty characters list")
    void sceneValidation() {
        Engine engine = new Engine(SoundStage.BUZZ);
        SpaceEnvironment space = new SpaceEnvironment("BLACK", List.of(new Star(100.0)));
        Airlock airlock = new Airlock(AirlockState.CLOSED);

        assertThrows(IllegalArgumentException.class,
                () -> new Scene(8, engine, space, airlock, List.of()));

        assertDoesNotThrow(() -> new Scene(
                8, engine, space, airlock,
                List.of(new Character("Ford", Location.INSIDE_SHIP))
        ));
    }
}
