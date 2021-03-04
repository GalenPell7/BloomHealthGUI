package BloomHealthGUI;

public class Event {

    private String title;
    private String trainer;
    private String date;
    private String startTime;
    private String endTime;
    private int size;
    private String days;

    public Event()
    {
        title = "";
        trainer = "";
        date = "";
        startTime = "";
        endTime = "";
        size = 0;
        days = "";
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrainer() {
        return trainer;
    }


    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }


    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDays() {
        return days;
    }


    public void setDays(String days) {
        this.days = days;
    }
}
