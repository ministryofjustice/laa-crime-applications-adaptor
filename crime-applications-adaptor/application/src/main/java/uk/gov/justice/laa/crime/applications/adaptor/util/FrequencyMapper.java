package uk.gov.justice.laa.crime.applications.adaptor.util;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.AssessmentDetail.ApplicantFrequency;

@UtilityClass
public class FrequencyMapper {
  private static final String WEEK = "week";
  private static final String FORTNIGHT = "fortnight";
  private static final String FOUR_WEEKS = "four_weeks";
  private static final String MONTH = "month";
  private static final String ANNUAL = "annual";

  public ApplicantFrequency mapFrequency(String frequency) {

    switch (frequency) {
      case WEEK -> {
        return ApplicantFrequency.WEEKLY;
      }
      case FORTNIGHT -> {
        return ApplicantFrequency._2_WEEKLY;
      }
      case FOUR_WEEKS -> {
        return ApplicantFrequency._4_WEEKLY;
      }
      case MONTH -> {
        return ApplicantFrequency.MONTHLY;
      }
      case ANNUAL -> {
        return ApplicantFrequency.ANNUALLY;
      }
      default -> {
        return null;
      }
    }
  }
}
