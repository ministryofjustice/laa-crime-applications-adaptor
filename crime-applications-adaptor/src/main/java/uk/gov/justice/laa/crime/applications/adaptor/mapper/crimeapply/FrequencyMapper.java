package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;

public class FrequencyMapper {
    private static final String WEEK = "week";
    private static final String FORTNIGHT = "fortnight";
    private static final String FOUR_WEEKS = "four_weeks";
    private static final String MONTH = "month";
    private static final String ANNUAL = "annual";

    public void mapFrequency(String frequency, AssessmentDetail assessmentDetail){

        switch (frequency){
            case WEEK -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.WEEKLY);
            case FORTNIGHT -> assessmentDetail.setApplicantFrequency( AssessmentDetail.ApplicantFrequency._2_WEEKLY);
            case FOUR_WEEKS -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency._4_WEEKLY);
            case MONTH -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.MONTHLY);
            case ANNUAL -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
        }
    }
}
