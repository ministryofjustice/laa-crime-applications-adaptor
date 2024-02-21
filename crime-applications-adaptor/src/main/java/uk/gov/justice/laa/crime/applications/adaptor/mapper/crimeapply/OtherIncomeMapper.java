package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.OtherIncome;
import uk.gov.justice.laa.crime.applications.adaptor.util.NotesFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherIncomeMapper {
    private static final Map<String, String> OTHER_INCOME_CODES = new HashMap<>();
    private final List<String> codesNotInMAAT = new ArrayList();

    public OtherIncomeMapper() {
        OTHER_INCOME_CODES.put("private_pension", "PRIV_PENS");
        OTHER_INCOME_CODES.put("state_pension", "STATE_PEN");
        OTHER_INCOME_CODES.put("maintenance", "MAINT_INC");
        OTHER_INCOME_CODES.put("interest", "SAVINGS");
        OTHER_INCOME_CODES.put("other", "OTHER_INC");

        codesNotInMAAT.add("student");
        codesNotInMAAT.add("board_from_family");
        codesNotInMAAT.add("rent");
        codesNotInMAAT.add("friends_and_family");
    }

    public List<AssessmentDetail> mapOtherIncome(List<OtherIncome> otherIncome) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (otherIncome != null) {
            for (OtherIncome other : otherIncome) {
                String incomeType = other.getType().value();

                // If the assessment code isn't one we have in MAAT, set it to 'other'
                if (codesNotInMAAT.contains(incomeType)) {
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

                // If the assessment code isn't one we have in MAAT, make it human-readable and append to notes
                if (codesNotInMAAT.contains(incomeType)) {
                    String note = NotesFormatter.formatNote(incomeType);
                    sb.append("\n" + note);
                }
            }
        }

        String otherIncomeNotes = sb.toString();

        return otherIncomeNotes.trim();
    }
}
