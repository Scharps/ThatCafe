package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public abstract class Order {
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