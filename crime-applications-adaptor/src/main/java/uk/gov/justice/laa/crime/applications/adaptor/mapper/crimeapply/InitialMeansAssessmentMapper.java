package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.*;

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

        if (crimeApplyIncomeDetails == null) {
            return initialMeansAssessment;
        }


        // Benefits and Other Income will be merged into a single ArrayList
        List<AssessmentDetail> benefits = benefitsMapper.mapBenefits(crimeApplyIncomeDetails.getBenefits());
        List<AssessmentDetail> otherIncome = otherIncomeMapper.mapOtherIncome(crimeApplyIncomeDetails.getOtherIncome());
        List<AssessmentDetail> assessmentDetails = mergeBenefitsAndOtherIncome(benefits, otherIncome);

        initialMeansAssessment.setAssessmentDetails(assessmentDetails);
        initialMeansAssessment.setOtherBenefitNote(benefitsMapper.mapOtherBenefitNotes(crimeApplyIncomeDetails.getBenefits()));
        initialMeansAssessment.setOtherIncomeNote(otherIncomeMapper.mapOtherIncomeNotes(crimeApplyIncomeDetails.getOtherIncome()));
        initialMeansAssessment.setChildWeighting(childWeightingMapper.mapChildWeighting(crimeApplyIncomeDetails.getDependants()));

        return initialMeansAssessment;
    }

    private List<AssessmentDetail> mergeBenefitsAndOtherIncome(List<AssessmentDetail> benefits, List<AssessmentDetail> otherIncome) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();
        assessmentDetails.addAll(benefits);
        assessmentDetails.addAll(otherIncome);

        return assessmentDetails;
    }
}
