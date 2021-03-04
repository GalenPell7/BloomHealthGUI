package BloomHealthGUI;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class MarketPlace {
    @FXML
    private TableView<Item> inventoryList;
    @FXML
    private TableColumn<Item,String> item;
    @FXML
    private TableColumn<Item,Double> price;
    @FXML
    private TableColumn<Item,Integer> amount;
    @FXML
    private TextField filteredSearch;
    @FXML
    private TextField nameField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField priceField;
    @FXML
    private RadioButton weekly;
    @FXML
    private RadioButton monthly;
    @FXML
    private RadioButton paidInFull;

    private ToggleGroup toggleGroup;

    private DBconnect dBconnect;

    private ObservableList<Item> items;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        toggleGroup = new ToggleGroup();
        weekly.setSelected(false);
        monthly.setSelected(false);
        paidInFull.setSelected(false);
        items = FXCollections.observableArrayList();
        dBconnect = new DBconnect();
        item.setCellValueFactory(new PropertyValueFactory<>("Name"));
        price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        try {
            items = dBconnect.getInventory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FilteredList<Item> filteredList = new FilteredList<Item>(items, b -> true);
        filteredSearch.textProperty().addListener(((observable, oldValue, newValue) ->
                filteredList.setPredicate(selection ->
                {
                    if(newValue == null || newValue.isEmpty())
                        return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (selection.getName().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else
                        return false;
                })
        ));
        SortedList<Item> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(inventoryList.comparatorProperty());
        inventoryList.setItems(sortedList);
        weekly.setToggleGroup(toggleGroup);
        monthly.setToggleGroup(toggleGroup);
        paidInFull.setToggleGroup(toggleGroup);
    }
    public void sellItem() throws SQLException, ClassNotFoundException {
        String sale = "Sale";
        Item item = inventoryList.getSelectionModel().getSelectedItem();
        dBconnect.insertIntoFinancial(item.getName(),sale,item.getPrice());
        dBconnect.sellInventoryItem(item);
        refresh();
    }
    public void sellSession() throws SQLException {
        String text = "";
        if (weekly.isSelected())
        {
            text = "Weekly";
        }
        if (monthly.isSelected())
        {
            text = "Monthly";
        }
        if (paidInFull.isSelected())
        {
            text = "One Payment";
        }
        String name = nameField.getText();
        String session = "Sessions";
        int amount = Integer.parseInt(amountField.getText());
        double price = Double.parseDouble(priceField.getText());
        String sale = name + ": " + amount + " paid -> " + text;
        dBconnect.insertIntoFinancial(sale,session,price);
        nameField.clear();
        amountField.clear();
        priceField.clear();
        weekly.setSelected(false);
        monthly.setSelected(false);
        paidInFull.setSelected(false);
    }
    public void refresh() throws SQLException, ClassNotFoundException {
        items.clear();
        initialize();
    }
}
