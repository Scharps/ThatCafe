package services;

import java.sql.Connection;
import java.sql.DriverManager;

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
}
