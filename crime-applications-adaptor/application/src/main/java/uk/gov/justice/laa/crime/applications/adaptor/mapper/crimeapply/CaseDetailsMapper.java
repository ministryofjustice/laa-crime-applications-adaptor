package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import javax.validation.constraints.NotNull;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.CaseDetails;

class CaseDetailsMapper {

  private final CaseTypeMapper caseTypeMapper = new CaseTypeMapper();
  private final OffenceClassMapper offenceClassMapper = new OffenceClassMapper();

  @NotNull CaseDetails map(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.CaseDetails
          crimeApplyCaseDetails) {

    CaseDetails caseDetails = new CaseDetails();
    if (crimeApplyCaseDetails == null) {
      return caseDetails;
    }

    caseDetails.setUrn(crimeApplyCaseDetails.getUrn());
    caseDetails.setCaseType(caseTypeMapper.map(crimeApplyCaseDetails.getCaseType()));
    caseDetails.setOffenceClass(offenceClassMapper.map(crimeApplyCaseDetails.getOffenceClass()));
    caseDetails.setAppealMaatId(crimeApplyCaseDetails.getAppealMaatId());
    caseDetails.setAppealWithChangesDetails(crimeApplyCaseDetails.getAppealWithChangesDetails());
    // LASB-3851 Do not map the appealReceivedDate as this will not be persisted correctly in MAAT

    return caseDetails;
  }
}
