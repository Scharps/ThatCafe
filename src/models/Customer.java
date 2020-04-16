package models;

import services.DatabaseService;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends User {

    private Address address;
    private String username;

    private Customer(int id, String username, String firstName, String lastName, Address address) {
        super(id, firstName, lastName);
        this.username = username;
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    public String getUsername() { return this.username;}

    public static Customer createCustomer(Connection conn, String username, String password, String firstName, String lastName, Address address) throws SQLException{
        PreparedStatement st = conn.prepareStatement("INSERT INTO Customers(Username, Password, FName, LName, AddressId)  VALUES(?, ?, ?, ?, ?)");
        st.setString(1, username);
        st.setString(2, password);
        st.setString(3, firstName);
        st.setString(4, lastName);
        st.setInt(5, address.getId());
        st.executeUpdate();

        st = conn.prepareStatement("SELECT * FROM Customers\n" +
                "WHERE CustomerId = (SELECT MAX(CustomerId) FROM Customers)");
        ResultSet rs = st.executeQuery();
        rs.next();
        Customer customer = new Customer(
                rs.getInt("CustomerId"),
                rs.getString("Username"),
                rs.getString("FName"),
                rs.getString("FName"),
                Address.getAddress(DatabaseService.getConnection(), rs.getInt("AddressId"))
        );
        return customer;
    }

    public int getAddressId(Connection conn){
        try{
            PreparedStatement st = conn.prepareStatement("Select AddressId FROM Customers WHERE CustomerId = ?");
            st.setInt(1, this.getId());
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
            else{
                return 0;
            }
        }catch (SQLException se){
            System.out.println("HFGDH");
            return 0;

        }
    }
    public static Customer customerLogin(Connection conn, String username, String password) throws SQLException{
        PreparedStatement st = conn.prepareStatement("select * from Customers where Username = ? and Password = ?");
        st.setString(1, username);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return new Customer(
                    rs.getInt("CustomerId"),
                    rs.getString("Username"),
                    rs.getString("FName"),
                    rs.getString("LName"),
                    Address.getAddress(DatabaseService.getConnection(), rs.getInt("AddressId"))
            );
        } else {
            return null;
        }
    }

    public static Customer getCustomer(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Customers WHERE CustomerId = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return new Customer(
                    rs.getInt("CustomerId"),
                    rs.getString("Username"),
                    rs.getString("FName"),
                    rs.getString("LName"),
                    Address.getAddress(DatabaseService.getConnection(), rs.getInt("AddressId"))
            );
        } else {
            return null;
        }
    }

    public static void deleteCustomer(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM Customers WHERE CustomerId = ?");
        st.setInt(1, id);
        st.executeUpdate();
    }

    public static void updateCustomer(Connection conn, int id, String username, String password, String firstName, String lastName, Address address) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE Address " +
                "SET Username = ?, Password = ?, FName = ?, LName = ?, AddressId = ?" +
                "WHERE CustomerId = ?");
        st.setString(1, username);
        st.setString(2, password);
        st.setString(3, firstName);
        st.setString(4, lastName);
        st.setInt(5, address.getId());
        st.setInt(6, id);
        st.executeUpdate();
    }
}