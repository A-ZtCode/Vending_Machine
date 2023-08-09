package vmservice;

public class InsufficientFundsException extends Exception {

    // Creates an InsufficientFundsException with a specified detail message explaining the cause of the exception.

    public InsufficientFundsException(String message) {
        super(message);
    }
}
