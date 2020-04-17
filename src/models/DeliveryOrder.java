package models;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DeliveryOrder extends Order {
    private Timestamp estimatedDeliveryTime;
    private boolean confirmed;
    private boolean delivered;
    private int driverID;

    private DeliveryOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, OrderType orderType, boolean confirmed, Timestamp estimatedDeliveryTime, int driverID, boolean delivered) {
        super(orderId, orderDate, customerId, cooked, orderTotal, orderType);
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

    public static boolean isApproved(Connection conn, int orderId) throws SQLException{
        PreparedStatement st = conn.prepareStatement("SELECT Approved FROM DeliveryOrders WHERE OrderId = ?");
        st.setInt(1, orderId);
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            return rs.getBoolean(1);
        }
        else return false;
    }

    public static ResultSet getUndelivered(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, d.Confirmed, d.DeliveryTime, d.DriverId, d.Delivered FROM Orders o, DeliveryOrders d WHERE o.OrderId = d.OrderId AND d.DriverId IS NULL ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    public static ResultSet getUnassigned(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, d.Confirmed, d.DeliveryTime, d.DriverId, d.Delivered FROM Orders o, DeliveryOrders d WHERE o.OrderId = d.OrderId AND o.Cooked = 1 AND d.DriverId IS NULL ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    public static DeliveryOrder orderFromRS(ResultSet rs) throws SQLException{
        return new DeliveryOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), OrderType.Delivery, rs.getBoolean(6), rs.getTimestamp(7), rs.getInt(8), rs.getBoolean(9));
    }

    public void approveDelivery(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrders SET Confirmed = 1 WHERE OrderID = ?");
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
    }

    public String toString() {
        return String.format("%s : %s", getOrderId(), getEstimatedDeliveryTime());
    }

    public Timestamp getEstimatedDeliveryTime() {
        return this.estimatedDeliveryTime;
    }

    public boolean isDelivered() {
        return this.delivered;
    }

    public int getDriverID() {
        return this.driverID;
    }

    public void assignDriver(Connection conn, int driverId) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrders\n" +
                "SET DriverId = ?\n" +
                "WHERE OrderId = ?"
        );
        st.setInt(1, driverId);
        st.setInt(2, this.getOrderId());
        st.executeUpdate();
        this.driverID = driverId;
    }

    public void unassign(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrders\n" +
                "SET DriverId = NULL\n" +
                "WHERE OrderId = ?"
        );
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
        this.driverID = -1;
    }

    public void markDelivered(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrders\n" +
                "SET Delivered = TRUE\n" +
                "WHERE OrderId = ?");
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
        this.delivered = true;
    }
}