package BloomHealthGUI;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class DayPass {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;

    private DBconnect dBconnect;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        dBconnect = new DBconnect();

    }
    public void sendToSignIn() throws SQLException {
        dBconnect.sendToRoster(firstName.getText(),lastName.getText());
        firstName.clear();
        lastName.clear();
}
}
