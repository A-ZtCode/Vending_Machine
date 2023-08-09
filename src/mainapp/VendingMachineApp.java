package mainapp;

import vmcontroller.VendingMachineController;
import vmdao.VendingMachineAuditDao;
import vmdao.VendingMachineDao;
import vmservice.VendingMachineService;
import vmview.VendingMachineView;

// This is the Vending Machine main Application
public class VendingMachineApp {
    public static void main(String[] args) {
        try {
        // Step 1: Initialisation
            // Create the necessary objects for the application
        VendingMachineView view = new VendingMachineView();  // Initialise the view layer
        VendingMachineDao dao = new VendingMachineDao(); // Initialise the data access layer
        VendingMachineAuditDao auditDao = new VendingMachineAuditDao(); // Initialise the audit layer (for logging/auditing purposes)
        VendingMachineService service = new VendingMachineService(dao); // Initialise the service layer (dao)

        // Step 2: Dependency Injection
            // Initialise the controller with service, view, and audit components
        VendingMachineController controller = new VendingMachineController(service, view, auditDao);

        // Step 3: Running the Application
        controller.run();
        } catch (Exception e) { // To handle any unexpected exceptions
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
