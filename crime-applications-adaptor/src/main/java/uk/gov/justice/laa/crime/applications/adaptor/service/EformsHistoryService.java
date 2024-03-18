package uk.gov.justice.laa.crime.applications.adaptor.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformsHistory;

@Service
@RequiredArgsConstructor
@Slf4j
public class EformsHistoryService {

  private static final String SERVICE_NAME = "eformsHistoryService";
  private static final String ACTION = "Get";

  private final MaatCourtDataApiClient eformHistoryApiClient;
  private final ObservationRegistry observationRegistry;

  @Retry(name = SERVICE_NAME)
  public void createEformsHistoryRecord(long usn, String userCreated) {
    log.info("Start - call to Create Eforms History API {}", usn);
    EformsHistory eformsHistory =
        EformsHistory.builder()
            .usn(Math.toIntExact(usn))
            .action(ACTION)
            .userCreated(userCreated)
            .build();
    eformHistoryApiClient.createEformsHistoryRecord(eformsHistory);
    Observation.createNotStarted(SERVICE_NAME, observationRegistry)
        .observe(() -> log.info("Eform History Record Created"));
  }
}
