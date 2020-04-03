package models;

import java.sql.Date;

public class Booking {
    private int id;
    private int tableId;
    private Date dateOfBooking;
    private int customerId;
    private int numberOfGuests;

    private Booking(int id, int tableId, Date dateOfBooking, int customerId, int numberOfGuests) {
        this.id = id;
        this.tableId = tableId;
        this.dateOfBooking = dateOfBooking;
        this.customerId = customerId;
        this.numberOfGuests = numberOfGuests;
    }

    public int getId() {
        return this.id;
    }

    public int getTableId() {
        return this.tableId;
    }

    public Date getDateOfBooking() {
        return this.dateOfBooking;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public static Booking createBooking(int tableId, Date dateOfBooking, int customerId, int numberOfGuests) {
        throw new UnsupportedOperationException("createBooking() is not yet implemented");
    }

    public static Booking getBooking(int id) {
        throw new UnsupportedOperationException("getBooking() is not yet implemented");
    }

    public static boolean deleteBooking(int id) {
        throw new UnsupportedOperationException("deleteBooking() is not yet implemented");
    }

    public static boolean updateBooking(int id, int tableId, Date dateOfBooking, int customerId, int numberOfGuests) {
        throw new UnsupportedOperationException("updateBooking() is not yet implemented");
    }
}