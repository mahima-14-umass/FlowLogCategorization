package org.flc;

import java.util.AbstractMap;
import java.util.Map;

/**
 * FlowLogProcessor is responsible for parsing and processing individual flow logs.
 * It applies lookup tables to assign tags to each log entry based on protocol and destination port.
 */
public class FlowLogProcessor {

    /**
     * Processes a single log entry and assigns a tag using lookup tables.
     *
     * @param log The log entry to process
     * @param lookupTable The lookup table mapping destination ports and protocols to tags
     * @param protocolTable The protocol table mapping protocol numbers to names
     * @return A map entry containing the assigned tag and the log details
     */
    public static Map.Entry<String, String> processLog(String log, Map<String, String> lookupTable, Map<String, String> protocolTable) {
        // Parse log entry and apply lookup table for tagging
        String[] parts = log.split(" ");

        String protocol = protocolTable.getOrDefault(parts[7], "unknown");
        String dstPort = parts[6];
        String key = (dstPort + "," + protocol).toLowerCase();

        String tag = lookupTable.getOrDefault(key, "Untagged");
        return new AbstractMap.SimpleEntry<>(tag, dstPort + "," + protocol);
    }
}