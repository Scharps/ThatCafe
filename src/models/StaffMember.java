package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StaffMember extends User {
    private StaffPosition position;

    private StaffMember(int id, String firstName, String lastName, StaffPosition position) {
		super(id, firstName, lastName);
        this.position = position;
    }

    public StaffPosition getPosition() {
        return this.position;
    }


    public static void createStaffMember(Connection conn, String password, String firstName,
                                                String lastName, StaffPosition position) throws SQLException  {
        PreparedStatement st = conn.prepareStatement(
                "INSERT INTO Staff( Password, FName, LName, StaffPos) " +
                    "VALUES (?, ?, ?, ?)"
        );

        st.setString(1, password);
        st.setString(2, firstName);
        st.setString(3, lastName);
        st.setString(4, position.toString());
        st.executeUpdate();

    }

    public static StaffMember getStaffMember(Connection conn, int id) throws SQLException {
        StaffMember staffMember = null;
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Staff WHERE StaffID = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            String p = rs.getString("StaffPos");
            StaffPosition position;
            switch (p) {
                case "Chef":
                    position = StaffPosition.Chef;
                    break;
                case "Driver":
                    position = StaffPosition.Driver;
                    break;
                case "Manager":
                    position = StaffPosition.Manager;
                    break;
                case "Waiter":
                    position = StaffPosition.Waiter;
                    break;
                default:
                    position = StaffPosition.Waiter;
            }
            staffMember = new StaffMember(
                    rs.getInt("StaffId"),
                    rs.getString("FName"),
                    rs.getString("LName"),
                    position
            );
            return staffMember;
        }
        else{
            return null;
        }
    }

    public static void deleteStaffMember(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM Staff WHERE StaffId = ?");
        st.setInt(1, id);
        st.executeUpdate();
    }

    public static void updateStaffMember(Connection conn, int id, String password, String firstName,
                                            String lastName, StaffPosition position) throws SQLException  {
        PreparedStatement st = conn.prepareStatement("UPDATE Staff " +
                "SET Password = ?, FName = ?, LName = ?, StaffPos = ? " +
                "WHERE StaffId = ?");
        st.setString(1, password);
        st.setString(2, firstName);
        st.setString(3, lastName);
        st.setString(4, position.toString());
        st.setInt(5, id);
        st.executeUpdate();
    }

    public static ArrayList<StaffMember> getAllStaffMembers(Connection conn) throws SQLException {
        ArrayList<StaffMember> staffMembers = new ArrayList<>();
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Staff");
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            staffMembers.add(new StaffMember(
                    rs.getInt("StaffId"),
                    rs.getString("FName"),
                    rs.getString("LName"),
                    StaffPosition.valueOf(rs.getString("StaffPos"))
            ));
        }
        return staffMembers;
    }
}