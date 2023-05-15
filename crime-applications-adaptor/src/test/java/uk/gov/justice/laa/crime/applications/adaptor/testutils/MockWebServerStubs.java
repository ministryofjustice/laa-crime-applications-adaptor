package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

public class MockWebServerStubs {

    public static final String REQUEST_PATH_EFORM_STAGING_WITH_NO_MAAT_REF = "/initialise/6000308";
    public static final String REQUEST_PATH_EFORM_STAGING_WITH_MAAT_REF = "/initialise/6000309";
    public static final String REQUEST_PATH_CRIME_APPLY_DATASTORE = "/6000308";
    public static final String REQUEST_PATH_CRIME_APPLY_4XX = "/403";
    public static final String REQUEST_PATH_EFORM_STAGING_4XX = "/initialise/403";

    public static Dispatcher forDownstreamApiCalls() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) {
                return switch (recordedRequest.getPath()) {
                    case REQUEST_PATH_CRIME_APPLY_DATASTORE -> stubForCrimeApplyDatastore();
                    case REQUEST_PATH_CRIME_APPLY_4XX, REQUEST_PATH_EFORM_STAGING_4XX -> getMockResponseFor403();
                    case REQUEST_PATH_EFORM_STAGING_WITH_NO_MAAT_REF -> stubForEformStagingWithNoMaatRef();
                    case REQUEST_PATH_EFORM_STAGING_WITH_MAAT_REF -> stubForEformStagingWithMaatRef();
                    default -> new MockResponse().setResponseCode(503);
                };
            }
        };
    }

    private static MockResponse getMockResponseFor403() {
        return new MockResponse().setResponseCode(403);
    }

    private static MockResponse stubForCrimeApplyDatastore() {
        try {
            return new MockResponse().addHeader("Content-Type",
                            MediaType.APPLICATION_JSON)
                    .setResponseCode(200)
                    .setBody(FileUtils.readFileToString("data/application.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static MockResponse stubForEformStagingWithMaatRef() {
        try {
            return new MockResponse().addHeader("Content-Type",
                            MediaType.APPLICATION_JSON)
                    .setResponseCode(200)
                    .setBody(FileUtils.readFileToString("data/eform_staging_with_maatref.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static MockResponse stubForEformStagingWithNoMaatRef() {
        try {
            return new MockResponse().addHeader("Content-Type",
                            MediaType.APPLICATION_JSON)
                    .setResponseCode(200)
                    .setBody(FileUtils.readFileToString("data/eform_staging_with_no_maatref.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
