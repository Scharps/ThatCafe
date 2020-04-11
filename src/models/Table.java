package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}