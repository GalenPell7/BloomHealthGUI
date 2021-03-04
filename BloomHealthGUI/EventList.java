package BloomHealthGUI;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class EventList {
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event,String> title;
    @FXML
    private TableColumn<Event,String> trainer;
    @FXML
    private TableColumn<Event,String> days;
    @FXML
    private TableColumn<Event,String> startTime;
    @FXML
    private TableColumn<Event,String> endTime;
    @FXML
    private TableColumn<Event,Integer> size;
    @FXML
    private TableColumn<Event,String> date;
    @FXML
    private TextField amount;

    private DBconnect dBconnect = new DBconnect();

    private ObservableList<Event> events;

    public EventList() throws SQLException, ClassNotFoundException {
    }
    @FXML
    private void initialize() {
        events = FXCollections.observableArrayList();
        title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        trainer.setCellValueFactory(new PropertyValueFactory<>("Trainer"));
        days.setCellValueFactory(new PropertyValueFactory<>("Days"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        size.setCellValueFactory(new PropertyValueFactory<>("Size"));
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        try
        {
            events = dBconnect.getEventList();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        eventTable.setItems(events);
    }
    public void addPeople() throws SQLException {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        int a = Integer.parseInt(amount.getText());
        dBconnect.addPeopleToEvent(selectedEvent, a);
        eventTable.refresh();
        refresh();
        amount.clear();
    }
    public void deletePeople() throws SQLException
    {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        int a = Integer.parseInt(amount.getText());
        dBconnect.deletePeopleFromEvent(selectedEvent, a);
        eventTable.refresh();
        refresh();
        amount.clear();
    }
    public void deleteEvent() throws SQLException {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        dBconnect.deleteEvent(selectedEvent);
        events.remove(selectedEvent);
        refresh();
        eventTable.refresh();
    }
    public void refresh()
    {
        events.clear();
        initialize();
    }
}
