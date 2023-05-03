package uk.gov.justice.laa.crime.applications.adaptor.service;


import lombok.AllArgsConstructor;
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

    private final CrimeApplyDatastoreClient crimeApplyDatastoreClient;
    private final ServicesConfiguration servicesConfiguration;
    public MaatApplication callCrimeApplyDatastore(Long usn) {
        return crimeApplyDatastoreClient.getApplicationDetails(usn,
                CrimeApplicationUtil.getHttpHeaders(
                        servicesConfiguration.getCrimeApplyApi().getClientSecret(),
                        servicesConfiguration.getCrimeApplyApi().getIssuer()));

    }
}
