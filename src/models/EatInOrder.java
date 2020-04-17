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

    /**
     * Gets the Table of the EatInOrder
     * @return Table of Order
     */
    public int getTable() {
        return this.tableId;
    }

    /**
     * Creates an EatInOrder in the database
     * @param conn Database Connection
     * @param orderId OrderID
     * @param tableId TableID
     */
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

    /**
     * Gets all unserved EatInOrders
     * @param conn Database connection
     * @return ResultSet of Unserved Orders
     * @throws SQLException
     */
    public static ResultSet getUnServed(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT o.OrderId, o.OrderDate, o.CustomerId, o.Cooked, o.OrderTotal, e.TableId, e.Served FROM Orders o, EatinOrders e WHERE e.Served = 0 ");
        ResultSet rs = st.executeQuery();
        return rs;
    }

    /**
     * Creates an EatInOrder from a ResultSet
     * @param rs ResultSet
     * @return EatInOrder
     * @throws SQLException
     */
    public static EatInOrder orderFromRS(ResultSet rs) throws SQLException{
        return new EatInOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), rs.getInt(6), rs.getBoolean(7));
    }

    /**
     * Confirms that an EatInOrder is served
     * @param conn Database connection
     * @param orderId The ID of the Order that is served
     * @throws SQLException
     */
    public static void confirmedServed(Connection conn, int orderId) throws SQLException{
        PreparedStatement st = conn.prepareStatement("UPDATE EatinOrders SET Served = 1 WHERE OrderId = ?");
        st.setInt(1, orderId);
        st.executeUpdate();
    }
}