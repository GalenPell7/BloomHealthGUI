package BloomHealthGUI;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static javafx.fxml.FXMLLoader.load;

public class EventPlanner {
    @FXML
    private TextField titleField;
    @FXML
    private TextField trainerField;
    @FXML
    private TextField startTime;
    @FXML
    private TextField date;
    @FXML
    private TextField endTime;
    @FXML
    private CheckBox monday;
    @FXML
    private CheckBox tuesday;
    @FXML
    private CheckBox wednesday;
    @FXML
    private CheckBox thursday;
    @FXML
    private CheckBox friday;
    @FXML
    private CheckBox saturday;
    @FXML
    private CheckBox sunday;

    private DBconnect dBconnect;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        dBconnect = new DBconnect();
        monday.setText("Mon. ");
        monday.setSelected(false);
        tuesday.setText("Tues. ");
        tuesday.setSelected(false);
        wednesday.setText("Wed. ");
        wednesday.setSelected(false);
        thursday.setText("Thurs. ");
        thursday.setSelected(false);
        friday.setText("Fri. ");
        friday.setSelected(false);
        saturday.setText("Sat. ");
        saturday.setSelected(false);
        sunday.setText("Sun. ");
        sunday.setSelected(false);
    }
    public void sendToEvents() throws SQLException {
        Event event = new Event();
        String days = "";
        event.setTitle(titleField.getText());
        event.setTrainer(trainerField.getText());
        event.setStartTime(startTime.getText());
        event.setEndTime(endTime.getText());
        if (monday.isSelected())
            days += monday.getText();
        if (tuesday.isSelected())
            days += tuesday.getText();
        if (wednesday.isSelected())
            days += wednesday.getText();
        if (thursday.isSelected())
            days += thursday.getText();
        if (friday.isSelected())
            days += friday.getText();
        if (saturday.isSelected())
            days += saturday.getText();
        if (sunday.isSelected())
            days += sunday.getText();
        event.setDays(days);
        event.setDate(date.getText());
        dBconnect.sendToEvents(event);
        titleField.clear();
        trainerField.clear();
        startTime.clear();
        endTime.clear();
        date.clear();
        monday.setSelected(false);
        tuesday.setSelected(false);
        wednesday.setSelected(false);
        thursday.setSelected(false);
        friday.setSelected(false);
        saturday.setSelected(false);
        sunday.setSelected(false);
    }
    public void showEvents()
    {
        try {
            Parent root = load(getClass().getResource("EventList.fxml"));
            Stage stage = new Stage();
            stage.setTitle("EventList");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
