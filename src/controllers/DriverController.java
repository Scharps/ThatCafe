/*
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import models.Order;
import models.OrderType;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverController {
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm aa", Locale.getDefault());

    private List<DeliveryOrder> orders = new ArrayList<>();
    private Label[] orderIds;
    private Label[] orderAddress;
    private CheckBox[] orderDelivered;

    @FXML
    GridPane ordersGrid;

    @FXML
    Button logoutButton;


    @FXML
    public void initialize() {
        getAllOrders();

        fillOrdersTable();
    }

    private void fillOrdersTable() {

        orderIds = new Label[orders.size()];
        orderAddress = new Label[orders.size()];
        orderDelivered = new CheckBox[orders.size()];

        ordersGrid.getRowConstraints().clear();
        for (int i = 0; i < orders.size(); i++) {
            orderIds[i] = new Label(orders.get(i).getOrderId() + "");
            orderAddress[i] = new Label(orders.get(i).getAddress() + "");
            orderDelivered[i] = new CheckBox((orders.get(i).isDelivered() ? "Delivered" : "Not"));
            orderDelivered[i].setSelected(orders.get(i).isDelivered());
            orderDelivered[i].setDisable(orders.get(i).isDelivered());

            RowConstraints con = new RowConstraints();
            con.setMinHeight(25);

            ordersGrid.getRowConstraints().add(con);
            ordersGrid.add(orderIds[i], 0, i);
            ordersGrid.add(orderAddress[i], 1, i);
            ordersGrid.add(orderDelivered[i], 2, i);
            orderDelivered[i].setId("" + i);

            orderDelivered[i].setOnAction(e -> {
                markAction(e);
            });
        }
    }

    private void markAction(ActionEvent e) {
        CheckBox box = (CheckBox) e.getSource();
        int row = Integer.parseInt(box.getId());
        if (box.isSelected()) {
            orders.get(row).markDelivered();
            box.setText("Delivered");
            box.setDisable(true);
        }
        setDeliveredAction(row);
    }


    //// ------------------ STUB methods --------------------
    private void getAllOrders() {
        for (int i = 0; i < 25; i++) {
            DeliveryOrder order = DeliveryOrder.createDeliveryOrder(new ArrayList<Integer>(), i + 1, new Date(377733333),
                    Address.createAddress("Flowers Ave", "Old York", "000000"));
            orders.add(order);
        }
    }

    private void setDeliveredAction(int row) {
        //TODO
    }

    @FXML
    public void logoutAction() {
        ((Stage) logoutButton.getScene().getWindow()).close();
    }



    //////////////////remove in release/////////////////////
    private static class DeliveryOrder extends Order {
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
            delivered = true;
            return true;
        }

        public static DeliveryOrder createDeliveryOrder(ArrayList<Integer> items, int customerId, Date estimatedDeliveryTime, Address address) {
            return new DeliveryOrder(3, items, customerId, estimatedDeliveryTime, address);
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

    private static class Address {
        private int id;
        private String firstLine;
        private String city;
        private String postCode;

        private Address(int id, String firstLine, String city, String postCode){
            this.id = id;
            this.firstLine = firstLine;
            this.city = city;
            this.postCode = postCode;
        };

        public int getId() {
            return this.id;
        }

        public String getFirstLine() {
            return this.firstLine;
        }

        public String getCity() {
            return this.city;
        }

        public String getPostCode() {
            return this.postCode;
        }

        public static Address createAddress(String firstLine, String city, String postCode) {
            return new Address(3, firstLine, city, postCode);
        }

        public static Address getAddress(int id) {
            throw new UnsupportedOperationException("getAddress() is not yet implemented");
        }

        public static boolean deleteAddress(int id) {
            throw new UnsupportedOperationException("deleteAddress() is not yet implemented");
        }

        public static boolean updateAddress(int id, String firstLine, String city, String postCode) {
            throw new UnsupportedOperationException("updateAddress() is not yet implemented");
        }

        @Override
        public String toString() {
            return firstLine + ", " + city + ", " + postCode;
        }
    }


}

 */