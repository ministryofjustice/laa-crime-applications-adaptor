package uk.gov.justice.laa.crime.applications.adaptor.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.applications.adaptor.utils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.utils.JsonUtils;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrimeApplicationController.class)
public class CrimeApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrimeApplicationService crimeApplicationService;

    @Test
    void givenValidUsn_whenCrimeApplyDatastoreServiceIsInvoked_thenApplicationDataIsReturned() throws Exception {

        String fileContents = FileUtils.readFileToString("data/application.json");
        MaatApplication application = JsonUtils.jsonToObject(fileContents, MaatApplication.class);

        //Given
        when(crimeApplicationService.callCrimeApplyDatastore(ArgumentMatchers.<Long>any())).thenReturn(application);

        //When
        RequestBuilder request = MockMvcRequestBuilders.get("/api/internal/v1/crimeapply/{usn}", "1001")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        //Then
        String expectedJsonString = JsonUtils.objectToJson(application);
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().json(expectedJsonString)).andReturn();

        String actualJsonString = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJsonString, actualJsonString, true);

    }
}
