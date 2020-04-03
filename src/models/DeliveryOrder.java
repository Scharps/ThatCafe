package models;

import java.sql.Date;
import java.util.ArrayList;

public class DeliveryOrder extends Order {
    private Date estimatedDeliveryTime;
    private boolean approved = false;
    private boolean delivered = false;
    private Address address;

    private DeliveryOrder(int orderId, ArrayList<Integer> items, int customerId, Date estimatedDeliveryTime, Address address) {
        super(orderId, items, OrderType.Delivery, customerId);
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.address = address;
    }

    public Date getEstimatedDeliveryTime() {
        return this.estimatedDeliveryTime;
    }

    public boolean isApproved() {
        return this.approved;
    }

    public boolean isDelivered() {
        return this.delivered;
    }

    public Address getAddress() {
        return this.address;
    }

    public boolean markApproved() {
        throw new UnsupportedOperationException("markApproved() not yet implemented");
    }

    public boolean markDelivered() {
        throw new UnsupportedOperationException("markDelivered() not yet implemented");
    }

    public static DeliveryOrder createDeliveryOrder(ArrayList<Integer> items, int customerId, Date estimatedDeliveryTime, Address address) {
        throw new UnsupportedOperationException("createDeliveryOrder() is not yet implemented");
    }

    public static DeliveryOrder getDeliveryOrder(int id) {
        throw new UnsupportedOperationException("getDeliveryOrder() is not yet implemented");
    }

    public static boolean deleteDeliveryOrder(int id) {
        throw new UnsupportedOperationException("deleteDeliveryOrder() is not yet implemented");
    }

    public static boolean updateDeliveryOrder(int id, ArrayList<Integer> items, int customerId, Date estimatedDeliveryTime, Address address) {
        throw new UnsupportedOperationException("updateDeliveryOrder() is not yet implemented");
    }
}