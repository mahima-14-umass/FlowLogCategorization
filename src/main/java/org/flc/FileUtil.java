package org.flc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class FileUtil {
    public static void writeOutput(String filename, List<String> results) throws IOException {
        Files.write(Paths.get(filename), results);
    }
}
