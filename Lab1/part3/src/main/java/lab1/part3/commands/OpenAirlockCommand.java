package lab1.part3.commands;

import lab1.part3.models.Scene;

public class OpenAirlockCommand implements Command<Void> {
    private final Scene scene;

    public OpenAirlockCommand(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Void execute() {
        scene.validate();
        scene.getAirlock().open();
        return null;
    }
}
