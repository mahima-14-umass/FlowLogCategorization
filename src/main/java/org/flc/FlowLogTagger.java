package org.flc;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class FlowLogTagger {
    private static final String LOOKUP_FILE = "resources/lookup.csv";
    private static final String FLOW_LOG_FILE = "resources/flowlogs.txt";
    private static final String OUTPUT_FILE = "output.csv";

    // Defines the number of parallel threads based on the system's available CPU cores.
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        try {
            Map<String, String> lookupTable = LookupTableLoader.loadLookupTable(LOOKUP_FILE);
            List<String> flowLogs = Files.readAllLines(Paths.get(FLOW_LOG_FILE));

            TagCounter tagCounter = new TagCounter();
            try (ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)) {
                List<Future<Map.Entry<String, String>>> futures = new ArrayList<>();

                for (String log : flowLogs) {
                    futures.add(executorService.submit(() -> FlowLogProcessor.processLog(log, lookupTable)));
                }

                for (Future<Map.Entry<String, String>> future : futures) {
                    tagCounter.addEntry(future.get());
                }
                executorService.shutdown();
            }
            FileUtil.writeOutput(OUTPUT_FILE, tagCounter.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
