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
import javafx.stage.Stage;
import models.*;
import models.MenuItem;
import services.AppState;
import services.DatabaseService;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Responsible for carrying out the functionality of the Customer user interface.
 * @author Ashley Forster, Sam James
 */
public class CustomerController implements Initializable {
    private final ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> orderitems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> specialitems = FXCollections.observableArrayList();
    private final ObservableList<String> orderOption = FXCollections.observableArrayList();
    private final ObservableList<Order> orderHistory = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> orderHistoryItems = FXCollections.observableArrayList();
    @FXML
    private TableView<MenuItem> foodTable;
    @FXML
    private TableColumn<MenuItem, String> foodName;
    @FXML
    private TableColumn<MenuItem, Double> foodPrice;
    @FXML
    private TableView<MenuItem> drinkTable;
    @FXML
    private TableColumn<MenuItem, String> drinkName;
    @FXML
    private TableColumn<MenuItem, Double> drinkPrice;
    @FXML
    private TableView<MenuItem> specialsTable;
    @FXML
    private TableColumn<MenuItem, String> specialsName;
    @FXML
    private TableColumn<MenuItem, Double> specialsPrice;
    @FXML
    private TableView<MenuItem> ordertable;
    @FXML
    private TableColumn<MenuItem, String> ordereditem;
    @FXML
    private TableView<Order> orderHistoryTable;
    @FXML
    private TableColumn<Order, Integer> orderHistoryNo;
    @FXML
    private TableColumn<Order, Timestamp> orderHistoryDate;
    @FXML
    private TableColumn<Order, Double> orderHistoryTotal;
    @FXML
    private TableView<MenuItem> historyItemsTable;
    @FXML
    private TableColumn<MenuItem, String> historyItemName;
    @FXML
    private TableColumn<MenuItem, Double> historyItemPrice;
    @FXML
    private DatePicker dateSelector;
    @FXML
    private ComboBox timeCombo;
    @FXML
    private Spinner guestNumberSpinner;
    @FXML
    private Spinner durationSpinner;
    @FXML
    private Label bookingStatusLabel;
    private LocalDate dateChosen;
    private int hourChosen;
    private int guestCount;
    @FXML
    private ListView myBookingsList;
    @FXML
    private Label myBookingsStatus;
    private ArrayList<Booking> customerBookings;
    @FXML
    private ComboBox<String> orderType;
    @FXML
    private ComboBox<String> reorderType;
    @FXML
    private TextArea orderMessage;
    @FXML
    private ListView availableSlotsList;
    @FXML
    private Label username;
    @FXML
    private Label firstName;
    @FXML
    private Label lastName;
    @FXML
    private Label addressLine1;
    @FXML
    private Label city;
    @FXML
    private Label postCode;
    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmNewPassword;
    @FXML
    private TextField newLine1;
    @FXML
    private TextField newCity;
    @FXML
    private TextField newPostCode;
    private final AppState appState = AppState.getAppState();
    private final Customer currentCustomer = appState.getCustomer();

    /**
     * Returns user to previous page.
     * @param event
     * @throws IOException
     */
    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/Login_ui.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    /**
     * Changes Customer Password based on information input to change-password PasswordFields
     */
    public void changePassword() {
        if (currentPassword.getText().equals("") || newPassword.getText().equals("") || confirmNewPassword.getText().equals("")) {
        } else if (!newPassword.getText().equals(confirmNewPassword.getText())) {
        } else {
            try {
                Connection conn = DatabaseService.getConnection();
                if (DatabaseService.confirmPassword(conn, currentPassword.getText(), currentCustomer.getId())) {
                    DatabaseService.updateCustomerPassword(conn, newPassword.getText(), currentCustomer.getId());
                }
                conn.close();
                currentPassword.clear();
                newPassword.clear();
                confirmNewPassword.clear();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

            }
        }
    }

