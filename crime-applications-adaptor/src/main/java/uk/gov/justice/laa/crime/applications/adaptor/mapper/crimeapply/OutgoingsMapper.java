package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Outgoing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutgoingsMapper {
    private static final Map<String, String> ASSESSMENT_OUTGOINGS_CODES = new HashMap<>();
    private static final Map<String, String> ASSESSMENT_HOUSING_CODES = new HashMap<>();

    public OutgoingsMapper() {
        ASSESSMENT_OUTGOINGS_CODES.put("council_tax", "COUNCIL");
        ASSESSMENT_OUTGOINGS_CODES.put("childcare", "CHILD_COST");
        ASSESSMENT_OUTGOINGS_CODES.put("maintenance", "MAINT_COST");
        ASSESSMENT_OUTGOINGS_CODES.put("legal_aid", "OTHER_LAC");

        ASSESSMENT_HOUSING_CODES.put("board_lodgings", "OTHER_HOUS");
        ASSESSMENT_HOUSING_CODES.put("rent", "RENT_MORT");
        ASSESSMENT_HOUSING_CODES.put("mortgage", "RENT_MORT");
    }

    public List<AssessmentDetail> mapOutgoings(List<Outgoing> outgoings, Object housingPaymentType) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (outgoings != null) {
            for (Outgoing outgoing : outgoings) {
                AssessmentDetail assessmentDetail = new AssessmentDetail();
                String outgoingsType = outgoing.getType().value();
                String maatDetailCode = ASSESSMENT_OUTGOINGS_CODES.get(outgoingsType);
                Integer amount = outgoing.getAmount();

                // Check housing payment type - this affects where 'housing' maps to
                if (outgoingsType.equals("housing") && housingPaymentType != null) {
                    maatDetailCode = ASSESSMENT_HOUSING_CODES.get(housingPaymentType);
                }

                assessmentDetail.setAssessmentDetailCode(maatDetailCode);
                assessmentDetail.setApplicantAmount(new BigDecimal(amount));
                assessmentDetail.setApplicantFrequency(mapFrequency(outgoing.getFrequency()));

                assessmentDetails.add(assessmentDetail);
            }
        }

        return assessmentDetails;
    }

    public AssessmentDetail.ApplicantFrequency mapFrequency(Outgoing.Frequency crimeApplyFrequency) {
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

    public String mapOtherHousingFeesNotes(List<Outgoing> outgoings, Object housingPaymentType) {
        StringBuilder sb = new StringBuilder();

        if (outgoings != null) {
            for (Outgoing outgoing : outgoings) {
                String outgoingsType = outgoing.getType().value();

                // Check housing payment type - this affects where 'housing' maps to
                if (outgoingsType.equals("housing") && housingPaymentType != null && housingPaymentType.equals("board_lodgings")) {
                    // If board_lodgings, we need to create a field for the notes associated with it
                    sb.append("\nBoard lodgings");

                    if (outgoing.getDetails() != null) {
                        sb.append("\n" + outgoing.getDetails());
                    }
                }
            }
        }

        String otherHousingFeesNotes = sb.toString();

        return otherHousingFeesNotes.trim();
    }
}
