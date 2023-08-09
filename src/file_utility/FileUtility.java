package file_utility;
import itemdto.Item;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


// This class relates to the Inventory file of the Vending Machine.
public class FileUtility {

    /**
     * Reads the file from the specified path and returns a list of items.
     *
     * @param filePath The path to the file.
     * @return A list of items read from the file.
     * @throws FilePersistenceException if there's a problem reading the file.
     */
    public static List<Item> readFile(String filePath) throws FilePersistenceException {
        List<Item> items = new ArrayList<>();

        try {
            // Read all lines from the file into a list
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for(String line : lines) {
                String[] parts = line.split(","); // Split the line by comma
                if(parts.length < 4) {
                    // Skip bad lines
                    continue;
                }

                String id = parts[0].trim();
                String name = parts[1].trim();
                BigDecimal cost = new BigDecimal(parts[2].trim());
                int inventory = Integer.parseInt(parts[3].trim());
                items.add(new Item(id, name, cost, inventory));
            }
        } catch(IOException e) {
            Throwable cause = new Throwable();
            throw new FilePersistenceException("Could not read from the file.", e);
        }

        return items;
    }

    /**
     * Writes a list of items to the specified file.
     *
     * @param items The list of items to write.
     * @param filePath The path to the file.
     * @throws FilePersistenceException if there's a problem writing to the file.
     */
    public static void writeFile(List<Item> items, String filePath) throws FilePersistenceException {
        List<String> lines = new ArrayList<>();

        for(Item item : items) {
            // Format item details and add to the list
            lines.add(String.format("%s,%s,%s,%d", item.getId(), item.getName(), item.getCost(), item.getInventory()));
        }

        try {
            // Write the list of lines to the file
            Files.write(Paths.get(filePath), lines);
        } catch(IOException e) {
            Throwable cause = new Throwable();
            throw new FilePersistenceException("Could not write to the file.", e);
        }
    }

    /**
     * Appends a log entry to the specified audit file.
     *
     * @param auditFile The path to the audit file.
     * @param logEntry The log entry to append.
     * @throws FilePersistenceException if there's a problem appending to the file.
     */
    public static void appendToFile(String auditFile, String logEntry) throws FilePersistenceException {
        try {
            // Append the log entry to the file
            Files.write(Paths.get(auditFile), logEntry.getBytes(), StandardOpenOption.APPEND);
        } catch(IOException e) {
            throw new FilePersistenceException("Could not append to the file.", e);
        }
    }
}

