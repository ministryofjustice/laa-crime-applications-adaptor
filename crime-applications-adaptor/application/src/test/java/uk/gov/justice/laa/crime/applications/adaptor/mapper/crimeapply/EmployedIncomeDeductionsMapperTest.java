package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentIncomePayment;

class EmployedIncomeDeductionsMapperTest {
  private EmployedIncomeDeductionsMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new EmployedIncomeDeductionsMapper();
  }

  private List<EmploymentIncomePayment> getEmploymentDeductions() {
    List<EmploymentIncomePayment> employmentDeductions = new ArrayList<>();
    EmploymentIncomePayment applicantEmpDeduction = new EmploymentIncomePayment();
    applicantEmpDeduction.setIncomeTax(2000);
    applicantEmpDeduction.setNationalInsurance(1000);
    applicantEmpDeduction.setFrequency(EmploymentIncomePayment.Frequency.ANNUAL);
    applicantEmpDeduction.setOwnershipType(EmploymentIncomePayment.OwnershipType.APPLICANT);
    employmentDeductions.add(applicantEmpDeduction);

    EmploymentIncomePayment partnerEmpIncomePayment = new EmploymentIncomePayment();
    partnerEmpIncomePayment.setIncomeTax(1000);
    partnerEmpIncomePayment.setNationalInsurance(500);
    partnerEmpIncomePayment.setFrequency(EmploymentIncomePayment.Frequency.ANNUAL);
    partnerEmpIncomePayment.setOwnershipType(EmploymentIncomePayment.OwnershipType.PARTNER);
    employmentDeductions.add(partnerEmpIncomePayment);
    return employmentDeductions;
  }

  private List<AssessmentDetail> getExpectedDeductions() {
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();
    AssessmentDetail applicantIncomeTax = new AssessmentDetail();
    applicantIncomeTax.setApplicantAmount(new BigDecimal("20.00"));
    applicantIncomeTax.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
    applicantIncomeTax.setAssessmentDetailCode("TAX");
    assessmentDetails.add(applicantIncomeTax);

    AssessmentDetail partnerIncomeTax = new AssessmentDetail();
    partnerIncomeTax.setPartnerAmount(new BigDecimal("10.00"));
    partnerIncomeTax.setPartnerFrequency(AssessmentDetail.PartnerFrequency.ANNUALLY);
    partnerIncomeTax.setAssessmentDetailCode("TAX");
    assessmentDetails.add(partnerIncomeTax);

    AssessmentDetail applicantNI = new AssessmentDetail();
    applicantNI.setApplicantAmount(new BigDecimal("10.00"));
    applicantNI.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
    applicantNI.setAssessmentDetailCode("NI");
    assessmentDetails.add(applicantNI);

    AssessmentDetail partnerNI = new AssessmentDetail();
    partnerNI.setPartnerAmount(new BigDecimal("5.00"));
    partnerNI.setPartnerFrequency(AssessmentDetail.PartnerFrequency.ANNUALLY);
    partnerNI.setAssessmentDetailCode("NI");
    assessmentDetails.add(partnerNI);

    return assessmentDetails;
  }

  @Test
  void shouldMapEmploymentIncomePayments() {
    List<AssessmentDetail> actualDeductions =
        mapper.mapEmployedIncomeDeductions(getEmploymentDeductions());
    assertEquals(getExpectedDeductions(), actualDeductions);
  }
}
