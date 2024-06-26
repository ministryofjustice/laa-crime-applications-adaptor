package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.BenefitDetails;
import uk.gov.justice.laa.crime.applications.adaptor.util.AssessmentDetailMapperUtil;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.IncomeBenefit;

public class BenefitsMapper {

  private static final String UNIVERSAL_CREDIT = "Universal Credit";
  private static final String JSA = "Contribution-based Jobseeker Allowance";
  protected static final String DEFAULT_OWNERSHIP_TYPE = "applicant";
  private static final String APPLICANT = "Applicant: ";
  private static final String PARTNER = "Partner: ";
  private static final String PARTNER_OWNER_TYPE = "partner";
  private static final String APPLICANT_OWNER_TYPE = "applicant";

  public List<AssessmentDetail> mapBenefits(List<IncomeBenefit> benefits) {
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();

    if (Objects.nonNull(benefits)) {
      for (IncomeBenefit benefit : benefits) {
        String ownershipType =
            Objects.nonNull(benefit.getOwnershipType())
                ? benefit.getOwnershipType().value()
                : DEFAULT_OWNERSHIP_TYPE;
        String benefitType = benefit.getPaymentType().value();
        BenefitDetails benefitDetail = BenefitDetails.findByValue(benefitType);
        AssessmentDetail assessmentDetail =
            AssessmentDetailMapperUtil.mapMeansAssessmentDetails(
                ownershipType,
                benefitDetail.getCode(),
                benefit.getAmount(),
                benefit.getFrequency().value());
        assessmentDetails.add(assessmentDetail);
      }
    }
    return assessmentDetails;
  }

  public String mapOtherBenefitNotes(List<IncomeBenefit> benefits) {
    if (Objects.isNull(benefits)) {
      return StringUtils.EMPTY;
    }
    List<String> finalOtherBenefitNotes = new ArrayList<>();
    if (!mapApplicantOtherBenefitNotes(benefits).isEmpty()) {
      finalOtherBenefitNotes.add(mapApplicantOtherBenefitNotes(benefits));
    }
    if (!mapPartnerOtherBenefitNotes(benefits).isEmpty()) {
      finalOtherBenefitNotes.add(mapPartnerOtherBenefitNotes(benefits));
    }

    return String.join("\n", finalOtherBenefitNotes);
  }

  private String mapPartnerOtherBenefitNotes(List<IncomeBenefit> benefits) {
    List<IncomeBenefit> applicantOtherBenefit =
        benefits.stream()
            .filter(
                incomeBenefit ->
                    (Objects.nonNull(incomeBenefit.getOwnershipType())
                        && incomeBenefit.getOwnershipType().value().equals(PARTNER_OWNER_TYPE)))
            .toList();
    return getOtherBenefitNotes(applicantOtherBenefit, PARTNER);
  }

  private String mapApplicantOtherBenefitNotes(List<IncomeBenefit> benefits) {
    List<IncomeBenefit> applicantOtherBenefit =
        benefits.stream()
            .filter(
                incomeBenefit ->
                    (Objects.isNull(incomeBenefit.getOwnershipType())
                        || Objects.nonNull(incomeBenefit.getOwnershipType())
                            && incomeBenefit
                                .getOwnershipType()
                                .value()
                                .equals(APPLICANT_OWNER_TYPE)))
            .toList();
    return getOtherBenefitNotes(applicantOtherBenefit, APPLICANT);
  }

  private String getOtherBenefitNotes(List<IncomeBenefit> benefits, String owner) {
    List<String> otherBenefitNotes = new ArrayList<>();

    for (IncomeBenefit benefit : benefits) {
      if (Objects.nonNull(benefit.getDetails())) {
        otherBenefitNotes.add(benefit.getDetails().toString());
      }

      String benefitType = benefit.getPaymentType().value();
      BenefitDetails benefitDetail = BenefitDetails.findByValue(benefitType);
      if (benefitDetail.getValue().equals(BenefitDetails.UNIVERSAL_CREDIT.getValue())) {
        otherBenefitNotes.add(UNIVERSAL_CREDIT);
      } else if (benefitDetail.getValue().equals(BenefitDetails.JSA.getValue())) {
        otherBenefitNotes.add(JSA);
      }
    }
    return CollectionUtils.isEmpty(otherBenefitNotes)
        ? StringUtils.EMPTY
        : owner.concat(String.join("\n", otherBenefitNotes));
  }
}
