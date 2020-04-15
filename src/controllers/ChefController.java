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
import models.MenuItem;
import models.MenuItemType;
import models.Order;
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

    @FXML private TableView<Order> currentOrderTable;
    @FXML private TableColumn<Order, Integer> currentOrderNo;
    @FXML private TableColumn<Order, Timestamp> currentOrderDate;

    @FXML private TableView<MenuItem> currentItemTable;
    @FXML private TableColumn<MenuItem, String> currentOrderItem;

    @FXML private TextField newItemName;
    @FXML private TextField newItemPrice;
    @FXML private ComboBox<String> newItemType;
    @FXML private CheckBox newSpecial;

    ObservableList<Order> currentOrders = FXCollections.observableArrayList();
    ObservableList<MenuItem> currentOrderItems = FXCollections.observableArrayList();
    ObservableList<String> newTypeList = FXCollections.observableArrayList();

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
        if(newItemName.getText() == ""){

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
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseCurrentOrders();
        newTypeList.add(String.valueOf(MenuItemType.Food));
        newTypeList.add(String.valueOf(MenuItemType.Drink));
        newItemType.setItems(newTypeList);


    }

    public void initialiseCurrentOrders(){
        currentOrderNo.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
        currentOrderDate.setCellValueFactory((new PropertyValueFactory<Order, Timestamp>("orderDate")));
        currentOrderTable.setItems(currentOrders);

        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Order.getUncookedOrders(conn);
            while(rs.next()){
                currentOrders.add(Order.createOrder(rs.getInt(1), rs.getTimestamp(2), rs.getInt(3), rs.getBoolean(4), rs.getDouble(5)));
            }
            conn.close();
        }catch (SQLException se){
        }

    }

}
