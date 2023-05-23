package uk.gov.justice.laa.crime.applications.adaptor.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uk.gov.justice.laa.crime.applications.adaptor.exception.CrimeApplicationException;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.applications.adaptor.service.EformStagingService;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;

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
    void givenValidParams_whenMaatRefernceNotExistForUsnInEFormStaging_thenCrimeApplyDatastoreServiceIsInvokedAndApplicationDataIsReturned() throws Exception {
        String maatApplicationJson = FileUtils.readFileToString("data/crimeapplicationsadaptor/MaatApplication_default.json");
        MaatApplication maatApplication = JsonUtils.jsonToObject(maatApplicationJson, MaatApplication.class);

        when(crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(anyLong())).thenReturn(maatApplication);

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(maatApplicationJson)).andReturn();

        String actualJsonString = result.getResponse().getContentAsString();
        String expectedMaatApplicationJson = FileUtils.readFileToString("data/expected/maatapplication/MaatApplication_default.json");

        JSONAssert.assertEquals(expectedMaatApplicationJson, actualJsonString, JSONCompareMode.STRICT);
        verify(crimeApplicationService, times(1)).retrieveApplicationDetailsFromCrimeApplyDatastore(6000308);
    }

    @Test
    void givenValidParams_whenMaatRefernceExistForUsnInEFormStaging_thenCrimeApplyDatastoreServiceIsNotInvokedAndRecordExistsExceptionIsThrownWithAppropriateMessage() throws Exception {
        doThrow(new CrimeApplicationException("MAAT Reference for USN already exist")).when(eformStagingService).retrieveOrInsertDummyUsnRecord(any());

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "6000308")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.detail").value("MAAT Reference for USN already exist"));

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
