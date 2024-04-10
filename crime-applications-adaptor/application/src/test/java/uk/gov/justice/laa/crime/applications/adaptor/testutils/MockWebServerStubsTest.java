package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

class MockWebServerStubsTest {

    @Test
    void verifyThatThereAreNoDuplicatePathMappings() {
        MockWebServerStubs.RequestPathResponseMapping[] requestPathResponseMappings = MockWebServerStubs.RequestPathResponseMapping.values();
        Map<String, Integer> requestPathOccurrences = new HashMap<>();

        for (MockWebServerStubs.RequestPathResponseMapping requestPathResponseMapping : requestPathResponseMappings) {
            String requestPath = requestPathResponseMapping.getRequestPath();
            requestPathOccurrences.putIfAbsent(requestPath, 0);

            int updatedPathOccurrenceCount = requestPathOccurrences.get(requestPath) + 1;
            requestPathOccurrences.put(requestPath, updatedPathOccurrenceCount);
        }

        List<Map.Entry<String, Integer>> nonUniqueRequestPaths = requestPathOccurrences.entrySet().stream()
                .filter(stringIntegerEntry -> stringIntegerEntry.getValue() > 1)
                .toList();

        if (!nonUniqueRequestPaths.isEmpty()) {
            fail("Expected all requestPaths to be unique but found the following duplicates: " + nonUniqueRequestPaths);
        }
    }
}