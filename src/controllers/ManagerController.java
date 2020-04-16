package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Rota;
import models.Shift;
import models.StaffMember;
import models.StaffPosition;
import services.AppState;
import services.DatabaseService;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class ManagerController implements Initializable {
    @FXML private ListView staffMembersListView;
    @FXML private ComboBox positionCombo;
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private PasswordField passwordField;

    @FXML private Label statusLabel;
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
    private Rota staffMemberRota;

    private final static String[] TIMES = {
            "None",
            "8:00",
            "9:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00",
            "22:00",
            "23:00",
            "00:00"
    };

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
            monStartCombo.getItems().addAll(TIMES);
            monStartCombo.getSelectionModel().selectFirst();
            monEndCombo.getItems().addAll(TIMES);
            monEndCombo.getSelectionModel().selectFirst();
            tueStartCombo.getItems().addAll(TIMES);
            tueStartCombo.getSelectionModel().selectFirst();
            tueEndCombo.getItems().addAll(TIMES);
            tueEndCombo.getSelectionModel().selectFirst();
            wedStartCombo.getItems().addAll(TIMES);
            wedStartCombo.getSelectionModel().selectFirst();
            wedEndCombo.getItems().addAll(TIMES);
            wedEndCombo.getSelectionModel().selectFirst();
            thuStartCombo.getItems().addAll(TIMES);
            thuStartCombo.getSelectionModel().selectFirst();
            thuEndCombo.getItems().addAll(TIMES);
            thuEndCombo.getSelectionModel().selectFirst();
            friStartCombo.getItems().addAll(TIMES);
            friStartCombo.getSelectionModel().selectFirst();
            friEndCombo.getItems().addAll(TIMES);
            friEndCombo.getSelectionModel().selectFirst();
            satStartCombo.getItems().addAll(TIMES);
            satStartCombo.getSelectionModel().selectFirst();
            satEndCombo.getItems().addAll(TIMES);
            satEndCombo.getSelectionModel().selectFirst();
            sunStartCombo.getItems().addAll(TIMES);
            sunStartCombo.getSelectionModel().selectFirst();
            sunEndCombo.getItems().addAll(TIMES);
            sunEndCombo.getSelectionModel().selectFirst();
        } catch(SQLException e) {

        }
    }

    public void onStaffMemberChosen() {
        try {
            StaffMember staffMember = (StaffMember) staffMemberCombo.getSelectionModel().getSelectedItem();
            staffMemberRota = Rota.getRota(
                    DatabaseService.getConnection(),
                    staffMember.getRota().getRotaId()
            );
            updateRotaCombos();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in getting staff member rota details. " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateRotaCombos() {
        monStartCombo.getSelectionModel().select(staffMemberRota.getMondayShift().getStartTime());
        monEndCombo.getSelectionModel().select(staffMemberRota.getMondayShift().getFinishTime());
        tueStartCombo.getSelectionModel().select(staffMemberRota.getTuesdayShift().getStartTime());
        tueEndCombo.getSelectionModel().select(staffMemberRota.getTuesdayShift().getFinishTime());
        wedStartCombo.getSelectionModel().select(staffMemberRota.getWednesdayShift().getStartTime());
        wedEndCombo.getSelectionModel().select(staffMemberRota.getWednesdayShift().getFinishTime());
        thuStartCombo.getSelectionModel().select(staffMemberRota.getThursdayShift().getStartTime());
        thuEndCombo.getSelectionModel().select(staffMemberRota.getThursdayShift().getFinishTime());
        friStartCombo.getSelectionModel().select(staffMemberRota.getFridayShift().getStartTime());
        friEndCombo.getSelectionModel().select(staffMemberRota.getFridayShift().getFinishTime());
        satStartCombo.getSelectionModel().select(staffMemberRota.getSaturdayShift().getStartTime());
        satEndCombo.getSelectionModel().select(staffMemberRota.getSaturdayShift().getFinishTime());
        sunStartCombo.getSelectionModel().select(staffMemberRota.getSundayShift().getStartTime());
        sunEndCombo.getSelectionModel().select(staffMemberRota.getSundayShift().getFinishTime());
    }

    public void saveHours() {
        StaffMember staffMember = (StaffMember) staffMemberCombo.getSelectionModel().getSelectedItem();
        try {
            Rota.updateRota(
                DatabaseService.getConnection(),
                staffMember.getRota().getRotaId(),
                new Shift(
                    monStartCombo.getSelectionModel().getSelectedItem().toString(),
                    monEndCombo.getSelectionModel().getSelectedItem().toString()
                ),
                new Shift(
                    tueStartCombo.getSelectionModel().getSelectedItem().toString(),
                    tueEndCombo.getSelectionModel().getSelectedItem().toString()
                ),
                new Shift(
                    wedStartCombo.getSelectionModel().getSelectedItem().toString(),
                    wedEndCombo.getSelectionModel().getSelectedItem().toString()
                ),
                new Shift(
                    thuStartCombo.getSelectionModel().getSelectedItem().toString(),
                    thuEndCombo.getSelectionModel().getSelectedItem().toString()
                ),
                new Shift(
                    friStartCombo.getSelectionModel().getSelectedItem().toString(),
                    friEndCombo.getSelectionModel().getSelectedItem().toString()
                ),
                new Shift(
                    satStartCombo.getSelectionModel().getSelectedItem().toString(),
                    satEndCombo.getSelectionModel().getSelectedItem().toString()
                ),
                new Shift(
                    sunStartCombo.getSelectionModel().getSelectedItem().toString(),
                    sunEndCombo.getSelectionModel().getSelectedItem().toString()
                )
            );
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in updating staff member rota details. " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
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
                if(selectedStaffMember.getId() != AppState.getAppState().getStaff().getId()) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeStaffMembersTab();
        initializeRotaTab();
    }
}
