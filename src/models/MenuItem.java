package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuItem {
    private int id;
    private String name;
    private MenuItemType itemType;
    private double price; 
    private int numberSold;
    private Boolean special;

    private MenuItem(int id, String name, MenuItemType itemType, double price, int numberSold, boolean special) {
        this.id = id;
        this.name = name;
        this.itemType = itemType;
        this.price = price;
        this.numberSold = numberSold;
        this.special = special;
    }

    public static void createNewItem(Connection conn, String name, String itemType, double price, boolean special){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT INTO MenuItems (ItemName, ItemType, Price, SpecialSpecial) VALUES (?,?,?,?)");
            st.setString(1, name);
            st.setString(2, itemType);
            st.setDouble(3, price);
            st.setBoolean(4, special);
            st.executeUpdate();
        }catch (SQLException se){
        }
    }

    public static void createOrderedItem(Connection conn, int orderId, int itemId){
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO OrderedItems (OrderId, ItemId, Quantity) VALUES (?, ?, 1)");
            st.setInt(1, orderId);
            st.setInt(2, itemId);
            st.executeUpdate();
        } catch(SQLException se){
        }
    }

    public static ResultSet getOrderItems(Connection conn, int orderId){
        try{
            PreparedStatement st = conn.prepareStatement("SELECT m.ItemId, m.ItemName, m.ItemType, m.Price, m.Sold, m.SpecialSpecial FROM OrderedItems o, MenuItems m WHERE o.OrderId=? AND m.ItemId=o.ItemId");
            st.setInt(1,orderId);
            ResultSet rs = st.executeQuery();
            return rs;
        }catch (SQLException se){
            return null;
        }
    }

    public static ResultSet getMenuItems(Connection conn, boolean special){
        try{
            PreparedStatement st = conn.prepareStatement("SELECT * FROM MenuItems WHERE SpecialSpecial = ?");
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

        }

    }

    public static MenuItem getMenuItem(int id) {
        throw new UnsupportedOperationException("getMenuItem() is not yet implemented");
    }



    public static boolean updateMenuItem(int id, String name, String description, MenuItemType itemType, double price, int numberSold) {
        throw new UnsupportedOperationException("updateMenuItem() is not yet implemented");
    }
}