import org.flc.FlowLogTagger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

import static org.flc.Constants.BASE_PATH_TEST;
import static org.junit.jupiter.api.Assertions.*;

class FlowLogTaggerLoadTest {
    private static final String FLOW_LOG_FILE = BASE_PATH_TEST + "flowlogs.txt";
    private static final int THREAD_COUNT = 10; // Simulating 10 concurrent users
    private static final int ITERATIONS_PER_THREAD = 1000; // Number of logs per thread

    @BeforeEach
    void setUp() throws IOException {
        // Generate a large flow log file if not exists
        if (!Files.exists(Paths.get(FLOW_LOG_FILE))) {
            List<String> logs = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                logs.add("2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK");
            }
            Files.write(Paths.get(FLOW_LOG_FILE), logs);
        }
    }

    @Test
    void testConcurrentProcessingPerformance() throws InterruptedException, ExecutionException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<Long>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            futures.add(executorService.submit(() -> {
                try {
                    FlowLogTagger.main(new String[]{});
                } catch (Exception e) {
                    fail("Exception occurred during concurrent execution: " + e.getMessage());
                }
                return System.currentTimeMillis();
            }));
        }

        for (Future<Long> future : futures) {
            future.get(); // Wait for all threads to complete
        }

        long endTime = System.currentTimeMillis();
        long totalExecutionTime = endTime - startTime;

        executorService.shutdown();
        double fileSize = Math.round((double) Files.size(Paths.get(FLOW_LOG_FILE)) / (1024 * 1024 * 1024));
        System.out.println("Total Execution Time: " + totalExecutionTime + " ms for file with size : " + fileSize + "GB");
        assertTrue(totalExecutionTime < 10000, "Execution time should be under 10 seconds for performance test.");
    }
}
