package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffMember extends User {
    private StaffPosition position;

    private StaffMember(int id, String firstName, String lastName, StaffPosition position) {
		super(id, firstName, lastName);
        this.position = position;
    }

    public StaffPosition getPosition() {
        return this.position;
    }


    
    public static StaffMember createStaffMember(String firstName, String lastName, StaffPosition position) {
        throw new UnsupportedOperationException("createStaffMember() is not yet implemented");
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
            default: throw new UnsupportedOperationException("Staff Position was not given as 0-3");
        }
        staffMember = new StaffMember(
              rs.getInt("StaffId"),
              rs.getString("FName"),
              rs.getString("LName"),
              position
        );
        return staffMember;
    }

    public static boolean deleteStaffMember(int id) {
        throw new UnsupportedOperationException("deleteStaffMember() is not yet implemented");
    }

    public static boolean updateStaffMember(int id, String firstName, String lastName, StaffPosition position) {
        throw new UnsupportedOperationException("updateStaffMember() is not yet implemented");
    }
}