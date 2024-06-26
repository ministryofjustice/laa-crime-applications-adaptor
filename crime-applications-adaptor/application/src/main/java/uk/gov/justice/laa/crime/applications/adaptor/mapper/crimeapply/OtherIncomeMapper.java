package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply.BenefitsMapper.DEFAULT_OWNERSHIP_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
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
  private static final String APPLICANT = "Applicant: ";
  private static final String PARTNER = "Partner: ";
  private static final String PARTNER_OWNER_TYPE = "partner";
  private static final String APPLICANT_OWNER_TYPE = "applicant";

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
    if (Objects.isNull(otherIncome)) {
      return StringUtils.EMPTY;
    }
    List<String> finalOtherIncomeNotes = new ArrayList<>();
    if (!mapApplicantOtherIncomeNotes(otherIncome).isEmpty()) {
      finalOtherIncomeNotes.add(mapApplicantOtherIncomeNotes(otherIncome));
    }
    if (!mapPartnerOtherIncomeNotes(otherIncome).isEmpty()) {
      finalOtherIncomeNotes.add(mapPartnerOtherIncomeNotes(otherIncome));
    }

    return String.join("\n", finalOtherIncomeNotes);
  }

  private String mapApplicantOtherIncomeNotes(List<IncomePayment> otherIncome) {
    List<IncomePayment> applicantOtherIncome =
        otherIncome.stream()
            .filter(
                incomePayment ->
                    (Objects.isNull(incomePayment.getOwnershipType())
                        || Objects.nonNull(incomePayment.getOwnershipType())
                            && incomePayment
                                .getOwnershipType()
                                .value()
                                .equals(APPLICANT_OWNER_TYPE)))
            .toList();
    return getOtherIncomeNotes(applicantOtherIncome, APPLICANT);
  }

  private String mapPartnerOtherIncomeNotes(List<IncomePayment> otherIncome) {
    List<IncomePayment> partnerOtherIncome =
        otherIncome.stream()
            .filter(
                incomePayment ->
                    (Objects.nonNull(incomePayment.getOwnershipType())
                        && incomePayment.getOwnershipType().value().equals(PARTNER_OWNER_TYPE)))
            .toList();
    return getOtherIncomeNotes(partnerOtherIncome, PARTNER);
  }

  private String getOtherIncomeNotes(List<IncomePayment> otherIncome, String owner) {
    List<String> otherIncomeNotes = new ArrayList<>();
    otherIncome.forEach(
        incomePayment -> {
          if (Objects.nonNull(incomePayment.getDetails())) {
            otherIncomeNotes.add(incomePayment.getDetails().toString());
          }
          String otherIncomeNote = getNote(incomePayment);
          if (!StringUtils.EMPTY.equals(otherIncomeNote)) {
            otherIncomeNotes.add(otherIncomeNote);
          }
        });

    return CollectionUtils.isEmpty(otherIncomeNotes)
        ? StringUtils.EMPTY
        : owner.concat(String.join("\n", otherIncomeNotes));
  }

  private String getNote(IncomePayment other) {

    OtherIncomeDetails otherIncomeDetail =
        OtherIncomeDetails.findByValue(other.getPaymentType().value());
    String note;
    switch (otherIncomeDetail) {
      case STUDENT_LOAN_GRANT -> note = STUDENT_LOAN_GRANT;
      case BOARD_FROM_FAMILY -> note = BOARD_FROM_FAMILY;
      case RENT -> note = RENT;
      case FROM_FRIENDS_RELATIVES -> note = FROM_FRIENDS_RELATIVES;
      case FINANCIAL_SUPPORT_WITH_ACCESS -> note = FINANCIAL_SUPPORT_WITH_ACCESS;
      default -> note = StringUtils.EMPTY;
    }
    return note;
  }
}
