package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.InitialMeansAssessment;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;

import javax.validation.constraints.NotNull;
import java.util.*;

class InitialMeansAssessmentMapper {

    private final ChildWeightingMapper childWeightingMapper = new ChildWeightingMapper();
    private final BenefitsMapper benefitsMapper = new BenefitsMapper();
    private final OtherIncomeMapper otherIncomeMapper = new OtherIncomeMapper();

    @NotNull
    InitialMeansAssessment map(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.
                            IncomeDetails crimeApplyIncomeDetails) {

        InitialMeansAssessment initialMeansAssessment = new InitialMeansAssessment();
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (Objects.isNull(crimeApplyIncomeDetails)) {
            return initialMeansAssessment;
        }
        // Benefits and Other Income will be merged into a single ArrayList
        if(Objects.nonNull(crimeApplyIncomeDetails.getBenefits())) {
            List<AssessmentDetail> benefits = benefitsMapper.mapBenefits(crimeApplyIncomeDetails.getBenefits());
            assessmentDetails.addAll(benefits);
        }
        if(Objects.nonNull(crimeApplyIncomeDetails.getOtherIncome())) {
            List<AssessmentDetail> otherIncome = otherIncomeMapper.mapOtherIncome(crimeApplyIncomeDetails.getOtherIncome());
            assessmentDetails.addAll(otherIncome);
        }

        initialMeansAssessment.setAssessmentDetails(assessmentDetails);
        initialMeansAssessment.setOtherBenefitNote(benefitsMapper.mapOtherBenefitNotes(crimeApplyIncomeDetails.getBenefits()));
        initialMeansAssessment.setOtherIncomeNote(otherIncomeMapper.mapOtherIncomeNotes(crimeApplyIncomeDetails.getOtherIncome()));
        initialMeansAssessment.setChildWeighting(childWeightingMapper.mapChildWeighting(crimeApplyIncomeDetails.getDependants()));

        return initialMeansAssessment;
    }
}
