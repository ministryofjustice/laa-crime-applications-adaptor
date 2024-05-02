package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.CaseDetails;

class OffenceClassMapper {

  CaseDetails.OffenceClass map(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.CaseDetails.OffenceClass
          crimeApplyOffenceClass) {
    if (Objects.nonNull(crimeApplyOffenceClass)) {
      switch (crimeApplyOffenceClass) {
        case A -> {
          return CaseDetails.OffenceClass.MURDER;
        }
        case B -> {
          return CaseDetails.OffenceClass.SERIOUS_VIOL_DRUGS;
        }
        case C -> {
          return CaseDetails.OffenceClass.LESSER_VIOL_DRUGS;
        }
        case D -> {
          return CaseDetails.OffenceClass.SEX;
        }
        case E -> {
          return CaseDetails.OffenceClass.BURGLARY;
        }
        case F -> {
          return CaseDetails.OffenceClass.DISHONESTY_LT_30_K;
        }
        case G -> {
          return CaseDetails.OffenceClass.DISHONESTY_30_K_100_K;
        }
        case H -> {
          return CaseDetails.OffenceClass.OTHER;
        }
        case I -> {
          return CaseDetails.OffenceClass.REVENUE_PUBLIC_ORDER;
        }
        case J -> {
          return CaseDetails.OffenceClass.COMPLEX_SEX;
        }
        case K -> {
          return CaseDetails.OffenceClass.DISHONESTY_GT_100_K;
        }
      }
    }
    return null;
  }
}
