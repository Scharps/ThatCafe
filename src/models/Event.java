package models;

import java.sql.Date;
import java.util.ArrayList;

public class Event {
    private int id;
    private Date date;
    private ArrayList<Integer> tablesBooked;
    private int maxNumberOfAttendees;
    private String description;
    private int customerId;

    private Event(int id, Date date, ArrayList<Integer> tablesBooked, int maxNumberOfAttendees, String description, int customerId) {
        this.id = id;
        this.date = date;
        this.tablesBooked = tablesBooked;
        this.maxNumberOfAttendees = maxNumberOfAttendees;
        this.description = description;
        this.customerId = customerId;
    }

    public int getId() {
        return this.id;
    }

    public Date getDate() {
        return this.date;
    }

    public ArrayList<Integer> getTablesBooked() {
        return this.tablesBooked;
    }

    public int getMaxNumberOfAttendees() {
        return maxNumberOfAttendees;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public static Event createEvent(Date date, ArrayList<Integer> tablesBooked, int maxNumberOfAttendees, String description, int customerId) {
        throw new UnsupportedOperationException("createEvent() is not yet implemented");
    }

    public static Event getEvent(int id) {
        throw new UnsupportedOperationException("getEvent() is not yet implemented");
    }

    public static boolean deleteEvent(int id) {
        throw new UnsupportedOperationException("deleteEvent() is not yet implemented");
    }

    public static boolean updateEvent(int id, Date date, ArrayList<Integer> tablesBooked, int maxNumberOfAttendees, String description, int customerId) {
        throw new UnsupportedOperationException("updateEvent() is not yet implemented");
    }
}