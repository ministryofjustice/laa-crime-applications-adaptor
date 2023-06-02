package uk.gov.justice.laa.crime.applications.adaptor.service;

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
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        MaatApplication maatApplication = TestData.getMaatApplication();
        MaatCaaContract maatCaaContract = TestData.getMaatCaaContract();

        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenReturn(maatApplication);
        when(servicesConfiguration.getCrimeApplyApi())
                .thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());
        when(crimeApplyMapper.mapToMaatApplication(maatApplication))
                .thenReturn(maatCaaContract);

        MaatCaaContract response = crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308);

        assertEquals(maatCaaContract, response);
    }

    @Test
    void given4xxOr5xxHttpErrorsOnInvokingCrimeApplyDatastoreService_thenWebClientResponseExceptionIsThrown() {
        ServicesConfiguration.CrimeApplyApi crimeApplyApi = MockServicesConfiguration.getConfiguration().getCrimeApplyApi();

        when(servicesConfiguration.getCrimeApplyApi())
                .thenReturn(crimeApplyApi);
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(Mockito.mock(WebClientResponseException.class));

        assertThrows(WebClientResponseException.class, () ->
                crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308)
        );
    }

    @Test
    void givenNetworkUnreachableOnInvokingCrimeApplyDatastoreService_thenWebClientRequestExceptionIsThrown() {
        ServicesConfiguration.CrimeApplyApi crimeApplyApi = MockServicesConfiguration.getConfiguration().getCrimeApplyApi();

        when(servicesConfiguration.getCrimeApplyApi())
                .thenReturn(crimeApplyApi);
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenThrow(Mockito.mock(WebClientRequestException.class));

        assertThrows(WebClientRequestException.class, () ->
                crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(6000308)
        );
    }
}