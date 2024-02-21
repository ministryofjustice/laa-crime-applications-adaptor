package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.OtherIncome;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherIncomeMapper {
    private static final Map<String, String> OTHER_INCOME_CODES = new HashMap<>();

    public OtherIncomeMapper() {
        OTHER_INCOME_CODES.put("private_pension", "PRIV_PENS");
        OTHER_INCOME_CODES.put("state_pension", "STATE_PEN");
        OTHER_INCOME_CODES.put("maintenance", "MAINT_INC");
        OTHER_INCOME_CODES.put("interest", "SAVINGS");
        OTHER_INCOME_CODES.put("other", "OTHER_INC");
    }

    public List<AssessmentDetail> mapOtherIncome(List<OtherIncome> otherIncome) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (otherIncome != null) {
            for (OtherIncome other : otherIncome) {
                String incomeType = other.getType().value();

                // We don't have these income types in MAAT, so set them to 'other'
                if (incomeType.equals("student") || incomeType.equals("board_from_family") || incomeType.equals("rent")
                        || incomeType.equals("friends_and_family")) {
                    incomeType = "other";
                }

                AssessmentDetail assessmentDetail = new AssessmentDetail();
                assessmentDetail.setAssessmentDetailCode(OTHER_INCOME_CODES.get(incomeType));
                assessmentDetail.setApplicantAmount(new BigDecimal(other.getAmount()));
                assessmentDetail.setApplicantFrequency(mapFrequency(other.getFrequency()));

                assessmentDetails.add(assessmentDetail);
            }
        }

        return assessmentDetails;
    }

    public AssessmentDetail.ApplicantFrequency mapFrequency(OtherIncome.Frequency crimeApplyFrequency) {
        AssessmentDetail.ApplicantFrequency frequency = null;
        switch (crimeApplyFrequency) {
            case WEEK -> frequency = AssessmentDetail.ApplicantFrequency.WEEKLY;
            case FORTNIGHT -> frequency = AssessmentDetail.ApplicantFrequency._2_WEEKLY;
            case FOUR_WEEKS -> frequency = AssessmentDetail.ApplicantFrequency._4_WEEKLY;
            case MONTH -> frequency = AssessmentDetail.ApplicantFrequency.MONTHLY;
            case ANNUAL -> frequency = AssessmentDetail.ApplicantFrequency.ANNUALLY;
        }

        return frequency;
    }

    public String mapOtherIncomeNotes(List<OtherIncome> otherIncome) {
        StringBuilder sb = new StringBuilder();

        if (otherIncome != null) {
            for (OtherIncome other : otherIncome) {
                String incomeType = other.getType().value();

                if (other.getDetails() != null) {
                    sb.append("\n" + other.getDetails());
                }

                // We don't have these income types in MAAT, so append them to the notes
                if (incomeType.equals("student")) {
                    sb.append("\nStudent");
                }

                if (incomeType.equals("board_from_family")) {
                    sb.append("\nBoard from family");
                }

                if (incomeType.equals("rent")) {
                    sb.append("\nRent");
                }

                if (incomeType.equals("friends_and_family")) {
                    sb.append("\nFriends and family");
                }
            }
        }

        String otherIncomeNotes = sb.toString();

        return otherIncomeNotes.trim();
    }
}
