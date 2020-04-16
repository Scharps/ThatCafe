package models;

import java.sql.*;
import java.util.ArrayList;

public class EatInOrder extends Order {
    private int tableId;

    private EatInOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, OrderType orderType, int tableId) {
        super(orderId, orderDate, customerId, cooked, orderTotal, orderType);
        this.tableId = tableId;     
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