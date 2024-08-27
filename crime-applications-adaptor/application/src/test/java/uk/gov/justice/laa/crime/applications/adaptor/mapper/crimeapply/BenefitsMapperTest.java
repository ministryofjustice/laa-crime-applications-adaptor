package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.enums.BenefitDetails;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.IncomeBenefit;

class BenefitsMapperTest {

  private BenefitsMapper benefitsMapper;
  private static final int AMOUNT = 12550;
  // We receive the amount in pence, and need to convert it to pounds for MAAT.
  private static final BigDecimal OUTPUT_AMOUNT = new BigDecimal("125.50");
  private final String DETAILS = "Here are some details about the benefit";

  @BeforeEach
  void setUp() {
    benefitsMapper = new BenefitsMapper();
  }

  private IncomeBenefit getBenefitObject() {
    IncomeBenefit benefit = new IncomeBenefit();
    benefit.setAmount(AMOUNT);
    benefit.setDetails(DETAILS);
    benefit.setPaymentType(IncomeBenefit.PaymentType.CHILD);
    benefit.setFrequency(IncomeBenefit.Frequency.ANNUAL);

    return benefit;
  }

  private AssessmentDetail getAssessmentDetailObject() {
    AssessmentDetail assessmentDetail = new AssessmentDetail();
    assessmentDetail.setAssessmentDetailCode(BenefitDetails.CHILD.getCode());
    assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
    assessmentDetail.setApplicantAmount(OUTPUT_AMOUNT);

    return assessmentDetail;
  }

  @Test
  void shouldMapChildToChildBen() {
    IncomeBenefit benefit = getBenefitObject();
    List<IncomeBenefit> benefits = List.of(benefit);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapWorkingOrChildTaxCreditToTaxCred() {
    IncomeBenefit benefit = getBenefitObject();
    benefit.setPaymentType(IncomeBenefit.PaymentType.WORKING_OR_CHILD_TAX_CREDIT);
    List<IncomeBenefit> benefits = List.of(benefit);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(BenefitDetails.WORKING_OR_CHILD_TAX_CREDIT.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapIncapacityToIncapBen() {
    IncomeBenefit benefit = getBenefitObject();
    benefit.setPaymentType(IncomeBenefit.PaymentType.INCAPACITY);
    List<IncomeBenefit> benefits = List.of(benefit);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(BenefitDetails.INCAPACITY.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapIndustryInjuriesDisabledToInjBen() {
    IncomeBenefit benefit = getBenefitObject();
    benefit.setPaymentType(IncomeBenefit.PaymentType.INDUSTRIAL_INJURIES_DISABLEMENT);
    List<IncomeBenefit> benefits = List.of(benefit);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(
        BenefitDetails.INDUSTRIAL_INJURIES_DISABLEMENT.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapOtherToOtherBen() {
    IncomeBenefit benefit = getBenefitObject();
    benefit.setPaymentType(IncomeBenefit.PaymentType.OTHER);
    List<IncomeBenefit> benefits = List.of(benefit);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(BenefitDetails.OTHER.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapUniversalCreditToOtherBen() {
    IncomeBenefit benefit = getBenefitObject();
    benefit.setPaymentType(IncomeBenefit.PaymentType.UNIVERSAL_CREDIT);
    List<IncomeBenefit> benefits = List.of(benefit);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(BenefitDetails.OTHER.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapJsaToOtherBen() {
    IncomeBenefit benefit = getBenefitObject();
    benefit.setPaymentType(IncomeBenefit.PaymentType.JSA);
    List<IncomeBenefit> benefits = List.of(benefit);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(BenefitDetails.OTHER.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldAddBenefitDetailsToNotes() {
    IncomeBenefit benefit = getBenefitObject();
    List<IncomeBenefit> benefits = List.of(benefit);

    String actualBenefitNotes = benefitsMapper.mapOtherBenefitNotes(benefits);

    assertEquals(DETAILS, actualBenefitNotes);
  }

  @Test
  void shouldAddUniversalCreditToNotes() {
    IncomeBenefit benefit = getBenefitObject();
    benefit.setPaymentType(IncomeBenefit.PaymentType.UNIVERSAL_CREDIT);
    List<IncomeBenefit> benefits = List.of(benefit);

    String expectedBenefitNotes = DETAILS + "\nUniversal Credit";
    String actualBenefitNotes = benefitsMapper.mapOtherBenefitNotes(benefits);

    assertEquals(expectedBenefitNotes, actualBenefitNotes);
  }
}
