package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OtherIncomeDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.OtherIncome;
import uk.gov.justice.laa.crime.applications.adaptor.util.FrequencyMapper;

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
                assessmentDetail.setApplicantFrequency(FrequencyMapper.mapFrequency(other.getFrequency().value()));
                assessmentDetails.add(assessmentDetail);
            }
        }

        return assessmentDetails;
    }

    public String mapOtherIncomeNotes(List<OtherIncome> otherIncome) {
        List<String> otherBenefitNotes = new ArrayList<>();
        if (Objects.isNull(otherIncome)) {
            return StringUtils.EMPTY;
        }

        for (OtherIncome other : otherIncome) {
            if (Objects.nonNull(other.getDetails())) {
                otherBenefitNotes.add(other.getDetails().toString());
            }

            String incomeType = other.getType().value();
            OtherIncomeDetails otherIncomeDetail = OtherIncomeDetails.findByValue(incomeType);
            String note;
            switch (otherIncomeDetail) {
                case STUDENT -> note = STUDENT;
                case BOARD_FROM_FAMILY -> note = BOARD_FROM_FAMILY;
                case RENT -> note = RENT;
                case FRIENDS_AND_FAMILY -> note = FRIENDS_AND_FAMILY;
                default -> note = StringUtils.EMPTY;
            }
            if (!StringUtils.EMPTY.equals(note)) {
                otherBenefitNotes.add(note);
            }
        }
        return String.join("\n", otherBenefitNotes);
    }
}
