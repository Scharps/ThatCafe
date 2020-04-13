package models;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DeliveryOrder extends Order {
    private Timestamp estimatedDeliveryTime;
    private boolean confirmed = false;
    private boolean delivered = false;
    private int driverID;

    private DeliveryOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, boolean confirmed, Timestamp estimatedDeliveryTime, int driverID, boolean delivered) {
        super(orderId, orderDate, customerId, cooked, orderTotal);
        this.confirmed = confirmed;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.driverID = driverID;
        this.delivered = delivered;
    }

    public static Timestamp estimateDeliveryTime(LocalDateTime orderTime){
        orderTime = orderTime.withSecond(0).withNano(0);
        return Timestamp.valueOf(orderTime.plusHours(1));
    }

    public static void createDeliveryOrder(Connection conn, int orderId, Timestamp estimatedDeliveryTime) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO DeliveryOrders (OrderId, DeliveryTime) VALUES (?,?)");
            st.setInt(1, orderId);
            st.setTimestamp(2, estimatedDeliveryTime);
            st.executeUpdate();
        } catch (SQLException se){
        }
    }

    public Timestamp getEstimatedDeliveryTime() {
        return this.estimatedDeliveryTime;
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }

    public boolean isDelivered() {
        return this.delivered;
    }


    public boolean markApproved() {
        throw new UnsupportedOperationException("markApproved() not yet implemented");
    }

    public boolean markDelivered() {
        throw new UnsupportedOperationException("markDelivered() not yet implemented");
    }

    public static DeliveryOrder createDeliveryOrder(ArrayList<Integer> items, int customerId, Date estimatedDeliveryTime, Address address) {
        throw new UnsupportedOperationException("createDeliveryOrder() is not yet implemented");
    }

    public static DeliveryOrder getDeliveryOrder(int id) {
        throw new UnsupportedOperationException("getDeliveryOrder() is not yet implemented");
    }

    public static boolean deleteDeliveryOrder(int id) {
        throw new UnsupportedOperationException("deleteDeliveryOrder() is not yet implemented");
    }

    public static boolean updateDeliveryOrder(int id, ArrayList<Integer> items, int customerId, Date estimatedDeliveryTime, Address address) {
        throw new UnsupportedOperationException("updateDeliveryOrder() is not yet implemented");
    }
}