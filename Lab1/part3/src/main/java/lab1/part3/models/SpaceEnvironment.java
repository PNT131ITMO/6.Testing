package lab1.part3.models;

import java.util.List;
import lab1.part3.models.Validatable;
import lab1.part3.models.Star;

public class SpaceEnvironment implements Validatable {
    private final String voidColor;
    private final List<Star> stars;

    public SpaceEnvironment(String voidColor, List<Star> stars) {
        this.voidColor = voidColor;
        this.stars = List.copyOf(stars);
        validate();
    }

    public String getVoidColor() {
        return voidColor;
    }

    public List<Star> getStars() {
        return stars;
    }

    @Override
    public void validate() {
        if (voidColor == null || voidColor.isBlank()) {
            throw new IllegalArgumentException("SpaceEnvironment.voidColor must be non-empty");
        }
        if (!"BLACK".equalsIgnoreCase(voidColor)) {
            throw new IllegalArgumentException("SpaceEnvironment.voidColor must be BLACK (per text)");
        }
        if (stars == null || stars.isEmpty()) {
            throw new IllegalArgumentException("SpaceEnvironment.stars must be non-empty (per text)");
        }
        for (Star s : stars) s.validate();
    }
}