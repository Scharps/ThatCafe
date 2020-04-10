package cafe.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.*;

public class BookingController {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  //change ??
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

    @FXML
    Label nameLabel;

    @FXML
    ComboBox<Integer> guestsBox;

    @FXML
    ComboBox<String> dateBox;

    @FXML
    ComboBox<String> timeBox;

    @FXML
    ComboBox<Integer> tablesBox;

    @FXML
    Button confirmButton;

    @FXML
    Button exitButton;

    private int customerId;
    private String name;


    @FXML
    public void initialize() {
        guestsBox.setItems(FXCollections.observableArrayList(getGuestNumbers()));
        guestsBox.getSelectionModel().select(1);

        List<String> dateStrings = new ArrayList<>();
        List<Calendar> dates = getAvailableDates();
        for (Calendar date: dates) {
            dateStrings.add(dateFormat.format(date.getTime()));
        }
        dateBox.setItems(FXCollections.observableArrayList(dateStrings));
        dateBox.getSelectionModel().select(0);
        dateChanged();
    }

    public void setCustomer(int customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        nameLabel.setText(name);
    }

    @FXML
    public void dateChanged() {
        timeBox.getItems().clear();
        tablesBox.getItems().clear();

        try {
            Date date = dateFormat.parse(dateBox.getSelectionModel().getSelectedItem());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            List<String> timeStrings = new ArrayList<>();
            List<Calendar> times = getAvailableTimeForDate(calendar);
            for (Calendar time: times) {
                timeStrings.add(timeFormat.format(time.getTime()));
            }
            timeBox.setItems(FXCollections.observableArrayList(timeStrings));
        } catch (Exception ex) {
            System.out.println("internal error");
        }
    }

    private Calendar uniteDateAndTime() {
        try {
            Date date = dateFormat.parse(dateBox.getSelectionModel().getSelectedItem());
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(date);

            Date time = timeFormat.parse(timeBox.getSelectionModel().getSelectedItem());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTime(time);
            int h = calendarTime.get(Calendar.HOUR_OF_DAY);
            int m = calendarTime.get(Calendar.MINUTE);

            calendarDate.set(Calendar.HOUR, h);
            calendarDate.set(Calendar.MINUTE, m);

            return calendarDate;
        } catch (Exception ex) {
            return null;
        }
    }

    @FXML
    public void timeChanged() {
        tablesBox.getItems().clear();

        try {
            Calendar calendarDate = uniteDateAndTime();
            tablesBox.setItems(FXCollections.observableArrayList(getAvailableTablesForTime(calendarDate)));
        } catch (Exception ex) {
            System.out.println("error");
        }
    }

    @FXML
    public void confirmAction() {
        int customerId = 1; /////////////
        int guests = guestsBox.getSelectionModel().getSelectedItem();
        Calendar dateTime = uniteDateAndTime();
        Integer tableId = tablesBox.getSelectionModel().getSelectedItem();



        if (dateTime == null || tableId == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill all fields");
            alert.showAndWait();
            return;
        }

        String message = "Created booking for: " +  new SimpleDateFormat("yyyy/MM/dd hh:mm aa").format(dateTime.getTime()) + "\n"
                + "Customer id: " +  customerId + "\n"
                + "Number of guests: " +  guests + "\n"
                + "Table Id: " +  tableId;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
        createBooking(customerId, guests, dateTime, tableId);
    }

    @FXML
    public void exitAction() { //////////////////// replace!! ////////////
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    private static List<Integer> getGuestNumbers() {
        List<Integer> list = new ArrayList<>();
        int maxGuests = 8;

        for (int i = 1; i <= maxGuests; i++) {
            list.add(i);
        }
        return list;
    }

    //-------------------------STUBS for external methods------------------------------------//

    public static List<Calendar> getAvailableDates() {
        Calendar date = Calendar.getInstance();
        int maxDays = 14;  //2 weeks from today

        List<Calendar> list = new ArrayList<>();

        for (int d = 0; d <= maxDays; d++) {
            Calendar item = (Calendar) date.clone();
            item.add(Calendar.DATE, d);
            list.add(item);
        }
        return list;
    }


    public static List<Calendar> getAvailableTimeForDate(Calendar date) {
        int firstHour = 16;     //working hours
        int lastHour = 23;

        List<Calendar> list = new ArrayList<>();

        for (int h = firstHour; h <= lastHour; h++) {
            Calendar item = (Calendar) date.clone();

            item.set(Calendar.HOUR, h);
            item.set(Calendar.MINUTE, 0);
            item.set(Calendar.SECOND, 0);

            list.add(item);
        }
        return list;
    }

    public static List<Integer> getAvailableTablesForTime(Calendar dateTime) {
        //get free tables list from database
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            set.add((int) (Math.random() * 10));
        }
        return new ArrayList<>(set);
    }

    public static void createBooking(int customerId, int guests, Calendar dateTime, int tableId) {
        //////////////////
    }
}
