package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.BenefitDetails;
import uk.gov.justice.laa.crime.applications.adaptor.factory.PoundSterling;
import uk.gov.justice.laa.crime.applications.adaptor.util.FrequencyMapper;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.IncomeBenefit;

public class BenefitsMapper {

  private static final String UNIVERSAL_CREDIT = "Universal Credit";
  private static final String JSA = "Contribution-based Jobseeker Allowance";

  public List<AssessmentDetail> mapBenefits(List<IncomeBenefit> benefits) {
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();

    if (Objects.nonNull(benefits)) {
      for (IncomeBenefit benefit : benefits) {
        AssessmentDetail assessmentDetail = new AssessmentDetail();
        String benefitType = benefit.getPaymentType().value();
        BenefitDetails benefitDetail = BenefitDetails.findByValue(benefitType);
        assessmentDetail.setAssessmentDetailCode(benefitDetail.getCode());
        assessmentDetail.setApplicantAmount(
            PoundSterling.ofPennies(benefit.getAmount()).toPounds());
        assessmentDetail.setApplicantFrequency(
            FrequencyMapper.mapFrequency(benefit.getFrequency().value()));
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
      } else if (benefitDetail.getValue().equals(BenefitDetails.JSA.getValue())) {
        otherBenefitNotes.add(JSA);
      }
    }

    return String.join("\n", otherBenefitNotes);
  }
}
