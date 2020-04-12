import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.StaffMember;
import models.StaffPosition;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gui/Login_ui.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
/*
        try {
            Connection conn = DatabaseService.getConnection();
            StaffMember.createStaffMember(conn, "1234", "ash", "f", StaffPosition.Waiter);
            conn.close();
        } catch(SQLException se){
            se.printStackTrace();
            System.out.println(se);
        }

 */
    }
}
