package services;

import javafx.scene.control.PasswordField;

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

    public static Boolean confirmPassword(Connection conn, String password, int customerId){
        try{
            PreparedStatement st = conn.prepareStatement("SELECT * FROM Customers WHERE Password = ? AND CustomerId = ?");
            st.setString(1, password);
            st.setInt(2, customerId);
            ResultSet rs = st.executeQuery();
            return rs.next();
        }catch (SQLException se){
            return false;
        }
    }

    public static void updateCustomerPassword(Connection conn, String newPassword, int customerId){
        try{
            PreparedStatement st = conn.prepareStatement("UPDATE Customers SET Password = ? WHERE CustomerId = ?");
            st.setString(1, newPassword);
            st.setInt(2, customerId);
            st.executeUpdate();
        }catch (SQLException se){

        }
    }
}
