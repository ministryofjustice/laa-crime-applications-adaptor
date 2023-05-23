package uk.gov.justice.laa.crime.applications.adaptor.service;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uk.gov.justice.laa.crime.applications.adaptor.client.CrimeApplyDatastoreClient;
import uk.gov.justice.laa.crime.applications.adaptor.config.MockServicesConfiguration;
import uk.gov.justice.laa.crime.applications.adaptor.config.ServicesConfiguration;
import uk.gov.justice.laa.crime.applications.adaptor.mapper.CrimeApplyMapper;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CadApplicationResponse;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrimeApplicationServiceTest {

    @Mock
    private CrimeApplyDatastoreClient crimeApplyDatastoreClient;

    @Mock
    private ServicesConfiguration servicesConfiguration;

    @Mock
    private CrimeApplyMapper crimeApplyMapper;

    @InjectMocks
    private CrimeApplicationService crimeApplicationService;

    @Test
    void givenValidParams_whenCrimeApplyDatastoreServiceIsInvoked_thenReturnApplicationData() throws JSONException {
        String cadApplicationResponseJson = FileUtils.readFileToString("data/criminalapplicationsdatastore/CADApplicationResponse_default.json");
        CadApplicationResponse cadApplicationResponse = JsonUtils.jsonToObject(cadApplicationResponseJson, CadApplicationResponse.class);

        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenReturn(cadApplicationResponse);
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());

        MaatApplication response = crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308);
        String responseJson = JsonUtils.objectToJson(response);
        String expectedMaatApplicationJson = FileUtils.readFileToString("data/expected/maatapplication/MaatApplication_default.json");

        JSONAssert.assertEquals(expectedMaatApplicationJson, responseJson, JSONCompareMode.STRICT);
    }

    @Test
    void given4xxOr5xxHttpErrorsOnInvokingCrimeApplyDatastoreService_thenWebClientResponseExceptionIsThrown() {
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(Mockito.mock(WebClientResponseException.class));
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());
        assertThrows(WebClientResponseException.class, () ->
                crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308)
        );
    }
    @Test
    void givenNetworkUnrechableOnInvokingCrimeApplyDatastoreService_thenWebClientRequestExceptionIsThrown() {
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(Mockito.mock(WebClientRequestException.class));
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());
        assertThrows(WebClientRequestException.class, () ->
            crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308L)
        );
    }
}

