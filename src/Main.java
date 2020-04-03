import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gui/Login_ui.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static Connection connect(Connection con){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Cafe94", "root", "CafeCovidCSCM94");
        }
        catch(Exception e){
            System.out.println("error");
        }
        return con;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
