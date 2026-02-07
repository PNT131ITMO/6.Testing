package lab1.part3;

import lab1.part3.controllers.EngineController;
import lab1.part3.controllers.EjectionController;
import lab1.part3.controllers.SceneController;
import lab1.part3.enums.AirlockState;
import lab1.part3.enums.Location;
import lab1.part3.enums.SoundStage;
import lab1.part3.events.DomainEvent;
import lab1.part3.models.Character;
import lab1.part3.models.*;

import java.util.List;

public class App {

    public static void main(String[] args) {
        Scene scene = buildSceneFromText();

        SceneController sceneController = new SceneController(scene);
        EngineController engineController = new EngineController(scene);
        EjectionController ejectionController = new EjectionController(scene);

        sceneController.validateScene();

        System.out.println("Initial state!");
        printState(scene);

        System.out.println("\n Increase sound!");
        engineController.increaseSound();
        engineController.increaseSound();
        printState(scene);

        System.out.println("\n Open Airclock!");
        ejectionController.openAirlock();
        printState(scene);

        System.out.println("\n Eject characters!");
        ejectionController.eject(List.of("Ford", "Arthur"));
        printState(scene);

        // Print event log
        System.out.println("\n Events");
        for (DomainEvent e : sceneController.getEvents()) {
            System.out.println(" - " + e);
        }
    }

    private static Scene buildSceneFromText() {
        Engine engine = new Engine(SoundStage.BUZZ);

        SpaceEnvironment space = new SpaceEnvironment(
                "BLACK",
                List.of(
                        new Star(1000.0),
                        new Star(1200.0),
                        new Star(900.0),
                        new Star(1500.0)
                )
        );

        Airlock airlock = new Airlock(AirlockState.CLOSED);

        List<Character> characters = List.of(
                new Character("Ford", Location.INSIDE_SHIP),
                new Character("Arthur", Location.INSIDE_SHIP)
        );

        return new Scene(8, engine, space, airlock, characters);
    }

    private static void printState(Scene scene) {
        System.out.println("Chapter: " + scene.getChapter());
        System.out.println("Engine stage: " + scene.getEngine().getStage());
        System.out.println("Airlock state: " + scene.getAirlock().getState());
        System.out.println("Space voidColor: " + scene.getSpace().getVoidColor()
                + ", stars=" + scene.getSpace().getStars().size());

        for (Character c : scene.getCharacters()) {
            System.out.println("Character: " + c.getName() + ", location=" + c.getLocation());
        }
        System.out.println("Events stored: " + scene.getEvents().size());
    }
}
