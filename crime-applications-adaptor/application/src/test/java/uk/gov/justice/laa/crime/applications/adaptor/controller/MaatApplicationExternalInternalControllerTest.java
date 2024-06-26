package uk.gov.justice.laa.crime.applications.adaptor.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.applications.adaptor.service.EformStagingService;
import uk.gov.justice.laa.crime.applications.adaptor.service.EformsHistoryService;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;
import uk.gov.justice.laa.crime.model.common.crimeapplication.MaatApplicationInternal;

@WebMvcTest(
    controllers = CrimeApplicationController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class MaatApplicationExternalInternalControllerTest {

  private static final String DEFAULT_USER = "causer";

  @Autowired private MockMvc mockMvc;

  @MockBean private CrimeApplicationService crimeApplicationService;

  @MockBean private EformStagingService eformStagingService;

  @MockBean private EformsHistoryService eformsHistoryService;

  @Test
  void
      givenValidParams_whenMaatReferenceNotExistForUsnInEFormStaging_thenCallCrimeApplyAndReturnApplicationData()
          throws Exception {
    MaatApplicationInternal maatApplicationInternal =
        TestData.getCrimeApplication("CrimeApplication_6000308.json");
    EformStagingResponse eformStagingResponse =
        TestData.getEformStagingResponse("EformStagingResponse_WithNoMaatRef_6000308.json");

    when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308))
        .thenReturn(maatApplicationInternal);
    when(eformStagingService.retrieveOrInsertDummyUsnRecord(6000308, DEFAULT_USER))
        .thenReturn(eformStagingResponse);
    doNothing().when(eformsHistoryService).createEformsHistoryRecord(6000308, DEFAULT_USER);

    RequestBuilder request =
        MockMvcRequestBuilders.get(
                "/api/internal/v1/crimeapply/{usn}/userCreated/causer", "6000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.usn", is(6000308)))
        .andExpect(jsonPath("$.maatRef").doesNotExist());
    verify(crimeApplicationService, times(1))
        .retrieveApplicationDetailsFromCrimeApplyDatastore(6000308);
    verify(eformStagingService, times(1)).retrieveOrInsertDummyUsnRecord(6000308, DEFAULT_USER);
    verify(eformsHistoryService, times(1)).createEformsHistoryRecord(6000308, DEFAULT_USER);
  }

  @Test
  void
      givenValidParams_whenMaatReferenceExistForUsnInEFormStaging_thenCallCrimeApplyAndReturnApplicationDataWithMaatRef()
          throws Exception {
    MaatApplicationInternal maatApplicationInternal =
        TestData.getCrimeApplication("CrimeApplication_6000308.json");
    EformStagingResponse eformStagingResponse =
        TestData.getEformStagingResponse("EformStagingResponse_WithMaatRef_6000308.json");

    when(eformStagingService.retrieveOrInsertDummyUsnRecord(6000308, DEFAULT_USER))
        .thenReturn(eformStagingResponse);
    when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308))
        .thenReturn(maatApplicationInternal);
    doNothing().when(eformsHistoryService).createEformsHistoryRecord(6000308, DEFAULT_USER);

    RequestBuilder request =
        MockMvcRequestBuilders.get(
                "/api/internal/v1/crimeapply/{usn}/userCreated/causer", "6000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.usn", is(6000308)))
        .andExpect(jsonPath("$.maatRef", is(5676399)));
    verify(crimeApplicationService, times(1))
        .retrieveApplicationDetailsFromCrimeApplyDatastore(6000308);
    verify(eformStagingService, times(1)).retrieveOrInsertDummyUsnRecord(6000308, DEFAULT_USER);
    verify(eformsHistoryService, times(1)).createEformsHistoryRecord(6000308, DEFAULT_USER);
  }

  @Test
  void givenInvalidParams_whenDownstreamServiceIsInvoked_then4xxClientExceptionIsThrown()
      throws Exception {
    when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(anyLong()))
        .thenThrow(new WebClientResponseException(403, "Forbidden", null, null, null));

    RequestBuilder request =
        MockMvcRequestBuilders.get(
                "/api/internal/v1/crimeapply/{usn}/userCreated/causer", "6000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(request)
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
        .andExpect(jsonPath("$.status").value("403"))
        .andExpect(jsonPath("$.detail").value("403 Forbidden"));
  }

  @Test
  void whenDownstreamServiceIsUnavailable_then5xxServerExceptionIsThrown() throws Exception {
    when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(anyLong()))
        .thenThrow(new WebClientResponseException(503, "SERVICE_UNAVAILABLE", null, null, null));

    RequestBuilder request =
        MockMvcRequestBuilders.get(
                "/api/internal/v1/crimeapply/{usn}/userCreated/causer", "6000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(request)
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value("503"))
        .andExpect(jsonPath("$.detail").value("503 SERVICE_UNAVAILABLE"));
  }

  @Test
  void whenCrimeApplyDatastoreServiceIsNotReachable_then_500ServerExceptionIsThrown()
      throws Exception {
    when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(anyLong()))
        .thenThrow(Mockito.mock(WebClientRequestException.class));

    RequestBuilder request =
        MockMvcRequestBuilders.get(
                "/api/internal/v1/crimeapply/{usn}/userCreated/causer", "6000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(request)
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value("500"));
  }
}
