package File_Utility;

// An exception for file persistence problems.
public class FilePersistenceException extends Exception {
    public FilePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
