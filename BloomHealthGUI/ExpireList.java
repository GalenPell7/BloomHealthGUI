package BloomHealthGUI;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpireList
{
    @FXML
    private TableView<BloomClient> listOfExpires;
    @FXML
    private TableColumn<BloomClient,String> firstName;
    @FXML
    private TableColumn<BloomClient,String> lastName;
    @FXML
    private TableColumn<BloomClient,String> phoneNumber;
    @FXML
    private TableColumn<BloomClient,String> birthday;
    @FXML
    private TableColumn<BloomClient,String> dateExpired;
    @FXML
    private TextField planField;
    @FXML
    private RadioButton weeks;
    @FXML
    private RadioButton months;
    @FXML
    private TextField priceField;
    @FXML
    private CheckBox silverSneaker;
    @FXML
    private CheckBox noSessions;
    @FXML
    private TextField sessionAmountField;
    @FXML
    private TextField sessionPriceField;
    @FXML
    private TextField trainerField;
    @FXML
    private RadioButton weekly;
    @FXML
    private RadioButton monthly;
    @FXML
    private RadioButton paidInFull;
    @FXML
    private Label label;
    @FXML
    private TextField filteredSearch;
    @FXML
    private CheckBox autoRenew;

    private ObservableList<BloomClient> clients;

    private ToggleGroup toggleGroup;

    private ToggleGroup paymentOptions;


    private DBconnect dBconnect;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        toggleGroup = new ToggleGroup();
        paymentOptions = new ToggleGroup();
        dBconnect = new DBconnect();
        clients = FXCollections.observableArrayList();
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        birthday.setCellValueFactory(new PropertyValueFactory<>("Birthday"));
        dateExpired.setCellValueFactory(new PropertyValueFactory<>("ExpireDate"));
        weeks.setText("WEEK");
        months.setText("MONTH");
        weekly.setText("Weekly");
        monthly.setText("Monthly");
        paidInFull.setText("One Payment");
        weeks.setSelected(false);
        months.setSelected(false);
        weekly.setSelected(false);
        monthly.setSelected(false);
        paidInFull.setSelected(false);
        weeks.setToggleGroup(toggleGroup);
        months.setToggleGroup(toggleGroup);
        weekly.setToggleGroup(paymentOptions);
        monthly.setToggleGroup(paymentOptions);
        paidInFull.setToggleGroup(paymentOptions);
        label.setText("Plan and Price Fields ARE REQUIRED.\n\n" +
                      "If No Sessions is selected.\n" +
                      "-> Sessions fields not required.\n" +
                      "-> Otherwise, USE THEM.\n\n" +
                      "CLEAR LIST WILL DELETE CONTENT FROM TABLE.\n "+
                      "-> THIS IS THE END OF THE LINE FOR THIS DATA.\n"+
                      "-> IF YOU DELETE, YOU WILL NOT GET DATA BACK."  );
        try {
            clients = dBconnect.getListOfExpires();
        } catch (Exception e) {
            e.printStackTrace();
        }
        filteredSearch.setText("Search");
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
        sortedList.comparatorProperty().bind(listOfExpires.comparatorProperty());
        listOfExpires.setItems(sortedList);
    }
    public void clearList() throws SQLException {
        clients.clear();
        dBconnect.clearExpiredList();
    }
    public void addClient() throws SQLException {
        String text = "";
        String financial = "";
        String memType = "";
        String newMembership = "New Membership";
        BloomClient bloomClient = listOfExpires.getSelectionModel().getSelectedItem();
        bloomClient.setStartDate(systemDate());
        int amt = 0;
        double prc = 0.0;
        if (autoRenew.isSelected())
        {
            bloomClient.setExpireDate("2049-12-31");
            financial = bloomClient.getFirstName() + " " + bloomClient.getLastName() + " : Paid annually with a card";
            text = "AUTO";
            memType = "Annual Payments";
        } else {
            amt = Integer.parseInt(planField.getText());
            prc = Double.parseDouble(priceField.getText());
        if (weeks.isSelected())
        {
            text = weeks.getText();
            if (amt > 1) {
            financial = bloomClient.getFirstName() + " " + bloomClient.getLastName() + " : "+amt+" Weeks";
            memType = amt + " Weeks";
            }
            else {
                financial = bloomClient.getFirstName() + " " + bloomClient.getLastName() + " : "+amt+" Week";
                memType = amt + " Week";
            }
        }
        if (months.isSelected())
        {
            text = months.getText();
            if (amt > 1) {
                financial = bloomClient.getFirstName() + " " + bloomClient.getLastName() + " : "+amt+" Months";
                memType = amt + " Months";
            }
            else {
                financial = bloomClient.getFirstName() + " " + bloomClient.getLastName() + " : "+amt+" Month";
                memType = amt + " Month";
            }
        }
        }
        if (noSessions.isSelected())
        {
            bloomClient.setTrainer("None");
            bloomClient.setSessionAmount(0);
        }else
            {
                String paymentType = "";
                if (weekly.isSelected())
                {
                    paymentType = weekly.getText();
                }
                if (monthly.isSelected())
                {
                    paymentType = monthly.getText();
                }
                if (paidInFull.isSelected())
                {
                    paymentType = paidInFull.getText();
                }
                String sessions = "Sessions";
                String financialText = "";
                double seshPrc = Double.parseDouble(sessionPriceField.getText());
                bloomClient.setTrainer(trainerField.getText());
                int seshAmt = Integer.parseInt(sessionAmountField.getText());
                financialText = bloomClient.getFirstName() + " " + bloomClient.getLastName() +": "+ seshAmt + " paid -> " + paymentType;
                bloomClient.setSessionAmount(seshAmt);
                dBconnect.insertIntoFinancial(financialText,sessions,seshPrc);
            }
        if (silverSneaker.isSelected())
        {
            bloomClient.setSsMember("Yes");
        }else
            {
                bloomClient.setSsMember("No");
            }
        bloomClient.setMemType(memType);
        dBconnect.addToClientList(bloomClient,amt, text);
        dBconnect.insertIntoFinancial(financial,newMembership,prc);
        dBconnect.deleteFromExpiredList(bloomClient);
        planField.clear();
        priceField.clear();
        weeks.setSelected(false);
        months.setSelected(false);
        sessionAmountField.clear();
        sessionPriceField.clear();
        trainerField.clear();
        autoRenew.setSelected(false);
        noSessions.setSelected(false);
        silverSneaker.setSelected(false);
        clients.remove(bloomClient);
    }
    public String systemDate()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
