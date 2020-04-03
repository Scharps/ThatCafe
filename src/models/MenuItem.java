package models;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private MenuItemType itemType;
    private double price; 
    private int numberSold;

    private MenuItem(int id, String name, String description, MenuItemType itemType, double price, int numberSold) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.itemType = itemType;
        this.price = price;
        this.numberSold = numberSold;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
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

    public static MenuItem createMenuItem(String name, String description, MenuItemType itemType, double price, int numberSold) {
        throw new UnsupportedOperationException("createMenuItem() is not yet implemented");
    }

    public static MenuItem getMenuItem(int id) {
        throw new UnsupportedOperationException("getMenuItem() is not yet implemented");
    }

    public static boolean deleteMenuItem(int id) {
        throw new UnsupportedOperationException("deleteMenuItem() is not yet implemented");
    }

    public static boolean updateMenuItem(int id, String name, String description, MenuItemType itemType, double price, int numberSold) {
        throw new UnsupportedOperationException("updateMenuItem() is not yet implemented");
    }
}