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
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrimeApplicationServiceTest {

    @Mock
    private CrimeApplyDatastoreClient crimeApplyDatastoreClient;

    @Mock
    private ServicesConfiguration servicesConfiguration;

    @InjectMocks
    private CrimeApplicationService crimeApplicationService;

    @Test
    void givenValidUsn_whenCrimeApplicationDatastoreServiceIsInvoked_thenApplicationDataIsReturned() throws IOException {
        String fileContents = FileUtils.readFileToString("data/application.json");
        MaatApplication expected = JsonUtils.jsonToObject(fileContents, MaatApplication.class);
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenReturn(expected);
        when(servicesConfiguration.getCrimeApplyApi()).thenReturn(MockServicesConfiguration.getConfiguration().getCrimeApplyApi());
        MaatApplication response = crimeApplicationService.callCrimeApplyDatastore(1001L);
        Assertions.assertThat(response).isEqualTo(expected);
    }


}
