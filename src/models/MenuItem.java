package models;

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