package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.IncomeAndExpenditure;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Benefit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenefitsMapper {
    private static final Map<String, String> BENEFITS_CODES = new HashMap<>();
    private static final Map<String, Integer> FREQUENCY = new HashMap<>();

    public BenefitsMapper() {
        BENEFITS_CODES.put("child", "CHILD_BEN");
        BENEFITS_CODES.put("working_or_child_tax_credit", "TAX_CRED");
        BENEFITS_CODES.put("incapacity", "INCAP_BEN");
        BENEFITS_CODES.put("industrial_injuries_disablement", "INJ_BEN");
        BENEFITS_CODES.put("other", "OTHER_BEN");

        FREQUENCY.put("week", 52);
        FREQUENCY.put("fortnight", 26);
        FREQUENCY.put("four_weeks", 13);
        FREQUENCY.put("month", 12);
        FREQUENCY.put("annual", 1);
    }

    public List<IncomeAndExpenditure> mapBenefits(List<Benefit> benefits) {
        List<IncomeAndExpenditure> income = new ArrayList<>();

        if (benefits != null) {
            for (Benefit benefit : benefits) {
                IncomeAndExpenditure incomeAndExpenditure = new IncomeAndExpenditure();

                String benefitType = benefit.getType().value();

                // We don't have a universal credit type in MAAT, so set it to 'other'
                if (benefitType.equals("universal_credit")) {
                    benefitType = "other";
                }

                // We don't have a jsa type in MAAT, so set it to 'other'
                if (benefitType.equals("jsa")) {
                    benefitType = "other";
                }

                incomeAndExpenditure.setMaatDetailCode(BENEFITS_CODES.get(benefitType));
                incomeAndExpenditure.setAmount(benefit.getAmount());
                incomeAndExpenditure.setFrequency(FREQUENCY.get(benefit.getFrequency().value()));

                income.add(incomeAndExpenditure);
            }
        }

        return income;
    }

    public String mapOtherBenefitNotes(List<Benefit> benefits) {
        String otherBenefitNotes = "";

        if (benefits != null) {
            for (Benefit benefit : benefits) {
                if (benefit.getDetails() != null) {
                    otherBenefitNotes += "\n" + benefit.getDetails();
                }

                // We don't have a universal credit type in MAAT, so append it to the notes
                if (benefit.getType().value().equals("universal_credit")) {
                    otherBenefitNotes += "\nUniversal Credit";
                }

                // We don't have a jsa type in MAAT, so append it to the notes
                if (benefit.getType().value().equals("jsa")) {
                    otherBenefitNotes += "\nJSA";
                }
            }
        }

        return otherBenefitNotes.trim();
    }
}
