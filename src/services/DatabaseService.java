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

    public static boolean customerLogin(String username, String password) {
        Connection conn = null;
        try {
            conn = DatabaseService.getConnection(conn);
            PreparedStatement st = conn.prepareStatement("select * from Customers where Username = ? and Password = ?");
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            return (rs.next());
        } catch (SQLException se) {
            return false;
        }
    }
}
