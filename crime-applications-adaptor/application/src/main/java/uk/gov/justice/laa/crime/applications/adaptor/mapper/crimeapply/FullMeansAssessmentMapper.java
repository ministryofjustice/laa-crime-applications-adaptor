package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.FullMeansAssessment;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Means;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Outgoing;

class FullMeansAssessmentMapper {

  private final OutgoingsMapper outgoingsMapper = new OutgoingsMapper();

  @NotNull FullMeansAssessment map(Means meansDetails) {

    FullMeansAssessment fullMeansAssessment = new FullMeansAssessment();
    EmployedIncomeDeductionsMapper empIncomeDeductions = new EmployedIncomeDeductionsMapper();
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();

    if (!hasDeductions(meansDetails)) {
      return fullMeansAssessment;
    }
    if (Objects.nonNull(meansDetails.getOutgoingsDetails())) {
      List<Outgoing> outgoingList = meansDetails.getOutgoingsDetails().getOutgoings();
      List<AssessmentDetail> outgoings = outgoingsMapper.mapOutgoings(outgoingList);
      fullMeansAssessment.setOtherHousingNote(
          outgoingsMapper.mapOtherHousingFeesNotes(outgoingList));
      assessmentDetails.addAll(outgoings);
    }

    if (Objects.nonNull(meansDetails.getIncomeDetails().getEmploymentIncomePayments())) {
      List<AssessmentDetail> empIncomeDeductionList =
          empIncomeDeductions.mapEmployedIncomeDeductions(
              meansDetails.getIncomeDetails().getEmploymentIncomePayments());
      assessmentDetails.addAll(empIncomeDeductionList);
    }

    fullMeansAssessment.setAssessmentDetails(assessmentDetails);

    return fullMeansAssessment;
  }

  private boolean hasDeductions(Means meansDetails) {
    return (Objects.nonNull(meansDetails.getOutgoingsDetails())
            && Objects.nonNull(meansDetails.getOutgoingsDetails().getOutgoings()))
        || (Objects.nonNull(meansDetails.getIncomeDetails())
            && Objects.nonNull(meansDetails.getIncomeDetails().getEmploymentIncomePayments()));
  }
}
