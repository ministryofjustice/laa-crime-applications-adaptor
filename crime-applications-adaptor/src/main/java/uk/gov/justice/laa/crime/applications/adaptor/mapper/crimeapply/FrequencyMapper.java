package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail.ApplicantFrequency;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FrequencyMapper {
    private static final String WEEK = "week";
    private static final String FORTNIGHT = "fortnight";
    private static final String FOUR_WEEKS = "four_weeks";
    private static final String MONTH = "month";
    private static final String ANNUAL = "annual";

    public static ApplicantFrequency mapFrequency(String frequency) {

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
