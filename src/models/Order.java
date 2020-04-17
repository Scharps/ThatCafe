package models;

import java.sql.*;

public class Order {
    private int orderId;
    private Timestamp orderDate;
    private int customerId;
    private boolean cooked;
    private double orderTotal;
    private OrderType orderType;

    protected Order(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, OrderType orderType) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.cooked = cooked;
        this.orderTotal = orderTotal;
        this.orderType = orderType;
    }

    public static Order createOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, OrderType orderType){
        return new Order(orderId, orderDate, customerId, cooked, orderTotal, orderType);
    }

    public static void createOrder(Connection conn, Timestamp orderDate, int customerId, double orderTotal, OrderType orderType){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT INTO Orders (OrderDate, CustomerId, OrderTotal, OrderType) VALUES (?,?,?, ?)");
            st.setTimestamp(1, orderDate);
            st.setInt(2, customerId);
            st.setDouble(3, orderTotal);
            st.setString(4, String.valueOf(orderType));
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

    public double getOrderTotal() {
        return orderTotal;
    }

    public String getOrderType(){
        return String.valueOf(this.orderType);
    }

    public boolean markCompleted() {
        throw new UnsupportedOperationException("markCompleted() is not yet implemented");
    }


    public boolean isCooked() {
        return cooked;
    }
}