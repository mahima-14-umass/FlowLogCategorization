package org.flc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(FlowLogTagger.class);

    /**
     * The entry point of the program. It loads necessary files, processes logs in parallel,
     * and writes the results to an output file.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            logger.info("Loading lookup and protocol tables");
            Map<String, String> lookupTable = LookupTableLoader.loadLookupTable(LOOKUP_FILE);
            Map<String, String> protocolTable = LookupTableLoader.loadProtocolTable(PROTOCOL_FILE);

            logger.info("Reading flow logs from the file");
            List<String> flowLogs = Files.readAllLines(Paths.get(FLOW_LOG_FILE));

            TagCounter tagCounter = new TagCounter();
            try (ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)) {
                List<Future<Map.Entry<String, String>>> futures = new ArrayList<>();

                logger.info("Processing each log entry concurrently");
                for (String log : flowLogs) {
                    futures.add(executorService.submit(() -> FlowLogProcessor.processLog(log, lookupTable, protocolTable)));
                }

                logger.info("Collecting results from future tasks");
                for (Future<Map.Entry<String, String>> future : futures) {
                    tagCounter.addEntry(future.get());
                }
                executorService.shutdown();
            }

            logger.info("Writing results to the output file");
            FileUtil.writeOutput(OUTPUT_FILE, tagCounter.getResults());
            long endTime = System.currentTimeMillis();
            long totalExecutionTime = endTime - startTime;
            logger.info("Total Execution Time: " + totalExecutionTime + " ms");
        } catch (Exception e) {
            // Print stack trace for debugging in case of errors
            e.printStackTrace();
        }
    }
}
