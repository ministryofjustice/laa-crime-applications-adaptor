package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CaseDetails;

import java.util.Objects;

class CaseTypeMapper {

    CaseDetails.CaseType map(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails.
                                     CaseType crimeApplyCaseType) {
        CaseDetails.CaseType caseType = null;

        if (Objects.nonNull(crimeApplyCaseType)) {
            switch (crimeApplyCaseType) {
                case SUMMARY_ONLY -> caseType = CaseDetails.CaseType.SUMMARY_ONLY;
                case EITHER_WAY -> caseType = CaseDetails.CaseType.EITHER_WAY;
                case INDICTABLE -> caseType = CaseDetails.CaseType.INDICTABLE;
                case ALREADY_IN_CROWN_COURT -> caseType = CaseDetails.CaseType.CC_ALREADY;
                case COMMITTAL -> caseType = CaseDetails.CaseType.COMMITAL;
                case APPEAL_TO_CROWN_COURT -> caseType = CaseDetails.CaseType.APPEAL_CC;
                case APPEAL_TO_CROWN_COURT_WITH_CHANGES -> caseType = CaseDetails.CaseType.APPEAL_CC;
            }
        }

        return caseType;
    }
}
