package models;

public class Shift {
    private String startTime;
    private String finishTime;

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
