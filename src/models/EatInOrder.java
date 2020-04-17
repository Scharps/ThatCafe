package models;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * This represents an eat-in order which is made by a customer.
 * @author Ashley Forster
 */
public class EatInOrder extends Order {
    private final int tableId;

    private EatInOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, int tableId, boolean served) {
        super(orderId, orderDate, customerId, cooked, orderTotal, OrderType.EatIn);
        this.tableId = tableId;
        boolean served1 = served;
    }

    public int getTable() {
        return this.tableId;
    }

    public static void createEatInOrder(Connection conn, int orderId, int tableId){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT INTO EatinOrders (OrderId, TableId) VALUES (?,?)");
            st.setInt(1, orderId);
            st.setInt(2, tableId);
            st.executeUpdate();
        } catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ResultSet getUnServed(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, e.TableId, e.Served FROM Orders o, EatinOrders e WHERE e.Served = 0 ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    public static EatInOrder orderFromRS(ResultSet rs) throws SQLException{
        return new EatInOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), rs.getInt(6), rs.getBoolean(7));
    }

    public static void confirmedServed(Connection conn, int orderId) throws SQLException{
        PreparedStatement st = conn.prepareStatement("UPDATE EatinOrders SET Served = 1 WHERE OrderId = ?");
        st.setInt(1, orderId);
        st.executeUpdate();
    }

    public static EatInOrder createEatInOrder(ArrayList<Integer> items, int customerId, int tableId) {
        throw new UnsupportedOperationException("createEatInOrder() is not yet implemented");
    }

    public static EatInOrder getEatInOrder(int id) {
        throw new UnsupportedOperationException("getEatInOrder() is not yet implemented");
    }

    public static boolean deleteEatInOrder(int id) {
        throw new UnsupportedOperationException("deleteEatInOrder() is not yet implemented");
    }

    public static boolean updateEatInOrder(int id, ArrayList<Integer> items, int customerId, int tableId) {
        throw new UnsupportedOperationException("updateEatInOrder() is not yet implemented");
    }
}