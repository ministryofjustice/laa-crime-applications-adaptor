package uk.gov.justice.laa.crime.applications.adaptor.controller;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrimeApplicationsAdaptorApplication.class, webEnvironment = DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
class CrimeApplicationIntegrationTest {

    private MockMvc mvc;

    private static MockWebServer mockCrimeApplyDatastoreApi;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void initialiseMockWebServer() throws IOException {
        mockCrimeApplyDatastoreApi = new MockWebServer();
        mockCrimeApplyDatastoreApi
                .setDispatcher(WireMockStubs.forDownstreamApiCalls());
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
    void givenValidParams_whenMaatRefernceNotExistForUsnInEFormStaging_thencrimeApplyDatastoreServiceIsInvokedAndApplicationDataIsReturned() throws Exception {
        String maatApplicationJson = FileUtils.readFileToString("data/application.json");
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "1001")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(maatApplicationJson)).andReturn();

        String actualJsonString = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(maatApplicationJson, actualJsonString, true);

    }

    @Test
    void givenValidParams_whenMaatRefernceExistForUsnInEFormStaging_thenCrimeApplyDatastoreServiceIsNotInvokedAndExceptionIsThrownWithAppropriateMessage() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "1002")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.detail").value("MAAT Reference [1000001] for USN [1002] already exist"));

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
