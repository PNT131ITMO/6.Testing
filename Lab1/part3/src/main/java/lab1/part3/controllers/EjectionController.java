package lab1.part3.controllers;

import java.util.List;

import lab1.part3.models.Scene;
import lab1.part3.commands.OpenAirlockCommand;
import lab1.part3.commands.EjectCharactersCommand;
import lab1.part3.events.EjectionEvent;

public class EjectionController implements Controller {
    private final Scene scene;

    public EjectionController(Scene scene) {
        this.scene = scene;
    }

    public void openAirlock() {
        new OpenAirlockCommand(scene).execute();
    }

    public EjectionEvent eject(List<String> names) {
        return new EjectCharactersCommand(scene, names).execute();
    }
}
