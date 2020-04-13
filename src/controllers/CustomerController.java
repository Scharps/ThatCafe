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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.*;
import services.AppState;
import services.DatabaseService;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
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
    @FXML private ComboBox<String> orderType;
    @FXML private TextArea orderMessage;

    private ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> orderitems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();
    private ObservableList<String> orderOption = FXCollections.observableArrayList();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        foodname.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        foodprice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        foodtable.setItems(fooditems);

        drinkname.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        drinkprice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        drinktable.setItems(drinksitems);

        orderOption.add("Takeaway");
        orderOption.add("Delivery");
        orderType.setItems(orderOption);

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
            conn.close();

        }
        catch (SQLException se){
            se.printStackTrace();
            System.out.println(se);
        }

    }
}