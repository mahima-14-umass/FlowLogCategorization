package org.flc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LookupTableLoader is responsible for loading lookup and protocol mapping tables from files.
 * It efficiently reads and stores mappings for quick lookup during log processing.
 */
public class LookupTableLoader {

    /**
     * Loads the lookup table from a CSV file.
     *
     * @param filename The path to the lookup table file
     * @return A map containing destination port and protocol combinations mapped to tags
     * @throws IOException If an error occurs while reading the file
     */
    public static Map<String, String> loadLookupTable(String filename) throws IOException {
        Map<String, String> lookupTable = new HashMap<>();
        List<String> lines = FileUtil.readFile(filename);

        // Remove header line
        if (!lines.isEmpty()) {
            lines.remove(0);
        }

        // Parses lookup table file and loads it into a HashMap
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(",");
                String key = (parts[0] + "," + parts[1]).toLowerCase();
                lookupTable.put(key, parts[2]);
            }
        }
        return lookupTable;
    }

    /**
     * Loads the protocol table from a CSV file.
     *
     * @param filename The path to the protocol table file
     * @return A map containing protocol numbers mapped to their corresponding protocol names
     * @throws IOException If an error occurs while reading the file
     */
    public static Map<String, String> loadProtocolTable(String filename) throws IOException {
        Map<String, String> protocolTable = new HashMap<>();
        List<String> lines = FileUtil.readFile(filename);

        // Remove header line
        if (!lines.isEmpty()) {
            lines.remove(0);
        }

        // Parses protocol table file and loads it into a HashMap
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    protocolTable.put(parts[0].trim(), parts[1].trim().toLowerCase());
                }
            }
        }
        return protocolTable;
    }
}
