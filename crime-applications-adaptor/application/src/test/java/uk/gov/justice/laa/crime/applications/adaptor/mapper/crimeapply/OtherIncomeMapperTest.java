package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OtherIncomeDetails;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.IncomePayment;

class OtherIncomeMapperTest {

  private OtherIncomeMapper otherIncomeMapper;
  private final String OTHER_INCOME_DETAILS = "Here are some details.";
  private static final int AMOUNT = 10000;
  // We receive the amount in pence, and need to convert it to pounds for MAAT.
  private static final BigDecimal OUTPUT_AMOUNT = new BigDecimal("100.00");

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
    assessmentDetail.setApplicantAmount(OUTPUT_AMOUNT);

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
}
