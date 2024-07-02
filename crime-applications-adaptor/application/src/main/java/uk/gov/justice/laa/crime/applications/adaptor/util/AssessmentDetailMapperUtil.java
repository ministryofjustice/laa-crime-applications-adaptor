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
      String assessmentFrequency) {
    AssessmentDetail assessmentDetail = new AssessmentDetail();
    assessmentDetail.setAssessmentDetailCode(assessmentCode);
    if (ownershipType.equals(APPLICANT)) {
      assessmentDetail.setApplicantAmount(PoundSterling.ofPennies(assessmentAmount).toPounds());
      assessmentDetail.setApplicantFrequency(
          FrequencyMapper.mapApplicantFrequency(assessmentFrequency));
    } else if (ownershipType.equals(PARTNER)) {
      assessmentDetail.setPartnerAmount(PoundSterling.ofPennies(assessmentAmount).toPounds());
      assessmentDetail.setPartnerFrequency(
          FrequencyMapper.mapPartnerFrequency(assessmentFrequency));
    }
    return assessmentDetail;
  }
}
