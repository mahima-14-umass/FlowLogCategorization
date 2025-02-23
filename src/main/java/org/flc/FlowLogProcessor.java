package org.flc;

import java.util.AbstractMap;
import java.util.Map;

public class FlowLogProcessor {
    public static Map.Entry<String, String> processLog(String log, Map<String, String> lookupTable, Map<String, String> protocolTable) {
        String[] parts = log.split(" ");
//        if (parts.length < 14) return new AbstractMap.SimpleEntry<>("Untagged", "0,unknown");

        String protocol = protocolTable.getOrDefault(parts[7], "unknown");
        String dstPort = parts[6];
        String key = (dstPort + "," + protocol).toLowerCase();

        String tag = lookupTable.getOrDefault(key, "Untagged");
        return new AbstractMap.SimpleEntry<>(tag, dstPort + "," + protocol);
    }
}