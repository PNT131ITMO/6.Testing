package lab1.part3;

import lab1.part3.commands.AdvanceSoundCommand;
import lab1.part3.commands.EjectCharactersCommand;
import lab1.part3.commands.OpenAirlockCommand;
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

class CommandControllerTest {

    private static Scene baseScene() {
        Engine engine = new Engine(SoundStage.BUZZ);
        SpaceEnvironment space = new SpaceEnvironment("BLACK",
                List.of(new Star(999.0), new Star(888.0), new Star(777.0)));
        Airlock airlock = new Airlock(AirlockState.CLOSED);

        List<Character> chars = List.of(
                new Character("Ford", Location.INSIDE_SHIP),
                new Character("Arthur", Location.INSIDE_SHIP)
        );

        return new Scene(8, engine, space, airlock, chars);
    }

    @Test
    @DisplayName("OpenAirlockCommand opens the airlock")
    void openAirlockCommandOpens() {
        Scene scene = baseScene();
        assertEquals(AirlockState.CLOSED, scene.getAirlock().getState());

        new OpenAirlockCommand(scene).execute();

        assertEquals(AirlockState.OPENED, scene.getAirlock().getState());
    }

    @Test
    @DisplayName("EjectCharactersCommand fails if airlock is CLOSED")
    void ejectFailsWhenClosed() {
        Scene scene = baseScene();
        assertEquals(AirlockState.CLOSED, scene.getAirlock().getState());

        assertThrows(IllegalStateException.class,
                () -> new EjectCharactersCommand(scene, List.of("Ford")).execute());
    }

    @Test
    @DisplayName("EjectCharactersCommand fails on unknown character")
    void ejectFailsUnknownCharacter() {
        Scene scene = baseScene();
        new OpenAirlockCommand(scene).execute();
        assertEquals(AirlockState.OPENED, scene.getAirlock().getState());

        assertThrows(IllegalArgumentException.class,
                () -> new EjectCharactersCommand(scene, List.of("Zaphod")).execute());
    }

    @Test
    @DisplayName("EjectCharactersCommand moves participants to OPEN_SPACE and adds EjectionEvent")
    void ejectMovesAndAddsEvent() {
        Scene scene = baseScene();
        new OpenAirlockCommand(scene).execute();

        EjectionEvent ev = new EjectCharactersCommand(scene, List.of("Ford", "Arthur")).execute();

        assertNotNull(ev);
        assertEquals(Location.OPEN_SPACE, scene.findCharacter("Ford").getLocation());
        assertEquals(Location.OPEN_SPACE, scene.findCharacter("Arthur").getLocation());

        // event added to scene
        List<DomainEvent> events = scene.getEvents();
        assertFalse(events.isEmpty());
        assertTrue(events.get(events.size() - 1) instanceof EjectionEvent);
    }

    @Test
    @DisplayName("AdvanceSoundCommand updates engine stage and adds SoundTransitionEvent")
    void advanceSoundAddsEvent() {
        Scene scene = baseScene();
        assertEquals(SoundStage.BUZZ, scene.getEngine().getStage());

        SoundTransitionEvent ev1 = new AdvanceSoundCommand(scene).execute();
        assertNotNull(ev1);
        assertEquals(SoundStage.WHISTLE, scene.getEngine().getStage());

        SoundTransitionEvent ev2 = new AdvanceSoundCommand(scene).execute();
        assertNotNull(ev2);
        assertEquals(SoundStage.ROAR, scene.getEngine().getStage());

        // events added
        List<DomainEvent> events = scene.getEvents();
        long soundCount = events.stream().filter(e -> e instanceof SoundTransitionEvent).count();
        assertEquals(2, soundCount);
    }

    @Test
    @DisplayName("Controllers call commands correctly")
    void controllersWork() {
        Scene scene = baseScene();

        EngineController engineController = new EngineController(scene);
        EjectionController ejectionController = new EjectionController(scene);

        engineController.increaseSound(); // BUZZ->WHISTLE
        engineController.increaseSound(); // WHISTLE->ROAR
        assertEquals(SoundStage.ROAR, scene.getEngine().getStage());

        ejectionController.openAirlock();
        assertEquals(AirlockState.OPENED, scene.getAirlock().getState());

        ejectionController.eject(List.of("Ford"));
        assertEquals(Location.OPEN_SPACE, scene.findCharacter("Ford").getLocation());
        assertEquals(Location.INSIDE_SHIP, scene.findCharacter("Arthur").getLocation());
    }
}
