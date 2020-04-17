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
import java.util.ResourceBundle;

/**
 * Responsible for carrying out the functionality of the Waiter user interface.
 * @author Ashley Forster, Sam James
 */
public class WaiterController implements Initializable {
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
    @FXML private ComboBox<Integer> tableno;
    @FXML private TextArea orderError;
    @FXML private Label namelabel;

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, Integer> bookingNo;
    @FXML private TableColumn<Booking, Date> bookingDate;
    @FXML private TableColumn<Booking, Integer> bookingHour;
    @FXML private TableColumn<Booking, Integer> tableNo;
    @FXML private TableColumn<Booking, Integer> guestNo;

    @FXML private TableView<Booking> todayBookingTable;
    @FXML private TableColumn<Booking, Integer> todayBookingNo;
    @FXML private TableColumn<Booking, Integer> todayBookingHour;
    @FXML private TableColumn<Booking, Integer> todayTableNo;
    @FXML private TableColumn<Booking, Integer> todayGuestNo;

    @FXML private TableView<EatInOrder> currentEatinTable;
    @FXML private TableColumn<EatInOrder, Integer> eatinNo;
    @FXML private TableColumn<EatInOrder, Timestamp> eatinTime;
    @FXML private TableColumn<EatInOrder, Integer> eatinTableNo;
    @FXML private TableColumn<EatInOrder, Boolean> eatinCooked;

    @FXML private TableView<MenuItem> eatinItemsTable;
    @FXML private TableColumn<MenuItem, String> eatinItem;

    @FXML private TableView<TakeawayOrder> currentTakeawayTable;
    @FXML private TableColumn<TakeawayOrder, Integer> takeawayNo;
    @FXML private TableColumn<TakeawayOrder, Timestamp> pickUp;
    @FXML private TableColumn<TakeawayOrder, Boolean> takeawayCooked;

    @FXML private TableView<MenuItem> takeawayItemsTable;
    @FXML private TableColumn<MenuItem, String> takeawayItem;
    @FXML private Label takeawayName;
    @FXML private Label takeawayTime;

    @FXML private TableView<DeliveryOrder> currentDeliveryTable;
    @FXML private TableColumn<DeliveryOrder, Integer> deliveryNo;
    @FXML private TableColumn<DeliveryOrder, Boolean> deliveryApproved;
    @FXML private TableColumn<DeliveryOrder, Boolean> deliveryCooked;

    @FXML private TableView<MenuItem> deliveryItemsTable;
    @FXML private TableColumn<MenuItem, String> deliveryItem;
    @FXML private Label deliveryName;
    @FXML private Label deliveryTime;
    @FXML private Label deliveryAddress1;
    @FXML private Label deliveryCity;
    @FXML private Label deliveryPostCode;

    private final ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> orderitems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> specialitems = FXCollections.observableArrayList();
    private final ObservableList<Integer> tables = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();
    private final ObservableList<Booking> unconfirmedBookings = FXCollections.observableArrayList();
    private final ObservableList<Booking> todaysBookings = FXCollections.observableArrayList();
    private final ObservableList<EatInOrder> eatinOrders = FXCollections.observableArrayList();
    private final ObservableList<TakeawayOrder> takeawayOrders = FXCollections.observableArrayList();
    private final ObservableList<DeliveryOrder> deliveryOrders = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> orderItemsList= FXCollections.observableArrayList();

    @FXML
    private Label monStartLabel;
    @FXML
    private Label monEndLabel;
    @FXML
    private Label tueStartLabel;
    @FXML
    private Label tueEndLabel;
    @FXML
    private Label wedStartLabel;
    @FXML
    private Label wedEndLabel;
    @FXML
    private Label thuStartLabel;
    @FXML
    private Label thuEndLabel;
    @FXML
    private Label friStartLabel;
    @FXML
    private Label friEndLabel;
    @FXML
    private Label satStartLabel;
    @FXML
    private Label satEndLabel;
    @FXML
    private Label sunStartLabel;
    @FXML
    private Label sunEndLabel;
    @FXML
    private DatePicker workedHoursDatePicker;
    @FXML
    private Spinner hoursWorkedSpinner;
    @FXML
    private Button saveButton;

