package models;

import javafx.util.Pair;
import services.DatabaseService;

import java.sql.*;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;

/**
 * Staff member structure
 * @author Ashley Forster, Sam James
 */
public class StaffMember extends User {
    private final StaffPosition position;
    private final Rota rota;

    private StaffMember(int id, String firstName, String lastName, StaffPosition position, Rota rota) {
		super(id, firstName, lastName);
        this.position = position;
        this.rota = rota;
    }

    /**
     * Gets the StaffPosition of the StaffMember
     * @return StaffPosition
     */
    public StaffPosition getPosition() {
        return this.position;
    }

    /**
     * Gets the StaffMember's Rota
     * @return StaffMember's Rota
     */
    public Rota getRota() {
        return this.rota;
    }

    @Override
    public String toString() {
        return String.format("%s %s\t Position: %s", getFirstName(), getLastName(), getPosition());
    }

    /**
     * Creates a StaffMember entry in the database and returns the entry as an object.
     * @param conn Database connection
     * @param password StaffMember password
     * @param firstName First name
     * @param lastName Last name
     * @param position Position of the StaffMember
     * @param rota StaffMember's Rota
     * @return Creates StaffMember
     * @throws SQLException
     */
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

    /**
     * Gets the StaffMember the worked the most hours in the past week and their time worked.
     * @param conn Database connection
     * @return Key value pair K - StaffMember, V - Hours worked
     * @throws SQLException
     */
    public static Pair<StaffMember, Integer> getTopStaffMemberPast7Days(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT StaffId, SUM(WorkedHours) WorkedHours\n" +
                "FROM HoursWorked\n" +
                "WHERE DateWorked >= ?\n" +
                "GROUP BY StaffId\n" +
                "ORDER BY WorkedHours DESC\n" +
                "LIMIT 1");
        st.setDate(1, new Date(Date.from(Instant.now().minus(Period.ofWeeks(1))).getTime()));
        ResultSet rs = st.executeQuery();
        Pair<StaffMember, Integer> hoursWorked = null;
        while(rs.next()) {
            hoursWorked = new Pair<>(
                StaffMember.getStaffMember(conn, rs.getInt("StaffId")),
                rs.getInt("WorkedHours")
            );
        }
        return hoursWorked;
    }

    /**
     * Gets a StaffMember by its ID from the database.
     * @param conn Database Connection
     * @param id ID of the StaffMember
     * @return StaffMember
     * @throws SQLException
     */
    public static StaffMember getStaffMember(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Staff WHERE StaffID = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return staffMemberFromResultSet(rs);
        }
        else{
            return null;
        }
    }

    /**
     * Deletes a StaffMember from the database.
     * @param conn Database connection
     * @param id ID of StaffMember to delete
     * @throws SQLException
     */
    public static void deleteStaffMember(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM Staff WHERE StaffId = ?");
        st.setInt(1, id);
        st.executeUpdate();
    }

    /**
     * Updates a StaffMember by ID
     * @param conn Database connection
     * @param id StaffMember ID to update.
     * @param firstName New first name.
     * @param lastName New last name.
     * @param position New StaffPosition
     * @throws SQLException
     */
    public static void updateStaffMember(Connection conn, int id,  String firstName,
                                            String lastName, StaffPosition position) throws SQLException  {
        PreparedStatement st = conn.prepareStatement("UPDATE Staff " +
                "SET FName = ?, LName = ?, StaffPos = ?" +
                "WHERE StaffId = ?");
        st.setString(1, firstName);
        st.setString(2, lastName);
        st.setString(3, position.toString());
        st.setInt(4, id);
        st.executeUpdate();
    }

    /**
     * Gets all StaffMembers Registered in the system
     * @param conn Database connection
     * @return All registered StaffMembers
     * @throws SQLException
     */
    public static ArrayList<StaffMember> getAllStaffMembers(Connection conn) throws SQLException {
        ArrayList<StaffMember> staffMembers = new ArrayList<>();
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Staff");
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            staffMembers.add(staffMemberFromResultSet(rs));
        }
        return staffMembers;
    }

    /**
     * Sets the hours worked on a date
     * @param conn Database connection
     * @param date Date to set
     * @param hours Hours worked
     * @throws SQLException
     */
    public void setHoursWorked(Connection conn, Date date, int hours) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO HoursWorked(StaffId, DateWorked, WorkedHours)\n" +
                "VALUES(?, ?, ?)"
        );
        st.setInt(1, getId());
        st.setDate(2, date);
        st.setInt(3, hours);
        st.executeUpdate();
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