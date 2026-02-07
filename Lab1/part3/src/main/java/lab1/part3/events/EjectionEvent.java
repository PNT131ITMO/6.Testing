package lab1.part3.events;

import java.util.List;

import lab1.part3.enums.Location;

public class EjectionEvent extends AbstractEvent {
    private final List<String> participants;
    private final Location from;
    private final Location to;
    private final String comparisonTag;

    public EjectionEvent(long ts, List<String> participants, Location from, Location to, String comparisonTag) {
        super(ts);
        this.participants = List.copyOf(participants);
        this.from = from;
        this.to = to;
        this.comparisonTag = comparisonTag;
    }

    public List<String> getParticipants() { return participants; }
    public Location getFrom() { return from; }
    public Location getTo() { return to; }
    public String getComparisonTag() { return comparisonTag; }

    @Override
    public String toString() {
        return "EjectionEvent{" + participants + " " + from + "->" + to +
                ", tag='" + comparisonTag + "', ts=" + timestamp() + "}";
    }
}
