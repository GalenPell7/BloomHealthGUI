package BloomHealthGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;

public class SignInList {
    @FXML
    private Label label;
    @FXML
    private TableView<BloomClient> signInList;
    @FXML
    private TableColumn<BloomClient,String> firstNames;
    @FXML
    private TableColumn<BloomClient,String> lastNames;
    @FXML
    private TableColumn<BloomClient,String> date;
    @FXML
    private TableColumn<BloomClient,String> time;
    @FXML
    private TextField filteredSearch;

    private DBconnect dBconnect = new DBconnect();

    private ObservableList<BloomClient> clients;

    public SignInList() throws SQLException, ClassNotFoundException {
    }
    @FXML
    private void initialize()
    {
        clients = FXCollections.observableArrayList();
        firstNames.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastNames.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        time.setCellValueFactory(new PropertyValueFactory<>("Time"));
        try {
            clients = dBconnect.getSignInSheet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FilteredList<BloomClient> filteredList = new FilteredList<BloomClient>(clients, b -> true);
        filteredSearch.textProperty().addListener(((observable, oldValue, newValue) ->
                filteredList.setPredicate(person ->
                {
                    if(newValue == null || newValue.isEmpty())
                        return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (person.getFirstName().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else if (person.getLastName().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else
                        return false;
                })
        ));
        SortedList<BloomClient> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(signInList.comparatorProperty());
        signInList.setItems(sortedList);
    }
    public void clearList() throws SQLException {
        dBconnect.clearSignInSheet();
        clients.clear();
    }
    public void attendanceCount() throws SQLException {
        int i = dBconnect.countAttendance();
        label.setText("Count: " + i);
    }
}
