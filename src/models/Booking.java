package models;

import javax.swing.*;
import java.sql.*;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This represents a booking that can be made by a customer.
 * @author Sam James, Ashley Forster
 */

public class Booking {
    private final int id;
    private final int tableId;
    private final Date dateOfBooking;
    private final int hourOfBooking;
    private final int customerId;
    private final int numberOfGuests;
    private final boolean isApproved;
    public static final int OPENING_TIME = 6;
    public static final int CLOSING_TIME = 23;

    private Booking(int id, int tableId, int hourOfBooking, Date dateOfBooking, int customerId, int numberOfGuests, boolean approved) {
        this.id = id;
        this.tableId = tableId;
        this.dateOfBooking = dateOfBooking;
        this.hourOfBooking = hourOfBooking;
        this.customerId = customerId;
        this.numberOfGuests = numberOfGuests;
        this.isApproved = approved;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", dateOfBooking=" + dateOfBooking +
                ", hourOfBooking=" + hourOfBooking +
                ", customerId=" + customerId +
                ", numberOfGuests=" + numberOfGuests +
                ", isApproved=" + isApproved +
                '}';
    }

    /**
     * Gets the Booking ID
     * @return Booking ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the TableID
     * @return TableID
     */
    public int getTableId() {
        return this.tableId;
    }

    /**
     * Gets the date of the booking
     * @return Date of the booking
     */
    public Date getDateOfBooking() {
        return this.dateOfBooking;
    }

    /**
     * Gets the hour for which the booking is for
     * @return Booking hour
     */
    public int getHourOfBooking() {
        return this.hourOfBooking;
    }

    /**
     * Gets the approval status of the booking.
     * @return approval status
     */
    public boolean isApproved() {
        return isApproved;
    }

    /**
     * Creates a booking in the database.
     * @param conn Database Connection
     * @param tableId The booked table ID
     * @param hourOfBooking The hour for which the table is booked
     * @param dateOfBooking The date for which the table is booked     *
     * @param customerId The customer ID that made the booking
     * @param numberOfGuests The number of guests
     * @throws SQLException
     */
    public static void createBooking(Connection conn, int tableId, int hourOfBooking, Date dateOfBooking, int customerId, int numberOfGuests) throws SQLException {
        if(hourOfBooking < OPENING_TIME || hourOfBooking >= CLOSING_TIME) {
            throw new IllegalArgumentException(String.format("Booking must be between %d and %d", OPENING_TIME, CLOSING_TIME));
        }
        PreparedStatement st = conn.prepareStatement("INSERT INTO Bookings(TableId, BookingDate, BookingHour, CustomerId, GuestQuantity) VALUES (?, ?, ?, ?, ?)");
        st.setInt(1, tableId);
        st.setDate(2, dateOfBooking);
        st.setInt(3, hourOfBooking);
        st.setInt(4, customerId);
        st.setInt(5, numberOfGuests);
        st.executeUpdate();

        st = conn.prepareStatement("SELECT * FROM Bookings\n" +
                "WHERE BookingId = (SELECT MAX(BookingId) FROM Bookings)");
        ResultSet rs = st.executeQuery();
        rs.next();
        bookingFromResultSet(rs);
    }

    /**
     * Gets the customer ID based on the time, hour and table
     * @param conn Database Connection
     * @param time The date of the booking
     * @param hour The hour of the booking
     * @param tableId The booked table ID
     * @return
     * @throws SQLException
     */
    public static int getCustomerId(Connection conn, Date time, int hour, int tableId) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT CustomerId FROM Bookings\n" +
                "WHERE BookingDate = ? AND TableId = ? AND BookingHour = ?");
        st.setDate(1, time);
        st.setInt(2, tableId);
        st.setInt(3, hour);
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            return rs.getInt(1);
        }
        else {
            return 0;
        }
    }

    /**
     * Gets unconfirmed bookings from the database as a ResultSet
     * @param conn Database Connection
     * @return Unconfirmed Bookings
     * @throws SQLException
     */
    public static ResultSet getUnconfirmedBookings(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Bookings WHERE Approved = 0");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    /**
     * Updates a booking and marks it as confirmed in the database.
     * @param conn Database connection
     * @param id The booking ID
     */
    public static void confirmBooking(Connection conn, int id){

        try{
            PreparedStatement st = conn.prepareStatement("UPDATE Bookings SET Approved = 1 WHERE BookingId = ?");
            st.setInt(1, id);
            st.executeUpdate();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gets the bookings that occur on the current day.
     * @param conn Database Connection
     * @return Bookings that occur today.
     */
    public static ResultSet getTodaysBookings(Connection conn){
        try{
            PreparedStatement st = conn.prepareStatement("SELECT * FROM Bookings WHERE BookingDate = ? AND Approved = 1");
            st.setDate(1, new Date(System.currentTimeMillis()));
            ResultSet rs = st.executeQuery();
            return rs;
        }catch (SQLException se){
            return null;
        }
    }

    /**
     * Gets a booking by ID
     * @param conn Database Connection
     * @param id The ID of the booking
     * @return A Booking object from the database
     * @throws SQLException
     */
    public static Booking getBooking(Connection conn, int id) throws SQLException {
        PreparedStatement st =  conn.prepareStatement("SELECT * FROM Bookings WHERE BookingId = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return bookingFromResultSet(rs);
        } else {
            return null;
        }
    }

    /**
     * Deletes a booking from the database
     * @param conn Database Connection
     * @param id ID of the booking to delete.
     * @throws SQLException
     */
    public static void deleteBooking(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM Bookings WHERE BookingId = ?");
        st.setInt(1, id);
        st.executeUpdate();
    }

    /**
     * Gets all bookings for a customer
     * @param conn Database Connection
     * @param customerId Customer's ID
     * @return ArrayList of customer's Bookings
     * @throws SQLException
     */
    public static ArrayList<Booking> getBookingsForCustomer(Connection conn, int customerId) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Bookings WHERE CustomerId = ?");
        st.setInt(1, customerId);
        ResultSet rs = st.executeQuery();
        ArrayList<Booking> bookings = new ArrayList<>();
        while(rs.next()) {
            bookings.add(bookingFromResultSet(rs));
        }
        return bookings;
    }

    /**
     * Gets the total amount of bookings per date from last week to next week.
     * @param conn Database connection
     * @return  KeyValue pairs where String is the date and Integer is the number of bookings on that date.
     * @throws SQLException
     */
    public static HashMap<String, Integer> getBusyPeriods(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT BookingDate, Count(*) Count\n" +
                "FROM Bookings\n" +
                "WHERE BookingDate >= ? AND BookingDate <= ?\n" +
                "GROUP BY BookingDate\n" +
                "ORDER BY BookingDate");
        st.setDate(1, new Date(Date.from(Instant.now().minus(Period.ofWeeks(1))).getTime()));
        st.setDate(2, new Date(Date.from(Instant.now().plus(Period.ofWeeks(1))).getTime()));
        ResultSet rs = st.executeQuery();
        HashMap<String, Integer> periods = new HashMap<>();
        while(rs.next()) {
            periods.put(
                    rs.getDate("BookingDate").toString(),
                    rs.getInt("Count")
            );
        }
        return periods;
    }

    private static Booking bookingFromResultSet(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("BookingId"),
                rs.getInt("TableId"),
                rs.getInt("BookingHour"),
                rs.getDate("BookingDate"),
                rs.getInt("CustomerId"),
                rs.getInt("GuestQuantity"),
                rs.getBoolean("Approved")
        );
    }
}