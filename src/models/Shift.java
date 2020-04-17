package models;

/**
 * This represents a simple shift for a day. Contains a start and finish time.
 * @author Sam James
 */
public class Shift {
    private final String startTime;
    private final String finishTime;

    /**
     * Constructor for Shift
     * @param startTime Start time of shift
     * @param finishTime Finish time of shift
     */
    public Shift(String startTime, String finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    /**
     * Gets the start time of the shift
     * @return Shift start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Gets the finish time of the shift
     * @return Shift finish time
     */
    public String getFinishTime() {
        return finishTime;
    }
}
