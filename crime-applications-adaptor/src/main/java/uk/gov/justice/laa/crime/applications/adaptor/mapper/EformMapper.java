package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;

@Service
@RequiredArgsConstructor
public class EformMapper {

    public MaatApplication mapToMaatApplication(String xmlDoc) {
        // TODO Alex implement this
        MaatApplication maatApplication = new MaatApplication();
        return maatApplication;
    }
}
