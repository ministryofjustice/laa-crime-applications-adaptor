package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

public class WireMockStubs {

    public static Dispatcher forDownstreamApiCalls() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) {
                switch (recordedRequest.getPath()) {
                    case "/1001":
                        return stubForCrimeApplyDatastore();
                    case "/initialise/1001":
                        return stubForEformStagingWithNoMaatRef();
                    case "/initialise/1002":
                        return stubForEformStagingWithMaatRef();
                    case "/403":
                        return getMockResponseFor403();
                    case "/initialise/403":
                        return getMockResponseFor403();
                }
                return new MockResponse().setResponseCode(503);
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
