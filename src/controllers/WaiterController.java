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
import models.StaffMember;
import models.User;
import services.AppState;
import services.DatabaseService;

import java.io.IOException;
import java.io.PipedReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class WaiterController implements Initializable {
    @FXML private TableView<MenuItem> foodtable;
    @FXML private TableColumn<MenuItem, String> foodname;
    @FXML private TableColumn<MenuItem, Double> foodprice;
    @FXML private ListView drinkslist;
    @FXML private TableView<MenuItem> ordertable;
    @FXML private TableColumn<MenuItem, String> ordereditem;
    @FXML private ComboBox tableno;
    @FXML private ListView deliveryorderslist;
    @FXML private Label namelabel;

    private ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> orderitems = FXCollections.observableArrayList();
    private ObservableList<Integer> tables = FXCollections.observableArrayList();
    private ObservableList<String> drinksitems = FXCollections.observableArrayList();
    //private ObservableList<String> deliveryorders = FXCollections.observableArrayList();

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    public void itemSelect(MouseEvent event){
        MenuItem itemSelected = foodtable.getSelectionModel().getSelectedItem();
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

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppState appState = AppState.getAppState();

        namelabel.setText(appState.getUser().getFirstName());
        foodname.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        foodprice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
        foodtable.setItems(fooditems);

        drinkslist.setItems(drinksitems);
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
                drinksitems.add(rs.getString(2) + ": £" + rs.getString(4));
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
