package uk.gov.justice.laa.crime.applications.adaptor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.applications.adaptor.client.EformStagingApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.model.EformStagingResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EformStagingServiceTest {

    @Mock
    private EformStagingApiClient eformStagingApiClient;

    @InjectMocks
    private EformStagingService eformStagingService;

    @Test
    void givenValidParams_whenMaatReferenceExistForUsnInEformStaging_thenRuntimeExceptionIsThrown() throws IOException {
        EformStagingResponse retrievedData = EformStagingResponse.builder().maatRef(1001).usn(6000308).build();

        when(eformStagingApiClient.retriveOrInsertDummyUsnRecordInEformStaging(any()))
                .thenReturn(retrievedData);
        assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                eformStagingService.retriveOrInsertDummyUsnRecord(6000308L);
            }
        });
    }

    @Test
    void givenValidParams_whenMaatReferenceNotExistForUsnInEformStaging_thenCallEformStagingApiClientToCreateDummyRecord() throws IOException {
        EformStagingResponse retrievedData = EformStagingResponse.builder().maatRef(null).usn(6000308).build();
        when(eformStagingApiClient.retriveOrInsertDummyUsnRecordInEformStaging(any()))
                .thenReturn(retrievedData);
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                eformStagingService.retriveOrInsertDummyUsnRecord(6000308L);
            }
        });

        verify(eformStagingApiClient, times(1)).retriveOrInsertDummyUsnRecordInEformStaging(6000308L);
    }

}

