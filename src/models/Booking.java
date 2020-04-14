package models;

import java.sql.*;
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

    public int getCustomerId() {
        return this.customerId;
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
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
        return new Booking(
                rs.getInt("BookingId"),
                rs.getInt("TableId"),
                rs.getInt("BookingHour"),
                rs.getDate("BookingDate"),
                rs.getInt("CustomerId"),
                rs.getInt("GuestQuantity"),
                false
        );
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


    public static Booking getBooking(Connection conn, int id) throws SQLException {
        PreparedStatement st =  conn.prepareStatement("SELECT * FROM Bookings WHERE BookingId = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return new Booking(
                    rs.getInt("BookingId"),
                    rs.getInt("TableId"),
                    rs.getInt("BookingHour"),
                    rs.getDate("BookingDate"),
                    rs.getInt("CustomerId"),
                    rs.getInt("GuestQuantity"),
                    rs.getBoolean("Approved")
            );
        } else {
            return null;
        }
    }

    public static void deleteBooking(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM Bookings WHERE BookingId = ?");
        st.setInt(1, id);
        st.executeUpdate();
    }

    public static void updateBooking(Connection conn, int id, int tableId, Timestamp dateOfBooking, int customerId, int numberOfGuests) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE Staff\n" +
                "SET TableId = ?, CustomerId = ?, BookingDate = ?, GuestQuantity = ?\n" +
                "WHERE BookingId = ?");
        st.setInt(1,tableId);
        st.setInt(2, customerId);
        st.setTimestamp(3, dateOfBooking);
        st.setInt(4, numberOfGuests);
        st.setInt(5, id);
        st.executeUpdate();
    }

    public static ArrayList<Booking> getBookingsForTable(Connection conn, int tableId) throws SQLException {
        ArrayList<Booking> bookings = new ArrayList<>();
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Bookings WHERE TableId = ?");
        st.setInt(1, tableId);
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            bookings.add(new Booking(
                    rs.getInt("BookingId"),
                    rs.getInt("TableId"),
                    rs.getInt("BookingHour"),
                    rs.getDate("BookingDate"),
                    rs.getInt("CustomerId"),
                    rs.getInt("GuestQuantity"),
                    rs.getBoolean("Approved")
            ));
        }
        return bookings;
    }

    public static ArrayList<Booking> getAllBookings(Connection conn) throws SQLException {
        ArrayList<Booking> bookings = new ArrayList<>();
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Bookings");
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            bookings.add(new Booking(
                    rs.getInt("BookingId"),
                    rs.getInt("TableId"),
                    rs.getInt("BookingHour"),
                    rs.getDate("BookingDate"),
                    rs.getInt("CustomerId"),
                    rs.getInt("GuestQuantity"),
                    rs.getBoolean("Approved")
            ));
        }
        return bookings;
    }

    public static ArrayList<Integer> availableTimesForTable(Connection conn, int tableId, Date day) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT BookingHour FROM Bookings\n" +
                "WHERE TableId = ? AND BookingDate >= ?");
        st.setInt(1, tableId);
        st.setDate(2, day);
        ArrayList<Integer> bookedTimes = new ArrayList<>();
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            bookedTimes.add(rs.getInt("BookingHour"));
        }
        ArrayList<Integer> availableTimes = new ArrayList<>();
        for(int i = OPENING_TIME; i < CLOSING_TIME; i++) {
            availableTimes.add(i);
        }
        for (int i: bookedTimes) {
            availableTimes.removeIf(t -> t == i);
        }

        return availableTimes;
    }

    public static ArrayList<Table> getAvailableTables(Connection conn, Date day, int startHour, int duration, int capacity) throws SQLException {
        String sqlStatement = String.format(
            "SELECT TableId\n" +
            "FROM Bookings b, CafeTables t\n" +
            "WHERE b.TableId = t.TableId AND\n" +
            "t.Capacity >= ? AND\n" +
            "b.BookingDate = ? AND\n" +
            "("
        );

        for(int i = startHour; i < startHour + duration; i++) {
            sqlStatement += "b.BookingHour = " + i;
            if(i < startHour + duration + 1) {
                sqlStatement += " || ";
            } else {
                sqlStatement += ")";
            }
        }

        System.out.println(sqlStatement);

        PreparedStatement st = conn.prepareStatement(sqlStatement);
        st.setInt(1,capacity);
        st.setDate(2, day);
        ResultSet rs = st.executeQuery();
        ArrayList<Table> unavailableTables = new ArrayList<>();
        while(rs.next()) {
            unavailableTables.add(Table.getTable(conn, rs.getInt("TableId")));
        }
        ArrayList<Table> availableTables = Table.getAllTables(conn);
        for(Table t : unavailableTables) {
            availableTables.removeIf(table -> table.getId() == t.getId());
        }
        return availableTables;
    }
}