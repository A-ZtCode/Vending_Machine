package ItemDTO;
import java.math.BigDecimal;

public class Item {
    // Fields to store item details
    private  String id; // Item ID
    private String name; // Item Name
    private BigDecimal cost; // Item Cost
    private int inventory; // Item Inventory count

    //  Constructor to initialise an item with given details.
    public Item(String id, String name, BigDecimal cost, int inventory) {
    this.id = id;
    this.name = name;
    this.cost = cost;
    this.inventory = inventory;

    }

    //Getters and Setters

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void reduceInventoryByOne() {
        if (inventory > 0) {
            inventory--;
        }
    }
}
