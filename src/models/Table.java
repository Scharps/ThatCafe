package models;

import java.sql.*;
import java.util.ArrayList;

public class Table {
    private int id;
    private int capacity;

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

    public static ArrayList<Table> getAllTables(Connection conn) throws SQLException {
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
        String sqlStatement = String.format(
                "SELECT *\n" +
                "FROM Bookings b, CafeTables t\n" +
                "WHERE b.TableId = t.TableId AND\n" +
                "b.BookingDate = ? AND\n" +
                "("
        );

        for(int i = startHour; i < startHour + duration; i++) {
            sqlStatement += "b.BookingHour = " + i;
            if(i < startHour + duration - 1) {
                sqlStatement += " || ";
            } else {
                sqlStatement += ")";
            }
        }

        PreparedStatement st = conn.prepareStatement(sqlStatement);
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
            availableTables.removeIf(table -> table.id == t.id || table.capacity < capacity);
        }
        return availableTables;
    }
}