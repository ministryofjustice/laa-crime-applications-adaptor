package uk.gov.justice.laa.crime.applications.adaptor.controller;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.justice.laa.crime.applications.adaptor.CrimeApplicationsAdaptorApplication;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.MockWebServerStubs;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrimeApplicationsAdaptorApplication.class, webEnvironment = DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CrimeApplicationIntegrationTest {

    private MockMvc mvc;

    private static MockWebServer mockCrimeApplyDatastoreApi;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void initialiseMockWebServer() throws IOException {
        mockCrimeApplyDatastoreApi = new MockWebServer();
        mockCrimeApplyDatastoreApi
                .setDispatcher(MockWebServerStubs.forDownstreamApiCalls());
        mockCrimeApplyDatastoreApi.start(9999);
    }

    @AfterAll
    protected void shutdownMockWebServer() throws IOException {
        mockCrimeApplyDatastoreApi.shutdown();
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @Test
    void givenValidParams_whenMaatRefernceNotExistForUsnInEFormStaging_thenCrimeApplyDatastoreServiceIsInvokedAndApplicationDataIsReturned() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andExpect(status().isOk()).andReturn();

        String actualJsonString = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{}", actualJsonString, JSONCompareMode.STRICT);
    }

    @Test
    void givenValidParams_whenMaatRefernceExistForUsnInEFormStaging_thenCrimeApplyDatastoreServiceIsNotInvokedAndRecordExistsExceptionIsThrownWithAppropriateMessage() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000309")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.detail").value("USN: 1000001 already used on MAAT Id: 6000309"));

    }

    @Test
    void givenInvalidParams_whenDownstreamServiceIsCalled_then4xxClientExceptionIsThrown() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "403")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value("403"))
                .andExpect(jsonPath("$.detail", containsString("403 Forbidden")));
    }

    @Test
    void whenDownstreamServiceIsUnavailable_then5xxServerExceptionIsThrown() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "503")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value("503"))
                .andExpect(jsonPath("$.detail", containsString(("503 Service Unavailable"))));
    }

}
