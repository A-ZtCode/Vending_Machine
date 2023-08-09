package vmservice;
import file_utility.FilePersistenceException;
import file_utility.FileUtility;
import itemdto.Item;
import vmdao.VendingMachineDao;
import vmview.Change;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

//  This class provides services related to the Vending Machine's operations.
public class VendingMachineService {
    private VendingMachineDao dao;


    private  List<Item> items;  // This will hold items in memory

    // Constructor to initialise the service with a data access object to manage the inventory
    public VendingMachineService(VendingMachineDao dao) throws FilePersistenceException {
        this.dao = dao;
        try {
            loadItems();
        } catch (FilePersistenceException e) {
            System.out.println("Error loading items from file: " + e.getMessage());
            System.exit(1);
        }
    }
        public void loadItems() throws FilePersistenceException {
            items = FileUtility.readFile("/Users/luch/Wile Edge Software Dev Course/Software Devlopment Training/Engage Excersices/Vending_Machine/src/Inventory.txt");
        }

    // Retrieves all items present in the inventory.
    public List<Item> getAllItems() {
        return this.items.stream()
                .filter(item -> item.getInventory() > 0)
                .collect(Collectors.toList());
    }

    // Fetches an item from the inventory based on its  ID.
    public Item getItemById(String itemId) {
        return this.items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(itemId))
                .findFirst()
                .orElse(null);
    }

        /**
         * Facilitates the vending of a specified item.
         *
         * @param itemId The ID of the item.
         * @param depositedAmount The amount deposited by the user.
         * @throws InsufficientFundsException If the deposited amount is less than the item's cost.
         * @throws NoItemInventoryException If the item is not available in stock.
         */

    public void vendItem(String itemId, BigDecimal depositedAmount) throws InsufficientFundsException, NoItemInventoryException {
        Item item = getItemById(itemId);
        if(item == null) {
            throw new NoItemInventoryException("Item not found.");
        }
        checkItemAvailability(item);
        BigDecimal cost = item.getCost();
        if (cost.compareTo(depositedAmount) > 0) {
            throw new InsufficientFundsException("Deposited amount is less than item cost.");
        }
        item.reduceInventoryByOne();
        dao.updateItem(item);
        logTransaction(item, depositedAmount, calculateChange(cost, depositedAmount));
    }

        /**
         * Calculates the change to be returned to the user.
         *
         * @param cost The item's cost.
         * @param depositedAmount The amount deposited by the user.
         * @return The calculated change.
         */
    public Change calculateChange(BigDecimal cost, BigDecimal depositedAmount) {
        return new Change(depositedAmount.subtract(cost));
    }

        /**
         * Logs the transaction details.
         *
         * @param item The item being vended.
         * @param depositedAmount The amount deposited by the user.
         * @param change The change to be returned to the user.
         */
    private void logTransaction(Item item, BigDecimal depositedAmount, Change change) {
        String logEntry =
                "Item vended: " + item.getId() +
                ", Name" + item.getName() +
                ", Cost: £" + item.getCost() +
                ", Deposited Amount: £" + depositedAmount +
                ", Change: " + change.totalChangeValue();
        System.out.println(logEntry);
    }

        /**
         * Checks if the specified item is available in stock.
         *
         * @param item The item to be checked.
         * @throws NoItemInventoryException If the item is out of stock.
         */
    private void checkItemAvailability(Item item) throws NoItemInventoryException {
        if (item.getInventory() <= 0) {
            throw new NoItemInventoryException("Item out of stock.");
        }
    }

        /**
         * Calculates the remaining balance after deducting the item's cost.
         *
         * @param depositedAmount The amount deposited by the user.
         * @param itemCost The cost of the item.
         * @return The remaining balance.
         */
    public BigDecimal getRemainingAmount(BigDecimal depositedAmount, BigDecimal itemCost) {
        return depositedAmount.subtract(itemCost);
    }
}
