package models;

import services.DatabaseService;

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

    public static Customer createCustomer(int id, String username, String firstName, String lastName, Address address) {
        Customer customer = new Customer(id, username, firstName, lastName, address);
        return customer;
        //throw new UnsupportedOperationException("createCustomer() is not yet implemented");
    }

    public static void registerCustomer(Connection conn, String username, String password, String firstname, String lastname, int address_id) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO Customers(Username, Password, FName, LName, AddressId)  values(?, ?, ?, ?, ?)");
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, firstname);
            st.setString(4, lastname);
            st.setInt(5, address_id);
            st.executeUpdate();
        } catch (SQLException se){
            se.printStackTrace();
            System.out.println(se);
        }

    }
    public static Customer customerLogin(Connection conn, String username, String password){
        try {
            PreparedStatement st = conn.prepareStatement("select * from Customers where Username = ? and Password = ?");
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                PreparedStatement address_st = conn.prepareStatement("SELECT * FROM Address WHERE AddressId = ? ");
                address_st.setInt(1, rs.getInt(6));
                ResultSet address_rs = address_st.executeQuery();
                if (address_rs.next()) {
                    Address address = Address.createAddress(address_rs.getInt(1), address_rs.getString(2), address_rs.getString(3), address_rs.getString(4));
                    Customer customer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(5), address);
                    return customer;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println(se);
            return null;
        }
    }

    public static Customer getCustomer(int id) {
        throw new UnsupportedOperationException("getCustomer() is not yet implemented");
    }

    public static boolean deleteCustomer(int id) {
        throw new UnsupportedOperationException("deleteCustomer() is not yet implemented");
    }

    public static boolean updateCustomer(int id, String firstName, String lastName, Address address) {
        throw new UnsupportedOperationException("updateCustomer() is not yet implemented");
    }
}