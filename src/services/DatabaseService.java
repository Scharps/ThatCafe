package services;

import java.sql.*;

public class DatabaseService {

    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Cafe94", "root", "CafeCovidCSCM94");
        }
        catch(Exception e){
            System.out.println("error");
        }
        return conn;
    }
}
