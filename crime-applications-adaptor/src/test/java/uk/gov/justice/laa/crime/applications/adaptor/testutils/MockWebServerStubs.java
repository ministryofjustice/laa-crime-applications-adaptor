package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MockWebServerStubs {

    private static final Map<String, RequestPathResponseMapping> PATH_TO_RESPONSE_MAPPING_MAP =
            Arrays.stream(RequestPathResponseMapping.values())
                    .collect(Collectors.toMap(RequestPathResponseMapping::getRequestPath,
                            requestPathResponseMapping -> requestPathResponseMapping));

    public static Dispatcher forDownstreamApiCalls() {
        return new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                String requestPath = recordedRequest.getPath();
                RequestPathResponseMapping responseMapping = PATH_TO_RESPONSE_MAPPING_MAP.get(requestPath);
                return responseMapping.getMockResponse();
            }
        };
    }

    enum RequestPathResponseMapping {
        EFORM_STAGING_WITH_NO_MAAT_REF("/initialise/6000308", "data/eformstaging/EformStagingResponse_WithNoMaatRef_6000308.json", HttpStatus.OK),
        EFORM_STAGING_WITH_MAAT_REF("/initialise/6000288", "data/eformstaging/EformStagingResponse_WithMaatRef_6000288.json", HttpStatus.OK),
        EFORM_STAGING_HUB_USER("/initialise/6000310", "data/eformstaging/EformStagingResponse_CreatedByHub_6000310.json", HttpStatus.OK),
        EFORM_STAGING_403("/initialise/403", null, HttpStatus.FORBIDDEN),
        EFORM_STAGING_503("/initialise/503", null, HttpStatus.SERVICE_UNAVAILABLE),
        REQUEST_PATH_CRIME_APPLY_DATASTORE_200OK_6000288("/6000288", "data/criminalapplicationsdatastore/MaatApplication_6000288.json", HttpStatus.OK),
        REQUEST_PATH_CRIME_APPLY_DATASTORE_200OK_6000308("/6000308", "data/criminalapplicationsdatastore/MaatApplication_6000308.json", HttpStatus.OK),
        REQUEST_PATH_CRIME_APPLY_403("/403", null, HttpStatus.FORBIDDEN),
        REQUEST_PATH_CRIME_APPLY_503("/503", null, HttpStatus.SERVICE_UNAVAILABLE),
        DEFAULT_FALLBACK_FOR_NULL_PATH(null, null, HttpStatus.SERVICE_UNAVAILABLE);

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

        String getRequestPath() {
            return requestPath;
        }

        MockResponse getMockResponse() {
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
