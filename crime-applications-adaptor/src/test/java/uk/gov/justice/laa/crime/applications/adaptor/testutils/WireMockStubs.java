package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

public class WireMockStubs {

    public static Dispatcher forCrimeApplyDatastoreAPI() {
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) {
                switch (recordedRequest.getPath()) {
                    case "/1001":
                        return stubForCrimeApplyDatastore();
                    case "/404":
                        return new MockResponse().setResponseCode(404);
                }
                return new MockResponse().setResponseCode(503);
            }
        };
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
}
