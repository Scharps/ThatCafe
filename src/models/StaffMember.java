package models;

import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StaffMember extends User {
    private StaffPosition position;
    private Rota rota;

    private StaffMember(int id, String firstName, String lastName, StaffPosition position, Rota rota) {
		super(id, firstName, lastName);
        this.position = position;
        this.rota = rota;
    }

    public StaffPosition getPosition() {
        return this.position;
    }

    public Rota getRota() {
        return this.rota;
    }

    @Override
    public String toString() {
        return String.format("%s %s\t Position: %s", getFirstName(), getLastName(), getPosition());
    }

    public static StaffMember createStaffMember(Connection conn, String password, String firstName,
                                                String lastName, StaffPosition position, Rota rota) throws SQLException  {
        PreparedStatement st = conn.prepareStatement(
                "INSERT INTO Staff( Password, FName, LName, StaffPos, RotaId) " +
                    "VALUES (?, ?, ?, ?, ?)"
        );

        st.setString(1, password);
        st.setString(2, firstName);
        st.setString(3, lastName);
        st.setString(4, position.toString());
        st.setInt(5, rota.getRotaId());
        st.executeUpdate();

        st = conn.prepareStatement("SELECT * FROM Staff\n" +
                "WHERE StaffId = (SELECT MAX(StaffId) FROM Staff)");
        ResultSet rs = st.executeQuery();
        rs.next();
        return staffMemberFromResultSet(rs);

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
            return staffMemberFromResultSet(rs);
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
                                            String lastName, StaffPosition position, int rotaId) throws SQLException  {
        PreparedStatement st = conn.prepareStatement("UPDATE Staff " +
                "SET Password = ?, FName = ?, LName = ?, StaffPos = ?, rotaId = ?" +
                "WHERE StaffId = ?");
        st.setString(1, password);
        st.setString(2, firstName);
        st.setString(3, lastName);
        st.setString(4, position.toString());
        st.setInt(5, rotaId);
        st.setInt(6, id);
        st.executeUpdate();
    }

    public static ArrayList<StaffMember> getAllStaffMembers(Connection conn) throws SQLException {
        ArrayList<StaffMember> staffMembers = new ArrayList<>();
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Staff");
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            staffMembers.add(staffMemberFromResultSet(rs));
        }
        return staffMembers;
    }

    private static StaffMember staffMemberFromResultSet(ResultSet rs) throws SQLException {
        return new StaffMember(
                rs.getInt("StaffId"),
                rs.getString("FName"),
                rs.getString("LName"),
                StaffPosition.valueOf(rs.getString("StaffPos")),
                Rota.getRota(DatabaseService.getConnection(), rs.getInt("RotaId"))
        );
    }
}