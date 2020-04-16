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

import javax.print.DocFlavor;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PipedReader;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.ResourceBundle;

public class WaiterController implements Initializable {
    private AppState appState = AppState.getAppState();
    private StaffMember currentStaff = appState.getStaff();
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

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
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
        LocalDateTime orderTime = LocalDateTime.now();
        LocalDate bookingDate = LocalDate.now();
        int bookingHour = LocalDateTime.now().getHour();
        int tableId = (Integer) tableno.getValue();
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
        }
    }

    public void confirmBooking(ActionEvent event){
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if(selectedBooking != null){
            try{
                Connection conn = DatabaseService.getConnection();
                Booking.confirmBooking(conn, selectedBooking.getId());
                conn.close();
                initialiseUnconfirmedBookings();
                initialiseTodaysBookings();
            } catch(Exception se){

            }
        }
    }

    public void refreshEatIn(ActionEvent event){
        initialiseCurrentEatInOrders();
    }

    public void refreshTakeaway(ActionEvent event) {
        initialiseCurrentTakeawayOrders();
    }

    public void refreshDelivery(ActionEvent event) {
        initialiseCurrentDeliveryOrders();
    }

    public void confirmServed(ActionEvent event){
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

                }
            }
        }
    }

    public void eatinSelect(MouseEvent event){
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
                    eatinItem.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
                    eatinItemsTable.setItems(orderItemsList);
                }
                conn.close();
            } catch (SQLException se){
                System.out.println("help");
            }
        }
    }

    public void confirmCollected(ActionEvent event){
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

                }
            }
        }
    }

    public void takeawaySelect(MouseEvent event){
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
                    takeawayItem.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
                    takeawayItemsTable.setItems(orderItemsList);
                }
                conn.close();
            } catch (SQLException se){
                System.out.println("help");
            }
        }
    }

    public void approveDelivery(ActionEvent event){
        DeliveryOrder selectedOrder = currentDeliveryTable.getSelectionModel().getSelectedItem();
        if(selectedOrder != null) {
            try {
                Connection conn = DatabaseService.getConnection();
                selectedOrder.approveDelivery(conn);
                conn.close();
                initialiseCurrentDeliveryOrders();
                orderItemsList.clear();
            } catch (SQLException se) {
            }
        }
    }

    public void deliverySelect(MouseEvent event){
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
                    deliveryItem.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
                    deliveryItemsTable.setItems(orderItemsList);
                }
                conn.close();
            } catch (SQLException se){
                System.out.println("help");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        namelabel.setText(currentStaff.getFirstName());

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

        }
        catch (SQLException se){
            se.printStackTrace();
            System.out.println(se);
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

    public void initialiseUnconfirmedBookings(){
        unconfirmedBookings.clear();

        bookingNo.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("id"));
        bookingDate.setCellValueFactory(new PropertyValueFactory<Booking, Date>("dateOfBooking"));
        bookingHour.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("hourOfBooking"));
        tableNo.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("tableId"));
        guestNo.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("numberOfGuests"));
        bookingTable.setItems(unconfirmedBookings);

        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Booking.getUncomfirmedBooking(conn);
            while(rs.next()){
                unconfirmedBookings.add(Booking.getBooking(conn, rs.getInt("BookingId")));
            }
            conn.close();
        }catch (SQLException se){
            se.printStackTrace();
        }
    }

    public void initialiseCurrentEatInOrders(){
        eatinOrders.clear();
        eatinNo.setCellValueFactory(new PropertyValueFactory<EatInOrder, Integer>("orderId"));
        eatinTime.setCellValueFactory(new PropertyValueFactory<EatInOrder, Timestamp>("orderTime"));
        eatinTableNo.setCellValueFactory(new PropertyValueFactory<EatInOrder, Integer>("tableId"));
        eatinCooked.setCellValueFactory(new PropertyValueFactory<EatInOrder, Boolean>("served"));
        currentEatinTable.setItems(eatinOrders);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = EatInOrder.getUnServed(conn);
            while (rs.next()){
                eatinOrders.add(EatInOrder.orderFromRS(rs));
            }
            conn.close();
        } catch (SQLException se){}
    }



    public void initialiseCurrentTakeawayOrders(){
        takeawayOrders.clear();
        takeawayNo.setCellValueFactory(new PropertyValueFactory<TakeawayOrder, Integer>("orderId"));
        pickUp.setCellValueFactory(new PropertyValueFactory<TakeawayOrder, Timestamp>("pickupTime"));
        takeawayCooked.setCellValueFactory(new PropertyValueFactory<TakeawayOrder, Boolean>("cooked"));
        currentTakeawayTable.setItems(takeawayOrders);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = TakeawayOrder.getUncollected(conn);
            while(rs.next()){
                takeawayOrders.add(TakeawayOrder.orderFromRS(rs));
            }
            conn.close();
        } catch (SQLException se){

        }
    }

    public void initialiseCurrentDeliveryOrders(){
        deliveryOrders.clear();
        deliveryNo.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, Integer>("orderId"));
        deliveryApproved.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, Boolean>("confirmed"));
        deliveryCooked.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, Boolean>("cooked"));
        currentDeliveryTable.setItems(deliveryOrders);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = DeliveryOrder.getUndelivered(conn);
            while(rs.next()){
                deliveryOrders.add(DeliveryOrder.orderFromRS(rs));
            }
            conn.close();
        }catch (SQLException se){

        }
    }

    public void initialiseTodaysBookings(){
        todaysBookings.clear();

        todayBookingNo.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("id"));
        todayBookingHour.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("hourOfBooking"));
        todayTableNo.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("tableId"));
        todayGuestNo.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("numberOfGuests"));
        todayBookingTable.setItems(todaysBookings);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Booking.getTodaysBookings(conn);
            while(rs.next()){
                todaysBookings.add(Booking.getBooking(conn, rs.getInt("BookingId")));
            }
            conn.close();
        }catch (SQLException se){

        }

    }
}