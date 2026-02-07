package lab1.part3.events;

import lab1.part3.enums.SoundStage;

public class SoundTransitionEvent extends AbstractEvent {
    private final SoundStage from;
    private final SoundStage to;

    public SoundTransitionEvent(long ts, SoundStage from, SoundStage to) {
        super(ts);
        this.from = from;
        this.to = to;
    }

    public SoundStage getFrom() { return from; }
    public SoundStage getTo() { return to; }

    @Override
    public String toString() {
        return "SoundTransitionEvent{" + from + "->" + to + ", ts=" + timestamp() + "}";
    }
}
