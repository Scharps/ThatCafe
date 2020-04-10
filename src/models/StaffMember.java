package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffMember extends User {
    private StaffPosition position;

    private StaffMember(int id, String username, String firstName, String lastName, StaffPosition position) {
		super(id, username, firstName, lastName);
        this.position = position;
    }

    public StaffPosition getPosition() {
        return this.position;
    }

    public static StaffMember createStaffMember(Connection conn, String username, String password, String firstName,
                                                String lastName, StaffPosition position) throws SQLException  {
        PreparedStatement st = conn.prepareStatement(
                "INSERT INTO STAFF(Username, Password, FName, LName, StaffPos) " +
                    "VALUES (?, ?, ?, ?, ?)"
        );

        st.setString(1, username);
        st.setString(2, password);
        st.setString(3, firstName);
        st.setString(4, lastName);
        st.setString(5, position.toString());
        ResultSet rs = st.executeQuery();
        return new StaffMember(
                rs.getInt("StaffId"),
                rs.getString("Username"),
                rs.getString("FName"),
                rs.getString("LName"),
                position
        );
    }

    public static StaffMember getStaffMember(Connection conn, int id) throws SQLException {
        StaffMember staffMember = null;
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Staff WHERE StaffID = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        String p = rs.getString("StaffPos");
        StaffPosition position;
        switch (p) {
            case "Chef": position = StaffPosition.Chef;
                    break;
            case "Driver": position = StaffPosition.Driver;
                    break;
            case "Manager": position = StaffPosition.Manager;
                    break;
            case "Waiter": position = StaffPosition.Waiter;
                    break;
            default: position = StaffPosition.Waiter;
        }
        staffMember = new StaffMember(
              rs.getInt("StaffId"),
              rs.getString("Username"),
              rs.getString("FName"),
              rs.getString("LName"),
              position
        );
        return staffMember;
    }

    public static boolean deleteStaffMember(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM Staff WHERE StaffId = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
    }

    public static boolean updateStaffMember(Connection conn, int id, String username, String password, String firstName,
                                            String lastName, StaffPosition position) throws SQLException  {
        throw new UnsupportedOperationException("updateStaffMember() is not yet implemented");
    }
}