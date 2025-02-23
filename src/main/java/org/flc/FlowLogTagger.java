package org.flc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.flc.Constants.*;


/**
 * FlowLogTagger is the main class responsible for processing flow logs.
 * It loads lookup and protocol tables, reads flow logs, processes them concurrently,
 * and writes the results to an output file.
 */
public class FlowLogTagger {
    // Defines the number of parallel threads based on the system's available CPU cores.
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * The entry point of the program. It loads necessary files, processes logs in parallel,
     * and writes the results to an output file.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Load lookup and protocol tables
            Map<String, String> lookupTable = LookupTableLoader.loadLookupTable(LOOKUP_FILE);
            Map<String, String> protocolTable = LookupTableLoader.loadProtocolTable(PROTOCOL_FILE);

            // Read flow logs from the file
            List<String> flowLogs = Files.readAllLines(Paths.get(FLOW_LOG_FILE));

            TagCounter tagCounter = new TagCounter();
            try (ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)) {
                List<Future<Map.Entry<String, String>>> futures = new ArrayList<>();

                // Process each log entry concurrently
                for (String log : flowLogs) {
                    futures.add(executorService.submit(() -> FlowLogProcessor.processLog(log, lookupTable, protocolTable)));
                }

                // Collect results from future tasks
                for (Future<Map.Entry<String, String>> future : futures) {
                    tagCounter.addEntry(future.get());
                }
                executorService.shutdown();
            }

            // Write results to the output file
            FileUtil.writeOutput(OUTPUT_FILE, tagCounter.getResults());
        } catch (Exception e) {
            // Print stack trace for debugging in case of errors
            e.printStackTrace();
        }
    }
}
