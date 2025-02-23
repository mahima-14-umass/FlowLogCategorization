import org.flc.FlowLogProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlowLogProcessorTest {

    @Mock
    FlowLogProcessor flowLogProcessor;

    @ParameterizedTest
    @CsvSource({
            "443, 6, sv_P2",
            "25, 6, sv_P1",
            "80, 6, Untagged"
    })
    void testProcessLog(String port, String protocolNum, String expectedTag) {
        Map<String, String> lookupTable = new HashMap<>();
        lookupTable.put("443,tcp", "sv_P2");
        lookupTable.put("25,tcp", "sv_P1");

        Map<String, String> protocolTable = new HashMap<>();
        protocolTable.put("6", "tcp");
        protocolTable.put("17", "udp");

        String log = "2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 49153 " + port + " " + protocolNum + " 25 20000 1620140761 1620140821 ACCEPT OK";
        Map.Entry<String, String> result = FlowLogProcessor.processLog(log, lookupTable, protocolTable);

        assertEquals(expectedTag, result.getKey());
    }

    @Test
    void testProcessLogUnknownProtocol() {
        Map<String, String> lookupTable = new HashMap<>();
        lookupTable.put("443,tcp", "sv_P2");

        Map<String, String> protocolTable = new HashMap<>();
        protocolTable.put("6", "tcp");
        protocolTable.put("17", "udp");

        String log = "2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 99 25 20000 1620140761 1620140821 ACCEPT OK";
        Map.Entry<String, String> result = FlowLogProcessor.processLog(log, lookupTable, protocolTable);

        assertEquals("Untagged", result.getKey());
        assertEquals("49153,unknown", result.getValue());
    }

}