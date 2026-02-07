package lab1.part3.controllers;

import lab1.part3.models.Scene;
import lab1.part3.events.SoundTransitionEvent;
import lab1.part3.commands.AdvanceSoundCommand;

public class EngineController implements Controller {
    private final Scene scene;

    public EngineController(Scene scene) {
        this.scene = scene;
    }

    public SoundTransitionEvent increaseSound() {
        return new AdvanceSoundCommand(scene).execute();
    }
}
