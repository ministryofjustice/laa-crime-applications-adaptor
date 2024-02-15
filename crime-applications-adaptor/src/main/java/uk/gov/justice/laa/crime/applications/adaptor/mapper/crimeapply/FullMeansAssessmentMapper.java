package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.FullMeansAssessment;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.IncomeAndExpenditure;

import javax.validation.constraints.NotNull;
import java.util.*;

class FullMeansAssessmentMapper {

    private final OutgoingsMapper outgoingsMapper = new OutgoingsMapper();

    @NotNull
    FullMeansAssessment map(
            uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.OutgoingsDetails crimeApplyOutgoingsDetails) {

        FullMeansAssessment fullMeansAssessment = new FullMeansAssessment();

        if (crimeApplyOutgoingsDetails == null) {
            return fullMeansAssessment;
        }

        List<IncomeAndExpenditure> expenditure = outgoingsMapper.mapOutgoings(crimeApplyOutgoingsDetails.getOutgoings(), crimeApplyOutgoingsDetails.getHousingPaymentType());
        fullMeansAssessment.setExpenditure(expenditure);
        fullMeansAssessment.setOtherHousingFeesNotes(outgoingsMapper.mapOtherHousingFeesNotes(crimeApplyOutgoingsDetails.getOutgoings(), crimeApplyOutgoingsDetails.getHousingPaymentType()));

        return fullMeansAssessment;
    }
}
