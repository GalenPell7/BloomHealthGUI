package BloomHealthGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

public class ExpireTable {
    @FXML
    private TableView<BloomClient> expireList;
    @FXML
    private TableColumn<BloomClient,String> FirstNames;
    @FXML
    private TableColumn<BloomClient,String> LastNames;
    @FXML
    private TableColumn<BloomClient,String> PhoneNumbers;
    @FXML
    private TableColumn<BloomClient,String> EndDates;
    @FXML
    private TextField planField;
    @FXML
    private TextField sessionAmountField;
    @FXML
    private TextField trainerField;
    @FXML
    private RadioButton weeks;
    @FXML
    private RadioButton months;
    @FXML
    private TextField planPriceField;
    @FXML
    private TextField sessionPriceField;
    @FXML
    private CheckBox noSessions;
    @FXML
    private Label label;
    @FXML
    private TextField filteredSearch;

    private ToggleGroup toggleGroup;

    private ObservableList<BloomClient> clients;

    private DBconnect connect;


    public void sendToExpire() throws SQLException {
       BloomClient person = expireList.getSelectionModel().getSelectedItem();
       connect.sendToExpired(person);
       clients.remove(person);
       expireList.refresh();
    }
    public void renewClient() throws SQLException
    {
        String sessions = "Sessions";
        String renewal = "Renewal";
        String text = "";
        String financialText = "";
        String sessionFinancialText = "";
        BloomClient person = expireList.getSelectionModel().getSelectedItem();
        int planAmt = Integer.parseInt(planField.getText());
        double planPrice = Double.parseDouble(planPriceField.getText());
        if (weeks.isSelected())
        {
            text = weeks.getText();
            if (planAmt > 1)
            {
                financialText = person.getFirstName() + " " + person.getLastName() + ": "+ planAmt + " weeks";
            }
            else{
                financialText = person.getFirstName() + " " + person.getLastName() + ": "+ planAmt + " week";
            }
            connect.insertIntoFinancial(financialText,renewal,planPrice);
        }
        if (months.isSelected())
        {
            text = months.getText();
            if (planAmt > 1){
                financialText = person.getFirstName() + " " + person.getLastName() + ": "+ planAmt + " months";
            }
            else{
                financialText = person.getFirstName() + " " + person.getLastName() + ": "+ planAmt + " month";
            }
            connect.insertIntoFinancial(financialText,renewal,planPrice);
        }
        if (noSessions.isSelected())
        {
            person.setSessionAmount(0);
            person.setTrainer("None");
        }else {
            int seshAmt = Integer.parseInt(sessionAmountField.getText());
            double sessionPrice = Double.parseDouble(sessionPriceField.getText());
            person.setSessionAmount(seshAmt);
            person.setTrainer(trainerField.getText());
        if (seshAmt > 1)
        {
            sessionFinancialText = person.getFirstName() + " " + person.getLastName() + ": "+ seshAmt + " Sessions";
        }
        else {
            sessionFinancialText = person.getFirstName() + " " + person.getLastName() + ": "+ seshAmt + " Session";
        }
            connect.insertIntoFinancial(sessionFinancialText,sessions,sessionPrice);
        }
        connect.renewClient(person,planAmt,text);
        planField.clear();
        sessionAmountField.clear();
        trainerField.clear();
        planPriceField.clear();
        sessionPriceField.clear();
        weeks.setSelected(false);
        months.setSelected(false);
        noSessions.setSelected(false);
        expireList.getItems().remove(person);
    }
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        connect = new DBconnect();
        toggleGroup = new ToggleGroup();
        clients = FXCollections.observableArrayList();
        weeks.setText("WEEK");
        months.setText("MONTH");
        weeks.setToggleGroup(toggleGroup);
        months.setToggleGroup(toggleGroup);
        FirstNames.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        LastNames.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        PhoneNumbers.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        EndDates.setCellValueFactory(new PropertyValueFactory<>("ExpireDate"));
        EndDates.setCellFactory( column -> new TableCell<BloomClient, String>() {
            protected void updateItem(String date, boolean empty) {
                LocalDate actualDate = null;
                super.updateItem(date, empty);
                if (date == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(date);
                    actualDate = LocalDate.parse(date);
                    if (actualDate.isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
                        setStyle("-fx-background-color: #ff0617");
                    }
                }
            }
        });
        try {
           clients = connect.getExpireList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        expireList.setOnSort(e -> {
            try {
                refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
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
        sortedList.comparatorProperty().bind(expireList.comparatorProperty());
        expireList.setItems(sortedList);
        expireList.refresh();
        label.setText("Plan and Price Fields ARE REQUIRED.\n\n"+
                      "If Expire Column Cell turns red.\n"+
                      "-> SEND THEM TO EXPIRE.\n" +
                      "-> USE PROGRAM TO ITS ABILITY.\n"+
                      "-> PRACTICE GOOD DATA MANAGEMENT.\n\n" +
                      "If No Sessions is selected.\n" +
                      "-> Sessions fields not required.\n" +
                      "-> Otherwise, USE THEM.");
    }
    public void refresh() throws SQLException, ClassNotFoundException {
        clients.clear();
        initialize();
    }
    public void clientInfo()
    {
        BloomClient bloomClient = expireList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Client Info");
        alert.setHeaderText(bloomClient.getFirstName() + " " + bloomClient.getLastName());
        alert.setContentText("Expire Date: "+bloomClient.getExpireDate()+"\n"
                + "Silver Sneakers: "+bloomClient.getSsMember()+"\n"
                + "Sessions: "+bloomClient.getSessionAmount()+"\n"
                + "Trainer: "+bloomClient.getTrainer() +"\n"
                + "Silver Sneaker ID:"+bloomClient.getSsID()+"\n"
                + "Membership Type: "+bloomClient.getMemType());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
            }
        });
    }
}
