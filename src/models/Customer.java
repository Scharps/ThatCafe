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

    /*public static Customer createCustomer(int id, String username, String firstName, String lastName, String addressline1, String city, String postcode) {
        Customer customer = new Customer(id, username, firstName, lastName, addressline1, city, postcode);
        return customer;
        //throw new UnsupportedOperationException("createCustomer() is not yet implemented");
    }*/
    public static Customer customerLogin(Connection conn, String username, String password){
        try {
            PreparedStatement st = conn.prepareStatement("select * from Customers where Username = ? and Password = ?");
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                Address address = Address.createAddress(rs.getInt(1), rs.getString(6), rs.getString(7), rs.getString(8));
                Customer customer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(5), address);
                return customer;
            } else {
                return null;
            }
        } catch (SQLException se) {
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