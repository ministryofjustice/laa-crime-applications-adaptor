package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapply.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.model.maat.MaatApplication;

@Service
@RequiredArgsConstructor
public class EformMapper {

    public MaatApplication mapToMaatApplication(MaatCaaContract crimeApplyApplicationDetails) {
        // TODO Alex implement this
        // apply mapping logic as per doc to go from Crime Apply response to MaatApplication
        MaatApplication maatApplication = new MaatApplication();
        
        return maatApplication;
    }
}
