package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.ArrayList;
import java.util.List;
import uk.gov.justice.laa.crime.applications.adaptor.util.AssessmentDetailMapperUtil;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentIncomePayment;

public class EmployedIncomeMapper {
  private static final String EMP_INC = "EMP_INC";

  public List<AssessmentDetail> mapEmployedIncome(
      List<EmploymentIncomePayment> employmentIncomePayments) {
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();

    employmentIncomePayments.forEach(
        employmentIncomePayment -> {
          AssessmentDetail assessmentDetail =
              AssessmentDetailMapperUtil.mapMeansAssessmentDetails(
                  employmentIncomePayment.getOwnershipType().value(),
                  EMP_INC,
                  employmentIncomePayment.getAmount(),
                  employmentIncomePayment.getFrequency().value());
          assessmentDetails.add(assessmentDetail);
        });

    return assessmentDetails;
  }
}
