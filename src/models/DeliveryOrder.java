package models;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * This represents a delivery order which is made by a customer.
 * @author Ashley Forster
 */
public class DeliveryOrder extends Order {
    private final Timestamp estimatedDeliveryTime;
    private boolean delivered;
    private int driverID;

    private DeliveryOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, boolean confirmed, Timestamp estimatedDeliveryTime, int driverID, boolean delivered) {
        super(orderId, orderDate, customerId, cooked, orderTotal, OrderType.Delivery);
        boolean confirmed1 = confirmed;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.driverID = driverID;
        this.delivered = delivered;
    }

    /**
     * Returns the estimated delivery time based on time ordered
     * @param orderTime Time ordered
     * @return Estimated delivery time
     */
    public static Timestamp estimateDeliveryTime(LocalDateTime orderTime){
        orderTime = orderTime.withSecond(0).withNano(0);
        return Timestamp.valueOf(orderTime.plusHours(1));
    }

    /**
     * Creates a delivery order in the database.
     * @param conn Database connection
     * @param orderId Corresponding order ID
     * @param estimatedDeliveryTime Estimated delivery time
     */
    public static void createDeliveryOrder(Connection conn, int orderId, Timestamp estimatedDeliveryTime) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO DeliveryOrders (OrderId, DeliveryTime) VALUES (?,?)");
            st.setInt(1, orderId);
            st.setTimestamp(2, estimatedDeliveryTime);
            st.executeUpdate();
        } catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gets the approval status of a Delivery Order from ID
     * @param conn Database connection
     * @param orderId Order ID
     * @return Boolean that represents whether or not the order is approved.
     * @throws SQLException
     */
    public static boolean isApproved(Connection conn, int orderId) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT Approved FROM DeliveryOrders WHERE OrderId = ?");
        st.setInt(1, orderId);
        ResultSet rs = st.executeQuery();
        return rs.next() && rs.getBoolean(1);
    }

    /**
     * Gets a result set of all the undelivered orders
     * @param conn Database connection
     * @return ResultSet of undelivered orders
     * @throws SQLException
     */
    public static ResultSet getUndelivered(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, d.Confirmed, d.DeliveryTime, d.DriverId, d.Delivered FROM Orders o, DeliveryOrders d WHERE o.OrderId = d.OrderId AND d.DriverId IS NULL ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    /**
     * Gets a result set of all unassigned delivery orders
     * @param conn Database connection
     * @return ResultSet of unassigned orders
     * @throws SQLException
     */
    public static ResultSet getUnassigned(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, d.Confirmed, d.DeliveryTime, d.DriverId, d.Delivered FROM Orders o, DeliveryOrders d WHERE o.OrderId = d.OrderId AND o.Cooked = 1 AND d.DriverId IS NULL ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    /**
     * Creates a DeliveryOrder from a result set
     * @param rs ResultSet
     * @return DeliveryOrder
     * @throws SQLException
     */
    public static DeliveryOrder orderFromRS(ResultSet rs) throws SQLException{
        return new DeliveryOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), rs.getBoolean(6), rs.getTimestamp(7), rs.getInt(8), rs.getBoolean(9));
    }

    /**
     * Marks a delivery as approved in the database.
     * @param conn Database Connection
     * @throws SQLException
     */
    public void approveDelivery(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrders SET Confirmed = 1 WHERE OrderID = ?");
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
    }

    public String toString() {
        return String.format("%s : %s", getOrderId(), getEstimatedDeliveryTime());
    }

    /**
     * Gets the estimated delivery time
     * @return Estimated delivery time
     */
    public Timestamp getEstimatedDeliveryTime() {
        return this.estimatedDeliveryTime;
    }

    /**
     * Assigns a driver in the database to the delivery order
     * @param conn Database connection
     * @param driverId Driver ID
     * @throws SQLException
     */
    public void assignDriver(Connection conn, int driverId) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrder\n" +
                "SET DriverId = ?\n" +
                "WHERE OrderId = ?"
        );
        st.setInt(1, driverId);
        st.setInt(2, this.getOrderId());
        st.executeUpdate();
        this.driverID = driverId;
    }


    /**
     * Unassigns a driver to the order
     * @param conn Database connection
     * @throws SQLException
     */
    public void unassign(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrder\n" +
                "SET DriverId = NULL\n" +
                "WHERE OrderId = ?"
        );
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
    }

    /**
     * Marks a DeliveryOrder has delivered
     * @param conn Database connection
     * @throws SQLException
     */
    public void markDelivered(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrder\n" +
                "SET Delivered = TRUE\n" +
                "WHERE OrderId = ?");
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
        this.delivered = true;
    }
}