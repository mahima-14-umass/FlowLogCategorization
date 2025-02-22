package org.flc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TagCounter {
    private final Map<String, Integer> tagCounts = new HashMap<>();
    private final Map<String, Integer> portProtocolCounts = new HashMap<>();

    public synchronized void addEntry(Map.Entry<String, String> entry) {
        tagCounts.put(entry.getKey(), tagCounts.getOrDefault(entry.getKey(), 0) + 1);
        portProtocolCounts.put(entry.getValue(), portProtocolCounts.getOrDefault(entry.getValue(), 0) + 1);
    }

    public List<String> getResults() {
        List<String> results = new ArrayList<>();
        results.add("Tag,Count");
        tagCounts.forEach((tag, count) -> results.add(tag + "," + count));

        results.add("\nPort/Protocol Combination Counts:");
        results.add("Port,Protocol,Count");
        portProtocolCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> results.add(entry.getKey() + "," + entry.getValue()));

        return results;
    }
}

