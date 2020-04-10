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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import services.DatabaseService;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class WaiterController implements Initializable {
    @FXML private ListView foodlist;
    @FXML private ListView drinkslist;

    private ObservableList<String> fooditems = FXCollections.observableArrayList();
    private ObservableList<String> drinksitems = FXCollections.observableArrayList();

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        foodlist.setItems(fooditems);
        try {
            Connection conn = DatabaseService.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM MenuItems WHERE ItemType = 'Food'");
            while(rs.next()){
                fooditems.add(rs.getString(2) + ": £" + rs.getString(4));
            }
            conn.close();

        }
        catch (SQLException se){
            se.printStackTrace();
            System.out.println(se);
        }

        drinkslist.setItems(drinksitems);
        try {
            Connection conn = DatabaseService.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM MenuItems WHERE ItemType = 'Drink'");
            while(rs.next()){
                drinksitems.add(rs.getString(2) + ": £" + rs.getString(4));
            }
            conn.close();

        }
        catch (SQLException se){
            se.printStackTrace();
            System.out.println(se);
        }
    }
}
