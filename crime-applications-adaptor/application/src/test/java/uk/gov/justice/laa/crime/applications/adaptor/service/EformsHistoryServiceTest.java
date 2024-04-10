package uk.gov.justice.laa.crime.applications.adaptor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformsHistory;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EformsHistoryServiceTest {

    @Mock
    private MaatCourtDataApiClient eformStagingApiClient;

    @InjectMocks
    private EformsHistoryService eformsHistoryService;

    @Test
    void shouldCreateEformsDecisionHistoryRecordForGivenUSN() {
        EformsHistory eformsHistory = EformsHistory.builder().usn(6000308).action("Get").userCreated("causer").build();
        doNothing().when(eformStagingApiClient).createEformsHistoryRecord(eformsHistory);
        eformsHistoryService.createEformsHistoryRecord(6000308L, "causer");
        verify(eformStagingApiClient, times(1)).createEformsHistoryRecord(eformsHistory);
    }
}
