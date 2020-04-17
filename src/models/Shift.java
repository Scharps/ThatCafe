package models;

/**
 * This represents a simple shift for a day. Contains a start and finish time.
 * @author Sam James
 */
public class Shift {
    private final String startTime;
    private final String finishTime;

    public Shift() {
        startTime = "None";
        finishTime = "None";
    }

    public Shift(String startTime, String finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }
}
