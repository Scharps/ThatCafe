package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.AppState;
import services.DatabaseService;
import models.StaffMember;
import models.StaffPosition;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class StaffProfilesController implements Initializable{
    @FXML private ListView stProfiles;
    private final ObservableList<String> items = FXCollections.observableArrayList();

    public void logoutpushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/Login_ui.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    public void profileSelect(MouseEvent event) {
        AppState appState = AppState.getAppState();
        Object profileSelected = stProfiles.getSelectionModel().getSelectedItem();
        String selected = (String) profileSelected;
        int selected_id = Integer.parseInt(selected.split("\\:")[0]);
        try {
            Connection conn = DatabaseService.getConnection();
            StaffMember staff = StaffMember.getStaffMember(conn, selected_id);
            conn.close();
            StaffPosition position = staff.getPosition();
            appState.setUser(staff);

             if (position == StaffPosition.Manager) {
                 Parent waiterParent = FXMLLoader.load(getClass().getResource("/gui/Manager_ui.fxml"));
                 Scene waiterScene = new Scene(waiterParent);
                 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 window.setScene(waiterScene);
                 window.show();
             }
             else if (position == StaffPosition.Waiter) {
                 Parent waiterParent = FXMLLoader.load(getClass().getResource("/gui/WaiterUI.fxml"));
                 Scene waiterScene = new Scene(waiterParent);
                 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 window.setScene(waiterScene);
                 window.show();
             } else if (position == StaffPosition.Chef) {
                 Parent waiterParent = FXMLLoader.load(getClass().getResource("/gui/chef_ui.fxml"));
                 Scene waiterScene = new Scene(waiterParent);
                 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 window.setScene(waiterScene);
                 window.show();
             } else if (position == StaffPosition.Driver) {
                 //ToDo create DriverUI;
             }

        }
        catch(Exception se){
            se.printStackTrace();
            System.out.println(se);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stProfiles.setItems(items);
        try {
            Connection conn = DatabaseService.getConnection();
            Statement st = conn.createStatement();
            ResultSet set = st.executeQuery("SELECT StaffID, FName, LName FROM Staff");
            while(set.next()){
                items.add(set.getInt(1) + ": " + set.getString(2) + " " + set.getString(3));
            }
            conn.close();

        }
        catch (SQLException se){

        }



    }
}