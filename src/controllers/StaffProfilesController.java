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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Responsible for carrying out the functionality of the Staff Profile user interface.
 * @author Ashley Forster
 */
public class StaffProfilesController implements Initializable{
    @FXML private ListView stProfiles;
    private final ObservableList<String> items = FXCollections.observableArrayList();

    /**
     * Returns user to previous page.
     * @param event
     * @throws IOException
     */
    public void logoutpushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/Login_ui.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    /**
     * Selects profile from List of Staff Members, depending on Staff Position of selected profile loads next page.
     * @param event
     */
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
            appState.setStaff(staff);

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
                 Parent waiterParent = FXMLLoader.load(getClass().getResource("/gui/Chef_ui.fxml"));
                 Scene waiterScene = new Scene(waiterParent);
                 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 window.setScene(waiterScene);
                 window.show();
             } else if (position == StaffPosition.Driver) {
                 Parent waiterParent = FXMLLoader.load(getClass().getResource("/gui/driver_ui.fxml"));
                 Scene waiterScene = new Scene(waiterParent);
                 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 window.setScene(waiterScene);
                 window.show();
             }

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initialises list of Staff Members when page is loaded.
     * @param url
     * @param resourceBundle
     */
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
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }



    }
}