package models;

import javafx.beans.binding.BooleanBinding;

import java.sql.*;
import java.util.ArrayList;

public class EatInOrder extends Order {
    private int tableId;
    private boolean served;

    private EatInOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, OrderType orderType, int tableId, boolean served) {
        super(orderId, orderDate, customerId, cooked, orderTotal, orderType);
        this.tableId = tableId;
        this.served = served;
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
        }
    }

    public static ResultSet getUnServed(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, e.TableId, e.Served FROM Orders o, EatinOrders e WHERE e.Served = 0 ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    public static EatInOrder orderFromRS(ResultSet rs) throws SQLException{
        return new EatInOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), OrderType.EatIn, rs.getInt(6), rs.getBoolean(7));
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