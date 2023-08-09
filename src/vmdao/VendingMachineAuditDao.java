package vmdao;

import file_utility.FilePersistenceException;
import file_utility.FileUtility;

    // This class creates and writes to an Audit file regarding  the Vending Machine's operations.
public class VendingMachineAuditDao {
    private static final String AUDIT_FILE = "/Users/luch/Wile Edge Software Dev Course/Software Devlopment Training/Engage Excersices/Vending_Machine/src/audit_log.txt";

    /**
     * Write an audit entry to the audit log.
     * @param entry
     * Generate a timestamped entry for tracking
     * @throws FilePersistenceException
     * Append the entry to the audit file
     */
    public void writeAuditEntry(String entry) throws FilePersistenceException {
        String logEntry = timestampedEntry(entry);
        FileUtility.appendToFile(AUDIT_FILE, logEntry);
    }

    /**
     * Generate a timestamped log entry for  auditing.
     * @entry - The content of the audit entry.
     * @return A string that combines the current timestamp with the provided entry.
     */
    private String timestampedEntry(String entry) {
        String timestamp = java.time.LocalDateTime.now().toString();
        return timestamp + " : " + entry + "\n";
    }
}
