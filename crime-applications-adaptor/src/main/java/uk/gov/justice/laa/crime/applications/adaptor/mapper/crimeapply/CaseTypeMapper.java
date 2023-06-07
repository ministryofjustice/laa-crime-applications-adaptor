package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails;

class CaseTypeMapper {

    CaseDetails.CaseType map(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails.CaseType crimeApplyCaseType) {
        if (crimeApplyCaseType == null) {
            return null;
        }

        switch (crimeApplyCaseType) {
            case SUMMARY_ONLY -> {
                return CaseDetails.CaseType.SUMMARY_ONLY;
            }
            case EITHER_WAY -> {
                return CaseDetails.CaseType.EITHER_WAY;
            }
            case INDICTABLE -> {
                return CaseDetails.CaseType.INDICTABLE;
            }
            case ALREADY_IN_CROWN_COURT -> {
                return CaseDetails.CaseType.CC_ALREADY;
            }
            case COMMITTAL -> {
                return CaseDetails.CaseType.COMMITAL;
            }
            case APPEAL_TO_CROWN_COURT, APPEAL_TO_CROWN_COURT_WITH_CHANGES -> {
                return CaseDetails.CaseType.APPEAL_CC;
            }
            default -> throw new IllegalStateException("Unexpected value: " + crimeApplyCaseType);
        }
    }
}
