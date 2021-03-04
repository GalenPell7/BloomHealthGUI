package BloomHealthGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class BirthdayTable {
    @FXML
    private TableView<BloomClient> birthdayList;
    @FXML
    private TableColumn<BloomClient,String> firstNames;
    @FXML
    private TableColumn<BloomClient,String> lastNames;
    @FXML
    private TableColumn<BloomClient,String> phoneNumbers;

    private DBconnect connect = new DBconnect();

    public BirthdayTable() throws SQLException, ClassNotFoundException {
    }

    @FXML
    private void initialize()
    {
        ObservableList<BloomClient> clients = FXCollections.observableArrayList();
        firstNames.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastNames.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        phoneNumbers.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        try {
            clients = connect.getBirthdayList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        birthdayList.setItems(clients);
    }
}
