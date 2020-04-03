package models;

import java.lang.UnsupportedOperationException;

public class Table {
    private int id;
    private int capacity;

    private Table(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public int getId() {
        return this.id;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public static Table createTable(int capacity) {
        throw new UnsupportedOperationException("createTable() is not yet implemented");
    }

    public static Table getTable(int id) {
        throw new UnsupportedOperationException("getTable() is not yet implemented");
    }

    public static boolean deleteTable(int id) {
        throw new UnsupportedOperationException("deleteTable() is not yet implemented");
    }

    public static boolean updateTable(int id, int capacity) {
        throw new UnsupportedOperationException("updateTable() is not yet implemented");
    }
}