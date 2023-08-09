package vmservice;

public class NoItemInventoryException extends Exception {
    // Constructs a NoItemInventoryException with a specified detail message explaining the cause of the exception
    public NoItemInventoryException(String message) {
        super(message);
    }
}
