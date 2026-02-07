package lab1.part3.events;

public abstract class AbstractEvent implements DomainEvent {
    private final long ts;

    protected AbstractEvent(long ts) {
        this.ts = ts;
    }

    @Override
    public long timestamp() {
        return ts;
    }
}
