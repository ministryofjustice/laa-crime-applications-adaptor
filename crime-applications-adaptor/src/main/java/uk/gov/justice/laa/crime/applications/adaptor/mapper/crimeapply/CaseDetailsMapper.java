package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails;

import javax.validation.constraints.NotNull;

class CaseDetailsMapper {

    private final CaseTypeMapper caseTypeMapper;
    private final OffenceClassMapper offenceClassMapper;

    CaseDetailsMapper() {
        caseTypeMapper = new CaseTypeMapper();
        offenceClassMapper = new OffenceClassMapper();
    }

    @NotNull
    CaseDetails map(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails crimeApplyCaseDetails) {

        CaseDetails caseDetails = new CaseDetails();
        if (crimeApplyCaseDetails == null) {
            return caseDetails;
        }

        caseDetails.setUrn(crimeApplyCaseDetails.getUrn());
        caseDetails.setCaseType(caseTypeMapper.map(crimeApplyCaseDetails.getCaseType()));
        caseDetails.setOffenceClass(offenceClassMapper.map(crimeApplyCaseDetails.getOffenceClass()));

        return caseDetails;
    }
}
