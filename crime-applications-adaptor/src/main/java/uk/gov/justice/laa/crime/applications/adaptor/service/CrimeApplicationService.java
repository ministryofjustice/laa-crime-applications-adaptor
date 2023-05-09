package uk.gov.justice.laa.crime.applications.adaptor.service;


import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.client.CrimeApplyDatastoreClient;
import uk.gov.justice.laa.crime.applications.adaptor.config.ServicesConfiguration;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.util.CrimeApplicationUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrimeApplicationService {

    private static final String SERVICE_NAME = "callCrimeApplyDatastore";

    private final CrimeApplyDatastoreClient crimeApplyDatastoreClient;
    private final ServicesConfiguration servicesConfiguration;

    private final ObservationRegistry observationRegistry;

    @Retry(name=SERVICE_NAME)
    public MaatApplication callCrimeApplyDatastore(Long usn) {
        MaatApplication maatApplication = crimeApplyDatastoreClient.getApplicationDetails(usn,
                CrimeApplicationUtil.getHttpHeaders(
                        servicesConfiguration.getCrimeApplyApi().getClientSecret(),
                        servicesConfiguration.getCrimeApplyApi().getIssuer()));

        return Observation.createNotStarted(SERVICE_NAME, observationRegistry)
                .observe(()->maatApplication);
    }
}
