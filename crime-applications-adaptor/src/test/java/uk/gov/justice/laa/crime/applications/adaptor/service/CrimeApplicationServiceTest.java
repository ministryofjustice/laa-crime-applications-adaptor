package uk.gov.justice.laa.crime.applications.adaptor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.applications.adaptor.client.CrimeApplyDatastoreClient;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.utils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.utils.JsonUtils;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CrimeApplicationServiceTest {

    @Mock
    private CrimeApplyDatastoreClient crimeApplyDatastoreClient;

    @InjectMocks
    private CrimeApplicationService crimeApplicationService;

    @Test
    void givenUsn_whenCrimeApplicationServiceIsInvoked_thenApplicationDataIsReturned() throws IOException {
        String fileContents = FileUtils.readFileToString("data/application.json");
        MaatApplication expected = JsonUtils.jsonToObject(fileContents, MaatApplication.class);
        when(crimeApplyDatastoreClient.getApplicationDetails(anyLong(), anyMap()))
                .thenReturn(expected);
        MaatApplication response = crimeApplicationService.callCrimeApplyDatastore(1001L);
        assertThat(response).isEqualTo(expected);
    }


}
