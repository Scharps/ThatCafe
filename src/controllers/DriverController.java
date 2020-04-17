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
import javafx.stage.Stage;
import models.DeliveryOrder;
import models.Rota;
import services.AppState;
import services.DatabaseService;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DriverController implements Initializable {
    AppState appState = AppState.getAppState();
    @FXML private ListView availableList;
    @FXML private ListView myList;

    ObservableList<DeliveryOrder> availableOrders = FXCollections.observableArrayList();
    ObservableList<DeliveryOrder> myOrders = FXCollections.observableArrayList();

    @FXML
    private Label monStartLabel;
    @FXML
    private Label monEndLabel;
    @FXML
    private Label tueStartLabel;
    @FXML
    private Label tueEndLabel;
    @FXML
    private Label wedStartLabel;
    @FXML
    private Label wedEndLabel;
    @FXML
    private Label thuStartLabel;
    @FXML
    private Label thuEndLabel;
    @FXML
    private Label friStartLabel;
    @FXML
    private Label friEndLabel;
    @FXML
    private Label satStartLabel;
    @FXML
    private Label satEndLabel;
    @FXML
    private Label sunStartLabel;
    @FXML
    private Label sunEndLabel;
    @FXML
    private DatePicker workedHoursDatePicker;
    @FXML
    private Spinner hoursWorkedSpinner;
    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initialiseAvailable();
            initializeMyRota();
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in initializing Driver UI. " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void initialiseAvailable() throws SQLException {
        availableOrders.clear();
        Connection conn = DatabaseService.getConnection();
        ResultSet rs = DeliveryOrder.getUnassigned(conn);
        while(rs.next()){
            availableOrders.add(DeliveryOrder.orderFromRS(rs));
        }
        conn.close();
        availableList.getItems().addAll(availableOrders);
    }

    public void saveWorkedHours() {
        if(!workedHoursDatePicker.getValue().isAfter(LocalDate.now())) {
            try {
                AppState.getAppState().getStaff().setHoursWorked(
                        DatabaseService.getConnection(),
                        Date.valueOf(workedHoursDatePicker.getValue()),
                        (Integer) hoursWorkedSpinner.getValue()
                );
                saveButton.setText("Saved!");
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error in updating worked hours. " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "You can't set hours for the future!",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void initializeMyRota() throws SQLException {
        Rota myRota = Rota.getRota(
                DatabaseService.getConnection(),
                AppState.getAppState().getStaff().getRota().getRotaId()
        );
        monStartLabel.setText(myRota.getMondayShift().getStartTime());
        monEndLabel.setText(myRota.getMondayShift().getFinishTime());
        tueStartLabel.setText(myRota.getTuesdayShift().getStartTime());
        tueEndLabel.setText(myRota.getTuesdayShift().getFinishTime());
        wedStartLabel.setText(myRota.getWednesdayShift().getStartTime());
        wedEndLabel.setText(myRota.getWednesdayShift().getFinishTime());
        thuStartLabel.setText(myRota.getThursdayShift().getStartTime());
        thuEndLabel.setText(myRota.getThursdayShift().getFinishTime());
        friStartLabel.setText(myRota.getFridayShift().getStartTime());
        friEndLabel.setText(myRota.getFridayShift().getFinishTime());
        satStartLabel.setText(myRota.getSaturdayShift().getStartTime());
        satEndLabel.setText(myRota.getSaturdayShift().getFinishTime());
        sunStartLabel.setText(myRota.getSundayShift().getStartTime());
        sunEndLabel.setText(myRota.getSundayShift().getFinishTime());
        workedHoursDatePicker.getChronology().dateNow();
    }

    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }
}
