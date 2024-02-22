package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.FullMeansAssessment;

import javax.validation.constraints.NotNull;
import java.util.List;

class FullMeansAssessmentMapper {

    private final OutgoingsMapper outgoingsMapper = new OutgoingsMapper();

    @NotNull
    FullMeansAssessment map(
            uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.OutgoingsDetails crimeApplyOutgoingsDetails) {

        FullMeansAssessment fullMeansAssessment = new FullMeansAssessment();

        if (crimeApplyOutgoingsDetails == null) {
            return fullMeansAssessment;
        }
        List<AssessmentDetail> assessmentDetails = outgoingsMapper.mapOutgoings(crimeApplyOutgoingsDetails.getOutgoings(), crimeApplyOutgoingsDetails.getHousingPaymentType());
        fullMeansAssessment.setAssessmentDetails(assessmentDetails);
        fullMeansAssessment.setOtherHousingNote(outgoingsMapper.mapOtherHousingFeesNotes(crimeApplyOutgoingsDetails.getOutgoings(), crimeApplyOutgoingsDetails.getHousingPaymentType()));

        return fullMeansAssessment;
    }
}
