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
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        String maatApplicationJson = FileUtils.readFileToString("data/criminalapplicationsdatastore/MaatApplication_6000308.json");
        MaatApplication expected = JsonUtils.jsonToObject(maatApplicationJson, MaatApplication.class);

        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenReturn(TestData.getMaatApplication());
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());

        MaatCaaContract response = crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308L);

        JSONAssert.assertEquals(JsonUtils.objectToJson(expected), JsonUtils.objectToJson(response), JSONCompareMode.STRICT);
    }

    @Test
    void given4xxOr5xxHttpErrorsOnInvokingCrimeApplyDatastoreService_thenWebClientResponseExceptionIsThrown() {
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(Mockito.mock(WebClientResponseException.class));
        ServicesConfiguration.CrimeApplyApi crimeApplyApi = MockServicesConfiguration.getConfiguration().getCrimeApplyApi();
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(crimeApplyApi);

        WebClientResponseException responseException = assertThrows(WebClientResponseException.class, () ->
                crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308L)
        );

        assertNull(responseException.getMessage());
    }

    @Test
    void givenNetworkUnreachableOnInvokingCrimeApplyDatastoreService_thenWebClientRequestExceptionIsThrown() {
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(Mockito.mock(WebClientRequestException.class));
        ServicesConfiguration.CrimeApplyApi crimeApplyApi = MockServicesConfiguration.getConfiguration().getCrimeApplyApi();
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(crimeApplyApi);

        WebClientRequestException requestException = assertThrows(WebClientRequestException.class, () ->
                crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308L)
        );

        assertNull(requestException.getMessage());
    }
}