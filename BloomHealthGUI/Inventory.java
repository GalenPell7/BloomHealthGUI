package BloomHealthGUI;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class Inventory {
    @FXML
    private TableView<Item> inventoryTable;
    @FXML
    private TableColumn<Item,String> name;
    @FXML
    private TableColumn<Item,String> type;
    @FXML
    private TableColumn<Item,Double> price;
    @FXML
    private TableColumn<Item,Integer> amount;
    @FXML
    private TextField filteredSearch;
    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<String> types;
    @FXML
    private TextField priceField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField changeAmountField;
    @FXML
    private TextField changePriceField;

    private ObservableList<String> diffTypes;

    private ArrayList<String> arrayOfTypes;



    private DBconnect dBconnect = new DBconnect();

    private ObservableList<Item> items;

    public Inventory() throws SQLException, ClassNotFoundException {
    }
    @FXML
    private void initialize()
    {
        arrayOfTypes = new ArrayList<>();
        arrayOfTypes.add("Merchandise");
        arrayOfTypes.add("Consumables");
        arrayOfTypes.add("Supplies");
        arrayOfTypes.add("Other");
        diffTypes = FXCollections.observableArrayList();
        items = FXCollections.observableArrayList();
        diffTypes.addAll(arrayOfTypes);
        types.setItems(diffTypes);
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        try {
          items = dBconnect.getInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FilteredList<Item> filteredList = new FilteredList<Item>(items, b -> true);
        filteredSearch.textProperty().addListener(((observable, oldValue, newValue) ->
                filteredList.setPredicate(item ->
                {
                    if(newValue == null || newValue.isEmpty())
                        return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (item.getName().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else if (item.getType().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else
                        return false;
                })
        ));
        SortedList<Item> sortedList = new SortedList<Item>(filteredList);
        sortedList.comparatorProperty().bind(inventoryTable.comparatorProperty());
        inventoryTable.setItems(sortedList);
        inventoryTable.refresh();
    }
    public void setDelete() throws SQLException {
        Item selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        dBconnect.deleteInventoryItem(selectedItem);
        items.remove(selectedItem);
        inventoryTable.refresh();
    }
    public void addItem() throws SQLException {
        Item item = new Item();
        item.setName(nameField.getText());
        item.setType(types.getValue());
        double num = Double.parseDouble(priceField.getText());
        int amt = Integer.parseInt(amountField.getText());
        item.setPrice(num);
        item.setAmount(amt);
        dBconnect.addInventoryItem(item);
        refresh();
            nameField.clear();
            priceField.clear();
            amountField.clear();
    }
    public void increaseAmount() throws SQLException {
        Item item = inventoryTable.getSelectionModel().getSelectedItem();
        int amt = Integer.parseInt(changeAmountField.getText());
        dBconnect.increaseItemAmount(item,amt);
        changeAmountField.clear();
        refresh();
    }
    public void decreaseAmount() throws SQLException
    {
        Item item = inventoryTable.getSelectionModel().getSelectedItem();
        int amt = Integer.parseInt(changeAmountField.getText());
        dBconnect.decreaseItemAmount(item,amt);
        changeAmountField.clear();
        refresh();
    }
    public void changeItemPrice() throws SQLException {
        Item item = inventoryTable.getSelectionModel().getSelectedItem();
        double prc = Double.parseDouble(changePriceField.getText());
        dBconnect.changeItemPrice(item,prc);
        changePriceField.clear();
        refresh();
    }
    public void refresh()
    {
        items.clear();
        initialize();
    }
}
