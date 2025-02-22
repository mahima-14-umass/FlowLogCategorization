package org.flc;

import java.util.AbstractMap;
import java.util.Map;

class FlowLogProcessor {
    public static Map.Entry<String, String> processLog(String log, Map<String, String> lookupTable) {
        String[] parts = log.split(" ");
        if (parts.length < 7) return new AbstractMap.SimpleEntry<>("Untagged", "1");

        String port = parts[5];
        String protocol = parts[6];
        String key = (port + "," + protocol).toLowerCase();

        String tag = lookupTable.getOrDefault(key, "Untagged");
        return new AbstractMap.SimpleEntry<>(tag, "1");
    }
}
