package models;

import java.util.ArrayList;

public abstract class Order {
    private int orderId;
    private ArrayList<Integer> items;
    private OrderType orderType;
    private int customerId;
    private boolean completed = false;

    protected Order(int orderId, ArrayList<Integer> items, OrderType orderType, int customerId) {
        this.orderId = orderId;
        this.items = items;
        this.orderType = orderType;
        this.customerId = customerId;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public ArrayList<Integer> getItems() {
        return this.items;
    }

    public OrderType getOrderType() {
        return this.orderType;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public boolean markCompleted() {
        throw new UnsupportedOperationException("markCompleted() is not yet implemented");
    }
}