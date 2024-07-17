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
  private static final String STUDENT_LOAN_GRANT = "Student grant or loan";
  private static final String BOARD_FROM_FAMILY =
      "Board from family members living with your client";
  private static final String RENT = "Rent from a tenant";
  private static final String FROM_FRIENDS_RELATIVES = "Money from friends or family";
  private static final String FINANCIAL_SUPPORT_WITH_ACCESS =
      "Financial support from someone who allows your client access to their assets or money";

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
    List<String> otherBenefitNotes = new ArrayList<>();
    if (Objects.isNull(otherIncome)) {
      return StringUtils.EMPTY;
    }

    for (IncomePayment other : otherIncome) {
      if (Objects.nonNull(other.getDetails())) {
        otherBenefitNotes.add(other.getDetails().toString());
      }

      String incomeType = other.getPaymentType().value();
      OtherIncomeDetails otherIncomeDetail = OtherIncomeDetails.findByValue(incomeType);
      String note;
      switch (otherIncomeDetail) {
        case STUDENT_LOAN_GRANT -> note = STUDENT_LOAN_GRANT;
        case BOARD_FROM_FAMILY -> note = BOARD_FROM_FAMILY;
        case RENT -> note = RENT;
        case FROM_FRIENDS_RELATIVES -> note = FROM_FRIENDS_RELATIVES;
        case FINANCIAL_SUPPORT_WITH_ACCESS -> note = FINANCIAL_SUPPORT_WITH_ACCESS;
        default -> note = StringUtils.EMPTY;
      }
      if (!StringUtils.EMPTY.equals(note)) {
        otherBenefitNotes.add(note);
      }
    }
    return String.join("\n", otherBenefitNotes);
  }
}
