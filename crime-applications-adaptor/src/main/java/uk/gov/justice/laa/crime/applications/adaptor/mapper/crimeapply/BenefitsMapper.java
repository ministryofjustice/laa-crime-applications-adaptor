package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.maat.Benefit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenefitsMapper {
    private static final Map<String, String> ASSESSMENT_BENEFIT_CODES = new HashMap<>();

    public BenefitsMapper() {
        ASSESSMENT_BENEFIT_CODES.put("child", "CHILD_BEN");
        ASSESSMENT_BENEFIT_CODES.put("working_or_child_tax_credit", "TAX_CRED");
        ASSESSMENT_BENEFIT_CODES.put("incapacity", "INCAP_BEN");
        ASSESSMENT_BENEFIT_CODES.put("industrial_injuries_disablement", "INJ_BEN");
        ASSESSMENT_BENEFIT_CODES.put("other", "OTHER_BEN");
    }

    public List<AssessmentDetail> mapBenefits(List<Benefit> benefits) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (benefits != null) {
            for (Benefit benefit : benefits) {
                AssessmentDetail assessmentDetail = new AssessmentDetail();

                String benefitType = benefit.getType().value();

                // We don't have a universal credit type in MAAT, so set it to 'other'
                if (benefitType.equals("universal_credit")) {
                    benefitType = "other";
                }

                // We don't have a jsa type in MAAT, so set it to 'other'
                if (benefitType.equals("jsa")) {
                    benefitType = "other";
                }

                assessmentDetail.setAssessmentDetailCode(ASSESSMENT_BENEFIT_CODES.get(benefitType));
                assessmentDetail.setApplicantAmount(new BigDecimal(benefit.getAmount()));
                assessmentDetail.setApplicantFrequency(mapFrequency(benefit.getFrequency()));

                assessmentDetails.add(assessmentDetail);
            }
        }

        return assessmentDetails;
    }

    public AssessmentDetail.ApplicantFrequency mapFrequency(Benefit.Frequency crimeApplyFrequency) {
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

    public String mapOtherBenefitNotes(List<Benefit> benefits) {
        StringBuilder sb = new StringBuilder();

        if (benefits != null) {
            for (Benefit benefit : benefits) {
                if (benefit.getDetails() != null) {
                    sb.append("\n" + benefit.getDetails());
                }

                // We don't have a universal credit type in MAAT, so append it to the notes
                if (benefit.getType().value().equals("universal_credit")) {
                    sb.append("\nUniversal Credit");
                }

                // We don't have a jsa type in MAAT, so append it to the notes
                if (benefit.getType().value().equals("jsa")) {
                    sb.append("\nJSA");
                }
            }
        }

        String otherBenefitNotes = sb.toString();

        return otherBenefitNotes.trim();
    }
}
