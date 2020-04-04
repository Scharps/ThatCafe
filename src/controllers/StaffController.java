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
import services.DatabaseService;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class StaffController implements Initializable{
    @FXML private ListView stProfiles;
    private ObservableList<String> items = FXCollections.observableArrayList();

    public void logoutpushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/Login_ui.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    public void profileSelect(MouseEvent event) throws IOException {
        Object profileselected = stProfiles.getSelectionModel().getSelectedItem();
        String selected = (String) profileselected;
        int selected_id = Integer.parseInt(selected.split("\\:")[0]);
        try {
            Connection conn = DatabaseService.getConnection(null);
            int role = DatabaseService.staffLogin(conn, selected_id);
            conn.close();

             if (role == 1) {
                 //ToDO create managerUI
             }
             else if (role == 2) {
                 Parent waiterParent = FXMLLoader.load(getClass().getResource("/gui/WaiterUI.fxml"));
                 Scene waiterScene = new Scene(waiterParent);
                 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 window.setScene(waiterScene);
                 window.show();
             } else if (role == 3) {
                 //ToDo create ChefUI;
             } else if (role == 4) {
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
            Connection conn = DatabaseService.getConnection(null);
            Statement st = conn.createStatement();
            ResultSet set = st.executeQuery("SELECT StaffID, FName, Surname FROM Staff");
            while(set.next()){
                items.add(set.getInt(1) + ": " + set.getString(2) + " " + set.getString(3));
            }
            conn.close();

        }
        catch (SQLException se){

        }



    }
}