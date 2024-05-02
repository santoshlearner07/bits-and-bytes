package staff;

public class WaiterOrderItem {
    private String name;
    private String price;

    public WaiterOrderItem(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
