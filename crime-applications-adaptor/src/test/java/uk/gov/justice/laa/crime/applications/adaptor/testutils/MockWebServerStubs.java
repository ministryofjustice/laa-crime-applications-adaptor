package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

public class MockWebServerStubs {

    private static final String REQUEST_PATH_EFORM_STAGING_WITH_NO_MAAT_REF = "/initialise/6000308";
    private static final String REQUEST_PATH_EFORM_STAGING_WITH_MAAT_REF = "/initialise/6000309";
    private static final String REQUEST_PATH_EFORM_STAGING_HUB_USER = "/initialise/6000310";
    private static final String REQUEST_PATH_EFORM_STAGING_4XX = "/initialise/403";
    private static final String REQUEST_PATH_CRIME_APPLY_DATASTORE_1 = "/6000308";
    private static final String REQUEST_PATH_CRIME_APPLY_DATASTORE_2 = "/6000309";
    private static final String REQUEST_PATH_CRIME_APPLY_4XX = "/403";

    public static Dispatcher forDownstreamApiCalls() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) {
                return switch (recordedRequest.getPath()) {
                    case REQUEST_PATH_CRIME_APPLY_DATASTORE_1 -> getMockResponse("data/crimeapply/application_1.json");
                    case REQUEST_PATH_CRIME_APPLY_DATASTORE_2 -> getMockResponse("data/crimeapply/application_2.json");
                    case REQUEST_PATH_CRIME_APPLY_4XX, REQUEST_PATH_EFORM_STAGING_4XX -> getMockResponseFor403();
                    case REQUEST_PATH_EFORM_STAGING_WITH_NO_MAAT_REF -> getMockResponse("data/eformstaging/record_with_no_maatref.json");
                    case REQUEST_PATH_EFORM_STAGING_WITH_MAAT_REF -> getMockResponse("data/eformstaging/record_with_maatref.json");
                    case REQUEST_PATH_EFORM_STAGING_HUB_USER -> getMockResponse("data/eformstaging/record_created_by_hub.json");
                    default -> new MockResponse().setResponseCode(503);
                };
            }
        };
    }

    private static MockResponse getMockResponseFor403() {
        return new MockResponse().setResponseCode(403);
    }

    private static MockResponse getMockResponse(String filePath) {
        try {
            return new MockResponse().addHeader("Content-Type",
                            MediaType.APPLICATION_JSON)
                    .setResponseCode(200)
                    .setBody(FileUtils.readFileToString(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