    /**
     * Returns user to previous page.
     * @param event
     * @throws IOException
     */
    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    /**
     * Adds MenuItem from FoodItems table to OrderItems table.
     */
    public void foodSelect(){
        MenuItem itemSelected = foodTable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<>("name")));
        ordertable.setItems(orderitems);
    }

    /**
     * Adds MenuItem from drinksItems table to OrderItems table.
     */
    public void drinkSelect(){
        MenuItem itemSelected = drinkTable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<>("name")));
        ordertable.setItems(orderitems);
    }

    /**
     * Adds MenuItem from SpecialItems table to OrderItems table.
     */
    public void specialSelect(){
        MenuItem itemSelected = specialsTable.getSelectionModel().getSelectedItem();
        orderitems.add(itemSelected);
        ordereditem.setCellValueFactory((new PropertyValueFactory<>("name")));
        ordertable.setItems(orderitems);
    }

    /**
     * Removes MenuItem from OrderItems table.
     */
    public void itemRemove(){
        MenuItem itemSelected = ordertable.getSelectionModel().getSelectedItem();
        orderitems.remove(itemSelected);
        ordertable.setItems(orderitems);
    }

    /**
     * Places Eatin Order for customer seated at specified table.
     */
    public void confirmOrder(){
        LocalDateTime orderTime = LocalDateTime.now();
        LocalDate bookingDate = LocalDate.now();
        int bookingHour = LocalDateTime.now().getHour();
        int tableId = tableno.getValue();
        Timestamp sqlordertime = Timestamp.valueOf(orderTime);
        Date sqlbookingDate = Date.valueOf(bookingDate);
        double orderTotal = 0.0;
        for(MenuItem item: orderitems) {
            orderTotal += item.getPrice();
        }
        try {
            Connection conn = DatabaseService.getConnection();
            int customerId = Booking.getCustomerId(conn, sqlbookingDate, bookingHour, tableId);
            if(customerId == 0){
                orderError.setText("No booking at this table at this time.");
            }
            else {
                Order.createOrder(conn, sqlordertime, customerId, orderTotal, OrderType.EatIn);
                int orderId = DatabaseService.getLastInsert(conn);
                EatInOrder.createEatInOrder(conn, orderId, tableId);
                for (MenuItem item : orderitems) {
                    MenuItem.createOrderedItem(conn, orderId, item.getId());
                }
                orderitems.clear();
                ordertable.setItems(orderitems);
            }

            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets selected unConfirmed booking as confirmed.
     */
    public void confirmBooking(){
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if(selectedBooking != null){
            try{
                Connection conn = DatabaseService.getConnection();
                Booking.confirmBooking(conn, selectedBooking.getId());
                conn.close();
                initialiseUnconfirmedBookings();
                initialiseTodaysBookings();
            } catch(Exception se){
                JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Refresh current Eatin orders table.
     */
    public void refreshEatIn(){
        initialiseCurrentEatInOrders();
    }

    /**
     * Refresh current Takeaway orders table.
     */
    public void refreshTakeaway() {
        initialiseCurrentTakeawayOrders();
    }

    /**
     * Refresh current Delivery orders table.
     */
    public void refreshDelivery() {
        initialiseCurrentDeliveryOrders();
    }

    /**
     * Allows waiter to confirm an Eatin Order as Served, only if it has been Cooked.
     */
    public void confirmServed(){
        EatInOrder selectedOrder = currentEatinTable.getSelectionModel().getSelectedItem();
        if(selectedOrder != null) {
            if (!selectedOrder.isCooked()) {

            } else {
                try {
                    Connection conn = DatabaseService.getConnection();
                    EatInOrder.confirmedServed(conn, selectedOrder.getOrderId());
                    conn.close();
                    initialiseCurrentEatInOrders();
                    orderItemsList.clear();
                } catch (SQLException se) {
                    JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Select Eatin Order from current Eatin Orders table. Displays MenuItems for that order.
     */
    public void eatinSelect(){
        EatInOrder selectedOrder = currentEatinTable.getSelectionModel().getSelectedItem();
        orderItemsList.clear();
        if(selectedOrder != null) {

            try{
                Connection conn = DatabaseService.getConnection();
                ResultSet rs = MenuItem.getOrderItems(conn, selectedOrder.getOrderId());
                if(rs.next()) {
                    while (rs.next()) {
                        orderItemsList.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                    }
                    eatinItem.setCellValueFactory(new PropertyValueFactory<>("name"));
                    eatinItemsTable.setItems(orderItemsList);
                }
                conn.close();
            } catch (SQLException se){
                System.out.println("help");
            }
        }
    }

    /**
     * Allows waiter to confirm a Takeaway order as having been Collected, only if order has been Cooked.
     */
    public void confirmCollected(){
        TakeawayOrder selectedOrder = currentTakeawayTable.getSelectionModel().getSelectedItem();
        if(selectedOrder != null) {
            if (!selectedOrder.isCooked()) {

            } else {
                try {
                    Connection conn = DatabaseService.getConnection();
                    selectedOrder.confirmCollected(conn);
                    conn.close();
                    initialiseCurrentTakeawayOrders();
                    orderItemsList.clear();
                } catch (SQLException se) {
                    JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Select Takeaway Order from current Takeaway Orders table. Displays details for that order.
     */
    public void takeawaySelect(){
        TakeawayOrder selectedOrder = currentTakeawayTable.getSelectionModel().getSelectedItem();
        orderItemsList.clear();
        if(selectedOrder != null) {
            takeawayTime.setText(String.valueOf(selectedOrder.getPickupTime()));

            try{
                Connection conn = DatabaseService.getConnection();
                Customer customer = Customer.getCustomer(conn, selectedOrder.getCustomerId());
                takeawayName.setText(customer.getFirstName() + " " + customer.getLastName());
                ResultSet rs = MenuItem.getOrderItems(conn, selectedOrder.getOrderId());
                if(rs.next()) {
                    while (rs.next()) {
                        orderItemsList.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                    }
                    takeawayItem.setCellValueFactory(new PropertyValueFactory<>("name"));
                    takeawayItemsTable.setItems(orderItemsList);
                }
                conn.close();
            } catch (SQLException se){
                System.out.println("help");
            }
        }
    }

    /**
     * Allows staff member to input their hours worked for a specified day.
     */
    public void saveWorkedHours() {
        if(!workedHoursDatePicker.getValue().isAfter(LocalDate.now())) {
            try {
                AppState.getAppState().getStaff().setHoursWorked(
                        DatabaseService.getConnection(),
                        Date.valueOf(workedHoursDatePicker.getValue()),
                        (Integer) hoursWorkedSpinner.getValue()
                );
                saveButton.setText("Saved!");
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error in updating worked hours. " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "You can't set hours for the future!",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Initialises Rota information for current Staff member.
     * @throws SQLException
     */
    public void initializeMyRota() throws SQLException {
        Rota myRota = Rota.getRota(
                DatabaseService.getConnection(),
                AppState.getAppState().getStaff().getRota().getRotaId()
        );
        monStartLabel.setText(myRota.getMondayShift().getStartTime());
        monEndLabel.setText(myRota.getMondayShift().getFinishTime());
        tueStartLabel.setText(myRota.getTuesdayShift().getStartTime());
        tueEndLabel.setText(myRota.getTuesdayShift().getFinishTime());
        wedStartLabel.setText(myRota.getWednesdayShift().getStartTime());
        wedEndLabel.setText(myRota.getWednesdayShift().getFinishTime());
        thuStartLabel.setText(myRota.getThursdayShift().getStartTime());
        thuEndLabel.setText(myRota.getThursdayShift().getFinishTime());
        friStartLabel.setText(myRota.getFridayShift().getStartTime());
        friEndLabel.setText(myRota.getFridayShift().getFinishTime());
        satStartLabel.setText(myRota.getSaturdayShift().getStartTime());
        satEndLabel.setText(myRota.getSaturdayShift().getFinishTime());
        sunStartLabel.setText(myRota.getSundayShift().getStartTime());
        sunEndLabel.setText(myRota.getSundayShift().getFinishTime());
        workedHoursDatePicker.getChronology().dateNow();
    }

    /**
     * Allows waiter to approve a request for a delivery order made by a customer.
     */
    public void approveDelivery(){
        DeliveryOrder selectedOrder = currentDeliveryTable.getSelectionModel().getSelectedItem();
        if(selectedOrder != null) {
            try {
                Connection conn = DatabaseService.getConnection();
                selectedOrder.approveDelivery(conn);
                conn.close();
                initialiseCurrentDeliveryOrders();
                orderItemsList.clear();
            } catch (SQLException se) {
                JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Select Delivery Order from current Delivery Orders table. Displays details for that order.
     */
    public void deliverySelect(){
        DeliveryOrder selectedOrder = currentDeliveryTable.getSelectionModel().getSelectedItem();
        orderItemsList.clear();
        if(selectedOrder != null) {
            deliveryTime.setText(String.valueOf(selectedOrder.getEstimatedDeliveryTime()));
            try{
                Connection conn = DatabaseService.getConnection();
                Customer customer = Customer.getCustomer(conn, selectedOrder.getCustomerId());
                deliveryName.setText(customer.getFirstName() + " " + customer.getLastName());
                deliveryAddress1.setText(customer.getAddress().getFirstLine());
                deliveryCity.setText(customer.getAddress().getCity());
                deliveryPostCode.setText(customer.getAddress().getPostCode());
                ResultSet rs = MenuItem.getOrderItems(conn, selectedOrder.getOrderId());
                if(rs.next()) {
                    while (rs.next()) {
                        orderItemsList.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                    }
                    deliveryItem.setCellValueFactory(new PropertyValueFactory<>("name"));
                    deliveryItemsTable.setItems(orderItemsList);
                }
                conn.close();
            } catch (SQLException se){
                System.out.println("help");
            }
        }
    }

    /**
     * Initialises information when page is loaded.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        namelabel.setText(AppState.getAppState().getStaff().getFirstName());

        tableno.setItems(tables);
        initialiseMenu();
        initialiseUnconfirmedBookings();
        initialiseTodaysBookings();
        initialiseCurrentEatInOrders();
        initialiseCurrentTakeawayOrders();
        initialiseCurrentDeliveryOrders();
        try {
            Connection conn = DatabaseService.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT TableId from CafeTables");
            while(rs.next()){
                tables.add(rs.getInt(1));
            }
            conn.close();
            initializeMyRota();
        }
        catch (SQLException se){
            JOptionPane.showMessageDialog(
                    null,
                    "Error initializing waiter UI. " + se.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }

    }

    /**
     * Initialises MenuItems in Menu Tables.
     */
    private void initialiseMenu(){
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
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Initialises unconfirmed Bookings in Confirm Bookings table.
     */
    private void initialiseUnconfirmedBookings(){
        unconfirmedBookings.clear();

        bookingNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookingDate.setCellValueFactory(new PropertyValueFactory<>("dateOfBooking"));
        bookingHour.setCellValueFactory(new PropertyValueFactory<>("hourOfBooking"));
        tableNo.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        guestNo.setCellValueFactory(new PropertyValueFactory<>("numberOfGuests"));
        bookingTable.setItems(unconfirmedBookings);

        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Booking.getUnconfirmedBookings(conn);
            while(rs.next()){
                unconfirmedBookings.add(Booking.getBooking(conn, rs.getInt("BookingId")));
            }
            conn.close();
        }catch (SQLException se){
            se.printStackTrace();
        }
    }

    /**
     * Initialises unserved Eatin orders in current Eatin orders table.
     */
    private void initialiseCurrentEatInOrders(){
        eatinOrders.clear();
        eatinNo.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        eatinTime.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        eatinTableNo.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        eatinCooked.setCellValueFactory(new PropertyValueFactory<>("served"));
        currentEatinTable.setItems(eatinOrders);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = EatInOrder.getUnServed(conn);
            while (rs.next()){
                eatinOrders.add(EatInOrder.orderFromRS(rs));
            }
            conn.close();
        } catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initialises uncollected Takeaway orders in current Takeaway orders table.
     */
    private void initialiseCurrentTakeawayOrders(){
        takeawayOrders.clear();
        takeawayNo.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        pickUp.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
        takeawayCooked.setCellValueFactory(new PropertyValueFactory<>("cooked"));
        currentTakeawayTable.setItems(takeawayOrders);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = TakeawayOrder.getUncollected(conn);
            while(rs.next()){
                takeawayOrders.add(TakeawayOrder.orderFromRS(rs));
            }
            conn.close();
        } catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initialises Delivery orders not yet assigned to a driver in current Delivery orders table.
     */
    private void initialiseCurrentDeliveryOrders(){
        deliveryOrders.clear();
        deliveryNo.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        deliveryApproved.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        deliveryCooked.setCellValueFactory(new PropertyValueFactory<>("cooked"));
        currentDeliveryTable.setItems(deliveryOrders);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = DeliveryOrder.getUndelivered(conn);
            while(rs.next()){
                deliveryOrders.add(DeliveryOrder.orderFromRS(rs));
            }
            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initialises confirmed bookings for todays date in Todays bookings table.
     */
    private void initialiseTodaysBookings(){
        todaysBookings.clear();

        todayBookingNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        todayBookingHour.setCellValueFactory(new PropertyValueFactory<>("hourOfBooking"));
        todayTableNo.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        todayGuestNo.setCellValueFactory(new PropertyValueFactory<>("numberOfGuests"));
        todayBookingTable.setItems(todaysBookings);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Booking.getTodaysBookings(conn);
            while(rs.next()){
                todaysBookings.add(Booking.getBooking(conn, rs.getInt("BookingId")));
            }
            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}