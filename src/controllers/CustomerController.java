package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.*;
import models.MenuItem;
import services.AppState;
import services.DatabaseService;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML private TableView<MenuItem> foodtable;
    @FXML private TableColumn<MenuItem, String> foodname;
    @FXML private TableColumn<MenuItem, Double> foodprice;

    @FXML private TableView<MenuItem> drinktable;
    @FXML private TableColumn<MenuItem, String> drinkname;
    @FXML private TableColumn<MenuItem, Double> drinkprice;

    @FXML private TableView<MenuItem> ordertable;
    @FXML private TableColumn<MenuItem, String> ordereditem;

    @FXML private TableView<Order> orderHistoryTable;
    @FXML private TableColumn<Order, Integer> orderHistoryNo;
    @FXML private TableColumn<Order, Timestamp> orderHistoryDate;
    @FXML private TableColumn<Order, Double> orderHistoryTotal;

    @FXML private TableView<MenuItem> historyItemsTable;
    @FXML private TableColumn<MenuItem, String> historyItemName;
    @FXML private TableColumn<MenuItem, Double> historyItemPrice;

    @FXML private DatePicker dateSelector;
    @FXML private ComboBox timeCombo;
    @FXML private Spinner guestNumberSpinner;
    @FXML private Spinner durationSpinner;
    @FXML private Label bookingStatusLabel;
    private LocalDate dateChosen;
    private int hourChosen;
    private int guestCount;


    @FXML private ComboBox<String> orderType;
    @FXML private ComboBox<String> reorderType;
    @FXML private TextArea orderMessage;
    @FXML private ListView availableSlotsList;

    private ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> orderitems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();
    private ObservableList<String> orderOption = FXCollections.observableArrayList();
    private ObservableList<Order> orderHistory = FXCollections.observableArrayList();
    private ObservableList<MenuItem> orderHistoryItems = FXCollections.observableArrayList();
    private AppState appState = AppState.getAppState();

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/Login_ui.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    public void foodSelect(MouseEvent event){
        MenuItem itemSelected = foodtable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<MenuItem, String>("name")));
        ordertable.setItems(orderitems);
    }

    public void drinkSelect(MouseEvent event){
        MenuItem itemSelected = drinktable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<MenuItem, String>("name")));
        ordertable.setItems(orderitems);
    }

    public void itemRemove(MouseEvent event){
        MenuItem itemSelected = ordertable.getSelectionModel().getSelectedItem();
        orderitems.remove(itemSelected);
        ordertable.setItems(orderitems);
    }

    public void confirmOrder(ActionEvent event){
        int customerId = appState.getUser().getId();
        LocalDateTime ordertime = LocalDateTime.now();
        Timestamp sqlordertime = Timestamp.valueOf(ordertime);
        double sum = 0.0;
        for(MenuItem item: orderitems) {
            sum += item.getPrice();
        }

        if(orderType.getValue() == "Delivery" || orderType.getValue() == "Takeaway"){
            try {
                Connection conn = DatabaseService.getConnection();
                Order.createOrder(conn, sqlordertime, customerId, sum);
                int orderId = DatabaseService.getLastInsert(conn);
                if(orderType.getValue() == "Delivery"){
                    Timestamp sqldeliverytime = DeliveryOrder.estimateDeliveryTime(ordertime);
                    DeliveryOrder.createDeliveryOrder(conn, orderId, sqldeliverytime);
                    orderMessage.setText("Thank you for your order: \nEstimated Delivery time will be " + sqldeliverytime);
                } else {
                    Timestamp sqlpickuptime = TakeawayOrder.estimatePickUpTime(ordertime);
                    TakeawayOrder.createTakeawayOrder(conn, orderId, sqlpickuptime);
                    orderMessage.setText("Thank you for your order: \nEstimated Pickup time will be " + sqlpickuptime);
                }
                for (MenuItem item : orderitems) {
                    MenuItem.createOrderedItem(conn, orderId, item.getId());
                }
                orderitems.clear();

                ordertable.setItems(orderitems);
            } catch(Exception e){
                System.out.println("something has gone wrong");
            }

        }
        else {
        }
    }

    public void confirmReOrder(ActionEvent event){
        int customerId = appState.getUser().getId();
        LocalDateTime ordertime = LocalDateTime.now();
        Timestamp sqlordertime = Timestamp.valueOf(ordertime);
        double sum = 0.0;
        for(MenuItem item: orderHistoryItems) {
            sum += item.getPrice();
        }

        if(reorderType.getValue() == "Delivery" || reorderType.getValue() == "Takeaway"){
            try {
                Connection conn = DatabaseService.getConnection();
                Order.createOrder(conn, sqlordertime, customerId, sum);
                int orderId = DatabaseService.getLastInsert(conn);
                if(reorderType.getValue() == "Delivery"){
                    Timestamp sqldeliverytime = DeliveryOrder.estimateDeliveryTime(ordertime);
                    DeliveryOrder.createDeliveryOrder(conn, orderId, sqldeliverytime);
                    orderMessage.setText("Thank you for your order: \nEstimated Delivery time will be " + sqldeliverytime);
                } else {
                    Timestamp sqlpickuptime = TakeawayOrder.estimatePickUpTime(ordertime);
                    TakeawayOrder.createTakeawayOrder(conn, orderId, sqlpickuptime);
                    orderMessage.setText("Thank you for your order: \nEstimated Pickup time will be " + sqlpickuptime);
                }
                for (MenuItem item : orderHistoryItems) {
                    MenuItem.createOrderedItem(conn, orderId, item.getId());
                }
                orderHistoryItems.clear();

                historyItemsTable.setItems(orderHistoryItems);
            } catch(Exception e){
                System.out.println("something has gone wrong");
            }

        }
        else {
        }
    }

    public void selectHistoricOrder(MouseEvent event){
        Order selectedOrder = orderHistoryTable.getSelectionModel().getSelectedItem();
        //orderHistoryItems.clear();
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = MenuItem.getOrderItems(conn, selectedOrder.getOrderId());
            if(rs.next()) {
                while (rs.next()) {
                    orderHistoryItems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                }
                historyItemName.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
                historyItemPrice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
                historyItemsTable.setItems(orderHistoryItems);
            }
            conn.close();
        } catch (SQLException se){
            System.out.println("help");
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        foodname.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        foodprice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        foodtable.setItems(fooditems);

        drinkname.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        drinkprice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        drinktable.setItems(drinksitems);

        orderHistoryNo.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
        orderHistoryDate.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("orderDate"));
        orderHistoryTotal.setCellValueFactory(new PropertyValueFactory<Order, Double>("orderTotal"));
        orderHistoryTable.setItems(orderHistory);

        orderOption.add("Takeaway");
        orderOption.add("Delivery");
        orderType.setItems(orderOption);
        reorderType.setItems(orderOption);

        initializeBookingTab();

        try {
            Connection conn = DatabaseService.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM MenuItems WHERE ItemType = 'Food'");
            while(rs.next()){
                fooditems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.Food, rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
            }
            rs = st.executeQuery("SELECT * FROM MenuItems WHERE ItemType = 'Drink'");
            while(rs.next()){
                drinksitems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.Drink, rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
            }
            rs = Order.getOrderHistory(conn, appState.getUser().getId());
            while(rs.next()){
                orderHistory.add(Order.createOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5)));
            }
            conn.close();

        }
        catch (SQLException se){
            se.printStackTrace();
            System.out.println(se);
        }

    }

    private void initializeBookingTab() {
        for(int i = Booking.OPENING_TIME; i < Booking.CLOSING_TIME; i++) {
            timeCombo.getItems().add(String.format("%d:00", i));
        }
    }

    public void viewAvailableSlots(ActionEvent e) {
        int startTime = Integer.parseInt(timeCombo.getValue().toString().substring(0,2));
        hourChosen = startTime;
        dateChosen = dateSelector.getValue();
        guestCount = (Integer) guestNumberSpinner.getValue();
        Date date = Date.valueOf(dateChosen);
        try {
            ArrayList<Table> availableTables = Table.getAvailableTables(
                    DatabaseService.getConnection(),
                    date,
                    startTime,
                    (Integer) durationSpinner.getValue(),
                    guestCount
            );
            availableSlotsList.getItems().clear();
            for(Table t : availableTables) {
                availableSlotsList.getItems().add(t);
            }
        } catch(SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void requestBooking() {
        Table selectedTable = (Table) availableSlotsList.getSelectionModel().getSelectedItem();

        if(selectedTable != null) {
            try {
                Booking.createBooking(
                    DatabaseService.getConnection(),
                    selectedTable.getId(),
                    hourChosen,
                    Date.valueOf(dateChosen),
                    appState.getUser().getId(),
                    guestCount
                );
                bookingStatusLabel.setText("Booking requested!");
            } catch (SQLException e) {

            }
        } else {
            bookingStatusLabel.setText("Please select a table.");
        }
    }

}