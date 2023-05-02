package uk.gov.justice.laa.crime.applications.adaptor.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.client.CrimeApplyDatastoreClient;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.util.CrimeApplicationUtil;

@Service
@AllArgsConstructor
@Slf4j
public class CrimeApplicationService {

    private CrimeApplyDatastoreClient crimeApplyDatastoreClient;
    public MaatApplication callCrimeApplyDatastore(Long usn) {

        return crimeApplyDatastoreClient.getApplicationDetails(usn,
                CrimeApplicationUtil.getHttpHeaders());

    }


}
