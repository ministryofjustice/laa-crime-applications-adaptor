package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply.BenefitsMapper.DEFAULT_OWNERSHIP_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OtherIncomeDetails;
import uk.gov.justice.laa.crime.applications.adaptor.util.AssessmentDetailMapperUtil;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.IncomePayment;

public class OtherIncomeMapper {
  public List<AssessmentDetail> mapOtherIncome(List<IncomePayment> otherIncome) {
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();

    if (Objects.nonNull(otherIncome)) {
      for (IncomePayment other : otherIncome) {
        String ownershipType =
            Objects.nonNull(other.getOwnershipType())
                ? other.getOwnershipType().value()
                : DEFAULT_OWNERSHIP_TYPE;
        String incomeType = other.getPaymentType().value();
        OtherIncomeDetails otherIncomeDetail = OtherIncomeDetails.findByValue(incomeType);
        AssessmentDetail assessmentDetail =
            AssessmentDetailMapperUtil.mapMeansAssessmentDetails(
                ownershipType,
                otherIncomeDetail.getCode(),
                other.getAmount(),
                other.getFrequency().value());
        assessmentDetails.add(assessmentDetail);
      }
    }

    return assessmentDetails;
  }

  public String mapOtherIncomeNotes(List<IncomePayment> otherIncome) {
    List<String> otherIncomeNotes = new ArrayList<>();
    if (Objects.isNull(otherIncome)) {
      return StringUtils.EMPTY;
    }

    for (IncomePayment other : otherIncome) {
      if (Objects.nonNull(other.getDetails())) {
        otherIncomeNotes.add(other.getDetails().toString());
      }
    }
    return String.join("\n", otherIncomeNotes);
  }
}
