package uk.gov.justice.laa.crime.applications.adaptor.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply.CrimeApplicationResultMapper;
import uk.gov.justice.laa.crime.applications.adaptor.model.RepOrderState;
import uk.gov.justice.laa.crime.model.common.atis.CrimeApplicationResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrimeApplicationResultService {
  private static final String SERVICE_NAME = "crimeApplicationResultService";

  private final MaatCourtDataApiClient maatCourtDataApiClient;
  private final CrimeApplicationResultMapper crimeApplicationResultMapper;
  private final ObservationRegistry observationRegistry;

  @Retry(name = SERVICE_NAME)
  public CrimeApplicationResult getCrimeApplicationResult(int usn) {
    log.info("Start - call to Rep Order State API ");
    RepOrderState repOrderState = maatCourtDataApiClient.retrieveCrimeApplicationResultsByUsn(usn);
    CrimeApplicationResult crimeApplicationResult = crimeApplicationResultMapper.map(repOrderState);
    return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
        .observe(() -> crimeApplicationResult);
  }
}
