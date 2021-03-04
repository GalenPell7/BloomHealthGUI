package BloomHealthGUI;


public class BloomClient {

    private String FirstName;
    private String LastName;
    private String PhoneNumber;
    private String Birthday;
    private String StartDate;
    private String ExpireDate;
    private int daysLeft;
    private String time;
    private String date;
    private int sessionAmount;
    private String SsMember;
    private String trainer;
    private String ssID;
    private String memType;
    private String clientID;

    public BloomClient()
    {
        clientID = "";
       FirstName ="";
       LastName ="";
       PhoneNumber ="";
       Birthday ="";
       StartDate ="";
       ExpireDate ="";
       time ="";
       date ="";
       daysLeft = 0;
       sessionAmount = 0;
       SsMember ="";
       trainer ="";
       ssID = "";
       memType = "";
    }
    public void setFirstName(String name)
    {
        this.FirstName = name;
    }
    public String getFirstName()
    {
        return FirstName;
    }

    public void setLastName(String name)
    {
        this.LastName = name;
    }
    public String getLastName()
    {
        return LastName;
    }

    public void setPhoneNumber(String num)
    {
        this.PhoneNumber = num;
    }
    public String getPhoneNumber()
    {
        return PhoneNumber;
    }


    public void setBirthday(String date)
    {
        this.Birthday = date;
    }
    public String getBirthday()
    {
        return Birthday;
    }


    public void setStartDate(String date)
    {
        this.StartDate = date;
    }
    public String getStartDate()
    {
        return StartDate;
    }
    public void setExpireDate(String date)
    {
        this.ExpireDate = date;
    }

    public String getExpireDate()
    {
        return ExpireDate;
    }

    public int getDaysLeft()
    {
        return daysLeft;
    }
    public void setDaysLeft(int num)
    {
        this.daysLeft = num;
    }
    public String getTime()
    {
        return time;
    }
    public void setTime(String timeStamp)
    {
        this.time = timeStamp;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public String getDate()
    {
        return date;
    }
    public void setSessionAmount(int amt)
    {
        this.sessionAmount = amt;
    }
    public int getSessionAmount()
    {
        return sessionAmount;
    }
    public void setSsMember(String ssMember)
    {
        this.SsMember = ssMember;
    }
    public String getSsMember()
    {
        return SsMember;
    }
    public void setTrainer(String trainer)
    {
        this.trainer = trainer;
    }
    public String getTrainer()
    {
        return trainer;
    }
    public void setSsID(String id)
    {
        ssID = id;
    }
    public String getSsID()
    {
        return ssID;
    }

    public String getMemType() {
        return memType;
    }

    public void setMemType(String memType) {
        this.memType = memType;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}
