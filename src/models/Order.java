package models;

import javax.swing.*;
import java.sql.*;

/**
 * This is the datastructure for an Order.
 * @author Ashley Forster
 */
public class Order {
    private final int orderId;
    private final Timestamp orderDate;
    private final int customerId;
    private final boolean cooked;
    private final double orderTotal;
    private final OrderType orderType;

    /**
     * Constructor of Order
     * @param orderId OrderID
     * @param orderDate Date of the Order
     * @param customerId Customer ID whom made the order
     * @param cooked Cooked status
     * @param orderTotal The total price of the Order
     * @param orderType The Order Type
     */
    Order(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, OrderType orderType) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.cooked = cooked;
        this.orderTotal = orderTotal;
        this.orderType = orderType;
    }

    /**
     * Creates an Order entry in the database
     * @param conn Database Connection
     * @param orderDate Date of the Order
     * @param customerId CustomerID
     * @param orderTotal Price Total
     * @param orderType Order TYpe
     */
    public static void createOrder(Connection conn, Timestamp orderDate, int customerId, double orderTotal, OrderType orderType){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT INTO Orders (OrderDate, CustomerId, OrderTotal, OrderType) VALUES (?,?,?, ?)");
            st.setTimestamp(1, orderDate);
            st.setInt(2, customerId);
            st.setDouble(3, orderTotal);
            st.setString(4, orderType.toString());
            st.executeUpdate();
        } catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gets the order history of a specified customer
     * @param conn Database connection
     * @param customerId Customer's ID
     * @return A ResultSet of the customer's history
     */
    public static ResultSet getOrderHistory(Connection conn, int customerId){
        try{
            PreparedStatement st = conn.prepareStatement("SELECT * FROM Orders WHERE CustomerId = ? AND Cooked = 1");
            st.setInt(1, customerId);
            ResultSet rs = st.executeQuery();
            return rs;
        }catch (SQLException se){
            return null;
        }
    }

    /**
     * Creates Order object from ResultSet from Orders table in the Database
     * @param rs
     * @return Order object created from a ResultSet
     * @throws SQLException
     */
    public static Order orderFromRS(ResultSet rs) throws SQLException{
        return new Order(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), OrderType.valueOf(rs.getString(6)));
    }

    /**
     * Gets all uncooked ordered as a ResultSet
     * @param conn Database connection.
     * @return ResultSet of uncooked orders.
     */
    public static ResultSet getUncookedOrders(Connection conn){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Orders WHERE Cooked = 0");
            return rs;
        } catch (SQLException se){
            return null;
        }
    }

    /**
     * Sets the cooked status of the Order to true.
     * @param conn Database connection
     */
    public void setCooked(Connection conn){
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE Orders SET Cooked =1 WHERE OrderId = ?");
            st.setInt(1, this.orderId);
            st.executeUpdate();
        } catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Gets the ID of the Order
     * @return ID of the Order
     */
    public int getOrderId() {
        return this.orderId;
    }

    /**
     * Gets the Customer ID
     * @return ID of the Customer
     */
    public int getCustomerId() {
        return this.customerId;
    }

    /**
     * Gets the cooked status of the order.
     * @return Cooked status
     */
    public boolean isCooked() {
        return cooked;
    }
}