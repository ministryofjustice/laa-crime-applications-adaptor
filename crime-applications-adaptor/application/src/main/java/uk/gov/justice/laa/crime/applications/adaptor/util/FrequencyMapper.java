package uk.gov.justice.laa.crime.applications.adaptor.util;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail.ApplicantFrequency;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail.PartnerFrequency;

@UtilityClass
public class FrequencyMapper {
  private static final String WEEK = "week";
  private static final String FORTNIGHT = "fortnight";
  private static final String FOUR_WEEKS = "four_weeks";
  private static final String MONTH = "month";
  private static final String ANNUAL = "annual";

  public ApplicantFrequency mapApplicantFrequency(String frequency) {

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

  public PartnerFrequency mapPartnerFrequency(String frequency) {

    switch (frequency) {
      case WEEK -> {
        return PartnerFrequency.WEEKLY;
      }
      case FORTNIGHT -> {
        return PartnerFrequency._2_WEEKLY;
      }
      case FOUR_WEEKS -> {
        return PartnerFrequency._4_WEEKLY;
      }
      case MONTH -> {
        return PartnerFrequency.MONTHLY;
      }
      case ANNUAL -> {
        return PartnerFrequency.ANNUALLY;
      }
      default -> {
        return null;
      }
    }
  }
}
