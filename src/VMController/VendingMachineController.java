package VMController;

import File_Utility.FilePersistenceException;
import ItemDTO.Item;
import VMDao.VendingMachineAuditDao;
import VMService.InsufficientFundsException;
import VMService.NoItemInventoryException;
import VMService.VendingMachineService;
import VMView.Change;
import VMView.VendingMachineView;

import java.math.BigDecimal;
import java.util.List;

public class VendingMachineController {
    private VendingMachineService service;
    private VendingMachineView view;
    private VendingMachineAuditDao auditDao;
    private BigDecimal amountDeposited = BigDecimal.ZERO;

    // Constructor to initialise the controller with service, view, and audit DAO components.
    public VendingMachineController(VendingMachineService service, VendingMachineView view, VendingMachineAuditDao auditDao) {
        this.service = service;
        this.view = view;
        this.auditDao = auditDao;
    }

    public void run() {
        boolean keepRunning = true;
        while(keepRunning) {
            int menuSelection = view.displayMenuAndGetSelection();
            switch(menuSelection) {
                case 1:
                    BigDecimal newDeposit = view.getDepositedAmount();
                    amountDeposited = amountDeposited.add(newDeposit);
                    view.displayRemainingAmount(amountDeposited);  // Display remaining amount after deposit
                    break;
                case 2:
                    displayItemsAndPrices();
                    String selectedItem = view.getItemSelection();
                    purchaseItem(selectedItem);
                    view.displayRemainingAmount(amountDeposited);  // Display remaining amount after purchase
                    break;
                case 3:
                    view.displayGoodbyeMessage();  // Display goodbye message
                    amountDeposited = BigDecimal.ZERO;
                    keepRunning = false; // This will break out of the loop, ending the program
                    break;
                default:
                    view.displayErrorMessage("Unknown Command!");
            }
        }
    }

    // Display all items and their prices.
    private void processDepositedAmount() {
    }

    private void displayItemsAndPrices() {
        List<Item> items = service.getAllItems();
        view.displayItems(items);
    }

    // To purchase a selected item.
    private void purchaseItem(String selectedItemId) {
        try {
            Item chosenItem = service.getItem(selectedItemId);
            if (chosenItem.getCost() == null) {
                view.displayErrorMessage("Item cost is missing.");
                return;
            }
            else if (chosenItem == null) {
                view.displayErrorMessage("Item not found in inventory.");
                return;
            }

            // Check if the deposited amount is enough
            while (amountDeposited.compareTo(chosenItem.getCost()) < 0) {
                view.displayErrorMessage("Insufficient funds. Please deposit more money.");
                BigDecimal additionalAmount = view.getDepositedAmount();
                amountDeposited = amountDeposited.add(additionalAmount);
            }

            service.vendItem(selectedItemId, amountDeposited);
            amountDeposited = amountDeposited.subtract(chosenItem.getCost());

            Change change = service.calculateChange(chosenItem.getCost(), amountDeposited);
            view.displayChange(change);

            try {
                auditDao.writeAuditEntry("Item purchased: " + selectedItemId + ", Amount: " + amountDeposited + ", Change given: " + change.totalChangeValue());
            } catch (FilePersistenceException e) {
                throw new RuntimeException(e);
            }
        } catch(InsufficientFundsException | NoItemInventoryException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    // Process the deposited amount ensuring it's valid.
    private void processDepositedAmount(BigDecimal amountDeposited) {
        BigDecimal deposited = view.getDepositedAmount();
        while(deposited.compareTo(BigDecimal.ZERO) <= 0) {
            view.displayErrorMessage("Invalid amount deposited. Please deposit a valid amount.");
            deposited = view.getDepositedAmount();
        }
        amountDeposited = deposited;
    }


}
