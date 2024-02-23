package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.enums.BenefitDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Benefit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BenefitsMapper {

    private static final String UNIVERSAL_CREDIT = "Universal Credit";
    private static final String JSA = "JSA";
    public List<AssessmentDetail> mapBenefits(List<Benefit> benefits) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (Objects.nonNull(benefits)) {
            for (Benefit benefit : benefits) {
                AssessmentDetail assessmentDetail = new AssessmentDetail();
                String benefitType = benefit.getType().value();
                BenefitDetails benefitDetail = BenefitDetails.findByValue(benefitType);
                assessmentDetail.setAssessmentDetailCode(benefitDetail.getCode());
                assessmentDetail.setApplicantAmount(new BigDecimal(benefit.getAmount()));
                mapFrequency(assessmentDetail, benefit.getFrequency());
                assessmentDetails.add(assessmentDetail);
            }
        }
        return assessmentDetails;
    }

    private void mapFrequency(AssessmentDetail assessmentDetail, Benefit.Frequency frequency) {
        switch (frequency) {
            case WEEK -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.WEEKLY);
            case FORTNIGHT -> assessmentDetail.setApplicantFrequency( AssessmentDetail.ApplicantFrequency._2_WEEKLY);
            case FOUR_WEEKS -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency._4_WEEKLY);
            case MONTH -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.MONTHLY);
            case ANNUAL -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
        }
    }

    public String mapOtherBenefitNotes(List<Benefit> benefits) {
        StringBuilder otherNote = new StringBuilder();
        if (Objects.nonNull(benefits)) {
            for (Benefit benefit : benefits) {
                if (Objects.nonNull(benefit.getDetails())) {
                    otherNote.append("\n");
                    otherNote.append(benefit.getDetails().toString());
                }

                String benefitType = benefit.getType().value();
                BenefitDetails benefitDetail = BenefitDetails.findByValue(benefitType);
                if (benefitDetail.getValue().equals(BenefitDetails.UNIVERSAL_CREDIT.getValue())) {
                    otherNote.append("\n" + UNIVERSAL_CREDIT);
                }
               else if (benefitDetail.getValue().equals(BenefitDetails.JSA.getValue())) {
                    otherNote.append("\n" + JSA);
                }
            }
        }
        return otherNote.toString().trim();
    }
}
