package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CaseDetails;

import javax.validation.constraints.NotNull;

class CaseDetailsMapper {

    private final CaseTypeMapper caseTypeMapper = new CaseTypeMapper();
    private final OffenceClassMapper offenceClassMapper = new OffenceClassMapper();

    @NotNull
    CaseDetails map(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.
                            CaseDetails crimeApplyCaseDetails) {

        CaseDetails caseDetails = new CaseDetails();
        if (crimeApplyCaseDetails == null) {
            return caseDetails;
        }

        caseDetails.setUrn(crimeApplyCaseDetails.getUrn());
        caseDetails.setCaseType(caseTypeMapper.map(crimeApplyCaseDetails.getCaseType()));
        caseDetails.setOffenceClass(offenceClassMapper.map(crimeApplyCaseDetails.getOffenceClass()));
        caseDetails.setAppealMaatId(crimeApplyCaseDetails.getAppealMaatId());
        caseDetails.setAppealWithChangesDetails(crimeApplyCaseDetails.getAppealWithChangesDetails());

        return caseDetails;
    }
}
