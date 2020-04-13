package models;

import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class Order {
    private int orderId;
    private Timestamp orderDate;
    private int customerId;
    private boolean cooked = false;
    private double orderTotal;

    protected Order(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.cooked = cooked;
        this.orderTotal = orderTotal;
    }

    public static Order createOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal){
        return new Order(orderId, orderDate, customerId, cooked, orderTotal);
    }

    public static void createOrder(Connection conn, Timestamp orderDate, int customerId, double orderTotal){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT INTO Orders (OrderDate, CustomerId, OrderTotal) VALUES (?,?,?)");
            st.setTimestamp(1, orderDate);
            st.setInt(2, customerId);
            st.setDouble(3, orderTotal);
            st.executeUpdate();
        } catch (SQLException se){

        }
    }

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
    public int getOrderId() {
        return this.orderId;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public boolean isCompleted() {
        return this.cooked;
    }

    public boolean markCompleted() {
        throw new UnsupportedOperationException("markCompleted() is not yet implemented");
    }
}