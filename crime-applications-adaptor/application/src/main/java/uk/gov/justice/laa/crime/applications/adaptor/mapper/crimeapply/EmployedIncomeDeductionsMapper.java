package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.ArrayList;
import java.util.List;
import uk.gov.justice.laa.crime.applications.adaptor.util.AssessmentDetailMapperUtil;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentIncomePayment;

public class EmployedIncomeDeductionsMapper {

  private static final String INCOME_TAX = "TAX";
  private static final String NI_NUMBER = "NI";

  public List<AssessmentDetail> mapEmployedIncomeDeductions(
      List<EmploymentIncomePayment> employmentIncomePayments) {
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();
    mapIncomeTaxDeduction(employmentIncomePayments, assessmentDetails);
    mapNINumberDeduction(employmentIncomePayments, assessmentDetails);
    return assessmentDetails;
  }

  private void mapNINumberDeduction(
      List<EmploymentIncomePayment> employmentIncomePayments,
      List<AssessmentDetail> assessmentDetails) {
    employmentIncomePayments.forEach(
        employmentIncomePayment -> {
          AssessmentDetail assessmentDetail =
              AssessmentDetailMapperUtil.mapMeansAssessmentDetails(
                  employmentIncomePayment.getOwnershipType().value(),
                  NI_NUMBER,
                  employmentIncomePayment.getNationalInsurance(),
                  employmentIncomePayment.getFrequency().value());
          assessmentDetails.add(assessmentDetail);
        });
  }

  private void mapIncomeTaxDeduction(
      List<EmploymentIncomePayment> employmentIncomePayments,
      List<AssessmentDetail> assessmentDetails) {
    employmentIncomePayments.forEach(
        employmentIncomePayment -> {
          AssessmentDetail assessmentDetail =
              AssessmentDetailMapperUtil.mapMeansAssessmentDetails(
                  employmentIncomePayment.getOwnershipType().value(),
                  INCOME_TAX,
                  employmentIncomePayment.getIncomeTax(),
                  employmentIncomePayment.getFrequency().value());
          assessmentDetails.add(assessmentDetail);
        });
  }
}
