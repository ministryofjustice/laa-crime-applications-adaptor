package uk.gov.justice.laa.crime.applications.adaptor.service;

import org.fest.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.applications.adaptor.client.CrimeApplyDatastoreClient;
import uk.gov.justice.laa.crime.applications.adaptor.config.MockServicesConfiguration;
import uk.gov.justice.laa.crime.applications.adaptor.config.ServicesConfiguration;
import uk.gov.justice.laa.crime.applications.adaptor.exception.APIClientException;
import uk.gov.justice.laa.crime.applications.adaptor.exception.RetryableWebClientResponseException;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;

import java.io.IOException;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrimeApplicationServiceTest {

    @Mock
    private CrimeApplyDatastoreClient crimeApplyDatastoreClient;

    @Mock
    private ServicesConfiguration servicesConfiguration;

    @InjectMocks
    private CrimeApplicationService crimeApplicationService;

    @Test
    void givenValidParams_whenCrimeApplyDatastoreServiceIsInvoked_thenReturnApplicationData() throws IOException {
        String maatApplicationJson = FileUtils.readFileToString("data/application.json");
        MaatApplication expected = JsonUtils.jsonToObject(maatApplicationJson, MaatApplication.class);

        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenReturn(expected);
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());

        MaatApplication response = crimeApplicationService.callCrimeApplyDatastore(1001L);

        Assertions.assertThat(response).isEqualTo(expected);
    }
    @Test
    void givenInvaidParams_whenCrimeApplyDatastoreServiceIsInvoked_then4xxClientExceptionIsThrown() {
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(new APIClientException("404"));
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());

        assertThrows(APIClientException.class, () ->
            crimeApplicationService.callCrimeApplyDatastore(1001L)
        );
    }
    @Test
    void whenCrimeApplyDatastoreServiceIsUnavailable_then5xxServerExceptionIsThrown() {
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(new RetryableWebClientResponseException("503"));
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());

        assertThrows(RetryableWebClientResponseException.class, () ->
            crimeApplicationService.callCrimeApplyDatastore(1001L)
        );
    }


}

