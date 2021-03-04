package BloomHealthGUI;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class RenewClient {

    @FXML
    private TextField planField;
    @FXML
    private TextField priceField;
    @FXML
    private RadioButton weeks;
    @FXML
    private RadioButton months;
    @FXML
    private TextField sessionAmountField;
    @FXML
    private TextField sessionPriceField;
    @FXML
    private TextField trainerField;
    @FXML
    private Label label;

    private ToggleGroup toggleGroup;

    @FXML
    private void initialize()
    {
        weeks.setToggleGroup(toggleGroup);
        months.setToggleGroup(toggleGroup);
        weeks.setSelected(false);
        months.setSelected(false);
        weeks.setText("WEEK");
        months.setText("MONTH");
        label.setText("");
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
