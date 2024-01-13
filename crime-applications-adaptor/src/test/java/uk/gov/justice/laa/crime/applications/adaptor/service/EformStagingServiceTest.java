package uk.gov.justice.laa.crime.applications.adaptor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EformStagingServiceTest {

    private static final String DEFAULT_USER = "causer";
    @Mock
    private MaatCourtDataApiClient eformStagingApiClient;

    @InjectMocks
    private EformStagingService eformStagingService;

    @Test
    void givenValidParams_whenEformStagingServiceIsInvoked_thenEformStagingRecordIsRetrievedForUsn() throws IOException {
        EformStagingResponse retrievedData = EformStagingResponse.builder().maatRef(null).usn(6000308).build();

        when(eformStagingApiClient.retrieveOrInsertDummyUsnRecordInEformStaging(anyLong(), anyString()))
                .thenReturn(retrievedData);

        assertDoesNotThrow(invokeEformStagingServiceForRetrieveOrInsertDummyUsnRecord());

        verify(eformStagingApiClient, times(1)).retrieveOrInsertDummyUsnRecordInEformStaging(6000308L, DEFAULT_USER);
    }

    private Executable invokeEformStagingServiceForRetrieveOrInsertDummyUsnRecord() {
        return new Executable() {
            @Override
            public void execute() throws Throwable {
                eformStagingService.retrieveOrInsertDummyUsnRecord(6000308L, DEFAULT_USER);
            }
        };
    }

}

