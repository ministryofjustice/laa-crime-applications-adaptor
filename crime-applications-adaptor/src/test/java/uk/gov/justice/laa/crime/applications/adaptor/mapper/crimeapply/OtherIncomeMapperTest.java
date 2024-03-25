package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OtherIncomeDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.IncomePayment;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OtherIncomeMapperTest {

    private OtherIncomeMapper otherIncomeMapper;
    private final String OTHER_INCOME_DETAILS = "Here are some details.";
    private static final int AMOUNT = 100;

    @BeforeEach
    void setUp() { otherIncomeMapper = new OtherIncomeMapper(); }

    private IncomePayment getOtherIncomeObject() {
        IncomePayment otherIncome = new IncomePayment();
        otherIncome.setDetails(OTHER_INCOME_DETAILS);
        otherIncome.setAmount(AMOUNT);
        otherIncome.setPaymentType(IncomePayment.PaymentType.MAINTENANCE);
        otherIncome.setFrequency(IncomePayment.Frequency.FORTNIGHT);

        return otherIncome;
    }

    private AssessmentDetail getAssessmentDetailObject() {
        AssessmentDetail assessmentDetail = new AssessmentDetail();
        assessmentDetail.setAssessmentDetailCode(OtherIncomeDetails.MAINTENANCE.getCode());
        assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency._2_WEEKLY);
        assessmentDetail.setApplicantAmount(new BigDecimal (AMOUNT));

        return assessmentDetail;
    }

    @Test
    void shouldMapOtherIncomeToAssessmentDetail() {
        IncomePayment otherIncome = getOtherIncomeObject();
        List<IncomePayment> income = List.of(otherIncome);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = otherIncomeMapper.mapOtherIncome(income);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldAddOtherIncomeDetailsToNotes() {
        IncomePayment otherIncome = getOtherIncomeObject();
        List<IncomePayment> income = List.of(otherIncome);

        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(OTHER_INCOME_DETAILS, actualOtherIncomeNotes);
    }

    @Test
    void shouldAddOtherIncomeStudentToNotes() {
        IncomePayment otherIncome = getOtherIncomeObject();
        otherIncome.setPaymentType(IncomePayment.PaymentType.STUDENT);
        List<IncomePayment> income = List.of(otherIncome);

        String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nStudent";
        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
    }

    @Test
    void shouldAddOtherIncomeBoardFromFamilyToNotes() {
        IncomePayment otherIncome = getOtherIncomeObject();
        otherIncome.setPaymentType(IncomePayment.PaymentType.BOARD_FROM_FAMILY);
        List<IncomePayment> income = List.of(otherIncome);

        String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nBoard from family";
        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
    }

    @Test
    void shouldAddOtherIncomeRentToNotes() {
        IncomePayment otherIncome = getOtherIncomeObject();
        otherIncome.setPaymentType(IncomePayment.PaymentType.RENT);
        List<IncomePayment> income = List.of(otherIncome);

        String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nRent";
        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
    }

    @Test
    void shouldAddOtherIncomeFriendsAndFamilyToNotes() {
        IncomePayment otherIncome = getOtherIncomeObject();
        otherIncome.setPaymentType(IncomePayment.PaymentType.FRIENDS_AND_FAMILY);
        List<IncomePayment> income = List.of(otherIncome);

        String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nFriends and Family";
        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
    }
}
