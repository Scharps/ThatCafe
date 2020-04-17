package models;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This represents a takeaway order which is made by a customer.
 * @author Ashley Forster
 */
public class TakeawayOrder extends Order {
    private final Timestamp pickupTime;
    private final boolean collected;

    private TakeawayOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, Timestamp pickupTime, boolean collected) {
        super(orderId, orderDate, customerId, cooked, orderTotal, OrderType.Takeaway);
        this.pickupTime = pickupTime;
        this.collected = collected;
    }

    /**
     * Returns the estimated pickup time based on time ordered
     * @param orderTime Time ordered
     * @return Estimated pickup time
     */
    public static Timestamp estimatePickUpTime(LocalDateTime orderTime){
        orderTime = orderTime.withSecond(0).withNano(0);
        return Timestamp.valueOf(orderTime.plusMinutes(20));
    }

    /**
     * Creates a takeaway order in the database
     * @param conn Database connection
     * @param orderId OrderId
     * @param pickupTime PickupTime
     */
    public static void createTakeawayOrder(Connection conn, int orderId, Timestamp pickupTime) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO TakeawayOrders (OrderId, PickUpTime) VALUES (?,?)");
            st.setInt(1, orderId);
            st.setTimestamp(2, pickupTime);
            st.executeUpdate();
        } catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gets a ResultSet containing all uncollected TakeawayOrders
     * @param conn Database connection
     * @return ResultSet containing all uncollected TakeawayOrders
     * @throws SQLException
     */
    public static ResultSet getUncollected(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, t.PickUpTime, t.Collected FROM Orders o, TakeawayOrders t WHERE o.OrderId = t.OrderId AND t.Collected = 0 ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    /**
     * Creates a TakeawayOrder from a ResultSet
     * @param rs ResultSet
     * @return TakeawayOrder
     * @throws SQLException
     */
    public static TakeawayOrder orderFromRS(ResultSet rs) throws SQLException{
        return new TakeawayOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), rs.getTimestamp(6), rs.getBoolean(7));
    }

    /**
     * Confirms an order as collected in a database.
     * @param conn Database connection
     * @throws SQLException
     */
    public void confirmCollected(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("UPDATE TakeawayOrders SET Collected =1 WHERE OrderId = ?");
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
    }

    /**
     * Gets the pickup time of a Takeaway
     * @return Pickup time.
     */
    public Timestamp getPickupTime() {
        return pickupTime;
    }
}