package models;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This represents a structure of a menu item.
 * @author Ashley Forster
 */
public class MenuItem {
    private final int id;
    private final String name;
    private final MenuItemType itemType;
    private final double price;
    private final int numberSold;

    private MenuItem(int id, String name, MenuItemType itemType, double price, int numberSold, boolean special) {
        this.id = id;
        this.name = name;
        this.itemType = itemType;
        this.price = price;
        this.numberSold = numberSold;
        Boolean special1 = special;
    }

    public static void createNewItem(Connection conn, String name, String itemType, double price, boolean special){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT INTO MenuItems (ItemName, ItemType, Price, Special) VALUES (?,?,?,?)");
            st.setString(1, name);
            st.setString(2, itemType);
            st.setDouble(3, price);
            st.setBoolean(4, special);
            st.executeUpdate();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void createOrderedItem(Connection conn, int orderId, int itemId){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO OrderedItems (OrderId, ItemId, Quantity) VALUES (?, ?, 1)");
            st.setInt(1, orderId);
            st.setInt(2, itemId);
            st.executeUpdate();
        } catch(SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ResultSet getOrderItems(Connection conn, int orderId){
        try{
            PreparedStatement st = conn.prepareStatement("SELECT m.ItemId, m.ItemName, m.ItemType, m.Price, m.Sold, m.Special FROM OrderedItems o, MenuItems m WHERE o.OrderId=? AND m.ItemId=o.ItemId");
            st.setInt(1,orderId);
            ResultSet rs = st.executeQuery();
            return rs;
        }catch (SQLException se){
            return null;
        }
    }

    public static ResultSet getMenuItems(Connection conn, boolean special){
        try{
            PreparedStatement st = conn.prepareStatement("SELECT * FROM MenuItems WHERE Special = ?");
            st.setBoolean(1, special);
            ResultSet rs = st.executeQuery();
            return rs;
        }catch (SQLException se){
            return null;
        }
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public MenuItemType getMenuItemType() {
        return this.itemType;
    }
    
    public double getPrice() {
        return this.price;
    }

    public int getNumberSold() {
        return this.numberSold;
    }

    public static MenuItem createMenuItem(int id, String name, MenuItemType itemType, double price, int numberSold, boolean special) {
        return new MenuItem(id, name, itemType, price, numberSold, special);
        //throw new UnsupportedOperationException("createMenuItem() is not yet implemented");
    }

    public static void deleteMenuItem(Connection conn, int id) {
        try{
            PreparedStatement st = conn.prepareStatement("DELETE FROM MenuItems WHERE ItemId = ?");
            st.setInt(1, id);
            st.executeUpdate();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    public static MenuItem getMenuItem(int id) {
        throw new UnsupportedOperationException("getMenuItem() is not yet implemented");
    }

    public static ArrayList<MenuItem> getTopMostSold(Connection conn, int top) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT * FROM MenuItems ORDER BY Sold DESC");
        ResultSet rs = st.executeQuery();
        int i = 0;
        ArrayList<MenuItem> mostSold = new ArrayList<>();
        while(rs.next() && i < top) {
            mostSold.add(menuItemFromResultSet(rs));
            ++i;
        }
        return mostSold;
    }

    private static MenuItem menuItemFromResultSet(ResultSet rs) throws SQLException{
        return new MenuItem(
            rs.getInt("ItemId"),
            rs.getString("ItemName"),
            MenuItemType.valueOf(rs.getString("ItemType")),
            rs.getDouble("Price"),
            rs.getInt("Sold"),
            rs.getBoolean("Special")
        );
    }

    @Override
    public String toString() {
        return String.format("Name: %s\t\tAmount Sold: %s", name, numberSold);
    }

    public static boolean updateMenuItem(int id, String name, String description, MenuItemType itemType, double price, int numberSold) {
        throw new UnsupportedOperationException("updateMenuItem() is not yet implemented");
    }
}