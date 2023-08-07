package VMService;
import ItemDTO.Item;
import VMDao.VendingMachineDao;
import VMView.Change;

import java.math.BigDecimal;
import java.util.List;

    //  This class provides services related to the Vending Machine's operations.
public class VendingMachineService {
    private VendingMachineDao dao;

    // Constructor to initialise the service with a data access object to manage the inventory
    public VendingMachineService(VendingMachineDao dao) {
        this.dao = dao;
    }

    // Retrieves all items present in the inventory.
    public List<Item> getAllItems() {
        return dao.getAllItems();
    }

    // Fetches an item from the inventory based on its name.
    public Item getItem(String itemName) {
        return dao.getItem(itemName);
    }

        /**
         * Facilitates the vending of a specified item.
         *
         * @param itemName The name of the item.
         * @param depositedAmount The amount deposited by the user.
         * @throws InsufficientFundsException If the deposited amount is less than the item's cost.
         * @throws NoItemInventoryException If the item is not available in stock.
         */

    public void vendItem(String itemName, BigDecimal depositedAmount) throws InsufficientFundsException, NoItemInventoryException {
        Item item = getItem(itemName);
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
        String logEntry = "Item vended: " + item.getName() +
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
