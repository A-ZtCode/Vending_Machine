package VMView;
import ItemDTO.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

 // This class provides the user interface for interacting with the vending machine.
public class VendingMachineView {
    private Scanner scanner = new Scanner(System.in);

     // Displays the main menu of the vending machine and takes the user's selection.

    public int displayMenuAndGetSelection() {
        System.out.println("1. Deposit Money");
        System.out.println("2. Purchase Item");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        return Integer.parseInt(scanner.nextLine());
    }

     // Takes the name of the item the user wishes to purchase.

    public String getItemSelection() {
        System.out.println("Enter the ID of the item you wish to purchase:");
        return scanner.nextLine();
    }
     // Takes the amount of money the user deposits in GBP.

    public BigDecimal getDepositedAmount() {
        while (true) {  // Using a loop to ensure valid input
            System.out.println("Enter the amount of money you are depositing (in GBP):");
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid monetary amount.");
            }
        }
    }

     //  Displays a list of available items in the vending machine.

    public void displayItems(List<Item> items) {
        System.out.println("\nAvailable Items:");
        for (Item item : items) {
            System.out.println(item.getId() + " - " + item.getName() + ": £" + item.getCost() + " (Inventory: " + item.getInventory() + ")");
        }
        System.out.println("\n");  // Adding a newline for better visibility
    }

     // Displays the change returned to the user in coin.

    public void displayChange(Change change) {
        System.out.println("Your change is: " +
                "\n£2 Coins: " + change.getTwoPounds() +
                "\n£1 Coins: " + change.getOnePound() +
                "\n50p Coins: " + change.getFiftyPence() +
                "\n20p Coins: " + change.getTwentyPence() +
                "\n10p Coins: " + change.getTenPence() +
                "\n5p Coins: " + change.getFivePence() +
                "\n2p Coins: " + change.getTwoPence() +
                "\n1p Coins: " + change.getOnePence());
    }

     // Displays the remaining amount after a transaction.

    public void displayRemainingAmount(BigDecimal remainingAmount) {
        System.out.println("Remaining Amount: £" + remainingAmount);
    }

    // Displays a goodbye message when the user exits the vending machine.
    public void displayGoodbyeMessage() {
        System.out.println("Thank you for using the vending machine. Goodbye!");
    }

    // Displays error messages to the user.
    public void displayErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }
}
