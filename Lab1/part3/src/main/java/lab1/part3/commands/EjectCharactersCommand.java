package lab1.part3.commands;

import lab1.part3.models.Scene;
import lab1.part3.models.Character;
import lab1.part3.events.EjectionEvent;
import lab1.part3.enums.AirlockState;
import lab1.part3.enums.Location;

import java.util.ArrayList;
import java.util.List;

public class EjectCharactersCommand implements Command<EjectionEvent> {
    private final Scene scene;
    private final List<String> names;

    public EjectCharactersCommand(Scene scene, List<String> names) {
        this.scene = scene;
        this.names = List.copyOf(names);
    }

    @Override
    public EjectionEvent execute() {
        scene.validate();

        if (scene.getAirlock().getState() != AirlockState.OPENED) {
            throw new IllegalStateException("Cannot eject: airlock must be OPEN");
        }
        if (names.isEmpty()) {
            throw new IllegalArgumentException("participants must be non-empty");
        }

        List<String> ejected = new ArrayList<>();
        for (String name : names) {
            Character c = scene.findCharacter(name);
            if (c == null) {
                throw new IllegalArgumentException("Unknown character: " + name);
            }
            c.moveTo(Location.OPEN_SPACE);
            ejected.add(c.getName());
        }

        EjectionEvent ev = new EjectionEvent(
                System.currentTimeMillis(),
                ejected,
                Location.INSIDE_SHIP,
                Location.OPEN_SPACE,
                "confetti"
        );
        scene.addEvent(ev);
        return ev;
    }
}
