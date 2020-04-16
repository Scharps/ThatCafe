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

    public static ResultSet getUncollected(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, t.PickUpTime, t.Collected FROM Orders o, TakeawayOrders t WHERE o.OrderId = t.OrderId AND t.Collected = 0 ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    public static TakeawayOrder orderFromRS(ResultSet rs) throws SQLException{
        return new TakeawayOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), OrderType.Takeaway, rs.getTimestamp(6), rs.getBoolean(7));
    }

    public void confirmCollected(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("UPDATE TakeawayOrders SET Collected =1 WHERE OrderId = ?");
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
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

    public Timestamp getPickupTime() {
        return pickupTime;
    }

    public boolean isCollected() {
        return collected;
    }
}