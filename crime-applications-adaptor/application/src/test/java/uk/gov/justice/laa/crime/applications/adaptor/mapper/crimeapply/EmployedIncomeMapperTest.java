package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentIncomePayment;

public class EmployedIncomeMapperTest {

  private EmployedIncomeMapper employedIncomeMapper;
  private static final int AMOUNT = 12550;
  // We receive the amount in pence, and need to convert it to pounds for MAAT.
  private static final BigDecimal OUTPUT_AMOUNT = new BigDecimal("125.50");

  @BeforeEach
  void setUp() {
    employedIncomeMapper = new EmployedIncomeMapper();
  }

  private List<EmploymentIncomePayment> getEmploymentIncomePayments() {
    List<EmploymentIncomePayment> employmentIncomePayments = new ArrayList<>();
    EmploymentIncomePayment applicantEmpIncomePayment = new EmploymentIncomePayment();
    applicantEmpIncomePayment.setAmount(AMOUNT);
    applicantEmpIncomePayment.setFrequency(EmploymentIncomePayment.Frequency.ANNUAL);
    applicantEmpIncomePayment.setOwnershipType(EmploymentIncomePayment.OwnershipType.APPLICANT);
    employmentIncomePayments.add(applicantEmpIncomePayment);

    EmploymentIncomePayment partnerEmpIncomePayment = new EmploymentIncomePayment();
    partnerEmpIncomePayment.setAmount(AMOUNT);
    partnerEmpIncomePayment.setFrequency(EmploymentIncomePayment.Frequency.ANNUAL);
    partnerEmpIncomePayment.setOwnershipType(EmploymentIncomePayment.OwnershipType.PARTNER);
    employmentIncomePayments.add(partnerEmpIncomePayment);
    return employmentIncomePayments;
  }

  private List<AssessmentDetail> getExpectedAssessmentDetails() {
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();
    AssessmentDetail applicantAssessmentDetail = new AssessmentDetail();
    applicantAssessmentDetail.setApplicantAmount(OUTPUT_AMOUNT);
    applicantAssessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
    applicantAssessmentDetail.setAssessmentDetailCode("EMP_INC");
    assessmentDetails.add(applicantAssessmentDetail);

    AssessmentDetail partnerAssessmentDetail = new AssessmentDetail();
    partnerAssessmentDetail.setPartnerAmount(OUTPUT_AMOUNT);
    partnerAssessmentDetail.setPartnerFrequency(AssessmentDetail.PartnerFrequency.ANNUALLY);
    partnerAssessmentDetail.setAssessmentDetailCode("EMP_INC");
    assessmentDetails.add(partnerAssessmentDetail);
    return assessmentDetails;
  }

  @Test
  void shouldMapEmploymentIncomePayments() {
    List<AssessmentDetail> actualAssessmentDetail =
        employedIncomeMapper.mapEmployedIncome(getEmploymentIncomePayments());
    assertEquals(getExpectedAssessmentDetails(), actualAssessmentDetail);
  }
}
