package services;

import com.mysql.cj.result.SqlDateValueFactory;

import java.sql.*;

public class DatabaseService {

    public static Connection getConnection(Connection con){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Cafe94", "root", "CafeCovidCSCM94");
        }
        catch(Exception e){
            System.out.println("error");
        }
        return con;
    }

    public static int profileSelect(Connection conn, int staffID){
        try{
            PreparedStatement st = conn.prepareStatement("select Role from Staff where StaffID = ?");
            st.setInt(1,staffID);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            } else return 0;
        } catch (SQLException se){
            return 0;
        }
    }
}
