package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import models.*;
import models.MenuItem;
import services.AppState;
import services.DatabaseService;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Responsible for carrying out the functionality of the Manager user interface.
 * @author Sam James
 */
public class ManagerController implements Initializable {
    @FXML
    private ListView staffMembersListView;
    @FXML
    private ComboBox positionCombo;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox staffMemberCombo;
    @FXML
    private ComboBox monStartCombo;
    @FXML
    private ComboBox monEndCombo;
    @FXML
    private ComboBox tueStartCombo;
    @FXML
    private ComboBox tueEndCombo;
    @FXML
    private ComboBox wedStartCombo;
    @FXML
    private ComboBox wedEndCombo;
    @FXML
    private ComboBox thuStartCombo;
    @FXML
    private ComboBox thuEndCombo;
    @FXML
    private ComboBox friStartCombo;
    @FXML
    private ComboBox friEndCombo;
    @FXML
    private ComboBox satStartCombo;
    @FXML
    private ComboBox satEndCombo;
    @FXML
    private ComboBox sunStartCombo;
    @FXML
    private ComboBox sunEndCombo;
    private Rota staffMemberRota;

    @FXML
    private ListView popularItemsListView;
    @FXML
    private BarChart barChart;
    @FXML
    private Label mostActiveCustomerLabel;
    @FXML
    private Label highestHoursWorkedLabel;

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
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ComboBox positionCombo2;

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

    /**
     * Initialises list of Staff members.
     */
    private void initializeStaffMembersTab() {
        try {
            staffMembersListView.getItems().clear();
            positionCombo.getItems().clear();
            positionCombo2.getItems().clear();
            staffMembersListView.getItems().addAll(
                    StaffMember.getAllStaffMembers(DatabaseService.getConnection())
            );
            positionCombo.getItems().addAll(StaffPosition.values());
            positionCombo2.getItems().addAll(StaffPosition.values());
            positionCombo.getSelectionModel().selectFirst();
            positionCombo2.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in getting staff members.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Edit details of staff members in Staff members list.
     */
    public void updateDetails() {
        if(staffMembersListView.getSelectionModel().getSelectedItem() != null) {
            StaffMember sm = (StaffMember) staffMembersListView.getSelectionModel().getSelectedItem();
            firstNameField.setText(sm.getFirstName());
            lastNameField.setText(sm.getLastName());
            positionCombo2.getSelectionModel().select(sm.getPosition());
        }
    }

    /**
     * Safe changes to Staff members details made in updateDetails method.
     */
    public void saveDetails() {
        if(staffMembersListView.getSelectionModel().getSelectedItem() != null) {
            StaffMember sm = (StaffMember) staffMembersListView.getSelectionModel().getSelectedItem();
            try {
                StaffMember.updateStaffMember(
                        DatabaseService.getConnection(),
                        sm.getId(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        (StaffPosition) positionCombo2.getSelectionModel().getSelectedItem()
                );
                initializeStaffMembersTab();
            }catch(SQLException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error in updating staffmember.\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "No staffmember selected.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Initialises information in rota tab.
     */
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in getting staff member rota details. " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Displays current rota for staff member selected.
     */
    public void onStaffMemberChosen() {
        try {
            StaffMember staffMember = (StaffMember) staffMemberCombo.getSelectionModel().getSelectedItem();
            if (staffMember != null) {
                staffMemberRota = Rota.getRota(
                        DatabaseService.getConnection(),
                        staffMember.getRota().getRotaId()
                );
                updateRotaCombos();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in getting staff member rota details. " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Update rota information
     */
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

    /**
     * Allows staff member to input their hours worked for a specified day.
     */
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

    /**
     * Initialises Rota information for current Staff member.
     * @throws SQLException
     */
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

    /**
     * Save Rota for Staff member selected.
     */
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in updating staff member rota details. " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Returns user to previous page.
     * @param event
     * @throws IOException
     */
    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    /**
     * Delete selected staff member.
     */
    public void removeSelected() {
        StaffMember selectedStaffMember = (StaffMember) staffMembersListView.getSelectionModel().getSelectedItem();
        if (selectedStaffMember != null) {
            try {
                if (selectedStaffMember.getId() != AppState.getAppState().getStaff().getId()) {
                    StaffMember.deleteStaffMember(DatabaseService.getConnection(), selectedStaffMember.getId());
                    staffMembersListView.getItems().remove(selectedStaffMember);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Cannot remove yourself!",
                            "Info", JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (SQLException e) {
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

    /**
     * Creates a staff member with inputted details and selected staff position.
     */
    public void createStaffMember() {
        if (
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
            } catch (SQLException e) {
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

    /**
     * Initialises information when page is loaded.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeStaffMembersTab();
        initializeRotaTab();
        initializeReports();
    }

    /**
     * Initialises information on reports tab, gets list of most sold items.
     */
    public void initializeReports() {
        try {
            popularItemsListView.getItems().clear();
            popularItemsListView.getItems().addAll(
                    MenuItem.getTopMostSold(
                            DatabaseService.getConnection(),
                            5
                    )
            );
            initializeBarChart();
            initializeStats();
            initializeMyRota();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error retrieving report information." + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Initialises report information for most active customer and staff member with highest hours worked.
     * @throws SQLException
     */
    private void initializeStats() throws SQLException {
        Customer mostActive = Customer.getMostActive(DatabaseService.getConnection());
        if (mostActive != null) {
            mostActiveCustomerLabel.setText(
                    String.format("%s %s", mostActive.getFirstName(), mostActive.getLastName())
            );
        } else {
            mostActiveCustomerLabel.setText("No customers!");
        }
        Pair<StaffMember, Integer> mostHours = StaffMember.getTopStaffMemberPast7Days(DatabaseService.getConnection());
        if(mostHours != null) {
            highestHoursWorkedLabel.setText(String.format("%s: %s %s, %d hours", mostHours.getKey().getPosition(), mostHours.getKey().getFirstName(), mostHours.getKey().getLastName(), mostHours.getValue()));
        } else {
            highestHoursWorkedLabel.setText("Your staff haven't worked any hours\n in the past week.");
        }
    }

    /**
     * Initialises bar chart that displays busiest restaurant periods.
     * @throws SQLException
     */
    private void initializeBarChart() throws SQLException {
        barChart.getXAxis().setLabel("Date");
        barChart.getXAxis().setTickLabelRotation(70);
        barChart.getYAxis().setLabel("Number of Bookings");
        barChart.setLegendVisible(false);
        HashMap<String, Integer> bookings = Booking.getBusyPeriods(DatabaseService.getConnection());
        barChart.getData().clear();
        XYChart.Series dataSeries = new XYChart.Series();
        bookings.forEach((k, v) -> dataSeries.getData().add(new XYChart.Data<>(k, v)));
        barChart.getData().add(dataSeries);
    }

}
