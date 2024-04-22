package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.List;
import javax.validation.constraints.NotNull;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.FullMeansAssessment;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.OutgoingsDetails;

class FullMeansAssessmentMapper {

  private final OutgoingsMapper outgoingsMapper = new OutgoingsMapper();

  @NotNull FullMeansAssessment map(OutgoingsDetails crimeApplyOutgoingsDetails) {

    FullMeansAssessment fullMeansAssessment = new FullMeansAssessment();

    if (crimeApplyOutgoingsDetails == null) {
      return fullMeansAssessment;
    }
    List<AssessmentDetail> assessmentDetails =
        outgoingsMapper.mapOutgoings(crimeApplyOutgoingsDetails.getOutgoings());
    fullMeansAssessment.setAssessmentDetails(assessmentDetails);
    fullMeansAssessment.setOtherHousingNote(
        outgoingsMapper.mapOtherHousingFeesNotes(crimeApplyOutgoingsDetails.getOutgoings()));

    return fullMeansAssessment;
  }
}
