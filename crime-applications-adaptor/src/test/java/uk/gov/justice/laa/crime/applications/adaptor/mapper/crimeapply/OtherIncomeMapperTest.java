package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OtherIncomeDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.OtherIncome;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtherIncomeMapperTest {

    private OtherIncomeMapper otherIncomeMapper;
    private final String OTHER_INCOME_DETAILS = "Here are some details.";
    private final int AMOUNT = 100;

    @BeforeEach
    void setUp() { otherIncomeMapper = new OtherIncomeMapper(); }

    private OtherIncome getOtherIncomeObject() {
        OtherIncome otherIncome = new OtherIncome();
        otherIncome.setDetails(OTHER_INCOME_DETAILS);
        otherIncome.setAmount(AMOUNT);
        otherIncome.setType(OtherIncome.Type.MAINTENANCE);
        otherIncome.setFrequency(OtherIncome.Frequency.FORTNIGHT);

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
        OtherIncome otherIncome = getOtherIncomeObject();
        List<OtherIncome> income = List.of(otherIncome);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = otherIncomeMapper.mapOtherIncome(income);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldAddOtherIncomeDetailsToNotes() {
        OtherIncome otherIncome = getOtherIncomeObject();
        List<OtherIncome> income = List.of(otherIncome);

        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(OTHER_INCOME_DETAILS, actualOtherIncomeNotes);
    }

    @Test
    void shouldAddOtherIncomeStudentToNotes() {
        OtherIncome otherIncome = getOtherIncomeObject();
        otherIncome.setType(OtherIncome.Type.STUDENT);
        List<OtherIncome> income = List.of(otherIncome);

        String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nStudent";
        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
    }

    @Test
    void shouldAddOtherIncomeBoardFromFamilyToNotes() {
        OtherIncome otherIncome = getOtherIncomeObject();
        otherIncome.setType(OtherIncome.Type.BOARD_FROM_FAMILY);
        List<OtherIncome> income = List.of(otherIncome);

        String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nBoard from family";
        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
    }

    @Test
    void shouldAddOtherIncomeRentToNotes() {
        OtherIncome otherIncome = getOtherIncomeObject();
        otherIncome.setType(OtherIncome.Type.RENT);
        List<OtherIncome> income = List.of(otherIncome);

        String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nRent";
        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
    }

    @Test
    void shouldAddOtherIncomeFriendsAndFamilyToNotes() {
        OtherIncome otherIncome = getOtherIncomeObject();
        otherIncome.setType(OtherIncome.Type.FRIENDS_AND_FAMILY);
        List<OtherIncome> income = List.of(otherIncome);

        String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nFriends and Family";
        String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

        assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
    }
}
