package uk.gov.justice.laa.crime.applications.adaptor.controller;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
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
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.WireMockStubs;

import java.io.IOException;

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
                .setDispatcher(WireMockStubs.forCrimeApplyDatastoreAPI());
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
    void givenValidParams_whenCrimeApplyDatastoreServiceIsInvoked_thenReturnApplicationData() throws Exception {
        String maatApplicationJson = FileUtils.readFileToString("data/application.json");
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "1001")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(maatApplicationJson)).andReturn();

        String actualJsonString = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(maatApplicationJson, actualJsonString, true);

    }

    @Test
    void givenInvalidParams_whenCrimeApplyDatastoreServiceIsInvoked_then4xxClientExceptionIsThrown() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "404")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").value("404 Not Found from GET http://localhost:9999/404"));
    }

    @Test
    void whenCrimeApplyDatastoreServiceIsUnavailable_then5xxServerExceptionIsThrown() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "503")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value("503"))
                .andExpect(jsonPath("$.detail").value("503 Service Unavailable from GET http://localhost:9999/503"));

    }
}
