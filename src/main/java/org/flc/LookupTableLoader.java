package org.flc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LookupTableLoader {
    public static Map<String, String> loadLookupTable(String filename) throws IOException {
        Map<String, String> lookupTable = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(filename));

        if (!lines.isEmpty()) {
            lines.remove(0); // Remove header line
        }

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String key = (parts[0] + "," + parts[1]).toLowerCase();
                    lookupTable.put(key, parts[2]);
                }
            }
        }
        return lookupTable;
    }

    public static Map<String, String> loadProtocolTable(String filename) throws IOException {
        Map<String, String> protocolTable = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(filename));

        if (!lines.isEmpty()) {
            lines.remove(0); // Remove header line
        }

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
