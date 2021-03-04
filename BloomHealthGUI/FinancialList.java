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

public class FinancialList {

    @FXML
    private TableView<Purchase> financialList;
    @FXML
    private TableColumn<Purchase,String> purchase;
    @FXML
    private TableColumn<Purchase,String> type;
    @FXML
    private TableColumn<Purchase,Double> price;
    @FXML
    private TableColumn<Purchase,String> date;
    @FXML
    private TextField filteredSearch;
    @FXML
    private Label dateCleared;
    @FXML
    private ChoiceBox<String> options;
    @FXML
    private TextField typeField;
    @FXML
    private TextField priceField;

    private ObservableList<Purchase> data;

    private DBconnect dBconnect;
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        dBconnect = new DBconnect();
        options.getItems().add("Rental");
        options.getItems().add("Day Pass");
        options.getItems().add("Other");
        data = FXCollections.observableArrayList();
        purchase.setCellValueFactory(new PropertyValueFactory<>("Purchase"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        try {
            data = dBconnect.getFinancialList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FilteredList<Purchase> filteredList = new FilteredList<Purchase>(data, b -> true);
        filteredSearch.textProperty().addListener(((observable, oldValue, newValue) ->
                filteredList.setPredicate(selection ->
                {
                    if(newValue == null || newValue.isEmpty())
                        return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (selection.getPurchase().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else if (selection.getType().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else
                        return false;
                })
        ));
        SortedList<Purchase> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(financialList.comparatorProperty());
        financialList.setItems(sortedList);
        financialList.refresh();
        dateCleared.setText(systemDate());
    }
    public String systemDate()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    public void clearList() throws SQLException {
        data.clear();
        dBconnect.clearFinancialsList();
    }
    public void addToFinancial() throws SQLException, ClassNotFoundException {
        double prc = Double.parseDouble(priceField.getText());
        dBconnect.insertIntoFinancial(typeField.getText(),options.getValue(),prc);
        typeField.clear();
        priceField.clear();
        options.getItems().clear();
        refresh();
    }
    public void refresh() throws SQLException, ClassNotFoundException {
        data.clear();
        initialize();
    }
}
