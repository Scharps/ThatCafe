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

    private Rota(int rotaId) {

    }

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

    public static Rota createRota(
            Connection conn,
            Shift mondayShift,
            Shift tuesdayShift,
            Shift wednesdayShift,
            Shift thursdayShift,
            Shift fridayShift,
            Shift saturdayShift,
            Shift sundayShift
    ) throws SQLException {
        PreparedStatement st = conn.prepareStatement(
        "INSERT INTO Rota(" +
                "RotaId, " +
                "MonStart, " +
                "MonFinish, " +
                "TueStart, " +
                "TueFinish, " +
                "WedStart, " +
                "WedFinish, " +
                "ThuStart, " +
                "ThuFinish, " +
                "FriStart, " +
                "FriFinish, " +
                "SatStart, " +
                "SatFinish, " +
                "SunStart, " +
                "SunFinish" +
            ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        );
        st.setInt(1, mondayShift.getStartTime());
        st.setInt(2, mondayShift.getFinishTime());
        st.setInt(3, tuesdayShift.getStartTime());
        st.setInt(4, tuesdayShift.getFinishTime());
        st.setInt(5, wednesdayShift.getStartTime());
        st.setInt(6, wednesdayShift.getFinishTime());
        st.setInt(7, thursdayShift.getStartTime());
        st.setInt(8, thursdayShift.getFinishTime());
        st.setInt(9, fridayShift.getStartTime());
        st.setInt(10, fridayShift.getFinishTime());
        st.setInt(11, saturdayShift.getStartTime());
        st.setInt(12, saturdayShift.getFinishTime());
        st.setInt(13, sundayShift.getStartTime());
        st.setInt(14, sundayShift.getFinishTime());
        st.executeUpdate();

        st = conn.prepareStatement("SELECT * FROM Rota\n" +
                "WHERE RotaId = (SELECT MAX(RotaId) FROM Rota)");
        ResultSet rs = st.executeQuery();
        rs.next();
        return rotaFromResultSet(rs);
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
                        rs.getInt("MonStart"),
                        rs.getInt("MonFinish")
                ),
                new Shift(
                        rs.getInt("TueStart"),
                        rs.getInt("TueFinish")
                ),
                new Shift(
                        rs.getInt("WedStart"),
                        rs.getInt("WedFinish")
                ),
                new Shift(
                        rs.getInt("ThuStart"),
                        rs.getInt("ThuFinish")
                ),
                new Shift(
                        rs.getInt("FriStart"),
                        rs.getInt("FriFinish")
                ),
                new Shift(
                        rs.getInt("SatStart"),
                        rs.getInt("SatFinish")
                ),
                new Shift(
                        rs.getInt("SunStart"),
                        rs.getInt("SunFinish")
                )
        );
    }
}
