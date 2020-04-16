package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Rota {
    private int rotaId;
    private Shift mondayShift;
    private Shift tuesdayShift;
    private Shift wednesdayShift;
    private Shift thursdayShift;
    private Shift fridayShift;
    private Shift saturdayShift;
    private Shift sundayShift;

    private Rota(int rotaId, Shift mondayShift, Shift tuesdayShift, Shift wednesdayShift, Shift thursdayShift, Shift fridayShift, Shift saturdayShift, Shift sundayShift) {
        this.rotaId = rotaId;
        this.mondayShift = mondayShift;
        this.tuesdayShift = tuesdayShift;
        this.wednesdayShift = wednesdayShift;
        this.thursdayShift = thursdayShift;
        this.fridayShift = fridayShift;
        this.saturdayShift = saturdayShift;
        this.sundayShift = sundayShift;
    }

    public int getRotaId() {
        return rotaId;
    }

    public Shift getMondayShift() {
        return mondayShift;
    }

    public Shift getTuesdayShift() {
        return tuesdayShift;
    }

    public Shift getWednesdayShift() {
        return wednesdayShift;
    }

    public Shift getThursdayShift() {
        return thursdayShift;
    }

    public Shift getFridayShift() {
        return fridayShift;
    }

    public Shift getSaturdayShift() {
        return saturdayShift;
    }

    public Shift getSundayShift() {
        return sundayShift;
    }

    public static Rota createRota(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO Rota() VALUES ()");
        st.executeUpdate();

        st = conn.prepareStatement("SELECT * FROM Rota\n" +
                "WHERE RotaId = (SELECT MAX(RotaId) FROM Rota)");
        ResultSet rs = st.executeQuery();
        rs.next();
        return rotaFromResultSet(rs);
    }

    public static void updateRota(
            Connection conn,
            int rotaId,
            Shift mondayShift,
            Shift tuesdayShift,
            Shift wednesdayShift,
            Shift thursdayShift,
            Shift fridayShift,
            Shift saturdayShift,
            Shift sundayShift
        ) throws SQLException {
            PreparedStatement st = conn.prepareStatement(
            "UPDATE Rota\n" +
                "SET MonStart = ?, MonFinish = ?, " +
                "TueStart = ?, TueFinish = ?, " +
                "WedStart = ?, WedFinish = ?, " +
                "ThuStart = ?, ThuFinish = ?, " +
                "FriStart = ?, FriFinish = ?, " +
                "SatStart = ?, SatFinish = ?, " +
                "SunStart = ?, SunFinish = ? " +
                "WHERE RotaId = ?"
            );
            st.setString(1, mondayShift.getStartTime());
            st.setString(2, mondayShift.getFinishTime());
            st.setString(3, tuesdayShift.getStartTime());
            st.setString(4, tuesdayShift.getFinishTime());
            st.setString(5, wednesdayShift.getStartTime());
            st.setString(6, wednesdayShift.getFinishTime());
            st.setString(7, thursdayShift.getStartTime());
            st.setString(8, thursdayShift.getFinishTime());
            st.setString(9, fridayShift.getStartTime());
            st.setString(10, fridayShift.getFinishTime());
            st.setString(11, saturdayShift.getStartTime());
            st.setString(12, saturdayShift.getFinishTime());
            st.setString(13, sundayShift.getStartTime());
            st.setString(14, sundayShift.getFinishTime());
            st.setInt(15, rotaId);
            st.executeUpdate();
    }

    public static Rota getRota(Connection conn, int rotaId) throws SQLException {
        PreparedStatement st = conn.prepareStatement(
            "SELECT * " +
                "FROM Rota " +
                "WHERE RotaId = ?"
        );
        st.setInt(1, rotaId);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return rotaFromResultSet(rs);
        } else {
            return null;
        }
    }

    private static Rota rotaFromResultSet(ResultSet rs) throws SQLException {
        return new Rota(
                rs.getInt("RotaId"),
                new Shift(
                        rs.getString("MonStart"),
                        rs.getString("MonFinish")
                ),
                new Shift(
                        rs.getString("TueStart"),
                        rs.getString("TueFinish")
                ),
                new Shift(
                        rs.getString("WedStart"),
                        rs.getString("WedFinish")
                ),
                new Shift(
                        rs.getString("ThuStart"),
                        rs.getString("ThuFinish")
                ),
                new Shift(
                        rs.getString("FriStart"),
                        rs.getString("FriFinish")
                ),
                new Shift(
                        rs.getString("SatStart"),
                        rs.getString("SatFinish")
                ),
                new Shift(
                        rs.getString("SunStart"),
                        rs.getString("SunFinish")
                )
        );
    }
}
