package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.*;

import javax.validation.constraints.NotNull;
import java.util.*;

class InitialMeansAssessmentMapper {

    private final DependantsMapper dependantsMapper = new DependantsMapper();
    private final BenefitsMapper benefitsMapper = new BenefitsMapper();
    private final OtherIncomeMapper otherIncomeMapper = new OtherIncomeMapper();

    @NotNull
    InitialMeansAssessment map(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.maat.
                            IncomeDetails crimeApplyIncomeDetails) {

        InitialMeansAssessment initialMeansAssessment = new InitialMeansAssessment();

        if (crimeApplyIncomeDetails == null) {
            return initialMeansAssessment;
        }

        // Benefits and Other Income will be merged into a single ArrayList
        List<IncomeAndExpenditure> benefits = benefitsMapper.mapBenefits(crimeApplyIncomeDetails.getBenefits());
        List<IncomeAndExpenditure> otherIncome = otherIncomeMapper.mapOtherIncome(crimeApplyIncomeDetails.getOtherIncome());
        List<IncomeAndExpenditure> income = mergeBenefitsAndOtherIncome(benefits, otherIncome);

        initialMeansAssessment.setIncome(income);
        initialMeansAssessment.setOtherBenefitNotes(benefitsMapper.mapOtherBenefitNotes(crimeApplyIncomeDetails.getBenefits()));
        initialMeansAssessment.setOtherIncomeNotes(otherIncomeMapper.mapOtherIncomeNotes(crimeApplyIncomeDetails.getOtherIncome()));
        initialMeansAssessment.setDependants(dependantsMapper.mapDependants(crimeApplyIncomeDetails.getDependants()));

        return initialMeansAssessment;
    }

    private List<IncomeAndExpenditure> mergeBenefitsAndOtherIncome(List<IncomeAndExpenditure> benefits, List<IncomeAndExpenditure> otherIncome) {
        List<IncomeAndExpenditure> income = new ArrayList<>();
        income.addAll(benefits);
        income.addAll(otherIncome);

        return income;
    }
}
