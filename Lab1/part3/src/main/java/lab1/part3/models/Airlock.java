package lab1.part3.models;

import lab1.part3.enums.AirlockState;

public class Airlock implements Validatable {
    private AirlockState state;

    public Airlock(AirlockState initial) {
        this.state = initial;
        validate();
    }

    public AirlockState getState() { return state; }

    public void open() { state = AirlockState.OPENED; }
    public void close() { state = AirlockState.CLOSED; }

    @Override
    public void validate() {
        if (state == null) throw new IllegalArgumentException("Airlock.state must not be null");
    }
}
