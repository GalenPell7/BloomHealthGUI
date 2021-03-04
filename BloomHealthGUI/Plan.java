package BloomHealthGUI;



public class Plan {
    private String plan;

    private Double individualPrice;

    private Double couplePrice;

    public Plan()
    {
        plan = "";
        individualPrice = 0.0;
        couplePrice = 0.0;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Double getIndividualPrice() {
        return individualPrice;
    }

    public void setIndividualPrice(Double individualPrice) {
        this.individualPrice = individualPrice;
    }

    public Double getCouplePrice() {
        return couplePrice;
    }

    public void setCouplePrice(Double couplePrice) {
        this.couplePrice = couplePrice;
    }
}
