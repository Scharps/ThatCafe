package models;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TakeawayOrder extends Order {
    private Timestamp pickupTime;
    private boolean collected;

    private TakeawayOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, OrderType orderType, Timestamp pickupTime, boolean collected) {
        super(orderId, orderDate, customerId, cooked, orderTotal, orderType);
        this.pickupTime = pickupTime;
        this.collected = collected;
    }

    public static Timestamp estimatePickUpTime(LocalDateTime orderTime){
        orderTime = orderTime.withSecond(0).withNano(0);
        return Timestamp.valueOf(orderTime.plusMinutes(20));
    }

    public static void createTakeawayOrder(Connection conn, int orderId, Timestamp pickupTime) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO TakeawayOrders (OrderId, PickUpTime) VALUES (?,?)");
            st.setInt(1, orderId);
            st.setTimestamp(2, pickupTime);
            st.executeUpdate();
        } catch (SQLException se){
        }
    }

    public Timestamp getPickUpTime() {
        return this.pickupTime;
    }

    public static TakeawayOrder createTakeawayOrder(ArrayList<Integer> items, int customerId, Date pickupTime) {
        throw new UnsupportedOperationException("createTakeawayOrder() is not yet implemented");
    }

    public static TakeawayOrder getTakeawayOrder(int id) {
        throw new UnsupportedOperationException("getTakeawayOrder() is not yet implemented");
    }

    public static boolean deleteTakeawayOrder(int id) {
        throw new UnsupportedOperationException("deleteTakeawayOrder() is not yet implemented");
    }

    public static boolean updateTakeawayOrder(int id, ArrayList<Integer> items, int customerId, Date pickupTime) {
        throw new UnsupportedOperationException("updateTakeawayOrder() is not yet implemented");
    }
}