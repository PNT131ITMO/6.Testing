package lab1.part3.models;

import java.util.ArrayList;
import java.util.List;

import lab1.part3.events.*;

public class Scene implements Validatable {
    private final int chapter;
    private final Engine engine;
    private final SpaceEnvironment space;
    private final Airlock airlock;
    private final List<Character> characters;
    private final List<DomainEvent> events = new ArrayList<>();

    public Scene(int chapter, Engine engine, SpaceEnvironment space, Airlock airlock, List<Character> characters) {
        this.chapter = chapter;
        this.engine = engine;
        this.space = space;
        this.airlock = airlock;
        this.characters = new ArrayList<>(characters);
        validate();
    }

    public int getChapter() { return chapter; }
    public Engine getEngine() { return engine; }
    public SpaceEnvironment getSpace() { return space; }
    public Airlock getAirlock() { return airlock; }
    public List<Character> getCharacters() { return List.copyOf(characters); }
    public List<DomainEvent> getEvents() { return List.copyOf(events); }

    public void addEvent(DomainEvent e) {
        if (e == null) throw new IllegalArgumentException("event must not be null");
        events.add(e);
    }

    public Character findCharacter(String name) {
        for (Character c : characters) {
            if (c.getName().equalsIgnoreCase(name)) return c;
        }
        return null;
    }

    @Override
    public void validate() {
        if (chapter <= 0) throw new IllegalArgumentException("Scene.chapter must be > 0");
        if (engine == null || space == null || airlock == null) {
            throw new IllegalArgumentException("Scene must have engine, space, airlock");
        }
        engine.validate();
        space.validate();
        airlock.validate();

        if (characters == null || characters.isEmpty()) {
            throw new IllegalArgumentException("Scene.characters must be non-empty");
        }
        for (Character c : characters) c.validate();
    }
}
