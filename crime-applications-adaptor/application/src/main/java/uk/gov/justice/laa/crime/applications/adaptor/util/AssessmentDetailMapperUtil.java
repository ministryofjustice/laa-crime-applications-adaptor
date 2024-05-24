package uk.gov.justice.laa.crime.applications.adaptor.util;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.applications.adaptor.factory.PoundSterling;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;

@UtilityClass
public class AssessmentDetailMapperUtil {

  private static final String APPLICANT = "applicant";
  private static final String PARTNER = "partner";

  public AssessmentDetail mapMeansAssessmentDetails(
      String ownershipType,
      String assessmentCode,
      Integer assessmentAmount,
      String AssessmentFrequency) {
    AssessmentDetail assessmentDetail = new AssessmentDetail();
    assessmentDetail.setAssessmentDetailCode(assessmentCode);
    switch (ownershipType) {
      case APPLICANT -> {
        assessmentDetail.setApplicantAmount(PoundSterling.ofPennies(assessmentAmount).toPounds());
        assessmentDetail.setApplicantFrequency(
            FrequencyMapper.mapApplicantFrequency(AssessmentFrequency));
      }
      case PARTNER -> {
        assessmentDetail.setPartnerAmount(PoundSterling.ofPennies(assessmentAmount).toPounds());
        assessmentDetail.setPartnerFrequency(
            FrequencyMapper.mapPartnerFrequency(AssessmentFrequency));
      }
    }
    return assessmentDetail;
  }
}
