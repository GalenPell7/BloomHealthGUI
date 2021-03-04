package BloomHealthGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class FreezeList
{
    @FXML
    private TableView<BloomClient> freezeList;
    @FXML
    private TableColumn<BloomClient,String> firstNames;
    @FXML
    private TableColumn<BloomClient,String> lastNames;
    @FXML
    private TableColumn<BloomClient,String> phoneNumbers;
    @FXML
    private TableColumn<BloomClient,String> birthdays;
    @FXML
    private TableColumn<BloomClient,Long> daysLeft;

    private DBconnect dBconnect = new DBconnect();

    private ObservableList<BloomClient> clients;

    public FreezeList() throws SQLException, ClassNotFoundException {
    }
    @FXML
    private void initialize()
    {
        clients = FXCollections.observableArrayList();
        firstNames.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastNames.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        phoneNumbers.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        birthdays.setCellValueFactory(new PropertyValueFactory<>("Birthday"));
        daysLeft.setCellValueFactory(new PropertyValueFactory<>("DaysLeft"));
        try {
            clients = dBconnect.getFreezeList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        freezeList.setItems(clients);
    }
    public void unFreeze() throws SQLException {
        BloomClient person = freezeList.getSelectionModel().getSelectedItem();
        dBconnect.unFreezeClient(person);
        setDelete();
    }
    public void setDelete() throws SQLException {
        BloomClient person = freezeList.getSelectionModel().getSelectedItem();
        dBconnect.deleteFreezedClient(person);
        freezeList.getItems().remove(person);
        freezeList.refresh();
    }
    public void refresh()
    {
        clients.clear();
        initialize();
    }
}
