package BloomHealthGUI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

import static javafx.fxml.FXMLLoader.load;

public class BloomHealthGUI extends Application
{
    private DBconnect dBconnect;
    public static void main(String[] args)
    {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = load(getClass().getResource("BloomHealthGUI.fxml"));
        primaryStage.setScene(new Scene(root,600,600));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
    public void reConnect() throws SQLException, ClassNotFoundException {
        dBconnect = new DBconnect();
        dBconnect.reConnect();
    }
}

