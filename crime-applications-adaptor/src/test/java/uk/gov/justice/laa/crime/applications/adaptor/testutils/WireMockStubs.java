package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

public class WireMockStubs {

    public static Dispatcher forCrimeApplyDatastoreAPI() throws IOException {
        String maatApplicationJson = FileUtils.readFileToString("data/application.json");
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
                switch (recordedRequest.getPath()) {
                    case "/1001":
                        return new MockResponse().addHeader("Content-Type",
                                        MediaType.APPLICATION_JSON)
                                .setResponseCode(200)
                                .setBody(maatApplicationJson);
                    case "/404":
                        return new MockResponse().setResponseCode(404);
                }
                return new MockResponse().setResponseCode(503);
            }
        };
    }
}
