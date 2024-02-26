package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.BenefitDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Benefit;
import uk.gov.justice.laa.crime.applications.adaptor.util.FrequencyMapper;

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
                assessmentDetail.setApplicantFrequency(FrequencyMapper.mapFrequency(benefit.getFrequency().value()));
                assessmentDetails.add(assessmentDetail);
            }
        }
        return assessmentDetails;
    }

    public String mapOtherBenefitNotes(List<Benefit> benefits) {
        List<String> otherBenefitNotes = new ArrayList<>();

        if (Objects.isNull(benefits)) {
            return StringUtils.EMPTY;
        }

        for (Benefit benefit : benefits) {
            if (Objects.nonNull(benefit.getDetails())) {
                otherBenefitNotes.add(benefit.getDetails().toString());
            }

            String benefitType = benefit.getType().value();
            BenefitDetails benefitDetail = BenefitDetails.findByValue(benefitType);
            if (benefitDetail.getValue().equals(BenefitDetails.UNIVERSAL_CREDIT.getValue())) {
                otherBenefitNotes.add(UNIVERSAL_CREDIT);
            } else if (benefitDetail.getValue().equals(BenefitDetails.JSA.getValue())) {
                otherBenefitNotes.add(JSA);
            }
        }

        return String.join("\n", otherBenefitNotes);
    }
}
