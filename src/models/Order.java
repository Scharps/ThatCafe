package models;

import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

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

    public static ResultSet getUncookedOrders(Connection conn){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Orders WHERE Cooked = 0");
            return rs;
        } catch (SQLException se){
            return null;
        }
    }

    public void setCooked(Connection conn){
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE Orders SET Cooked =1 WHERE OrderId = ?");
            st.setInt(1, this.orderId);
            st.executeUpdate();
        } catch (SQLException se){

        }
    }


    public int getOrderId() {
        return this.orderId;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public Timestamp getOrderDate() {
        return this.orderDate;
    }

    public boolean isCompleted() {
        return this.cooked;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public boolean markCompleted() {
        throw new UnsupportedOperationException("markCompleted() is not yet implemented");
    }


}