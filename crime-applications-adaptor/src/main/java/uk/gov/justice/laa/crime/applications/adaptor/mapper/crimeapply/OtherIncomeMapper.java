package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.enums.OtherIncomeDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.OtherIncome;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OtherIncomeMapper {
    private static final String STUDENT = "Student";
    private static final String BOARD_FROM_FAMILY = "Board from family";
    private static final String RENT = "Rent";
    private static final String FRIENDS_AND_FAMILY = "Friends and Family";

    public List<AssessmentDetail> mapOtherIncome(List<OtherIncome> otherIncome) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (Objects.nonNull(otherIncome)) {
            for (OtherIncome other : otherIncome) {
                String incomeType = other.getType().value();
                OtherIncomeDetails otherIncomeDetail = OtherIncomeDetails.findByValue(incomeType);
                AssessmentDetail assessmentDetail = new AssessmentDetail();
                assessmentDetail.setAssessmentDetailCode(otherIncomeDetail.getCode());
                assessmentDetail.setApplicantAmount(new BigDecimal(other.getAmount()));
                mapFrequency(assessmentDetail, other.getFrequency());
                assessmentDetails.add(assessmentDetail);
            }
        }

        return assessmentDetails;
    }

    private void mapFrequency(AssessmentDetail assessmentDetail, OtherIncome.Frequency frequency) {
        switch (frequency) {
            case WEEK -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.WEEKLY);
            case FORTNIGHT -> assessmentDetail.setApplicantFrequency( AssessmentDetail.ApplicantFrequency._2_WEEKLY);
            case FOUR_WEEKS -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency._4_WEEKLY);
            case MONTH -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.MONTHLY);
            case ANNUAL -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
        }
    }
    public String mapOtherIncomeNotes(List<OtherIncome> otherIncome) {
        StringBuilder otherNote = new StringBuilder();

        if (Objects.nonNull(otherIncome)) {
            for (OtherIncome other : otherIncome) {
                if (Objects.nonNull(other.getDetails())) {
                    otherNote.append("\n");
                    otherNote.append(other.getDetails());
                }

                String incomeType = other.getType().value();
                OtherIncomeDetails otherIncomeDetail = OtherIncomeDetails.findByValue(incomeType);
                String note;
                switch (otherIncomeDetail){
                    case STUDENT -> note = "\n" + STUDENT;
                    case BOARD_FROM_FAMILY -> note = "\n" + BOARD_FROM_FAMILY;
                    case RENT -> note = "\n" + RENT;
                    case FRIENDS_AND_FAMILY -> note = "\n" + FRIENDS_AND_FAMILY;
                    default -> note = "";
                }

                otherNote.append(note);
            }
        }

        return otherNote.toString().trim();
    }
}
