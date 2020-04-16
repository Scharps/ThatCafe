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

    public static DeliveryOrder orderFromRS(ResultSet rs) throws SQLException{
        return new DeliveryOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), OrderType.Delivery, rs.getBoolean(6), rs.getTimestamp(7), rs.getInt(8), rs.getBoolean(9));
    }

    public void approveDelivery(Connection conn) throws SQLException{
        PreparedStatement st = conn.prepareStatement("UPDATE DeliveryOrders SET Confirmed = 1 WHERE OrderID = ?");
        st.setInt(1, this.getOrderId());
        st.executeUpdate();
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