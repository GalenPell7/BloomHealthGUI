package BloomHealthGUI;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static javafx.fxml.FXMLLoader.load;

public class SignInMenu {
    @FXML
    private TableView<BloomClient> clientList;
    @FXML
    private TableColumn<BloomClient,String> firstNames;
    @FXML
    private TableColumn<BloomClient,String> lastNames;
    @FXML
    private TableColumn<BloomClient,String> phoneNumbers;
    @FXML
    private TableColumn<BloomClient,String> endDates;
    @FXML
    private TableColumn<BloomClient,String> startDates;
    @FXML
    private TableColumn<BloomClient,String> birthdays;
    @FXML
    private TextField filteredSearch;
    @FXML
    private Label label;

    private ObservableList<BloomClient> clients;

    private DBconnect dBconnect = new DBconnect();

    public SignInMenu() throws SQLException, ClassNotFoundException {
    }
    @FXML
    private void initialize()
    {
        clients = FXCollections.observableArrayList();
        firstNames.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastNames.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        phoneNumbers.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        birthdays.setCellValueFactory(new PropertyValueFactory<>("Birthday"));
        startDates.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        endDates.setCellValueFactory(new PropertyValueFactory<>("ExpireDate"));
        try {
            clients = dBconnect.getClientList();
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
                    else if (person.getClientID().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else
                        return false;
                })
                ));
        SortedList<BloomClient> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(clientList.comparatorProperty());
        clientList.setItems(sortedList);
        clientList.refresh();
    }
    public void setDelete() throws SQLException {
        BloomClient person = clientList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes,no);
        alert.setHeaderText(person.getFirstName() + " " + person.getLastName());
        alert.setContentText("Are you sure you want to delete client?");
        alert.setTitle("Delete Client");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==yes)
        {
            dBconnect.deleteClient(person);
            clients.remove(person);
            clientList.refresh();
        }else if (result.get()==no)
        {
            alert.close();
        }
        refresh();
    }
    public void signIn() throws SQLException {
        BloomClient person = clientList.getSelectionModel().getSelectedItem();
        dBconnect.sendToRoster(person.getFirstName(),person.getLastName());
        int actualAmount = person.getSessionAmount() - 1;
        if (person.getSessionAmount()>0)
        {
            Alert makeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType subtract = new ButtonType("Subtract");
            ButtonType dontSubtract = new ButtonType("Don't Subtract");
            makeAlert.setContentText("Is client here for training session? If so, subtract a session.");
            makeAlert.getButtonTypes().setAll(subtract,dontSubtract);
            Optional<ButtonType> result = makeAlert.showAndWait();
                if (result.get()==subtract)
                {
                dBconnect.subtractSession(person);
                    if (person.getSsMember().equals("Yes"))
                    {
                    label.setText("Name:" + person.getFirstName() + " " + person.getLastName() + "\n"+
                            "Trainer: " + person.getTrainer()+"\n" +
                            "Silver Sneaker Member: "+person.getSsMember()+"\n"+
                            "Silver Sneaker ID:" +person.getSsID() +"\n"+
                            "SessionAmount: "+ actualAmount);
                    }else
                        {
                            label.setText("Name:" + person.getFirstName() + " " + person.getLastName() + "\n"+
                                    "Trainer: " + person.getTrainer()+"\n" +
                                    "SessionAmount: "+ actualAmount);
                        }
                }
                if (result.get()==dontSubtract)
                {
                    makeAlert.close();
                    if (person.getSsMember().equals("Yes"))
                    {
                        label.setText("Name:" + person.getFirstName() + " " + person.getLastName() + "\n"+
                                "Trainer: " + person.getTrainer()+"\n" +
                                "Silver Sneaker Member: "+person.getSsMember()+"\n"+
                                "Silver Sneaker ID:" +person.getSsID() +"\n"+
                                "SessionAmount: "+ person.getSessionAmount());
                    }else
                    {
                        label.setText("Name:" + person.getFirstName() + " " + person.getLastName() + "\n"+
                                "Trainer: " + person.getTrainer()+"\n" +
                                "SessionAmount: "+ person.getSessionAmount());
                    }
                }
        }else {
            if (person.getSsMember().equals("Yes"))
            {
                label.setText("Name:" + person.getFirstName() + " " + person.getLastName() + "\n"+
                        "Trainer: " + person.getTrainer()+"\n" +
                        "Silver Sneaker Member: "+person.getSsMember()+"\n"+
                        "Silver Sneaker ID:" +person.getSsID() +"\n"+
                        "SessionAmount: "+ person.getSessionAmount());
            }else
            {
                label.setText("Name:" + person.getFirstName() + " " + person.getLastName() + "\n"+
                        "Trainer: " + person.getTrainer()+"\n" +
                        "SessionAmount: "+ person.getSessionAmount());
            }
        }
        refresh();
    }
    public void addSessions() throws SQLException {
        BloomClient bloomClient = clientList.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Member: "+bloomClient.getFirstName() +" "+ bloomClient.getLastName());
        dialog.setTitle("Add Sessions");
        dialog.setContentText("Amount: ");
        Optional<String> result = dialog.showAndWait();
        int Amount = Integer.parseInt(dialog.getResult());
        if (result.isPresent())
        {
            dBconnect.addSessions(bloomClient,Amount);
        }
        refresh();
    }
    public void clientInfo()
    {
        BloomClient bloomClient = clientList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Client Info");
        alert.setHeaderText(bloomClient.getFirstName() + " " + bloomClient.getLastName());
        alert.setContentText( "Expire Date: "+bloomClient.getExpireDate()+"\n"
                            + "Silver Sneakers: "+bloomClient.getSsMember()+"\n"
                            + "Sessions: "+bloomClient.getSessionAmount()+"\n"
                            + "Trainer: "+bloomClient.getTrainer() +"\n"
                            + "Silver Sneaker ID:"+bloomClient.getSsID()+"\n"
                            + "Membership Type: "+bloomClient.getMemType()+"\n"
                            + "ClientID:" + bloomClient.getClientID());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
            }
        });
    }
    public void makeSsMember() throws SQLException {
        BloomClient bloomClient = clientList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes,no);
        alert.setHeaderText(bloomClient.getFirstName() + " " + bloomClient.getLastName());
        alert.setContentText("Would you like to make this client a Silver Sneaker Member?\n" +
                             "If so, make sure to assign Silver Sneaker ID.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==yes)
        {
            dBconnect.changeSsMember("Yes",bloomClient);
        }else if (result.get()==no)
        {
            dBconnect.changeSsMember("No",bloomClient);
        }
        refresh();
    }
    public void changeTrainer() throws SQLException {
        BloomClient bloomClient = clientList.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Member: "+bloomClient.getFirstName() +" "+ bloomClient.getLastName());
        dialog.setTitle("Change Trainer");
        dialog.setContentText("Name: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            dBconnect.changeTrainer(dialog.getResult(),bloomClient);
        }
        refresh();
    }
    public void updateSSID() throws SQLException {
        BloomClient bloomClient = clientList.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Member: "+bloomClient.getFirstName() +" "+ bloomClient.getLastName()+ "\n" +
                             "CurrentID:"+bloomClient.getSsID());
        dialog.setTitle("Silver Sneaker ID Status");
        dialog.setContentText("ID(Include -): ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            dBconnect.changeSSID(dialog.getResult(),bloomClient);
        }
        refresh();
    }
    public void changeNumber() throws SQLException {
        BloomClient bloomClient = clientList.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Member: "+bloomClient.getFirstName() +" "+ bloomClient.getLastName()+ "\n" +
                "Current Number:"+bloomClient.getPhoneNumber());
        dialog.setTitle("Change Phone Number");
        dialog.setContentText("Enter: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            dBconnect.changeNumber(dialog.getResult(),bloomClient);
        }
        refresh();
    }
    public void freezeAccount() throws SQLException {
        BloomClient person = clientList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes,no);
        alert.setHeaderText(person.getFirstName() + " " + person.getLastName());
        alert.setContentText("Are you sure you want to freeze client?");
        alert.setTitle("Freeze Client");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==yes)
        {
            dBconnect.sendToFreeze(person);
            dBconnect.deleteClient(person);
            clients.remove(person);
            clientList.refresh();
        }else if (result.get()==no)
        {
            alert.close();
        }
        refresh();
    }
    public void changeID() throws SQLException {
        BloomClient bloomClient = clientList.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Member: "+bloomClient.getFirstName() +" "+ bloomClient.getLastName()+ "\n" +
                "CurrentID:"+bloomClient.getClientID());
        dialog.setTitle("Client ID Status");
        dialog.setContentText("Enter: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            dBconnect.changeClientID(dialog.getResult(),bloomClient);
        }
        refresh();
    }
    public void changeBirthday() throws SQLException {
        BloomClient bloomClient = clientList.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Member: "+bloomClient.getFirstName() +" "+ bloomClient.getLastName()+ "\n" +
                "Current Birthday:"+bloomClient.getBirthday());
        dialog.setTitle("Birthday Status");
        dialog.setContentText("Format(yyyy-mm-dd): ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            dBconnect.changeClientBirthday(dialog.getResult(),bloomClient);
        }
        refresh();
    }
    public void showFreezeList()
    {
        try {
            Parent root = load(getClass().getResource("FreezeList.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Freeze List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showAttendance()
    {
        try
        {
            Parent root = load(getClass().getResource("SignInList.fxml"));
            Stage stage = new Stage();
            stage.setTitle("SignIn Sheet");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showInventory()
    {
        try {
            Parent root = load(getClass().getResource("Inventory.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Inventory");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addClientMenu()
    {
        try {
            Parent root = load(getClass().getResource("AddClient.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Client");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showExpiredList()
    {
        try {
            Parent root = load(getClass().getResource("ExpireList.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Expired List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showFinancialList()
    {
        try {
            Parent root = load(getClass().getResource("FinancialList.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Financial List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refresh()
    {
        clients.clear();
        initialize();
    }
}

