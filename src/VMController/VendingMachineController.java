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

    public BigDecimal getRemainingAmount(BigDecimal depositedAmount, BigDecimal itemCost) {
        return depositedAmount.subtract(itemCost);
    }

    public void run() throws FilePersistenceException {
        service.loadItems();
        view.displayWelcomeMessage();
        displayItemsAndPrices();

        boolean keepRunning = true;
        while(keepRunning) {
            int menuSelection = view.displayMenuAndGetSelection();

            Item potentialItem = service.getItemById(String.valueOf(menuSelection));
            if (potentialItem != null) {
                if (amountDeposited.compareTo(BigDecimal.ZERO) <= 0) {
                    view.displayErrorMessage("Please deposit money before selecting an item.");
                } else {
                    purchaseItem(String.valueOf(menuSelection), amountDeposited);
                    amountDeposited = BigDecimal.ZERO;  // Reset the deposited amount after a purchase
                }
            } else {
                switch(menuSelection) {
                    case 1:
                        BigDecimal newDeposit = view.getDepositedAmount();
                        amountDeposited = amountDeposited.add(newDeposit);
                        break;
                    case 2:
                        if (amountDeposited.compareTo(BigDecimal.ZERO) <= 0) {
                            view.displayErrorMessage("Please deposit money before selecting an item.");
                        } else {
                            displayItemsAndPrices();
                            String selectedItem = view.getItemSelection();
                            purchaseItem(selectedItem, amountDeposited);
                            amountDeposited = BigDecimal.ZERO;  // Reset the deposited amount after a purchase
                        }
                        break;
                    case 3:
                        if (amountDeposited.compareTo(BigDecimal.ZERO) > 0) {
                            Change change = new Change(amountDeposited);
                            view.displayChange(change);
                            amountDeposited = BigDecimal.ZERO;  // Reset the deposited amount after a purchase
                        }
                        view.displayGoodbyeMessage();
                        keepRunning = false;
                        break;
                    default:
                        view.displayErrorMessage("Unknown Command!");
                }
            }
//            Item potentialItem = service.getItemById(String.valueOf(menuSelection));
//            if (potentialItem != null) {
//                purchaseItem(String.valueOf(menuSelection), amountDeposited);
//            } else if (menuSelection != 1 && menuSelection != 2 && menuSelection != 3) {

//            }
        }
    }

    // Display all items and their prices.
    private void displayItemsAndPrices() {
        List<Item> items = service.getAllItems();
        view.displayItems(items);
    }

    // To purchase a selected item.
    private void purchaseItem(String selectedItemId, BigDecimal amountDeposited ){
        try {
            Item chosenItem = service.getItemById(selectedItemId);

            if (chosenItem == null) {
                view.displayErrorMessage("Item not found in inventory.");
                return;
            }
            if (chosenItem.getCost() == null) {
                view.displayErrorMessage("Item cost is missing.");
                return;
            }

            // Check if the deposited amount is enough
            while (amountDeposited.compareTo(chosenItem.getCost()) < 0) {
                view.displayErrorMessage("Insufficient funds. Please deposit more money.");
                BigDecimal additionalAmount = view.getDepositedAmount();
                amountDeposited = amountDeposited.add(additionalAmount);
            }
            // Calculate the change before updating the deposited amount
            Change change = service.calculateChange(chosenItem.getCost(), amountDeposited);

            // Vend the item and update the deposited  amount
            service.vendItem(selectedItemId, amountDeposited);
            view.displayChange(change);
//            amountDeposited = getRemainingAmount(amountDeposited, chosenItem.getCost());


            try {
                auditDao.writeAuditEntry("Item purchased: " + selectedItemId + ", Amount: " + amountDeposited + ", Change given: " + change.totalChangeValue());
            } catch (FilePersistenceException e) {
                throw new RuntimeException(e);
            }
        } catch(InsufficientFundsException | NoItemInventoryException e) {
            view.displayErrorMessage(e.getMessage());
        }

        System.out.println("Thank you for your purchase!");
        System.out.println("\n");  // Adding a newline for better visibility
//        view.displayWelcomeMessage();
        displayItemsAndPrices();
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
