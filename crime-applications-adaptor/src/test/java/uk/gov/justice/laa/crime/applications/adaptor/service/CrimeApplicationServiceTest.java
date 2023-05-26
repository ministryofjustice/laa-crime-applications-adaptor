package uk.gov.justice.laa.crime.applications.adaptor.service;

import org.fest.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
    void givenValidParams_whenCrimeApplyDatastoreServiceIsInvoked_thenReturnApplicationData() {
        String maatApplicationJson = FileUtils.readFileToString("data/criminalapplicationsdatastore/MaatApplication_6000308.json");
        MaatApplication expected = JsonUtils.jsonToObject(maatApplicationJson, MaatApplication.class);

        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenReturn(TestData.getMaatApplication());
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());

        MaatCaaContract response = crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308L);

        Assertions.assertThat(response).isEqualTo(expected);
    }

    @Test
    void given4xxOr5xxHttpErrorsOnInvokingCrimeApplyDatastoreService_thenWebClientResponseExceptionIsThrown() {
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(Mockito.mock(WebClientResponseException.class));
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());
        assertThrows(WebClientResponseException.class, () ->
            crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308L)
        );
    }

    @Test
    void givenNetworkUnreachableOnInvokingCrimeApplyDatastoreService_thenWebClientRequestExceptionIsThrown() {
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(Mockito.mock(WebClientRequestException.class));
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());
        assertThrows(WebClientRequestException.class, () ->
                crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308L)
        );
    }
}