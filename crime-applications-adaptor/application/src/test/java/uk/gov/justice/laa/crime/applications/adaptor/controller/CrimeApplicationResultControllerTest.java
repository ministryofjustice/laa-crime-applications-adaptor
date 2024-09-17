package uk.gov.justice.laa.crime.applications.adaptor.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationResultService;
import uk.gov.justice.laa.crime.model.common.atis.CrimeApplicationResult;

@WebMvcTest(
    controllers = CrimeApplicationResultController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class CrimeApplicationResultControllerTest {

  private static final String BASE_ENDPOINT_FORMAT = "/api/external/v1/crime-application/result";

  @Autowired private MockMvc mockMvc;

  @MockBean private CrimeApplicationResultService crimeApplicationResultService;

  @Test
  void shouldReturnCrimeApplicationResultsForGivenUSN() throws Exception {
    CrimeApplicationResult crimeApplicationResult = new CrimeApplicationResult();
    crimeApplicationResult.setUsn(9000308);
    crimeApplicationResult.setMeansResult("PASS");
    when(crimeApplicationResultService.getCrimeApplicationResult(9000308))
        .thenReturn(crimeApplicationResult);

    RequestBuilder request =
        MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT + "/usn/{usn}", "9000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.usn", is(9000308)))
        .andExpect(jsonPath("$.means_result", is("PASS")));
    verify(crimeApplicationResultService, times(1)).getCrimeApplicationResult(9000308);
  }

  @Test
  void shouldReturn_4XX_NotFoundException() throws Exception {
    RequestBuilder request =
        MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT + "/usn/{usn}", "9000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    when(crimeApplicationResultService.getCrimeApplicationResult(9000308))
        .thenThrow(new WebClientResponseException(404, "NOT FOUND", null, null, null));

    mockMvc
        .perform(request)
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
        .andExpect(jsonPath("$.status").value("404"))
        .andExpect(jsonPath("$.detail").value("404 NOT FOUND"));
  }

  @Test
  void shouldReturn_5XX_ServerException_whenServiceIsUnavailable() throws Exception {
    RequestBuilder request =
        MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT + "/usn/{usn}", "9000308")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    when(crimeApplicationResultService.getCrimeApplicationResult(9000308))
        .thenThrow(new WebClientResponseException(503, "SERVICE_UNAVAILABLE", null, null, null));

    mockMvc
        .perform(request)
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value("503"))
        .andExpect(jsonPath("$.detail").value("503 SERVICE_UNAVAILABLE"));
  }

  @Test
  void shouldReturnCrimeApplicationResultsForGivenRepID() throws Exception {
    CrimeApplicationResult crimeApplicationResult = new CrimeApplicationResult();
    crimeApplicationResult.setMaatRef(9775838);
    crimeApplicationResult.setMeansResult("PASS");
    when(crimeApplicationResultService.getCrimeApplicationResultByRepId(9775838))
        .thenReturn(crimeApplicationResult);

    RequestBuilder request =
        MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT + "/rep-order-id/{repId}", "9775838")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(request)
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.maat_ref", is(9775838)))
        .andExpect(jsonPath("$.means_result", is("PASS")));
    verify(crimeApplicationResultService, times(1)).getCrimeApplicationResultByRepId(9775838);
  }
}
