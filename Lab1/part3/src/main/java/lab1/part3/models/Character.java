package lab1.part3.models;

import lab1.part3.enums.Location;

public class Character implements Validatable {
    private final String name;
    private Location location;

    public Character(String name, Location initialLocation) {
        this.name = name;
        this.location = initialLocation;
        validate();
    }

    public String getName() { return name; }
    public Location getLocation() { return location; }

    public void moveTo(Location loc) {
        this.location = loc;
        validate();
    }

    @Override
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Character.name must be non-empty");
        }
        if (location == null) {
            throw new IllegalArgumentException("Character.location must not be null");
        }
    }
}
