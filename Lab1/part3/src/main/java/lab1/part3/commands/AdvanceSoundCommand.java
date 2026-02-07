package lab1.part3.commands;

import lab1.part3.models.Scene;
import lab1.part3.events.SoundTransitionEvent;
import lab1.part3.enums.SoundStage;

public class AdvanceSoundCommand implements Command<SoundTransitionEvent> {
    private final Scene scene;

    public AdvanceSoundCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public SoundTransitionEvent execute() {
        scene.validate();

        SoundStage from = scene.getEngine().getStage();
        SoundStage to = scene.getEngine().advance();

        SoundTransitionEvent ev = new SoundTransitionEvent(System.currentTimeMillis(), from, to);
        scene.addEvent(ev);
        return ev;
    }
}
