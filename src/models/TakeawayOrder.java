package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class TakeawayOrder extends Order {
    private Timestamp pickupTime;
    private boolean collected;

    private TakeawayOrder(int orderId, Timestamp orderDate, int customerId, boolean cooked, double orderTotal, Timestamp pickupTime, boolean collected) {
        super(orderId, orderDate, customerId, cooked, orderTotal);
        this.pickupTime = pickupTime;
        this.collected = collected;
    }

    public Timestamp getPickUpTime() {
        return this.pickupTime;
    }

    public static TakeawayOrder createTakeawayOrder(ArrayList<Integer> items, int customerId, Date pickupTime) {
        throw new UnsupportedOperationException("createTakeawayOrder() is not yet implemented");
    }

    public static TakeawayOrder getTakeawayOrder(int id) {
        throw new UnsupportedOperationException("getTakeawayOrder() is not yet implemented");
    }

    public static boolean deleteTakeawayOrder(int id) {
        throw new UnsupportedOperationException("deleteTakeawayOrder() is not yet implemented");
    }

    public static boolean updateTakeawayOrder(int id, ArrayList<Integer> items, int customerId, Date pickupTime) {
        throw new UnsupportedOperationException("updateTakeawayOrder() is not yet implemented");
    }
}