import org.flc.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilTest {

    @Test
    void testFileUtil() throws IOException {
        String testFile = "test_output.csv";
        List<String> testData = Arrays.asList("Tag,Count", "sv_P1,2", "email,1");
        FileUtil.writeOutput(testFile, testData);

        List<String> readData = Files.readAllLines(Paths.get(testFile));
        assertEquals(testData, readData);
    }
}
