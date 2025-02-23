import org.flc.TagCounter;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagCounterTest {
    @Test
    void testTagCounter() {
        TagCounter tagCounter = new TagCounter();
        tagCounter.addEntry(new AbstractMap.SimpleEntry<>("sv_P1", "443,tcp"));
        tagCounter.addEntry(new AbstractMap.SimpleEntry<>("sv_P1", "443,tcp"));
        tagCounter.addEntry(new AbstractMap.SimpleEntry<>("email", "993,tcp"));

        List<String> results = tagCounter.getResults();
        assertTrue(results.contains("sv_P1,2"));
        assertTrue(results.contains("email,1"));
    }
}
