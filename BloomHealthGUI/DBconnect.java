package BloomHealthGUI;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DBconnect
{

    private ObservableList<BloomClient> clients;
    private Connection c;
    private LocalDateTime now;
    public DBconnect() throws SQLException, ClassNotFoundException {
        clients = FXCollections.observableArrayList();
        Class.forName("org.h2.Driver");
        c = DriverManager.getConnection
                ("jdbc:h2:~/Database;MV_STORE=false;LOCK_TIMEOUT=100000", "admin", "Fitness1");
    }
    public void reConnect() throws ClassNotFoundException, SQLException {
        clients = FXCollections.observableArrayList();
        Class.forName("org.h2.Driver");
        c = DriverManager.getConnection
                ("jdbc:h2:~/Database;MV_STORE=false;LOCK_TIMEOUT=100000", "admin", "Fitness1");
    }
    public ObservableList<BloomClient> getExpireList() throws Exception
    {
        PreparedStatement expires = c.prepareStatement("SELECT * FROM BLOOMHEALTH.CLIENTS" +
                " WHERE DATEADD('DAY',30, ? ) >= MEMBEREXPIRE;");
        expires.setString(1,systemDate());
        try {
            ResultSet data = expires.executeQuery();
            while (data.next())
            {
                BloomClient gym_client = new BloomClient();
                gym_client.setFirstName(data.getString("FIRSTNAME"));
                gym_client.setLastName(data.getString("LASTNAME"));
                gym_client.setPhoneNumber(data.getString("PHONENUMBER"));
                gym_client.setBirthday(data.getString("BIRTHDAY"));
                gym_client.setExpireDate(data.getString("MEMBEREXPIRE"));
                clients.add(gym_client);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clients;
    }
    public void deleteClient(BloomClient client) throws SQLException {
        PreparedStatement sql = c.prepareStatement("DELETE FROM BLOOMHEALTH.CLIENTS " +
                "WHERE CLIENTID = ?");
        c.setAutoCommit(false);
        try {
            sql.setString(1,client.getClientID());
            sql.execute();
            c.commit();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public String systemDate()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        now = LocalDateTime.now();
        return dtf.format(now);
    }
    public String systemTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        now = LocalDateTime.now();
        return dtf.format(now);
    }
    public ObservableList<BloomClient> getBirthdayList() throws SQLException {
        PreparedStatement birthdays = c.prepareStatement("SELECT * FROM bloomhealth.clients " +
                "where DAY_OF_YEAR(Birthday) = DAY_OF_YEAR(?);");
        birthdays.setString(1,systemDate());
        try {
            ResultSet data = birthdays.executeQuery();
            while (data.next())
            {
                BloomClient gym_client = new BloomClient();
                gym_client.setFirstName(data.getString("FIRSTNAME"));
                gym_client.setLastName(data.getString("LASTNAME"));
                gym_client.setPhoneNumber(data.getString("PHONENUMBER"));
                clients.add(gym_client);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clients;
    }
    public ObservableList<BloomClient> getClientList() throws Exception
    {
        PreparedStatement list = c.prepareStatement("SELECT * FROM bloomhealth.clients;");
        try {
            ResultSet data = list.executeQuery();
            while (data.next())
            {
                BloomClient gym_client = new BloomClient();
                gym_client.setClientID(data.getString("CLIENTID"));
                gym_client.setFirstName(data.getString("FIRSTNAME"));
                gym_client.setLastName(data.getString("LASTNAME"));
                gym_client.setPhoneNumber(data.getString("PHONENUMBER"));
                gym_client.setBirthday(data.getString("BIRTHDAY"));
                gym_client.setStartDate(data.getString("MEMBERSTART"));
                gym_client.setExpireDate(data.getString("MEMBEREXPIRE"));
                gym_client.setSessionAmount(data.getInt("SESSIONAMT"));
                gym_client.setTrainer(data.getString("TRAINER"));
                gym_client.setSsMember(data.getString("SSMEMBER"));
                gym_client.setSsID(data.getString("SSID"));
                gym_client.setMemType(data.getString("MEMTYPE"));
                clients.add(gym_client);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clients;
    }
    public ObservableList<BloomClient> getListOfExpires() throws SQLException {
        PreparedStatement sql = c.prepareStatement("SELECT * FROM BLOOMHEALTH.EXPIREDLIST;");
        ObservableList<BloomClient> expiredList = FXCollections.observableArrayList();
        try {
            ResultSet data = sql.executeQuery();
            while (data.next()) {
                BloomClient bloomClient = new BloomClient();
                bloomClient.setFirstName(data.getString("FIRSTNAME"));
                bloomClient.setLastName(data.getString("LASTNAME"));
                bloomClient.setPhoneNumber(data.getString("PHONENUMBER"));
                bloomClient.setBirthday(data.getString("BIRTHDAY"));
                bloomClient.setExpireDate(data.getString("DATEEXPIRED"));
                expiredList.add(bloomClient);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return expiredList;
    }
    public void sendToRoster(String fName, String lName) throws SQLException {
        PreparedStatement st = c.prepareStatement("INSERT INTO BLOOMHEALTH.SIGNINSHEET VALUES(?,?,?,?);");
        c.setAutoCommit(false);
        try {
            st.setString(1,fName);
            st.setString(2,lName);
            st.setString(3, systemDate());
            st.setString(4, systemTime());
            st.execute();
            c.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void sendToFreeze(BloomClient person) throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        PreparedStatement statement = c.prepareStatement("INSERT INTO BLOOMHEALTH.FROZENACCOUNTS" +
                " VALUES (?,?,?,?,?,?,?,?);");
        LocalDate currentDate = LocalDate.now();
        LocalDate expDate = LocalDate.parse(person.getExpireDate(),dtf);
        long differenceInDays = ChronoUnit.DAYS.between(currentDate,expDate);
        c.setAutoCommit(false);
        try {
            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setString(3, person.getPhoneNumber());
            statement.setString(4, person.getBirthday());
            statement.setLong(5, differenceInDays);
            statement.setString(6,person.getMemType());
            statement.setString(7,person.getSsMember());
            statement.setString(8,person.getSsID());
            statement.execute();
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ObservableList <BloomClient> getFreezeList() throws Exception
    {
        PreparedStatement list = c.prepareStatement("SELECT * FROM BLOOMHEALTH.FROZENACCOUNTS;");
        try {
            ResultSet data = list.executeQuery();
            while (data.next())
            {
                BloomClient gym_client = new BloomClient();
                gym_client.setFirstName(data.getString("FIRSTNAME"));
                gym_client.setLastName(data.getString("LASTNAME"));
                gym_client.setPhoneNumber(data.getString("PHONENUMBER"));
                gym_client.setBirthday(data.getString("BIRTHDAY"));
                gym_client.setDaysLeft(data.getInt("DAYLEFT"));
                gym_client.setMemType(data.getString("MEMTYPE"));
                gym_client.setSsMember(data.getString("SSMEMBER"));
                gym_client.setSsID(data.getString("SSID"));
                gym_client.setClientID(data.getString("CLIENTID"));
                clients.add(gym_client);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clients;
    }
    public ObservableList<BloomClient> getSignInSheet() throws SQLException {
        PreparedStatement sql = c.prepareStatement("SELECT * FROM BLOOMHEALTH.SIGNINSHEET;");
        try {
            ResultSet data = sql.executeQuery();
            while (data.next())
            {
                BloomClient gymClient = new BloomClient();
                gymClient.setFirstName(data.getString("FIRSTNAME"));
                gymClient.setLastName(data.getString("LASTNAME"));
                gymClient.setDate(data.getString("DATE"));
                gymClient.setTime(data.getString("TIME"));
                clients.add(gymClient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public void unFreezeClient(BloomClient person) throws SQLException {
        PreparedStatement sql = c.prepareStatement("INSERT INTO BLOOMHEALTH.CLIENTS VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        LocalDate curDate = LocalDate.now();
        LocalDate expDate = curDate.plusDays(person.getDaysLeft());
        person.setExpireDate(expDate.toString());
        try {
         sql.setString(1,person.getFirstName());
         sql.setString(2,person.getLastName());
         sql.setString(3,person.getPhoneNumber());
         sql.setString(4,person.getBirthday());
         sql.setString(5,systemDate());
         sql.setString(6,person.getExpireDate());
         sql.setInt(7,0);
         sql.setString(8,"none");
         sql.setString(9,person.getSsMember());
         sql.setString(10,person.getSsID());
         sql.setString(11,person.getMemType());
         sql.setString(12,person.getClientID());
         sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        deleteFreezedClient(person);
    }

    public void deleteFreezedClient(BloomClient person) throws SQLException {
        PreparedStatement sql = c.prepareStatement("DELETE FROM BLOOMHEALTH.FROZENACCOUNTS" +
                " WHERE FirstName = ? and LastName = ?;");
        try {
            sql.setString(1,person.getFirstName());
            sql.setString(2,person.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Item> getInventory() throws SQLException {
        ObservableList<Item> items = FXCollections.observableArrayList();
        PreparedStatement sql = c.prepareStatement("SELECT * FROM BLOOMHEALTH.INVENTORY;");
        try {
            ResultSet data = sql.executeQuery();
            while (data.next())
            {
            Item item = new Item();
            item.setName(data.getString("NAME"));
            item.setType(data.getString("DESCRIPTION"));
            item.setPrice(data.getDouble("PRICE"));
            item.setAmount(data.getInt("AMOUNT"));
            items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    public void deleteInventoryItem(Item selectedItem) throws SQLException {
        PreparedStatement sql = c.prepareStatement("DELETE FROM BLOOMHEALTH.INVENTORY" +
                " WHERE NAME = ? and DESCRIPTION = ?;");
        try {
            sql.setString(1,selectedItem.getName());
            sql.setString(2,selectedItem.getType());
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void addInventoryItem(Item item) throws SQLException {
        PreparedStatement sql = c.prepareStatement("INSERT INTO BLOOMHEALTH.INVENTORY VALUES ( ?,?,?,? )");
        try {
            sql.setString(1, item.getName());
            sql.setString(2,item.getType());
            sql.setDouble(3,item.getPrice());
            sql.setInt(4,item.getAmount());
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void subtractSession(BloomClient person) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS set SESSIONAMT = SESSIONAMT-1" +
                " where FIRSTNAME = ? and LASTNAME = ?");
        try {
            sql.setString(1,person.getFirstName());
            sql.setString(2,person.getLastName());
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void sendToEvents(Event event) throws SQLException {
        PreparedStatement sql = c.prepareStatement("INSERT INTO BLOOMHEALTH.EVENTS VALUES ( ?,?,?,?,?,?,? )");
        try {
            sql.setString(1,event.getTitle());
            sql.setString(2,event.getTrainer());
            sql.setString(3,event.getStartTime());
            sql.setString(4,event.getEndTime());
            sql.setInt(5,0);
            sql.setString(6,event.getDays());
            sql.setString(7,event.getDate());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Event> getEventList() throws SQLException {
        ObservableList<Event> events = FXCollections.observableArrayList();
        PreparedStatement sql = c.prepareStatement("SELECT * FROM BLOOMHEALTH.EVENTS;");
        try {
            ResultSet data = sql.executeQuery();
            while (data.next())
            {
                Event event = new Event();
                event.setTitle(data.getString("TITLE"));
                event.setTrainer(data.getString("TRAINER"));
                event.setDays(data.getString("DAYS"));
                event.setStartTime(data.getString("STARTTIME"));
                event.setEndTime(data.getString("ENDTIME"));
                event.setSize(data.getInt("ATTENDANCE"));
                event.setDate(data.getString("DATE"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void addPeopleToEvent(Event selectedEvent, int a) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.EVENTS set ATTENDANCE = ATTENDANCE + ? " +
                "where TITLE = ? ");
        try {
            sql.setInt(1, a);
            sql.setString(2, selectedEvent.getTitle());
            sql.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deletePeopleFromEvent(Event selectedEvent, int a) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.EVENTS set ATTENDANCE = ATTENDANCE - ? " +
                "where TITLE = ? ");
        try {
            sql.setInt(1,a);
            sql.setString(2,selectedEvent.getTitle());
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteEvent(Event selectedEvent) throws SQLException {
        PreparedStatement sql = c.prepareStatement("DELETE FROM BLOOMHEALTH.EVENTS WHERE TITLE = ?");
        try {
            sql.setString(1,selectedEvent.getTitle());
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void addToClientList(BloomClient bloomClient, int num, String text) throws SQLException {
        PreparedStatement sql = null;
        if (text.equals("AUTO"))
        {
            sql = c.prepareStatement("INSERT INTO BLOOMHEALTH.CLIENTS VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?)");
            try {
                assert sql != null;
                sql.setString(1, bloomClient.getFirstName());
                sql.setString(2, bloomClient.getLastName());
                sql.setString(3, bloomClient.getPhoneNumber());
                sql.setString(4, bloomClient.getBirthday());
                sql.setString(5, bloomClient.getStartDate());
                sql.setString(6,bloomClient.getExpireDate());
                sql.setInt(7, bloomClient.getSessionAmount());
                sql.setString(8, bloomClient.getTrainer());
                sql.setString(9, bloomClient.getSsMember());
                sql.setString(10, bloomClient.getSsID());
                sql.setString(11, bloomClient.getMemType());
                sql.setString(12,bloomClient.getClientID());
                sql.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            if (text.equals("MONTH"))
            {
                sql = c.prepareStatement("INSERT INTO BLOOMHEALTH.CLIENTS VALUES ( ?,?,?,?,?,DATEADD('MONTH',?,?),?,?,?,?,?,?)");
            }
            if (text.equals("WEEK"))
            {
                sql = c.prepareStatement("INSERT INTO BLOOMHEALTH.CLIENTS VALUES ( ?,?,?,?,?,DATEADD('WEEK',?,?),?,?,?,?,?,?)");
            }
            try {
                assert sql != null;
                sql.setString(1, bloomClient.getFirstName());
                sql.setString(2, bloomClient.getLastName());
                sql.setString(3, bloomClient.getPhoneNumber());
                sql.setString(4, bloomClient.getBirthday());
                sql.setString(5, bloomClient.getStartDate());
                sql.setInt(6, num);
                sql.setString(7, systemDate());
                sql.setInt(8, bloomClient.getSessionAmount());
                sql.setString(9, bloomClient.getTrainer());
                sql.setString(10, bloomClient.getSsMember());
                sql.setString(11, bloomClient.getSsID());
                sql.setString(12, bloomClient.getMemType());
                sql.setString(13,bloomClient.getClientID());
                sql.execute();
            }catch (SQLException e) {
            e.printStackTrace();
        }}
    }

    public void addSessions(BloomClient bloomClient, int amount) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS set SESSIONAMT = SESSIONAMT + ?" +
                " WHERE FIRSTNAME = ? and LASTNAME = ?;");
        try {
            sql.setInt(1,amount);
            sql.setString(2,bloomClient.getFirstName());
            sql.setString(3,bloomClient.getLastName());
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void sendToExpired(BloomClient bloomClient) throws SQLException {
        PreparedStatement sql = c.prepareStatement("INSERT INTO BLOOMHEALTH.EXPIREDLIST VALUES (?,?,?,?,?);");
        String birth = bloomClient.getBirthday();
        try {
            sql.setString(1,bloomClient.getFirstName());
            sql.setString(2,bloomClient.getLastName());
            sql.setString(3,bloomClient.getPhoneNumber());
            sql.setString(4,birth);
            sql.setString(5,bloomClient.getExpireDate());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            deleteClient(bloomClient);
        }
    }

    public void clearExpiredList() throws SQLException {
        PreparedStatement sql = c.prepareStatement("DELETE FROM BLOOMHEALTH.EXPIREDLIST;");
        try {
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void deleteFromExpiredList(BloomClient bloomClient) throws SQLException{
        PreparedStatement sql = c.prepareStatement("DELETE FROM BLOOMHEALTH.EXPIREDLIST" +
                " WHERE FIRSTNAME = ? AND LASTNAME = ?;");
        try {
            sql.setString(1, bloomClient.getFirstName());
            sql.setString(2,bloomClient.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void renewClient(BloomClient person, int planAmt, String text) throws SQLException {
        PreparedStatement sql = null;
        if (text.equals("WEEK"))
        {
            sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS SET MEMBEREXPIRE = DATEADD('WEEK',?,?), SESSIONAMT = SESSIONAMT + ?, TRAINER = ?,MEMTYPE = ?" +
                " WHERE FIRSTNAME = ? AND LASTNAME = ?;");
        }
        if (text.equals("MONTH"))
        {
            sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS SET MEMBEREXPIRE = DATEADD('MONTH',?,?),  SESSIONAMT = SESSIONAMT + ?, TRAINER = ?,MEMTYPE = ?" +
                    " WHERE FIRSTNAME = ? AND LASTNAME = ?;");
        }
        try {
            assert sql != null;
            sql.setInt(1,planAmt);
            sql.setString(2,person.getExpireDate());
            sql.setInt(3,person.getSessionAmount());
            sql.setString(4, person.getTrainer());
            sql.setString(5,person.getMemType());
            sql.setString(6,person.getFirstName());
            sql.setString(7,person.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insertIntoFinancial(String item, String description, double price) throws SQLException {
        PreparedStatement sql = c.prepareStatement("INSERT INTO BLOOMHEALTH.FINANCIALTABLE VALUES ( ?,?,?,? )");
        try {
            sql.setString(1,item);
            sql.setString(2,description);
            sql.setDouble(3,price);
            sql.setString(4,systemDate());
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public ObservableList<Purchase> getFinancialList() throws SQLException {
        ObservableList<Purchase> list = FXCollections.observableArrayList();
        PreparedStatement sql = c.prepareStatement("SELECT * FROM BLOOMHEALTH.FINANCIALTABLE;");
        try {
            ResultSet data = sql.executeQuery();
            while (data.next())
            {
                Purchase purchase = new Purchase();
                purchase.setPurchase(data.getString("PURCHASED"));
                purchase.setType(data.getString("DESCRIPTION"));
                purchase.setPrice(data.getDouble("PRICE"));
                purchase.setDate(data.getString("DATE"));
                list.add(purchase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void clearFinancialsList() throws SQLException {
        PreparedStatement sql = c.prepareStatement("DELETE FROM BLOOMHEALTH.FINANCIALTABLE;");
        try {
            sql.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void sellInventoryItem(Item item) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.INVENTORY" +
                                                " SET AMOUNT = AMOUNT - 1 WHERE NAME = ?");
        try {
            sql.setString(1,item.getName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeSsMember(String yn, BloomClient bloomClient) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS SET SSMEMBER = ?" +
                                                        "WHERE FIRSTNAME = ? AND LASTNAME = ?;");
        try {
            sql.setString(1,yn);
            sql.setString(2,bloomClient.getFirstName());
            sql.setString(3,bloomClient.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeTrainer(String trainer, BloomClient bloomClient) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS SET TRAINER = ?" +
                                                        "WHERE FIRSTNAME = ? AND LASTNAME = ?;");
        try {
            sql.setString(1,trainer);
            sql.setString(2,bloomClient.getFirstName());
            sql.setString(3,bloomClient.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseItemAmount(Item item, int amt) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.INVENTORY SET AMOUNT = AMOUNT + ? WHERE NAME = ?");
        try {
            sql.setInt(1,amt);
            sql.setString(2,item.getName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void changeItemPrice(Item item, double price) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.INVENTORY SET PRICE = ? WHERE NAME= ?");
        try {
            sql.setDouble(1,price);
            sql.setString(2,item.getName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearSignInSheet() throws SQLException {
        PreparedStatement sql = c.prepareStatement("DELETE FROM BLOOMHEALTH.SIGNINSHEET;");
        try {
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeSSID(String result, BloomClient bloomClient) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS SET SSID = ?" +
                                                        " WHERE FIRSTNAME = ? AND LASTNAME = ?;");
        try {
            sql.setString(1,result);
            sql.setString(2,bloomClient.getFirstName());
            sql.setString(3,bloomClient.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void changeClientID(String clientID, BloomClient bloomClient)throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS SET CLIENTID = ?" +
                "WHERE FIRSTNAME = ? and LASTNAME = ?");
        try {
            sql.setString(1,clientID);
            sql.setString(2,bloomClient.getFirstName());
            sql.setString(3,bloomClient.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void decreaseItemAmount(Item item, int amt) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.INVENTORY SET AMOUNT = AMOUNT - ? WHERE NAME = ?");
        try {
            sql.setInt(1,amt);
            sql.setString(2,item.getName());
            sql.execute();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void changeClientBirthday(String result, BloomClient bloomClient) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS SET BIRTHDAY = ?" +
                "WHERE FIRSTNAME = ? and LASTNAME = ?");
        bloomClient.setBirthday(result);
        try {
            sql.setString(1,bloomClient.getBirthday());
            sql.setString(2,bloomClient.getFirstName());
            sql.setString(3,bloomClient.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeNumber(String result, BloomClient bloomClient) throws SQLException {
        PreparedStatement sql = c.prepareStatement("UPDATE BLOOMHEALTH.CLIENTS SET PHONENUMBER = ?" +
                "WHERE FIRSTNAME = ? and LASTNAME = ?");
        bloomClient.setPhoneNumber(result);
        try {
            sql.setString(1,bloomClient.getPhoneNumber());
            sql.setString(2,bloomClient.getFirstName());
            sql.setString(3,bloomClient.getLastName());
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countAttendance() throws SQLException {
        int i = 0;
        PreparedStatement sql = c.prepareStatement("SELECT * FROM BLOOMHEALTH.SIGNINSHEET;");
        try {
            ResultSet data = sql.executeQuery();
            while (data.next()) {
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

}
