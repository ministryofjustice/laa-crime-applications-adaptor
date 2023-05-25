package uk.gov.justice.laa.crime.applications.adaptor.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.exception.CrimeApplicationException;
import uk.gov.justice.laa.crime.applications.adaptor.model.EformStagingResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformStagingService {
    private static final String EFORM_STAGING_HUB_USER = "HUB";
    private static final String EXCEPTION_MESSAGE_FORMAT = "USN: %d created by HUB user";
    private static final String SERVICE_NAME = "eformStagingService";

    private final MaatCourtDataApiClient eformStagingApiClient;

    private final ObservationRegistry observationRegistry;

    @Retry(name = SERVICE_NAME)
    public EformStagingResponse retriveOrInsertDummyUsnRecord(Long usn) {
        log.info("Start - call to Eform Staging API ");
        EformStagingResponse eformStagingResponse = eformStagingApiClient.retrieveOrInsertDummyUsnRecordInEformStaging(usn);
        if (isUsnInEformStagingCreatedByHubUser(eformStagingResponse)) {
            String message = String.format(EXCEPTION_MESSAGE_FORMAT, usn);
            throw new CrimeApplicationException(message);
        }
        return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(() -> eformStagingResponse);
    }

    private static boolean isUsnInEformStagingCreatedByHubUser(EformStagingResponse eformStagingResponse) {
        return StringUtils.isNotBlank(eformStagingResponse.getUserCreated())
                && eformStagingResponse.getUserCreated().equalsIgnoreCase(EFORM_STAGING_HUB_USER);
    }
}

