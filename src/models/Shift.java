package models;

public class Shift {
    private int startTime;
    private int finishTime;

    public Shift() {
        startTime = -1;
        finishTime = -1;
    }

    public Shift(int startTime, int finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }
}
