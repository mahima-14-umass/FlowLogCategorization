package org.flc;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import static org.flc.Constants.*;

public class FlowLogTagger {



    // Defines the number of parallel threads based on the system's available CPU cores.
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        try {
            Map<String, String> lookupTable = LookupTableLoader.loadLookupTable(LOOKUP_FILE);
            Map<String, String> protocolTable = LookupTableLoader.loadProtocolTable(PROTOCOL_FILE);
            List<String> flowLogs = Files.readAllLines(Paths.get(FLOW_LOG_FILE));

            TagCounter tagCounter = new TagCounter();
            try (ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)) {
                List<Future<Map.Entry<String, String>>> futures = new ArrayList<>();

                for (String log : flowLogs) {
                    futures.add(executorService.submit(() -> FlowLogProcessor.processLog(log, lookupTable, protocolTable)));
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
