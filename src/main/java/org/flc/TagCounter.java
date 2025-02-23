package org.flc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TagCounter is responsible for aggregating and counting tagged data.
 * It stores tag counts and produces a summary for reporting.
 */
public class TagCounter {
    private final Map<String, Integer> tagCounts = new HashMap<>();
    private final Map<String, Integer> portProtocolCounts = new HashMap<>();

    /**
     * Adds a new entry to the tag counter.
     *
     * @param entry The log entry with the assigned tag
     */
    public synchronized void addEntry(Map.Entry<String, String> entry) {
        // adding each entry to both maps in a thread safe method
        tagCounts.put(entry.getKey(), tagCounts.getOrDefault(entry.getKey(), 0) + 1);
        portProtocolCounts.put(entry.getValue(), portProtocolCounts.getOrDefault(entry.getValue(), 0) + 1);
    }

    /**
     * Returns a list of tag counts as formatted strings.
     *
     * @return A list of strings representing tag counts
     */
    public List<String> getResults() {
        // adding each entry to the lists to write in the output file
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

