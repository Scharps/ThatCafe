package models;

import java.sql.Date;
import java.util.ArrayList;

public class TakeawayOrder extends Order {
    private Date pickupTime;

    private TakeawayOrder(int orderId, ArrayList<Integer> items, int customerId, Date pickupTime) {
        super(orderId, items, OrderType.Takeaway, customerId);
        this.pickupTime = pickupTime;
    }

    public Date getPickUpTime() {
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