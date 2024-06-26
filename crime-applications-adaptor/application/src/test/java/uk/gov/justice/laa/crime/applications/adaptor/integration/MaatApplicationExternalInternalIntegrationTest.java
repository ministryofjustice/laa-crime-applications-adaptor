package uk.gov.justice.laa.crime.applications.adaptor.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
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
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.MockWebServerStubs;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrimeApplicationsAdaptorApplication.class, webEnvironment = DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MaatApplicationExternalInternalIntegrationTest {

  private MockMvc mvc;

  private static MockWebServer mockWebServer;

  @Autowired private WebApplicationContext webApplicationContext;

  @BeforeAll
  public void initialiseMockWebServer() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(MockWebServerStubs.forDownstreamApiCalls());
    mockWebServer.start(9999);
  }

  @AfterAll
  protected void shutdownMockWebServer() throws IOException {
    mockWebServer.shutdown();
  }

  @BeforeEach
  public void setup() {
    this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  void
      givenValidParams_whenMaatReferenceNotExistForUsnInEFormStaging_thenCallCrimeApplyAndReturnApplicationData()
          throws Exception {
    RequestBuilder request =
        MockMvcRequestBuilders.get(
                "/api/internal/v1/crimeapply/{usn}/userCreated/causer", "6000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result =
        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

    String actualJsonString = result.getResponse().getContentAsString();
    String expectedCrimeApplicationJson =
        FileUtils.readFileToString("data/crimeapplicationsadaptor/CrimeApplication_6000308.json");
    JSONAssert.assertEquals(expectedCrimeApplicationJson, actualJsonString, JSONCompareMode.STRICT);
  }

  @Test
  void
      givenValidParams_whenMaatReferenceExistForUsnInEFormStaging_thenCallCrimeApplyAndReturnApplicationDataWithMaatRef()
          throws Exception {
    RequestBuilder request =
        MockMvcRequestBuilders.get(
                "/api/internal/v1/crimeapply/{usn}/userCreated/causer", "6000288")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.usn", is(6000288)))
        .andExpect(jsonPath("$.maatRef", is(5676400)));
  }

  @Test
  void givenInvalidParams_whenDownstreamServiceIsCalled_then4xxClientExceptionIsThrown()
      throws Exception {
    RequestBuilder request =
        MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}/userCreated/causer", "403")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value("403"))
        .andExpect(jsonPath("$.detail", containsString("403 Forbidden")));
  }

  @Test
  void whenDownstreamServiceIsUnavailable_then5xxServerExceptionIsThrown() throws Exception {
    RequestBuilder request =
        MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}/userCreated/causer", "503")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value("503"))
        .andExpect(jsonPath("$.detail", containsString(("503 Service Unavailable"))));
  }
}
