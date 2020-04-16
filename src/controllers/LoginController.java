package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;

import models.Address;
import models.Customer;
import services.AppState;
import services.DatabaseService;


public class LoginController {

    @FXML private TextField customerusername;
    @FXML private PasswordField customerpassword;
    @FXML private TextField customerloginerror;
    @FXML private TextField registerusername;
    @FXML private TextField registername;
    @FXML private TextField registersurname;
    @FXML private TextField registeraddress;
    @FXML private TextField registercity;
    @FXML private TextField registerpostcode;
    @FXML private PasswordField registerpassword;
    @FXML private PasswordField confirmpassword;
    @FXML private TextArea registererror;
    @FXML private TextField stafflogin;
    @FXML private TextField staffpassword;

    public void loginpushed(ActionEvent event) throws IOException {
        customerloginerror.setText("");
        customerusername.setStyle("");
        customerpassword.setStyle("");

        if(customerusername.getText().equals("")) {
            customerloginerror.setText("Please insert username");
            customerusername.setStyle("-fx-border-color: red;");
            if(customerpassword.getText().equals("")){
                customerpassword.setStyle("-fx-border-color: red;");
                customerloginerror.appendText(" and password");
            }
        }
        else if(customerpassword.getText().equals("")){
            customerloginerror.setText("Please insert password");
            customerpassword.setStyle("-fx-border-color: red;");
        }

        else {
            try {
                Connection conn = DatabaseService.getConnection();
                Customer confirmLogin = Customer.customerLogin(conn, customerusername.getText(), customerpassword.getText());
                conn.close();
                if(confirmLogin != null) {
                    AppState appState = AppState.getAppState();
                    appState.setCustomer(confirmLogin);
                    Parent customerParent = FXMLLoader.load(getClass().getResource("/gui/CustomerUI.fxml"));
                    Scene customerScene = new Scene(customerParent);
                    Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
                    window.setScene(customerScene);
                    window.show();

                } else {
                    customerloginerror.setText("Username or Password incorrect");
                    customerusername.setStyle("-fx-border-color: red;");
                    customerpassword.setStyle("-fx-border-color: red;");
                }
            } catch(SQLException se){
            }

        }
    }

    public void registerPushed(ActionEvent event) throws IOException {
        registererror.setText("");
        registerusername.setStyle("");
        registername.setStyle("");
        registersurname.setStyle("");
        registeraddress.setStyle("");
        registercity.setStyle("");
        registerpostcode.setStyle("");
        registerpassword.setStyle("");
        confirmpassword.setStyle("");
        boolean check = true;
        if(registerusername.getText().equals("")) {
            registererror.appendText("Please input username \n");
            registerusername.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(registername.getText().equals("")) {
            registererror.appendText("Please input name \n");
            registername.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(registersurname.getText().equals("")) {
            registererror.appendText("Please input surname \n");
            registersurname.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(registeraddress.getText().equals("")) {
            registererror.appendText("Please input address line 1\n");
            registeraddress.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(registercity.getText().equals("")) {
            registererror.appendText("Please input city \n");
            registercity.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(registerpostcode.getText().equals("")) {
            registererror.appendText("Please input postcode \n");
            registerpostcode.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(registerpassword.getText().equals("")) {
            registererror.appendText("Please input password \n");
            registerpassword.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(confirmpassword.getText().equals("")) {
            registererror.appendText("Please confirm password \n");
            confirmpassword.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(!registerpassword.getText().equals(confirmpassword.getText())) {
            registererror.appendText("Passwords do not match \n");
            registerpassword.setStyle("-fx-border-color: red;");
            confirmpassword.setStyle("-fx-border-color: red;");
            check = false;
        }
        if(check == true) {
            try {
                Connection conn = DatabaseService.getConnection();
                Address address = Address.createAddress(conn, registeraddress.getText(), registercity.getText(), registerpostcode.getText());
                Customer.createCustomer(conn, registerusername.getText(), registerpassword.getText(), registername.getText(), registersurname.getText(), address);
                registererror.setText("Thank you for registering");
                registerusername.setText("");
                registername.setText("");
                registersurname.setText("");
                registeraddress.setText("");
                registercity.setText("");
                registerpostcode.setText("");
                registerpassword.setText("");
                confirmpassword.setText("");
                conn.close();
            }
            catch(SQLIntegrityConstraintViolationException se) {
                registererror.setText("Username is already in use");
            } catch (SQLException se2) {
                se2.printStackTrace();
                System.out.println(se2);
            }
            catch(Exception ex) {
                registererror.setText("ERROR");
            }
        }
    }


    public void staffLogin(ActionEvent event) throws IOException{
        String LOGIN = "Staff";
        String PASSWORD = "1234";
        if(stafflogin.getText().equals(LOGIN) && staffpassword.getText().equals(PASSWORD)){
            Parent staffParent = FXMLLoader.load(getClass().getResource("/gui/StaffProfiles.fxml"));
            Scene staffScene = new Scene(staffParent);
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(staffScene);
            window.show();
        }

    }
}
