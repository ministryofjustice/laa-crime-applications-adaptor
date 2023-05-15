package uk.gov.justice.laa.crime.applications.adaptor.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.exception.RecordExistsException;
import uk.gov.justice.laa.crime.applications.adaptor.model.EformStagingResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformStagingService {
    private static final String EXCEPTION_MESSAGE_FORMAT = "MAAT Reference [%d] for USN [%d] already exists";
    private static final String SERVICE_NAME = "eformStagingService";

    private final MaatCourtDataApiClient eformStagingApiClient;

    private final ObservationRegistry observationRegistry;

    @Retry(name=SERVICE_NAME)
    public void retriveOrInsertDummyUsnRecord(Long usn) {
        log.info("Start - call to Eform Staging API ");
        EformStagingResponse eformStagingResponse = eformStagingApiClient.retrieveOrInsertDummyUsnRecordInEformStaging(usn);
        Integer maatRef = eformStagingResponse.getMaatRef();
        if (maatRef != null) {
            String message = String.format(EXCEPTION_MESSAGE_FORMAT, maatRef, usn);
            throw new RecordExistsException(message);
        }
        Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(()->eformStagingResponse);
    }
}
