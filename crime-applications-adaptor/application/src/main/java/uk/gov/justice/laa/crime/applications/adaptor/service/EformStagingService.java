package uk.gov.justice.laa.crime.applications.adaptor.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformStagingService {

  private static final String SERVICE_NAME = "eformStagingService";

  private final MaatCourtDataApiClient eformStagingApiClient;

  private final ObservationRegistry observationRegistry;

  @Retry(name = SERVICE_NAME)
  public EformStagingResponse retrieveOrInsertDummyUsnRecord(long usn, String userCreated) {
    log.info("Start - call to Eform Staging API ");
    EformStagingResponse eformStagingResponse =
        eformStagingApiClient.retrieveOrInsertDummyUsnRecordInEformStaging(usn, userCreated);
    return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
        .observe(() -> eformStagingResponse);
  }
}
