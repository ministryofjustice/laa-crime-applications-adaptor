package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.BenefitDetails;
import uk.gov.justice.laa.crime.applications.adaptor.util.AssessmentDetailMapperUtil;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.IncomeBenefit;

public class BenefitsMapper {

  private static final String UNIVERSAL_CREDIT = "Universal Credit";
  protected static final String DEFAULT_OWNERSHIP_TYPE = "applicant";

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
    List<String> otherBenefitNotes = new ArrayList<>();

    if (Objects.isNull(benefits)) {
      return StringUtils.EMPTY;
    }

    for (IncomeBenefit benefit : benefits) {
      if (Objects.nonNull(benefit.getDetails())) {
        otherBenefitNotes.add(benefit.getDetails().toString());
      }

      String benefitType = benefit.getPaymentType().value();
      BenefitDetails benefitDetail = BenefitDetails.findByValue(benefitType);
      if (benefitDetail.getValue().equals(BenefitDetails.UNIVERSAL_CREDIT.getValue())) {
        otherBenefitNotes.add(UNIVERSAL_CREDIT);
      }
    }

    return String.join("\n", otherBenefitNotes);
  }
}
