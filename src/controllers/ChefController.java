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
import models.*;
import models.MenuItem;
import services.AppState;
import services.DatabaseService;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Responsible for carrying out the functionality of the Chef user interface.
 * @author Ashley Forster, Sam James
 */
public class ChefController implements Initializable {

    @FXML private TableView<Order> currentOrderTable;
    @FXML private TableColumn<Order, Integer> currentOrderNo;
    @FXML private TableColumn<Order, Timestamp> currentOrderDate;
    @FXML private TableColumn<Order, String> currentOrderType;

    @FXML private TableView<MenuItem> currentItemTable;
    @FXML private TableColumn<MenuItem, String> currentOrderItem;

    @FXML private TextField newItemName;
    @FXML private TextField newItemPrice;
    @FXML private ComboBox<String> newItemType;
    @FXML private CheckBox newSpecial;

    @FXML private TableView<MenuItem> foodTable;
    @FXML private TableColumn<MenuItem, String> foodName;
    @FXML private TableColumn<MenuItem, Double> foodPrice;
    @FXML private TableView<MenuItem> drinkTable;
    @FXML private TableColumn<MenuItem, String> drinkName;
    @FXML private TableColumn<MenuItem, Double> drinkPrice;
    @FXML private TableView<MenuItem> specialsTable;
    @FXML private TableColumn<MenuItem, String> specialsName;
    @FXML private TableColumn<MenuItem, Double> specialsPrice;

    private final ObservableList<Order> currentOrders = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> currentOrderItems = FXCollections.observableArrayList();
    private final ObservableList<String> newTypeList = FXCollections.observableArrayList();

    private final ObservableList<MenuItem> fooditems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> specialitems = FXCollections.observableArrayList();
    private final ObservableList<MenuItem> drinksitems = FXCollections.observableArrayList();

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

    /**
     * Returns user to previous page.
     * @param event
     * @throws IOException
     */
    public void logoutPushed(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    /**
     * Select order from list of uncooked orders, displays MenuItems for that order.
     */
    public void selectOrder(){
        Order selectedOrder = currentOrderTable.getSelectionModel().getSelectedItem();
        currentOrderItems.clear();
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = MenuItem.getOrderItems(conn, selectedOrder.getOrderId());
            if(rs.next()) {
                while (rs.next()) {
                    currentOrderItems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                }
                currentOrderItem.setCellValueFactory(new PropertyValueFactory<>("name"));
                currentItemTable.setItems(currentOrderItems);
            }
            conn.close();
        } catch (SQLException se){
            System.out.println("help");
        }
    }

    /**
     * Allows chef to confirm selected order as Cooked.
     */
    public void confirmCooked(){
        Order selectedOrder = currentOrderTable.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DatabaseService.getConnection();
            selectedOrder.setCooked(conn);
            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        currentOrders.clear();
        currentOrderItems.clear();
        initialiseCurrentOrders();

    }

    /**
     * Allows chef add MenuItems to menu, depending on inputted information.
     */
    public void addMenuItem(){
        if(newItemName.getText().equals("") || newItemType.getValue()==null){
        }
        else {
            String newName = newItemName.getText();
            String newType = newItemType.getValue();

            boolean special = newSpecial.isSelected();
            try {
                double newPrice = Double.parseDouble(newItemPrice.getText());
                Connection conn = DatabaseService.getConnection();
                MenuItem.createNewItem(conn, newName, newType, newPrice, special);
                conn.close();
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(null, ne.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            } catch (SQLException se) {
                JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            newItemName.clear();
            newItemPrice.clear();
            newSpecial.setSelected(false);
            newItemType.getSelectionModel().clearSelection();
            initialiseMenu();
        }
    }

    /**
     * Removes selected FoodItem from the Menu.
     */
    public void removeFoodItem(){
        MenuItem selectedItem = foodTable.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DatabaseService.getConnection();
            MenuItem.deleteMenuItem(conn, selectedItem.getId());
            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        initialiseMenu();
    }

    /**
     * Removes selected DrinkItem from the Menu.
     */
    public void removeDrinkItem(){
        MenuItem selectedItem = drinkTable.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DatabaseService.getConnection();
            MenuItem.deleteMenuItem(conn, selectedItem.getId());
            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        initialiseMenu();
    }

    /**
     * Removes selected SpecialsItem from the Menu.
     */
    public void removeSpecialItem(){
        MenuItem selectedItem = specialsTable.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DatabaseService.getConnection();
            MenuItem.deleteMenuItem(conn, selectedItem.getId());
            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        initialiseMenu();
    }

    /**
     * Initialises information when page is loaded.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseCurrentOrders();
        initialiseMenu();
        try {
            initializeMyRota();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error in initializing my rota. " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
        newTypeList.add(String.valueOf(MenuItemType.Food));
        newTypeList.add(String.valueOf(MenuItemType.Drink));
        newItemType.setItems(newTypeList);


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
     * Initialises current uncooked orders in current Orders table.
     */
    private void initialiseCurrentOrders(){
        currentOrderNo.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        currentOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        currentOrderType.setCellValueFactory(new PropertyValueFactory<>("orderType"));
        currentOrderTable.setItems(currentOrders);

        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = Order.getUncookedOrders(conn);
            while(rs.next()) {
                if (rs.getString(6) == "Delivery") {
                    if (DeliveryOrder.isApproved(conn, rs.getInt(1))) {
                        currentOrders.add(Order.orderFromRS(rs));
                    }
                } else {
                    currentOrders.add(Order.orderFromRS(rs));
                }
            }
            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
     * Initialises MenuItems in Menu Tables.
     */
    private void initialiseMenu(){
        fooditems.clear();
        drinksitems.clear();
        specialitems.clear();

        foodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        foodTable.setItems(fooditems);
        drinkName.setCellValueFactory(new PropertyValueFactory<>("name"));
        drinkPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        drinkTable.setItems(drinksitems);
        specialsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        specialsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        specialsTable.setItems(specialitems);
        try{
            Connection conn = DatabaseService.getConnection();
            ResultSet rs = MenuItem.getMenuItems(conn, false);
            while(rs.next()) {
                if (rs.getString(3).equals(String.valueOf(MenuItemType.Food))){
                    fooditems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.Food, rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                } else {
                    drinksitems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.Drink, rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
                }
            }

            rs = MenuItem.getMenuItems(conn, true);
            while(rs.next()){
                specialitems.add(MenuItem.createMenuItem(rs.getInt(1), rs.getString(2), MenuItemType.valueOf(rs.getString(3)), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6)));
            }
            conn.close();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, se.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}
