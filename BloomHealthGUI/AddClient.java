package BloomHealthGUI;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddClient {
    @FXML
    private TextField clientID;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField birthdayField;
    @FXML
    private TextField price;
    @FXML
    private TextField plan;
    @FXML
    private TextField sessionAmtField;
    @FXML
    private TextField sessionPriceField;
    @FXML
    private TextField trainerField;
    @FXML
    private TextField joinFeeField;
    @FXML
    private CheckBox joiningFee;
    @FXML
    private CheckBox noSessions;
    @FXML
    private CheckBox silverSneakers;
    @FXML
    private RadioButton weeks;
    @FXML
    private RadioButton months;
    @FXML
    private RadioButton weekly;
    @FXML
    private RadioButton monthly;
    @FXML
    private RadioButton paidInFull;
    @FXML
    private TextField idSegmentOne;
    @FXML
    private TextField idSegmentTwo;
    @FXML
    private TextField idSegmentThree;
    @FXML
    private TextField idSegmentFour;
    @FXML
    private CheckBox autoRenew;

    private ToggleGroup lengthOptions;

    private ToggleGroup paymentOptions;

    private DBconnect dBconnect;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        dBconnect = new DBconnect();
        lengthOptions = new ToggleGroup();
        paymentOptions = new ToggleGroup();
        weeks.setText("WEEK");
        months.setText("MONTH");
        joiningFee.setSelected(false);
        noSessions.setSelected(false);
        silverSneakers.setSelected(false);
        autoRenew.setSelected(false);
        weekly.setText("Weekly");
        monthly.setText("Monthly");
        paidInFull.setText("One Payment");
        autoRenew.setText("Auto-Renew");
        weeks.setToggleGroup(lengthOptions);
        months.setToggleGroup(lengthOptions);
        weekly.setToggleGroup(paymentOptions);
        monthly.setToggleGroup(paymentOptions);
        paidInFull.setToggleGroup(paymentOptions);
    }
    public String systemDate()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    public void addClient() throws SQLException {
        String newMembership = "New Membership";
        String financialText = "";
        String text = "";
        String paymentType = "";
        String memType = "";
        int num = 0;
        double planPrice = 0.0;
        BloomClient bloomClient = new BloomClient();
        if (clientID.getText().equals(""))
        {
            bloomClient.setClientID(null);
        }else {
            bloomClient.setClientID(clientID.getText());
        }
        bloomClient.setFirstName(firstNameField.getText());
        bloomClient.setLastName(lastNameField.getText());
        if(phoneNumberField.getText().equals(""))
        {
            bloomClient.setPhoneNumber(null);
        }else
            {
                bloomClient.setPhoneNumber(phoneNumberField.getText());
            }
        if (birthdayField.getText().equals(""))
        {
            bloomClient.setBirthday(null);
        }else {
            bloomClient.setBirthday(birthdayField.getText());
        }
        bloomClient.setStartDate(systemDate());
        if (noSessions.isSelected())
        {
            bloomClient.setTrainer("None");
            bloomClient.setSessionAmount(0);
        }else {
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
            int num1 = Integer.parseInt(sessionAmtField.getText());
            double sessionPrice = Double.parseDouble(sessionPriceField.getText());
            bloomClient.setTrainer(trainerField.getText());
            bloomClient.setSessionAmount(num1);
            String sessionFinancialText = bloomClient.getFirstName() + " " + bloomClient.getLastName() +
                    ": " + num1 + " paid -> "+paymentType;
            dBconnect.insertIntoFinancial(sessionFinancialText,sessions,sessionPrice);
        }
        if (silverSneakers.isSelected())
        {
            bloomClient.setSsMember("Yes");
            String data = idSegmentOne.getText() + "-" + idSegmentTwo.getText() +
                    "-" + idSegmentThree.getText() + "-" + idSegmentFour.getText();
            bloomClient.setSsID(data);
        }else {
            bloomClient.setSsMember("No");
            bloomClient.setSsID("NAM");
        }
        if (autoRenew.isSelected()) {
            bloomClient.setExpireDate("2049-12-31");
            financialText = bloomClient.getFirstName() + " " + bloomClient.getLastName() + ": Paid annually with a card";
            text = "AUTO";
            memType = "Annual Payments";
            if (price.getText().equals(""))
            {
                planPrice = 0.0;
            }
        }else {
        planPrice = Double.parseDouble(price.getText());
        num = Integer.parseInt(plan.getText());
            if (weeks.isSelected())
            {
                text = weeks.getText();
                if (num > 1)
                {
                financialText = bloomClient.getFirstName() + " " + bloomClient.getLastName() + ": "+ num + " Weeks";
                memType = num + " Weeks";
                }
                else {
                financialText = bloomClient.getFirstName() + " " + bloomClient.getLastName() + ": "+ num + " Week";
                memType = num + " Week";
                }
            }
            if (months.isSelected())
            {
                text = months.getText();
                if (num > 1)
                {
                financialText = bloomClient.getFirstName() + " " + bloomClient.getLastName() + ": "+ num + " Months";
                memType = num + " Months";
                }
                else {
                financialText = bloomClient.getFirstName() + " " + bloomClient.getLastName() + ": "+ num + " Month";
                memType = num + " Month";
            }
        }
        }
        if (joiningFee.isSelected())
        {
        double joiningFee = Double.parseDouble(joinFeeField.getText());
        planPrice += joiningFee;
        financialText += ": Joining Fee Applied";
        }
        bloomClient.setMemType(memType);
        dBconnect.addToClientList(bloomClient,num,text);
        dBconnect.insertIntoFinancial(financialText,newMembership, planPrice);
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        birthdayField.clear();
        idSegmentOne.clear();
        idSegmentTwo.clear();
        idSegmentThree.clear();
        idSegmentFour.clear();
        joiningFee.setSelected(false);
        silverSneakers.setSelected(false);
        noSessions.setSelected(false);
        weekly.setSelected(false);
        monthly.setSelected(false);
        paidInFull.setSelected(false);
        weeks.setSelected(false);
        months.setSelected(false);
        autoRenew.setSelected(false);
        price.clear();
        trainerField.clear();
        sessionAmtField.clear();
        sessionPriceField.clear();
        plan.clear();
        clientID.clear();
    }
}
