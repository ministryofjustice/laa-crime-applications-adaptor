package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OtherIncomeDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.IncomePayment;

class OtherIncomeMapperTest {

  private OtherIncomeMapper otherIncomeMapper;
  private final String OTHER_INCOME_DETAILS = "Here are some details.";
  private static final int AMOUNT = 100;

  @BeforeEach
  void setUp() {
    otherIncomeMapper = new OtherIncomeMapper();
  }

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
    assessmentDetail.setApplicantAmount(new BigDecimal(AMOUNT));

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
    otherIncome.setPaymentType(IncomePayment.PaymentType.STUDENT_LOAN_GRANT);
    List<IncomePayment> income = List.of(otherIncome);

    String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nStudent grant or loan";
    String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

    assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
  }

  @Test
  void shouldAddOtherIncomeBoardFromFamilyToNotes() {
    IncomePayment otherIncome = getOtherIncomeObject();
    otherIncome.setPaymentType(IncomePayment.PaymentType.BOARD_FROM_FAMILY);
    List<IncomePayment> income = List.of(otherIncome);

    String expectedOtherIncomeNotes =
        OTHER_INCOME_DETAILS + "\nBoard from family members living with your client";
    String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

    assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
  }

  @Test
  void shouldAddOtherIncomeRentToNotes() {
    IncomePayment otherIncome = getOtherIncomeObject();
    otherIncome.setPaymentType(IncomePayment.PaymentType.RENT);
    List<IncomePayment> income = List.of(otherIncome);

    String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nRent from a tenant";
    String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

    assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
  }

  @Test
  void shouldAddOtherIncomeFriendsAndFamilyToNotes() {
    IncomePayment otherIncome = getOtherIncomeObject();
    otherIncome.setPaymentType(IncomePayment.PaymentType.FROM_FRIENDS_RELATIVES);
    List<IncomePayment> income = List.of(otherIncome);

    String expectedOtherIncomeNotes = OTHER_INCOME_DETAILS + "\nMoney from friends or family";
    String actualOtherIncomeNotes = otherIncomeMapper.mapOtherIncomeNotes(income);

    assertEquals(expectedOtherIncomeNotes, actualOtherIncomeNotes);
  }
}
