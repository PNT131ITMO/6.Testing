package lab1.part3.controllers;

import java.util.List;

import lab1.part3.models.Scene;
import lab1.part3.events.DomainEvent;

public class SceneController implements Controller {
    private final Scene scene;

    public SceneController(Scene scene) {
        this.scene = scene;
    }

    public void validateScene() {
        scene.validate();
    }

    public List<DomainEvent> getEvents() {
        return scene.getEvents();
    }
}
