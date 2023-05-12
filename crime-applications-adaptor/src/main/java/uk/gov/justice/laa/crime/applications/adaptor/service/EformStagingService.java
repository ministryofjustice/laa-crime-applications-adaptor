package uk.gov.justice.laa.crime.applications.adaptor.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.client.EformStagingApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.model.EformStagingResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformStagingService {
    private static final String EXCEPTION_MESSAGE_FORMAT = "MAAT Reference [%d] for USN [%d] already exist";
    private static final String SERVICE_NAME = "retriveOrInsertDummyUsnRecord";
    private final EformStagingApiClient maatApiClient;

    @Retry(name=SERVICE_NAME)
    public void retriveOrInsertDummyUsnRecord(Long usn) {
        EformStagingResponse eformStagingResponse = maatApiClient.retriveOrInsertDummyUsnRecordInEformStaging(usn);
        Integer maatRef = eformStagingResponse.getMaatRef();
        if (maatRef != null) {
            String message = String.format(EXCEPTION_MESSAGE_FORMAT, maatRef, usn);
            throw new RuntimeException(message);
        }
    }
}
