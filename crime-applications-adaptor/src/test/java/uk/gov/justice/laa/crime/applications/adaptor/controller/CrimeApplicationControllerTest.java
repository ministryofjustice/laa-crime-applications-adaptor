package uk.gov.justice.laa.crime.applications.adaptor.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CrimeApplicationController.class)
class CrimeApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrimeApplicationService crimeApplicationService;

    @SpyBean
    private CrimeApplicationService spyedCrimeApplicationService;

    @Test
    void givenValidParams_whenCrimeApplyDatastoreServiceIsInvoked_thenReturnApplicationData() throws Exception {
        String maatApplicationJson = FileUtils.readFileToString("data/application.json");
        MaatApplication application = JsonUtils.jsonToObject(maatApplicationJson, MaatApplication.class);

        when(crimeApplicationService.callCrimeApplyDatastore(ArgumentMatchers.<Long>any())).thenReturn(application);

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "1001")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        String expectedJsonString = JsonUtils.objectToJson(application);
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(expectedJsonString)).andReturn();

        String actualJsonString = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJsonString, actualJsonString, true);
    }

    @Test
    void givenInvalidParams_whenCrimeApplyDatastoreServiceIsInvoked_then4xxClientExceptionIsThrown() throws Exception {
        when(crimeApplicationService.callCrimeApplyDatastore(ArgumentMatchers.<Long>any()))
                .thenThrow(new WebClientResponseException(404, "NOT_FOUND", null, null, null));

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "1001")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").value("404 NOT_FOUND"));
    }

    @Test
    void whenCrimeApplyDatastoreServiceIsUnavailable_then5xxServerExceptionIsThrown() throws Exception {
        when(crimeApplicationService.callCrimeApplyDatastore(ArgumentMatchers.<Long>any()))
                .thenThrow(new WebClientResponseException(503, "SERVICE_UNAVAILABLE", null, null, null));

        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "1001")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value("503"))
                .andExpect(jsonPath("$.detail").value("503 SERVICE_UNAVAILABLE"));
    }

}
