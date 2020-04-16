package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import models.DeliveryOrder;
import services.AppState;
import services.DatabaseService;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DriverController implements Initializable {
    AppState appState = AppState.getAppState();
    @FXML private ListView availableList;
    @FXML private ListView myList;

    ObservableList<DeliveryOrder> availableOrders = FXCollections.observableArrayList();
    ObservableList<DeliveryOrder> myOrders = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseAvailable();
    }

    public void initialiseAvailable() {
        availableOrders.clear();
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = DeliveryOrder.getUnassigned(conn);
            while(rs.next()){
                availableOrders.add(DeliveryOrder.orderFromRS(rs));
            }
            conn.close();
        } catch (SQLException se){

        }
        availableList.getItems().addAll(availableOrders);
    }
}
