package BloomHealthGUI;

public class Session {
    private Integer session;

    private Double price;

    private Double pPS;

    public Session()
    {
        session = 0;
        price = 0.0;
        pPS = 0.0;
    }

    public Integer getSessions() {
        return session;
    }

    public void setSessions(Integer sessions) {
        this.session = sessions;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getpPS() {
        return pPS;
    }

    public void setpPS(Double pPS) {
        this.pPS = pPS;
    }
}
