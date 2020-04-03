package models;

import java.util.ArrayList;

public class EatInOrder extends Order {
    private int tableId;

    private EatInOrder(int orderId, ArrayList<Integer> items, int customerId, int tableId) {
        super(orderId, items, OrderType.EatIn, customerId);   
        this.tableId = tableId;     
    }

    public int getTable() {
        return this.tableId;
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