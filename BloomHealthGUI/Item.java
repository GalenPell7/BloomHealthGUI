package BloomHealthGUI;



public class Item {
    private String name;
    private String type;
    private Double price;
    private Integer amount;

    public Item()
    {
        name = "";
        type = "";
        price = 0.0;
        amount = 0;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType()
    {
        return type;
    }
    public void setPrice(double price)
    {
        this.price= price;
    }
    public Double getPrice() {
        return price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public Integer getAmount(){
        return amount;
    }
}