    /**
     * Updates address details linked to customer.
     */
    public void changeAddress() {
        if (newLine1.getText().equals("") || newCity.getText().equals("") || newPostCode.getText().equals("")) {
        } else {
            try {
                Connection conn = DatabaseService.getConnection();
                Address.updateAddress(conn, currentCustomer.getAddressId(conn), newLine1.getText(), newCity.getText(), newPostCode.getText());
                conn.close();
                newLine1.clear();
                newCity.clear();
                newPostCode.clear();
                initialiseDetails();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * Adds MenuItem from FoodItems table to OrderItems table.
     */
    public void foodSelect() {
        MenuItem itemSelected = foodTable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<>("name")));
        ordertable.setItems(orderitems);
    }

    /**
     * Adds MenuItem from drinksItems table to OrderItems table.
     */
    public void drinkSelect() {
        MenuItem itemSelected = drinkTable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<>("name")));
        ordertable.setItems(orderitems);
    }

    /**
     * Adds MenuItem from SpecialItems table to OrderItems table.
     */
    public void specialSelect() {
        MenuItem itemSelected = specialsTable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<>("name")));
        ordertable.setItems(orderitems);
    }

    /**
     * Removes MenuItem from OrderItems table.
     */
    public void itemRemove() {
        MenuItem itemSelected = ordertable.getSelectionModel().getSelectedItem();
        orderitems.remove(itemSelected);
        ordertable.setItems(orderitems);
    }

    /**
     * Places Takeaway or Delivery order for MenuItems in orderItems table, depending on selected orderType.
     */
    public void confirmOrder() {
        int customerId = currentCustomer.getId();
        LocalDateTime ordertime = LocalDateTime.now();
        Timestamp sqlordertime = Timestamp.valueOf(ordertime);
        double sum = 0.0;
        for (MenuItem item : orderitems) {
            sum += item.getPrice();
        }

        if (orderType.getValue().equals("Delivery") || orderType.getValue().equals("Takeaway")) {
            try {
                Connection conn = DatabaseService.getConnection();
                Order.createOrder(conn, sqlordertime, customerId, sum, OrderType.valueOf(orderType.getValue()));
                int orderId = DatabaseService.getLastInsert(conn);
                if (orderType.getValue().equals("Delivery")) {
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
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } else {
        }
    }

    /**
     * Reorders a previous Order from Order History. Order Type depends on type selected.
     */
    public void confirmReOrder() {
        int customerId = currentCustomer.getId();
        LocalDateTime ordertime = LocalDateTime.now();
        Timestamp sqlordertime = Timestamp.valueOf(ordertime);
        double sum = 0.0;
        for (MenuItem item : orderHistoryItems) {
            sum += item.getPrice();
        }

        if (reorderType.getValue() == "Delivery" || reorderType.getValue() == "Takeaway") {
            try {
                Connection conn = DatabaseService.getConnection();
                Order.createOrder(conn, sqlordertime, customerId, sum, OrderType.valueOf(reorderType.getValue()));
                int orderId = DatabaseService.getLastInsert(conn);
                if (reorderType.getValue() == "Delivery") {
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
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } else {
        }
    }

    /**
     * Select previous order from Order History table, displays MenuItems of that order.
     */
    public void selectHistoricOrder() {
        Order selectedOrder = orderHistoryTable.getSelectionModel().getSelectedItem();
        orderHistoryItems.clear();
        try {
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = MenuItem.getOrderItems(conn, selectedOrder.getOrderId());
            if (rs.next()) {
                while (rs.next()) {
                    orderHistoryItems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                }
                historyItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
                historyItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
                historyItemsTable.setItems(orderHistoryItems);
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    /**
     * Initialises page when loaded.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderHistoryNo.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderHistoryDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderHistoryTotal.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
        orderHistoryTable.setItems(orderHistory);

        orderOption.add("Takeaway");
        orderOption.add("Delivery");
        orderType.setItems(orderOption);
        orderType.getSelectionModel().selectFirst();
        reorderType.setItems(orderOption);

        initialiseDetails();
        initializeBookingTab();
        initialiseMenu();

        try {
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Order.getOrderHistory(conn, currentCustomer.getId());
            while (rs.next()) {
                orderHistory.add(Order.orderFromRS(rs));
            }
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    /**
     * Initialise information in My Details tab.
     */
    private void initialiseDetails() {
        username.setText(currentCustomer.getUsername());
        firstName.setText(currentCustomer.getFirstName());
        lastName.setText(currentCustomer.getLastName());
        try {
            Connection conn = DatabaseService.getConnection();
            int addressId = currentCustomer.getAddressId(conn);
            Address address = Address.getAddress(conn, addressId);
            addressLine1.setText(address.getFirstLine());
            city.setText(address.getCity());
            postCode.setText(address.getPostCode());
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }


    }

    /**
     * Initialises MenuItems in Menu Tables.
     */
    private void initialiseMenu() {
        fooditems.clear();
        drinksitems.clear();
        specialitems.clear();

        foodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        foodTable.setItems(fooditems);
        drinkName.setCellValueFactory(new PropertyValueFactory<>("name"));
        drinkPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        drinkTable.setItems(drinksitems);
        specialsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        specialsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        specialsTable.setItems(specialitems);
        try {
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = MenuItem.getMenuItems(conn, false);
            while (rs.next()) {
                if (rs.getString(3).equals(String.valueOf(MenuItemType.Food))) {
                    fooditems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.Food, rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                } else {
                    drinksitems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.Drink, rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                }
            }

            rs = MenuItem.getMenuItems(conn, true);
            while (rs.next()) {
                specialitems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    /**
     * Allows customer to cancel selected Booking from their my Bookings table.
     */
    public void cancelSelectedBooking() {
        int selectedIndex = myBookingsList.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
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

    /**
     *
     */
    public void updateMyBookings() {
        try {
            customerBookings = Booking.getBookingsForCustomer(
                    DatabaseService.getConnection(),
                    currentCustomer.getId()
            );
            myBookingsStatus.setText("Status: Retrieved bookings.");
            myBookingsList.getItems().clear();
            for (Booking b : customerBookings) {
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

    /**
     * Displays tables available to book at a specified time and date.
     */
    public void viewAvailableSlots() {
        String stringInt = timeCombo.getValue().toString();
        int count = 0;
        char c = stringInt.charAt(count);
        while(c!=':') {
            count++;
            c = stringInt.charAt(count);
        }
        int startTime = Integer.parseInt(timeCombo.getValue().toString().substring(0, count));

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
            availableSlotsList.getItems().addAll(availableTables);
        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Requests a booking for chosen table and timeslot.
     */
    public void requestBooking() {
        Table selectedTable = (Table) availableSlotsList.getSelectionModel().getSelectedItem();
        if (selectedTable != null) {
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
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            bookingStatusLabel.setText("Please select a table.");
        }
    }

    /**
     * Initialises the times available to book for.
     */
    private void initializeBookingTab() {
        for (int i = Booking.OPENING_TIME; i < Booking.CLOSING_TIME; i++) {
            timeCombo.getItems().add(String.format("%d:00", i));
        }
    }
}
