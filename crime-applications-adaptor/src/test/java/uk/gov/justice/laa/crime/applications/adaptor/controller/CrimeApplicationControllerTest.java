package uk.gov.justice.laa.crime.applications.adaptor.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uk.gov.justice.laa.crime.applications.adaptor.exception.CrimeApplicationException;
import uk.gov.justice.laa.crime.applications.adaptor.model.EformStagingResponse;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.applications.adaptor.service.EformStagingService;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers =CrimeApplicationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class CrimeApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrimeApplicationService crimeApplicationService;

    @MockBean
    private EformStagingService eformStagingService;

    @Test
    void givenValidParams_whenMaatReferenceNotExistForUsnInEFormStagingAndUsnNotCreatedByHub_thenCallCrimeApplyAndReturnApplicationData() throws Exception {
        MaatCaaContract maatCaaContract = TestData.getMaatCaaContract("MaatCaaContract_6000308.json");
        EformStagingResponse eformStagingResponse = TestData.getEformStagingResponse("record_with_no_maatref.json");

        when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308))
                .thenReturn(maatCaaContract);
        when(eformStagingService.retrieveOrInsertDummyUsnRecord(6000308))
                .thenReturn(eformStagingResponse);

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.usn", is(6000308)))
                .andExpect(jsonPath("$.maatRef").doesNotExist());
        verify(crimeApplicationService, times(1)).retrieveApplicationDetailsFromCrimeApplyDatastore(6000308);
    }

    @Test
    void givenValidParams_whenMaatReferenceExistForUsnInEFormStagingAndUsnNotCreatedByHub_thenCallCrimeApplyAndReturnApplicationDataWithMaatRef() throws Exception {
        MaatCaaContract maatCaaContract = TestData.getMaatCaaContract("MaatCaaContract_6000308.json");
        EformStagingResponse eformStagingResponse = TestData.getEformStagingResponse("record_with_maatref.json");

        when(eformStagingService.retrieveOrInsertDummyUsnRecord(6000308)).thenReturn(eformStagingResponse);
        when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308))
                .thenReturn(maatCaaContract);

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.usn", is(6000308)))
                .andExpect(jsonPath("$.maatRef", is(5676399)));
        verify(crimeApplicationService, times(1)).retrieveApplicationDetailsFromCrimeApplyDatastore(6000308);
    }

    @Test
    void givenValidParams_whenUsnInEFormStagingCreatedByHubUser_thenCrimeApplyDatastoreServiceIsNotInvokedAndCrimeApplicationExceptionIsThrownWithAppropriateMessage() throws Exception {
        when(eformStagingService.retrieveOrInsertDummyUsnRecord(6000308))
                .thenThrow(new CrimeApplicationException(HttpStatus.NOT_FOUND, "USN created by Hub user"));

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").value("USN created by Hub user"));
        verify(crimeApplicationService, times(0)).retrieveApplicationDetailsFromCrimeApplyDatastore(6000308);
    }

    @Test
    void givenInvalidParams_whenDownstreamServiceIsInvoked_then4xxClientExceptionIsThrown() throws Exception {
        when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(anyLong()))
                .thenThrow(new WebClientResponseException(403, "Forbidden", null, null, null));

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("403"))
                .andExpect(jsonPath("$.detail").value("403 Forbidden"));
    }

    @Test
    void whenDownstreamServiceIsUnavailable_then5xxServerExceptionIsThrown() throws Exception {
        when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(anyLong()))
                .thenThrow(new WebClientResponseException(503, "SERVICE_UNAVAILABLE", null, null, null));

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value("503"))
                .andExpect(jsonPath("$.detail").value("503 SERVICE_UNAVAILABLE"));
    }

    @Test
    void whenCrimeApplyDatastoreServiceIsNotReachable_then_500ServerExceptionIsThrown() throws Exception {
        when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(anyLong()))
                .thenThrow(Mockito.mock(WebClientRequestException.class));

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value("500"));
    }
}