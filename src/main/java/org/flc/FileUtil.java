package org.flc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * FileUtil is responsible for handling file reading and writing operations efficiently.
 * It ensures that large files are processed without performance bottlenecks.
 */
public class FileUtil {

    /**
     * Writes data to the specified file.
     *
     * @param filename The file to write to
     * @param results The list of strings to write
     * @throws IOException If an error occurs during writing
     */
    public static void writeOutput(String filename, List<String> results) throws IOException {
        Files.write(Paths.get(filename), results);
    }

    /**
     * reads data from the specified file.
     *
     * @param filename The file to read from
     * @throws IOException If an error occurs during reading
     */
    public static List<String> readFile(String filename) throws IOException {
        return Files.readAllLines(Paths.get(filename));
    }
}
