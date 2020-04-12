package models;

import services.DatabaseService;

import java.sql.*;

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

    public static int getCustomerId(Connection conn, Timestamp time, int tableId){
        try {
            PreparedStatement st = conn.prepareStatement("SELECT CustomerId FROM Bookings WHERE BookingDate = ? AND TableId = ?");
            st.setTimestamp(1, time);
            st.setInt(2, tableId);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
            else {
                return 0;
            }
        } catch (SQLException se){
            return 0;
        }
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