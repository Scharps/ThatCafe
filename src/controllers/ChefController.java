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
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ChefController implements Initializable {
    private AppState appState = AppState.getAppState();
    private StaffMember currentStaff = appState.getStaff();

    @FXML private TableView<Order> currentOrderTable;
    @FXML private TableColumn<Order, Integer> currentOrderNo;
    @FXML private TableColumn<Order, Timestamp> currentOrderDate;
    @FXML private TableColumn<Order, String> currentOrderType;

    @FXML private TableView<MenuItem> currentItemTable;
    @FXML private TableColumn<MenuItem, String> currentOrderItem;

    @FXML private TextField newItemName;
    @FXML private TextField newItemPrice;
    @FXML private ComboBox<String> newItemType;
    @FXML private CheckBox newSpecial;

    @FXML private TableView<MenuItem> foodTable;
    @FXML private TableColumn<MenuItem, String> foodName;
    @FXML private TableColumn<MenuItem, Double> foodPrice;
    @FXML private TableView<MenuItem> drinkTable;
    @FXML private TableColumn<MenuItem, String> drinkName;
    @FXML private TableColumn<MenuItem, Double> drinkPrice;
    @FXML private TableView<MenuItem> specialsTable;
    @FXML private TableColumn<MenuItem, String> specialsName;
    @FXML private TableColumn<MenuItem, Double> specialsPrice;

    private final ObservableList<Order> currentOrders = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> currentOrderItems = FXCollections.observableArrayList();
    private final ObservableList<String> newTypeList = FXCollections.observableArrayList();

    private final ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> specialitems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    public void selectOrder(MouseEvent event){
        Order selectedOrder = currentOrderTable.getSelectionModel().getSelectedItem();
        currentOrderItems.clear();
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = MenuItem.getOrderItems(conn, selectedOrder.getOrderId());
            if(rs.next()) {
                while (rs.next()) {
                    currentOrderItems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                }
                currentOrderItem.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
                currentItemTable.setItems(currentOrderItems);
            }
            conn.close();
        } catch (SQLException se){
            System.out.println("help");
        }
    }

    public void confirmCooked(ActionEvent event){
        Order selectedOrder = currentOrderTable.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DatabaseService.getConnection();
            selectedOrder.setCooked(conn);
            conn.close();
        }catch (SQLException se){
        }
        currentOrders.clear();
        currentOrderItems.clear();
        initialiseCurrentOrders();

    }

    public void addMenuItem(ActionEvent event){
        if(newItemName.getText().equals("") || newItemType.getValue()==null){
        }
        else {
            String newName = newItemName.getText();
            String newType = newItemType.getValue();

            boolean special = newSpecial.isSelected();
            try {
                double newPrice = Double.parseDouble(newItemPrice.getText());
                Connection conn = DatabaseService.getConnection();
                MenuItem.createNewItem(conn, newName, newType, newPrice, special);
                conn.close();
            } catch (NumberFormatException ne) {

            } catch (SQLException se) {

            }
            newItemName.clear();
            newItemPrice.clear();
            newSpecial.setSelected(false);
            newItemType.getSelectionModel().clearSelection();
            initialiseMenu();
        }
    }

    public void removeFoodItem(ActionEvent event){
        MenuItem selectedItem = foodTable.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DatabaseService.getConnection();
            MenuItem.deleteMenuItem(conn, selectedItem.getId());
            conn.close();
        }catch (SQLException se){

        }
        initialiseMenu();
    }
    public void removeDrinkItem(ActionEvent event){
        MenuItem selectedItem = drinkTable.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DatabaseService.getConnection();
            MenuItem.deleteMenuItem(conn, selectedItem.getId());
            conn.close();
        }catch (SQLException se){

        }
        initialiseMenu();
    }
    public void removeSpecialItem(ActionEvent event){
        MenuItem selectedItem = specialsTable.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DatabaseService.getConnection();
            MenuItem.deleteMenuItem(conn, selectedItem.getId());
            conn.close();
        }catch (SQLException se){

        }
        initialiseMenu();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseCurrentOrders();
        initialiseMenu();
        newTypeList.add(String.valueOf(MenuItemType.Food));
        newTypeList.add(String.valueOf(MenuItemType.Drink));
        newItemType.setItems(newTypeList);


    }

    public void initialiseCurrentOrders(){
        currentOrderNo.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
        currentOrderDate.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("orderDate"));
        currentOrderType.setCellValueFactory(new PropertyValueFactory<Order, String>("orderType"));
        currentOrderTable.setItems(currentOrders);

        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Order.getUncookedOrders(conn);
            while(rs.next()) {
                if (rs.getString(6) == "Delivery") {
                    if (DeliveryOrder.isApproved(conn, rs.getInt(1))) {
                        currentOrders.add(Order.createOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), OrderType.valueOf(rs.getString(6))));
                    }
                } else {
                    currentOrders.add(Order.createOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5), OrderType.valueOf(rs.getString(6))));
                }
            }
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

}
