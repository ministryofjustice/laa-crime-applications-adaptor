package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

public class MockWebServerStubs {

    public static Dispatcher forDownstreamApiCalls() {
        return new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                String requestPath = recordedRequest.getPath();
                List<RequestPathResponseMapping> matchingMappings = Arrays.stream(RequestPathResponseMapping.values())
                        .filter(requestPathResponseMapping -> requestPathResponseMapping.matchesRequestPath(requestPath))
                        .toList();

                if (matchingMappings.isEmpty()) {
                    return new MockResponse().setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value());
                }

                if (matchingMappings.size() > 1) {
                    String message = "Multiple matching request path response mappings found for [%s] found %s".formatted(requestPath, matchingMappings);
                    throw new IllegalStateException(message);
                }

                return matchingMappings.get(0).getMockResponse();

            }
        };
    }

    enum RequestPathResponseMapping {
        EFORM_STAGING_WITH_NO_MAAT_REF("/initialise/6000308", "data/eformstaging/record_with_no_maatref.json", HttpStatus.OK),
        EFORM_STAGING_WITH_MAAT_REF("/initialise/6000309", "data/eformstaging/record_with_maatref.json", HttpStatus.OK),
        EFORM_STAGING_HUB_USER("/initialise/6000310", "data/eformstaging/record_created_by_hub.json", HttpStatus.OK),
        EFORM_STAGING_4XX("/initialise/403", null, HttpStatus.FORBIDDEN),
        REQUEST_PATH_CRIME_APPLY_DATASTORE_200OK_6000308("/6000308", "data/criminalapplicationsdatastore/MaatApplication_6000308.json", HttpStatus.OK),
        REQUEST_PATH_CRIME_APPLY_DATASTORE_200OK_6000309("/6000309", "data/criminalapplicationsdatastore/MaatApplication_6000295.json", HttpStatus.OK),
        REQUEST_PATH_CRIME_APPLY_4XX("/403", null, HttpStatus.FORBIDDEN);

        private final String requestPath;
        private final String responseBodyFilePath;
        private final HttpStatus httpResponseStatus;

        RequestPathResponseMapping(String requestPath,
                                   String responseBodyFilePath,
                                   HttpStatus httpResponseStatus) {
            this.requestPath = requestPath;
            this.responseBodyFilePath = responseBodyFilePath;
            this.httpResponseStatus = httpResponseStatus;
        }

        public boolean matchesRequestPath(String requestPathParameter) {
            return requestPath.equals(requestPathParameter);
        }

        public MockResponse getMockResponse() {
            MockResponse mockResponse = new MockResponse()
                    .addHeader("Content-Type", MediaType.APPLICATION_JSON)
                    .setResponseCode(httpResponseStatus.value());

            if (StringUtils.isNotBlank(responseBodyFilePath)) {
                mockResponse.setBody(FileUtils.readFileToString(responseBodyFilePath));
            }
            return mockResponse;
        }
    }
}
