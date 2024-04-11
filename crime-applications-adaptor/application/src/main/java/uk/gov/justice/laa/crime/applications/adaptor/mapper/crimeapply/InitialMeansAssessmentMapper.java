package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.*;
import javax.validation.constraints.NotNull;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.InitialMeansAssessment;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.IncomeDetails;

class InitialMeansAssessmentMapper {

  private final ChildWeightingMapper childWeightingMapper = new ChildWeightingMapper();
  private final BenefitsMapper benefitsMapper = new BenefitsMapper();
  private final OtherIncomeMapper otherIncomeMapper = new OtherIncomeMapper();

  @NotNull InitialMeansAssessment map(IncomeDetails crimeApplyIncomeDetails) {

    InitialMeansAssessment initialMeansAssessment = new InitialMeansAssessment();
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();

    if (Objects.isNull(crimeApplyIncomeDetails)) {
      return initialMeansAssessment;
    }
    // Benefits and Other Income will be merged into a single ArrayList
    if (Objects.nonNull(crimeApplyIncomeDetails.getIncomeBenefits())) {
      List<AssessmentDetail> benefits =
          benefitsMapper.mapBenefits(crimeApplyIncomeDetails.getIncomeBenefits());
      assessmentDetails.addAll(benefits);
    }
    if (Objects.nonNull(crimeApplyIncomeDetails.getIncomePayments())) {
      List<AssessmentDetail> otherIncome =
          otherIncomeMapper.mapOtherIncome(crimeApplyIncomeDetails.getIncomePayments());
      assessmentDetails.addAll(otherIncome);
    }

    initialMeansAssessment.setAssessmentDetails(assessmentDetails);
    initialMeansAssessment.setOtherBenefitNote(
        benefitsMapper.mapOtherBenefitNotes(crimeApplyIncomeDetails.getIncomeBenefits()));
    initialMeansAssessment.setOtherIncomeNote(
        otherIncomeMapper.mapOtherIncomeNotes(crimeApplyIncomeDetails.getIncomePayments()));
    initialMeansAssessment.setChildWeighting(
        childWeightingMapper.mapChildWeighting(crimeApplyIncomeDetails.getDependants()));

    return initialMeansAssessment;
  }
}
