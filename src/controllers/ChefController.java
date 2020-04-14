/*
package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.MenuItemType;
import models.Order;
import models.OrderType;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChefController {
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm aa", Locale.getDefault());

    private List<Order> allOrders = new ArrayList<>();
    private List<MenuItem> orderItems = new ArrayList<>();
    private Label[] orderIds;
    private Label[] orderTypes;
    private Label[] orderCompletes;

    private Label[] itemNames;
    private Label[] itemAmount;
    private CheckBox[] itemCheck;

    private Integer currentOrderNo = null;


    @FXML
    GridPane ordersGrid;

    @FXML
    GridPane itemsGrid;

    @FXML
    Button logoutButton;


    @FXML
    public void initialize() {
        getAllOrders();

        fillOrdersTable();
    }

    private void fillOrdersTable() {

        orderIds = new Label[allOrders.size()];
        orderTypes = new Label[allOrders.size()];
        orderCompletes = new Label[allOrders.size()];

        ordersGrid.getRowConstraints().clear();
        //System.out.println(allOrders.size());
        for (int i = 0; i < allOrders.size(); i++) {
            orderIds[i] = new Label(allOrders.get(i).getOrderId() + "");
            orderTypes[i] = new Label(allOrders.get(i).getOrderType() + "");
            orderCompletes[i] = new Label((allOrders.get(i).isCompleted() ? "Completed" : "Not"));


            RowConstraints con = new RowConstraints();
            con.setMinHeight(25);
            ordersGrid.getRowConstraints().add(con);
            ordersGrid.add(orderIds[i], 0, i);
            ordersGrid.add(orderTypes[i], 1, i);
            ordersGrid.add(orderCompletes[i], 2, i);

            orderIds[i].setOnMouseClicked(e -> mouseListener(e));
            orderTypes[i].setOnMouseClicked(e -> mouseListener(e));
            orderCompletes[i].setOnMouseClicked(e -> mouseListener(e));
        }
    }

    private void fillItemsTable(int row) {
        Order order = allOrders.get(row);
        orderItems.clear();
        for (int index = 0; index < order.getItems().size(); index++) {
            MenuItem item = getItemById(index);
            orderItems.add(item);
        }

        itemNames = new Label[orderItems.size()];
        itemAmount = new Label[orderItems.size()];
        itemCheck = new CheckBox[orderItems.size()];

        itemsGrid.getRowConstraints().clear();
        itemsGrid.getChildren().clear();
        for (int i = 0; i < orderItems.size(); i++) {
            itemNames[i] = new Label(orderItems.get(i).getName() + "");
            itemAmount[i] = new Label(orderItems.get(i).getNumberSold() + "");
            itemCheck[i] = new CheckBox("Done");

            RowConstraints con = new RowConstraints();
            con.setMinHeight(25);
            itemsGrid.getRowConstraints().add(con);
            itemsGrid.add(itemNames[i], 0, i);
            itemsGrid.add(itemAmount[i], 1, i);
            itemsGrid.add(itemCheck[i], 2, i);

            itemCheck[i].setOnAction(e -> itemCheckAction());
        }
    }

    private void mouseListener(MouseEvent e) {
        int row = GridPane.getRowIndex((Node) e.getSource());
        fillItemsTable(row);
        currentOrderNo = row;
    }

    private void itemCheckAction() {
        //TODO or not to do
    }

    @FXML
    public void logoutAction() {
        ((Stage) logoutButton.getScene().getWindow()).close();
    }


    @FXML
    private void markAction() {
        allOrders.get(currentOrderNo).markCompleted();
        orderCompletes[currentOrderNo].setText("Completed");
        currentOrderNo = null;
        //itemsGrid.getRowConstraints().clear();
        itemsGrid.getChildren().clear();
    }


    //// ------------------ STUB methods --------------------
    private void getAllOrders() {
        for (int i = 0; i < 25; i++) {
            TestOrder order;
            if (i % 2 == 0) {
                order = new TestOrder(i + 1, new ArrayList<>(), OrderType.EatIn, 100 + i);
            } else {
                order = new TestOrder(i + 1, new ArrayList<>(), OrderType.Delivery, 100 + i);
            }
            if (i % 3 == 0) {
                order.markCompleted();
            }

            for (int j = 0; j < 5; j++) {
                order.getItems().add(10 + j);
            }
            allOrders.add(order);
        }
    }

    private MenuItem getItemById(int id) {
        //now random
        String[] names = new String[] {"Steak", "Fish", "Chips", "Milk", "Cola", "Beer", "Vegetables"};
        MenuItemType[] types = new MenuItemType[] {MenuItemType.Food, MenuItemType.Food, MenuItemType.Food, MenuItemType.Drink, MenuItemType.Drink, MenuItemType.Drink, MenuItemType.Food};
        int r = new Random().nextInt(names.length);
        return MenuItem.createMenuItem(names[r], "Description", types[r], 10, r);
    }


    //////////////////remove in release/////////////////////
    private class TestOrder extends Order {
        private boolean completed;

        public TestOrder(int orderId, ArrayList<Integer> items, OrderType orderType, int customerId) {
            super(orderId, items, orderType, customerId);
        }

        @Override
        public boolean markCompleted() {
            completed = true;
            return true;
        }

        @Override
        public boolean isCompleted() {
            return completed;
        }
    }


    //////////////////remove in release/////////////////////
    private static class MenuItem {
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
            return new MenuItem(2, name, description, itemType, price, numberSold);
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
}
*/