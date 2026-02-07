package lab1.part3;

import lab1.part3.controllers.EngineController;
import lab1.part3.controllers.EjectionController;
import lab1.part3.enums.AirlockState;
import lab1.part3.enums.Location;
import lab1.part3.enums.SoundStage;
import lab1.part3.events.DomainEvent;
import lab1.part3.events.EjectionEvent;
import lab1.part3.events.SoundTransitionEvent;
import lab1.part3.models.Character;
import lab1.part3.models.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SceneScenarioTest {

    private static Scene sceneFromText() {
        Engine engine = new Engine(SoundStage.BUZZ);
        SpaceEnvironment space = new SpaceEnvironment(
                "BLACK",
                List.of(new Star(1000.0), new Star(1200.0), new Star(900.0))
        );
        Airlock airlock = new Airlock(AirlockState.CLOSED);

        List<Character> chars = List.of(
                new Character("Ford", Location.INSIDE_SHIP),
                new Character("Arthur", Location.INSIDE_SHIP)
        );

        return new Scene(8, engine, space, airlock, chars);
    }

    @Test
    @DisplayName("Scenario: engine sound grows, airlock opens, Ford & Arthur ejected to open space")
    void fullScenarioMatchesText() {
        Scene scene = sceneFromText();

        assertEquals("BLACK", scene.getSpace().getVoidColor().toUpperCase());
        assertFalse(scene.getSpace().getStars().isEmpty());
        assertEquals(SoundStage.BUZZ, scene.getEngine().getStage());
        assertEquals(AirlockState.CLOSED, scene.getAirlock().getState());
        assertEquals(Location.INSIDE_SHIP, scene.findCharacter("Ford").getLocation());
        assertEquals(Location.INSIDE_SHIP, scene.findCharacter("Arthur").getLocation());

        EngineController engineController = new EngineController(scene);
        EjectionController ejectionController = new EjectionController(scene);

        SoundTransitionEvent e1 = engineController.increaseSound(); 
        SoundTransitionEvent e2 = engineController.increaseSound(); 
        assertNotNull(e1);
        assertNotNull(e2);
        assertEquals(SoundStage.ROAR, scene.getEngine().getStage());

        ejectionController.openAirlock();
        assertEquals(AirlockState.OPENED, scene.getAirlock().getState());

        EjectionEvent ej = ejectionController.eject(List.of("Ford", "Arthur"));
        assertNotNull(ej);
        assertEquals(Location.OPEN_SPACE, scene.findCharacter("Ford").getLocation());
        assertEquals(Location.OPEN_SPACE, scene.findCharacter("Arthur").getLocation());

        List<DomainEvent> events = scene.getEvents();
        assertTrue(events.size() >= 3);

        DomainEvent last = events.get(events.size() - 1);
        DomainEvent prev = events.get(events.size() - 2);
        DomainEvent prev2 = events.get(events.size() - 3);

        assertTrue(prev2 instanceof SoundTransitionEvent);
        assertTrue(prev instanceof SoundTransitionEvent);
        assertTrue(last instanceof EjectionEvent);
    }
}
