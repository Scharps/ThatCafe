package models;

import java.sql.*;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;

public class Booking {
    private int id;
    private int tableId;
    private Date dateOfBooking;
    private int hourOfBooking;
    private int customerId;
    private int numberOfGuests;
    private boolean isApproved;
    public static final int OPENING_TIME = 17;
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

    public int getId() {
        return this.id;
    }

    public int getTableId() {
        return this.tableId;
    }

    public Date getDateOfBooking() {
        return this.dateOfBooking;
    }

    public int getHourOfBooking() {
        return this.hourOfBooking;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public static Booking createBooking(Connection conn, int tableId, int hourOfBooking, Date dateOfBooking, int customerId, int numberOfGuests) throws SQLException {
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
        return bookingFromResultSet(rs);
    }

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

    public static ResultSet getUncomfirmedBooking(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Bookings WHERE Approved = 0");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    public static void confirmBooking(Connection conn, int id){

        try{
            PreparedStatement st = conn.prepareStatement("UPDATE Bookings SET Approved = 1 WHERE BookingId = ?");
            st.setInt(1, id);
            st.executeUpdate();
        }catch (SQLException se){

        }
    }

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

    public static void deleteBooking(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM Bookings WHERE BookingId = ?");
        st.setInt(1, id);
        st.executeUpdate();
    }

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