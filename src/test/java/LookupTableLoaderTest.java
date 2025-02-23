import org.flc.LookupTableLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.flc.Constants.LOOKUP_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class LookupTableLoaderTest {

    @Test
    void testLookupTableLoader() throws IOException {
        LookupTableLoader lookupTableLoader = spy(new LookupTableLoader());
        Map<String, String> mockLookup = new HashMap<>();
        mockLookup.put("443,tcp", "sv_P2");
        doReturn(mockLookup).when(lookupTableLoader).loadLookupTable(LOOKUP_FILE);
        assertEquals("sv_P2", lookupTableLoader.loadLookupTable(LOOKUP_FILE).get("443,tcp"));
    }

    @Test
    void testLoadLookupTable() throws IOException {
        Map<String, String> lookupTable = LookupTableLoader.loadLookupTable("lookup.csv");
        assertNotNull(lookupTable, "Lookup table should not be null");
    }

    @Test
    void testLoadProtocolTable() throws IOException {
        Map<String, String> protocolTable = LookupTableLoader.loadProtocolTable("protocol-numbers-1.csv");
        assertNotNull(protocolTable, "Protocol table should not be null");
    }
}
