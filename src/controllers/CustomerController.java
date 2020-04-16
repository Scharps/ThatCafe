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
    @FXML private TableView<MenuItem> foodTable;
    @FXML private TableColumn<MenuItem, String> foodName;
    @FXML private TableColumn<MenuItem, Double> foodPrice;

    @FXML private TableView<MenuItem> drinkTable;
    @FXML private TableColumn<MenuItem, String> drinkName;
    @FXML private TableColumn<MenuItem, Double> drinkPrice;

    @FXML private TableView<MenuItem> specialsTable;
    @FXML private TableColumn<MenuItem, String> specialsName;
    @FXML private TableColumn<MenuItem, Double> specialsPrice;

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

    @FXML private ListView myBookingsList;
    @FXML private Label myBookingsStatus;
    ArrayList<Booking> customerBookings;


    @FXML private ComboBox<String> orderType;
    @FXML private ComboBox<String> reorderType;
    @FXML private TextArea orderMessage;
    @FXML private ListView availableSlotsList;

    @FXML private Label username;
    @FXML private Label firstName;
    @FXML private Label lastName;
    @FXML private Label addressLine1;
    @FXML private Label city;
    @FXML private Label postCode;

    @FXML private PasswordField currentPassword;
    @FXML private PasswordField newPassword;
    @FXML private PasswordField confirmNewPassword;
    @FXML private TextField newLine1;
    @FXML private TextField newCity;
    @FXML private TextField newPostCode;

    private final ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> orderitems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> specialitems = FXCollections.observableArrayList();
    private final ObservableList<String> orderOption = FXCollections.observableArrayList();
    private final ObservableList<Order> orderHistory = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> orderHistoryItems = FXCollections.observableArrayList();

    private AppState appState = AppState.getAppState();
    private Customer currentCustomer = appState.getCustomer();

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/Login_ui.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    public void changePassword(ActionEvent event){
        if(currentPassword.getText().equals("") || newPassword.getText().equals("") || confirmNewPassword.getText().equals("")){
        } else if(!newPassword.getText().equals(confirmNewPassword.getText())){
        } else{
            try{
                Connection conn = DatabaseService.getConnection();
                if(DatabaseService.confirmPassword(conn, currentPassword.getText(), currentCustomer.getId())){
                    DatabaseService.updateCustomerPassword(conn, newPassword.getText(), currentCustomer.getId());
                }
                conn.close();
                currentPassword.clear();
                newPassword.clear();
                confirmNewPassword.clear();
            } catch (Exception se){

            }
        }
    }

    public void changeAddress(ActionEvent event){
        if(newLine1.getText().equals("") || newCity.getText().equals("") || newPostCode.getText().equals("")){
        } else {
            try{
                Connection conn = DatabaseService.getConnection();
                Address.updateAddress(conn, currentCustomer.getAddressId(conn), newLine1.getText(), newCity.getText(), newPostCode.getText());
                conn.close();
                newLine1.clear();
                newCity.clear();
                newPostCode.clear();
                initialiseDetails();
            }catch (SQLException se){
            }
        }
    }

    public void foodSelect(MouseEvent event){
        MenuItem itemSelected = foodTable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<MenuItem, String>("name")));
        ordertable.setItems(orderitems);
    }

    public void drinkSelect(MouseEvent event){
        MenuItem itemSelected = drinkTable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<MenuItem, String>("name")));
        ordertable.setItems(orderitems);
    }

    public void specialSelect(MouseEvent event){
        MenuItem itemSelected = specialsTable.getSelectionModel().getSelectedItem();
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
        int customerId = currentCustomer.getId();
        LocalDateTime ordertime = LocalDateTime.now();
        Timestamp sqlordertime = Timestamp.valueOf(ordertime);
        double sum = 0.0;
        for(MenuItem item: orderitems) {
            sum += item.getPrice();
        }

        if(orderType.getValue().equals("Delivery") || orderType.getValue().equals("Takeaway")){
            try {
                Connection conn = DatabaseService.getConnection();
                Order.createOrder(conn, sqlordertime, customerId, sum, OrderType.valueOf(orderType.getValue()));
                int orderId = DatabaseService.getLastInsert(conn);
                if(orderType.getValue().equals("Delivery")){
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
        int customerId = currentCustomer.getId();
        LocalDateTime ordertime = LocalDateTime.now();
        Timestamp sqlordertime = Timestamp.valueOf(ordertime);
        double sum = 0.0;
        for(MenuItem item: orderHistoryItems) {
            sum += item.getPrice();
        }

        if(reorderType.getValue() == "Delivery" || reorderType.getValue() == "Takeaway"){
            try {
                Connection conn = DatabaseService.getConnection();
                Order.createOrder(conn, sqlordertime, customerId, sum , OrderType.valueOf(reorderType.getValue()));
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
        orderHistoryItems.clear();
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
        orderHistoryNo.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
        orderHistoryDate.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("orderDate"));
        orderHistoryTotal.setCellValueFactory(new PropertyValueFactory<Order, Double>("orderTotal"));
        orderHistoryTable.setItems(orderHistory);

        orderOption.add("Takeaway");
        orderOption.add("Delivery");
        orderType.setItems(orderOption);
        reorderType.setItems(orderOption);

        initialiseDetails();
        initializeBookingTab();
        initialiseMenu();

        try {
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Order.getOrderHistory(conn, currentCustomer.getId());
            while(rs.next()){
                orderHistory.add(Order.createOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), OrderType.valueOf(rs.getString(6))));
            }
            conn.close();

        }
        catch (SQLException se){
            se.printStackTrace();
            System.out.println(se);
        }

    }
    public void initialiseDetails(){
        username.setText(currentCustomer.getUsername());
        firstName.setText(currentCustomer.getFirstName());
        lastName.setText(currentCustomer.getLastName());
        try{
            Connection conn = DatabaseService.getConnection();
            int addressId = currentCustomer.getAddressId(conn);
            Address address = Address.getAddress(conn, addressId);
            addressLine1.setText(address.getFirstLine());
            city.setText(address.getCity());
            postCode.setText(address.getPostCode());
            conn.close();
        }catch (SQLException se){

        }


    }
    public void initialiseMenu(){
        fooditems.clear();
        drinksitems.clear();
        specialitems.clear();

        foodName.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        foodPrice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        foodTable.setItems(fooditems);
        drinkName.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        drinkPrice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        drinkTable.setItems(drinksitems);
        specialsName.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        specialsPrice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        specialsTable.setItems(specialitems);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = MenuItem.getMenuItems(conn, false);
            while(rs.next()) {
                if (rs.getString(3).equals(String.valueOf(MenuItemType.Food))){
                    fooditems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.Food, rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                } else {
                    drinksitems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.Drink, rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                }
            }

            rs = MenuItem.getMenuItems(conn, true);
            while(rs.next()){
                specialitems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
            }
            conn.close();
        }catch (SQLException se){
        }

    }


    public void cancelSelectedBooking() {
        int selectedIndex = myBookingsList.getSelectionModel().getSelectedIndex();
        if(selectedIndex != -1) {
            try {
                Booking.deleteBooking(
                    DatabaseService.getConnection(),
                    customerBookings.get(selectedIndex).getId()
                );
                customerBookings.remove(selectedIndex);
                myBookingsList.getItems().remove(selectedIndex);
                myBookingsStatus.setText("Status: Cancelled booking.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error in cancelling booking.\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                myBookingsStatus.setText("Status: Error, " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Please select a booking to cancel.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void updateMyBookings() {
        try {
            customerBookings = Booking.getBookingsForCustomer(
                    DatabaseService.getConnection(),
                    currentCustomer.getId()
            );
            myBookingsStatus.setText("Status: Retrieved bookings.");
            myBookingsList.getItems().clear();
            for(Booking b: customerBookings) {
                myBookingsList.getItems().add(
                    String.format(
                            "Date: %s\t Time: %s\t Table: %d\t Approval Status: %s",
                            b.getDateOfBooking().toString(),
                            b.getHourOfBooking() + ":00",
                            b.getTableId(),
                            b.isApproved() ? "Approved" : "Not Approved"
                    )
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in getting your bookings.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            myBookingsStatus.setText("Status: Error, " + e.getMessage());
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
                    currentCustomer.getId(),
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