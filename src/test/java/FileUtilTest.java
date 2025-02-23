import org.flc.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.flc.Constants.BASE_PATH_TEST;

public class FileUtilTest {

    @Test
    void testWriteOutputAndReadFile() throws IOException {
        String testFile = BASE_PATH_TEST + "test_output.csv";
        List<String> testData = Arrays.asList("Tag,Count", "sv_P1,2", "email,1");
        FileUtil.writeOutput(testFile, testData);

        List<String> readData = FileUtil.readFile(testFile);
        assertEquals(testData, readData);
    }
}
