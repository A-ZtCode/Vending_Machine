package VMDao;
import File_Utility.FilePersistenceException;
import File_Utility.FileUtility;
import ItemDTO.Item;

import java.util.ArrayList;
import java.util.List;

public class VendingMachineDao {
    private List<Item> inventory;

    /**
     * Constructor to initialise the vending machine data access object.
     * It loads the inventory upon creation.
     */
    public VendingMachineDao() {
        loadInventory();
    }

    /**
     * Fetches an item from the inventory based on its name.
     *
     * @param itemName Name of the item to be fetched.
     * @return The item if it's found, otherwise returns null.
     */
    public Item getItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves all items present in the inventory.
     *
     * @return List of items in the inventory.
     */

    public List<Item> getAllItems() {
        return inventory;
    }

    /**
     * Updates the details of a given item in the inventory.
     *
     * @param item The item to be updated.
     */
    public void updateItem(Item item) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getName().equals(item.getName())) {
                inventory.set(i, item);
                break;
            }
        }
        saveInventory();
    }

    // Loads the inventory from a file into the inventory list.
    private void loadInventory() {
        try {
            inventory = FileUtility.readFile("/Users/luch/Wile Edge Software Dev Course/Software Devlopment Training/Engage Excersices/Vending_Machine/src/Inventory.txt");
            if (inventory == null || inventory.isEmpty()) {
                Throwable cause = new Throwable();
                throw new FilePersistenceException("Inventory is empty or not found", cause);
            }
        } catch (FilePersistenceException e) {
            // Handle error gracefully, e.g., log it, show a user-friendly message, etc.
            inventory = new ArrayList<>();
            System.err.println("Error loading inventory: " + e.getMessage());
        }
    }

    // Saves the current inventory to a file.
    private void saveInventory() {
        try {
            FileUtility.writeFile(inventory, "/Users/luch/Wile Edge Software Dev Course/Software Devlopment Training/Engage Excersices/Vending_Machine/src/Inventory.txt");
        } catch (FilePersistenceException e) {
            // Handle error gracefully, e.g., log it, show a user-friendly message, etc.
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }
}
