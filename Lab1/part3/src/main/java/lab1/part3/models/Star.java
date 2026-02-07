package lab1.part3.models;

import lab1.part3.models.Validatable;

public class Star implements Validatable {
    private final double brightness;

    public Star(double brightness) {
        this.brightness = brightness;
        validate();
    }

    public double getBrightness() {
        return brightness;
    }

    @Override
    public void validate() {
        if (!Double.isFinite(brightness) || brightness <= 0.0) {
            throw new IllegalArgumentException("Star.brightness must be finite and greater than 0.0");
        }
    }
}