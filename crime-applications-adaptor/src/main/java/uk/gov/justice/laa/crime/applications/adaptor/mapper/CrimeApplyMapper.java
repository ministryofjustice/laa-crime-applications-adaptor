package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CadApplicationResponse;

/**
 * The responsibility of this class is to encapsulate the required logic to map from a
 * Criminal Applications Datastore response
 * to a
 * Crime Applications Adaptor MAAT Application (which should be structured like a MAAT ApplicationDTO
 */
@Service
@RequiredArgsConstructor
public class CrimeApplyMapper {

    public MaatApplication mapToMaatApplication(CadApplicationResponse crimeApplyResponse) {
        // TODO Alex implement this
        // apply mapping logic as per doc to go from Crime Apply response
        // uk.gov.justice.laa.crime.applications.adaptor.model.crimeapply.MaatCaaContract
        // to
        // uk.gov.justice.laa.crime.applications.adaptor.model.maat.MaatApplication
        MaatApplication maatApplication = new MaatApplication();

        return maatApplication;
    }
}
