package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OtherIncomeDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.IncomePayment;
import uk.gov.justice.laa.crime.applications.adaptor.util.FrequencyMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OtherIncomeMapper {
    private static final String STUDENT_LOAN_GRANT = "Student loan grant";
    private static final String BOARD_FROM_FAMILY = "Board from family";
    private static final String RENT = "Rent";
    private static final String FROM_FRIENDS_RELATIVES = "From Friends and Relatives";
    private static final String FINANCIAL_SUPPORT_WITH_ACCESS = "Financial support with access";

    public List<AssessmentDetail> mapOtherIncome(List<IncomePayment> otherIncome) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (Objects.nonNull(otherIncome)) {
            for (IncomePayment other : otherIncome) {
                String incomeType = other.getPaymentType().value();
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

    public String mapOtherIncomeNotes(List<IncomePayment> otherIncome) {
        List<String> otherBenefitNotes = new ArrayList<>();
        if (Objects.isNull(otherIncome)) {
            return StringUtils.EMPTY;
        }

        for (IncomePayment other : otherIncome) {
            if (Objects.nonNull(other.getDetails())) {
                otherBenefitNotes.add(other.getDetails().toString());
            }

            String incomeType = other.getPaymentType().value();
            OtherIncomeDetails otherIncomeDetail = OtherIncomeDetails.findByValue(incomeType);
            String note;
            switch (otherIncomeDetail) {
                case STUDENT_LOAN_GRANT -> note = STUDENT_LOAN_GRANT;
                case BOARD_FROM_FAMILY -> note = BOARD_FROM_FAMILY;
                case RENT -> note = RENT;
                case FROM_FRIENDS_RELATIVES -> note = FROM_FRIENDS_RELATIVES;
                case FINANCIAL_SUPPORT_WITH_ACCESS -> note = FINANCIAL_SUPPORT_WITH_ACCESS;
                default -> note = StringUtils.EMPTY;
            }
            if (!StringUtils.EMPTY.equals(note)) {
                otherBenefitNotes.add(note);
            }
        }
        return String.join("\n", otherBenefitNotes);
    }
}
