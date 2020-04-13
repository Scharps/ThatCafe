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
    @FXML private TableView<MenuItem> foodtable;
    @FXML private TableColumn<MenuItem, String> foodname;
    @FXML private TableColumn<MenuItem, Double> foodprice;
    @FXML private TableView<MenuItem> drinktable;
    @FXML private TableColumn<MenuItem, String> drinkname;
    @FXML private TableColumn<MenuItem, Double> drinkprice;
    @FXML private TableView<MenuItem> ordertable;
    @FXML private TableColumn<MenuItem, String> ordereditem;
    @FXML private ComboBox<Integer> tableno;
    @FXML private TextArea orderError;
    @FXML private TableView<DeliveryOrder> deliverytable;
    @FXML private Label namelabel;

    private ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> orderitems = FXCollections.observableArrayList();
    private ObservableList<Integer> tables = FXCollections.observableArrayList();
    private ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();
    //private ObservableList<String> deliveryorders = FXCollections.observableArrayList();

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppState appState = AppState.getAppState();
        namelabel.setText(appState.getUser().getFirstName());

        foodname.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        foodprice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        foodtable.setItems(fooditems);

        drinkname.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        drinkprice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        drinktable.setItems(drinksitems);

        tableno.setItems(tables);


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
            rs = st.executeQuery("SELECT TableId from CafeTables");
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
}