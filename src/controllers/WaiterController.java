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
    @FXML private TableView<DeliveryOrder> deliverytable;
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

    private ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> orderitems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> specialitems = FXCollections.observableArrayList();
    private ObservableList<Integer> tables = FXCollections.observableArrayList();
    private ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();
    private ObservableList<Booking> unconfirmedBookings = FXCollections.observableArrayList();
    private ObservableList<Booking> todaysBookings = FXCollections.observableArrayList();
    //private ObservableList<String> deliveryorders = FXCollections.observableArrayList();

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
                Order.createOrder(conn, sqlordertime, customerId, orderTotal);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppState appState = AppState.getAppState();
        namelabel.setText(appState.getUser().getFirstName());

        tableno.setItems(tables);
        initialiseMenu();
        initialiseUnconfirmedBookings();
        initialiseTodaysBookings();

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
                unconfirmedBookings.add(Booking.createBooking(rs.getInt(1), rs.getInt(2), rs.getInt(4), rs.getDate(5), rs.getInt(3), rs.getInt(6), rs.getBoolean(7)));
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
                todaysBookings.add(Booking.createBooking(rs.getInt(1), rs.getInt(2), rs.getInt(4), rs.getDate(5), rs.getInt(3), rs.getInt(6), rs.getBoolean(7)));
            }
            conn.close();
        }catch (SQLException se){

        }

    }
}