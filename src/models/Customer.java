package models;

import services.DatabaseService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Represents a customer data structure
 * @author Ashley Forster, Sam James
 */
public class Customer extends User {

    private final Address address;
    private final String username;

    private Customer(int id, String username, String firstName, String lastName, Address address) {
        super(id, firstName, lastName);
        this.username = username;
        this.address = address;
    }

    /**
     * Gets the address of the customer
     * @return Address of customer
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * Gets the username of the customer
     * @return Username of the customer
     */
    public String getUsername() { return this.username;}

    /**
     * Creates a customer on the database.
     * @param conn Database connection
     * @param username Username of the customer
     * @param password Password of the customer
     * @param firstName Customer's First name
     * @param lastName Customer's Last name
     * @param address Customer's address
     * @throws SQLException
     */
    public static void createCustomer(Connection conn, String username, String password, String firstName, String lastName, Address address) throws SQLException{
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
        if(rs.next()){
            customerFromResultSet(rs);
        } else {
        }
    }

    /**
     * Gets the address ID of the customer
     * @param conn Database connection
     * @return Customer's address ID
     */
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
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return 0;

        }
    }

    /**
     * Uses credentials to retrieve a Customer from the database. If there is no user that matches those credentials it will return null.
     * @param conn Database connection
     * @param username Username credentials
     * @param password Password credentials
     * @return Customer if correct credentials
     * @throws SQLException
     */
    public static Customer customerLogin(Connection conn, String username, String password) throws SQLException{
        PreparedStatement st = conn.prepareStatement("select * from Customers where Username = ? and Password = ?");
        st.setString(1, username);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return customerFromResultSet(rs);
        } else {
            return null;
        }
    }

    /**
     * Retrieves a customer object from the database from an ID
     * @param conn Database Connection
     * @param id Customer's ID
     * @return Customer
     * @throws SQLException
     */
    public static Customer getCustomer(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT * FROM Customers WHERE CustomerId = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return customerFromResultSet(rs);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the most active customer by number of orders made
     * @param conn Database connection
     * @return Most active customer.
     * @throws SQLException
     */
    public static Customer getMostActive(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT CustomerId, COUNT(CustomerId) AS 'Count'\n" +
                "FROM Orders\n" +
                "GROUP BY CustomerId\n" +
                "ORDER BY 'Count' DESC\n" +
                "LIMIT 1");
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return getCustomer(conn, rs.getInt("CustomerId"));
        } else {
            return null;
        }

    }

    /**
     * Deletes a customer from an ID
     * @param conn Database connection
     * @param id Customer ID
     * @throws SQLException
     */
    public static void deleteCustomer(Connection conn, int id) throws SQLException {
        PreparedStatement st = conn.prepareStatement("DELETE FROM Customers WHERE CustomerId = ?");
        st.setInt(1, id);
        st.executeUpdate();
    }

    /**
     * Updates a Customer in the database
     * @param conn Database Connection
     * @param id Customer ID to update
     * @param username New Username
     * @param password New Password
     * @param firstName New first name
     * @param lastName New surname
     * @param address New address
     * @throws SQLException
     */
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

    private static Customer customerFromResultSet(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("CustomerId"),
                rs.getString("Username"),
                rs.getString("FName"),
                rs.getString("LName"),
                Address.getAddress(DatabaseService.getConnection(), rs.getInt("AddressId"))
        );
    }
}