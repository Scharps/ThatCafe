package services;

import java.sql.*;

public class DatabaseService {

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
