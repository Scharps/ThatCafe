package services;

import java.sql.*;

public class DatabaseService {

    public static int getLastInsert(Connection conn){
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()");
            if(rs.next()){
                return rs.getInt(1);
            }
            else {
                System.out.println("Error1");
                return 0;
            }
        } catch (SQLException se){

            return 0;

        }
    }

    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Cafe94", "Cafe94", "Covid");
        }
        catch(Exception e){
            System.out.println(e);
        }
        return conn;
    }
}
