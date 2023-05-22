package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapply.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.model.maat.MaatApplication;

@Service
@RequiredArgsConstructor
public class CrimeApplyMapper {

    public MaatApplication mapToMaatApplication(MaatCaaContract crimeApplyApplicationDetails) {
        // TODO Alex implement this
        // apply mapping logic as per doc to go from Crime Apply response
        // uk.gov.justice.laa.crime.applications.adaptor.model.crimeapply.MaatCaaContract
        // to
        // uk.gov.justice.laa.crime.applications.adaptor.model.maat.MaatApplication
        MaatApplication maatApplication = new MaatApplication();

        return maatApplication;
    }
}
