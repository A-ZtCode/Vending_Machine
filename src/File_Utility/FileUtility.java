package File_Utility;
import ItemDTO.Item;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
                if(parts.length < 3) {
                    // Skip bad lines
                    continue;
                }
                String name = parts[0].trim();
                BigDecimal cost = new BigDecimal(parts[1].trim());
                int inventory = Integer.parseInt(parts[2].trim());
                items.add(new Item(name, cost, inventory));
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
            lines.add(String.format("%s,%s,%d", item.getName(), item.getCost(), item.getInventory()));
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

