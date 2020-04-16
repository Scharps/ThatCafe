package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Rota;
import models.StaffMember;
import models.StaffPosition;
import services.AppState;
import services.DatabaseService;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;


public class ManagerController {
    @FXML private ListView staffMembersListView;
    @FXML private ComboBox positionCombo;
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private PasswordField passwordField;

    @FXML private ComboBox staffMemberCombo;
    @FXML private ComboBox monStartCombo;
    @FXML private ComboBox monEndCombo;
    @FXML private ComboBox tueStartCombo;
    @FXML private ComboBox tueEndCombo;
    @FXML private ComboBox wedStartCombo;
    @FXML private ComboBox wedEndCombo;
    @FXML private ComboBox thuStartCombo;
    @FXML private ComboBox thuEndCombo;
    @FXML private ComboBox friStartCombo;
    @FXML private ComboBox friEndCombo;
    @FXML private ComboBox satStartCombo;
    @FXML private ComboBox satEndCombo;
    @FXML private ComboBox sunStartCombo;
    @FXML private ComboBox sunEndCombo;

    public void initializeStaffMembersTab() {
        try {
            staffMembersListView.getItems().clear();
            positionCombo.getItems().clear();
            staffMembersListView.getItems().addAll(
                StaffMember.getAllStaffMembers(DatabaseService.getConnection())
            );
            positionCombo.getItems().addAll(StaffPosition.values());
            positionCombo.getSelectionModel().selectFirst();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(
                null,
                "Error in getting staff members.\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void initializeRotaTab() {
        try {
            staffMemberCombo.getItems().clear();
            staffMemberCombo.getItems().addAll(
                StaffMember.getAllStaffMembers(
                    DatabaseService.getConnection()
                )
            );
        } catch(SQLException e) {

        }
    }

    public void onStaffMemberChosen() {

    }

    public void saveHours() {

    }

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    public void removeSelected() {
        StaffMember selectedStaffMember = (StaffMember) staffMembersListView.getSelectionModel().getSelectedItem();
        if(selectedStaffMember != null) {
            try {
                if(selectedStaffMember.getId() != AppState.getAppState().getUser().getId()) {
                    StaffMember.deleteStaffMember(DatabaseService.getConnection(), selectedStaffMember.getId());
                    staffMembersListView.getItems().remove(selectedStaffMember);
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Cannot remove yourself!",
                        "Info", JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error in removing staff member. " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                null,
                "Select a staff member from the list to remove.",
                "Info", JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void createStaffMember() {
        if(
            !nameTextField.getText().isEmpty() ||
            !surnameTextField.getText().isEmpty() ||
            !passwordField.getText().isEmpty()
        ) {
            try {
                staffMembersListView.getItems().add(
                    StaffMember.createStaffMember(
                        DatabaseService.getConnection(),
                        passwordField.getText(),
                        nameTextField.getText(),
                        surnameTextField.getText(),
                        (StaffPosition) positionCombo.getSelectionModel().getSelectedItem(),
                        Rota.createRota(DatabaseService.getConnection())
                    )
                );
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error in creating a staff member. " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                null,
                "A field is empty.",
                "Info", JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
