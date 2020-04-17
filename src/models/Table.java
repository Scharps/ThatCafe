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

    public int getId() {
        return this.id;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public static Table getTable(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT * FROM CafeTables WHERE TableId = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return new Table(
                    rs.getInt("TableId"),
                    rs.getInt("Capacity")
            );
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Table: " + id +", Capacity: " + capacity;
    }

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