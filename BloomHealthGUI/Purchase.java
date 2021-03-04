package BloomHealthGUI;



public class Purchase {

    private String purchase;
    private String type;
    private Double price;
    private String date;

    public Purchase()
    {
        purchase = "";
        type = "";
        price = 0.0;
        date = ";";

    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
