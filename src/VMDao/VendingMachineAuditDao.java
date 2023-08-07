package VMDao;

import File_Utility.FilePersistenceException;
import File_Utility.FileUtility;
public class VendingMachineAuditDao {
    private static final String AUDIT_FILE = "/Users/luch/Wile Edge Software Dev Course/GitHubCommits/Vending_Machine/src/audit_log.txt";

    // Write an audit entry to the audit log.
    public void writeAuditEntry(String entry) throws FilePersistenceException {
        // Generate a timestamped entry for tracking
        String logEntry = timestampedEntry(entry);
        // Append the entry to the audit file
        FileUtility.appendToFile(AUDIT_FILE, logEntry);
    }

    //Generate a timestamped log entry for better auditing.
    // entry - The content of the audit entry.
    // return A string that combines the current timestamp with the provided entry.

    private String timestampedEntry(String entry) {
        String timestamp = java.time.LocalDateTime.now().toString();
        return timestamp + " : " + entry + "\n";
    }
}
