package models;

import java.sql.*;
import java.util.ArrayList;

/**
 * This represents a table structure
 * @author Sam James,
 */

public class Table {
    private final int id;
    private final int capacity;

    private Table(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    /**
     * Gets the ID of the Table
     * @return Table ID
     */
    public int getId() {
        return this.id;
    }


    @Override
    public String toString() {
        return "Table: " + id +", Capacity: " + capacity;
    }

    /**
     * Gets all Tables registered on the database.
     * @param conn
     * @return All Tables
     * @throws SQLException
     */
    private static ArrayList<Table> getAllTables(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT * FROM CafeTables");
        ResultSet rs = st.executeQuery();
        ArrayList<Table> tables = new ArrayList<>();
        while(rs.next()) {
            tables.add(new Table(
                    rs.getInt("TableId"),
                    rs.getInt("Capacity")
            ));
        }
        return tables;
    }

    /**
     * Gets available tables based on the day, time, duration and size of the booking.
     * @param conn Database connection
     * @param day Day for availability
     * @param startHour Booking start hour
     * @param duration Booking duration
     * @param capacity Booking capcity
     * @return Available tables for potential booking.
     * @throws SQLException
     */
    public static ArrayList<Table> getAvailableTables(Connection conn, Date day, int startHour, int duration, int capacity) throws SQLException {
        StringBuilder sqlStatement = new StringBuilder("SELECT *\n" +
                "FROM Bookings b, CafeTables t\n" +
                "WHERE b.TableId = t.TableId AND\n" +
                "b.BookingDate = ? AND\n" +
                "(");

        for(int i = startHour; i < startHour + duration; i++) {
            sqlStatement.append("b.BookingHour = ").append(i);
            if(i < startHour + duration - 1) {
                sqlStatement.append(" || ");
            } else {
                sqlStatement.append(")");
            }
        }

        PreparedStatement st = conn.prepareStatement(sqlStatement.toString());
        st.setDate(1, day);
        ResultSet rs = st.executeQuery();
        ArrayList<Table> unavailableTables = new ArrayList<>();

        while(rs.next()) {
            unavailableTables.add(
                new Table(
                    rs.getInt("TableId"),
                    rs.getInt("Capacity")
                )
            );
        }
        ArrayList<Table> availableTables = getAllTables(conn);
        for(Table t : unavailableTables) {
            availableTables.removeIf(table -> (table.id == t.id));
        }
        availableTables.removeIf(table -> table.capacity < capacity);
        return availableTables;
    }
}