package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.IncomeAndExpenditure;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.OtherIncome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherIncomeMapper {
    private static final Map<String, String> OTHER_INCOME_CODES = new HashMap<>() {{
        put("private_pension", "PRIV_PENS");
        put("state_pension", "STATE_PEN");
        put("maintenance", "MAINT_INC");
        put("interest", "SAVINGS");
        put("other", "OTHER_INC");
    }};

    private static final Map<String, Integer> FREQUENCY = new HashMap<>() {{
        put("week", 52);
        put("fortnight", 26);
        put("four_weeks", 13);
        put("month", 12);
        put("annual", 1);
    }};

    public List<IncomeAndExpenditure> mapOtherIncome(List<OtherIncome> otherIncome) {
        List<IncomeAndExpenditure> income = new ArrayList<>();

        if (otherIncome != null) {
            for (OtherIncome other : otherIncome) {
                String incomeType = other.getType().value();

                // We don't have these income types in MAAT, so set them to 'other'
                if (incomeType.equals("student") || incomeType.equals("board_from_family") || incomeType.equals("rent")
                        || incomeType.equals("friends_and_family")) {
                    incomeType = "other";
                }

                IncomeAndExpenditure incomeAndExpenditure = new IncomeAndExpenditure();
                incomeAndExpenditure.setMaatDetailCode(OTHER_INCOME_CODES.get(incomeType));
                incomeAndExpenditure.setAmount(other.getAmount());
                incomeAndExpenditure.setFrequency(FREQUENCY.get(other.getFrequency().value()));

                income.add(incomeAndExpenditure);
            }
        }

        return income;
    }

    public String mapOtherIncomeNotes(List<OtherIncome> otherIncome) {
        String otherIncomeNotes = "";

        if (otherIncome != null) {
            for (OtherIncome other : otherIncome) {
                String incomeType = other.getType().value();

                if (other.getDetails() != null) {
                    otherIncomeNotes += "\n" + other.getDetails();
                }

                // We don't have these income types in MAAT, so append them to the notes
                if (incomeType.equals("student")) {
                    otherIncomeNotes += "\nStudent";
                }

                if (incomeType.equals("board_from_family")) {
                    otherIncomeNotes += "\nBoard from family";
                }

                if (incomeType.equals("rent")) {
                    otherIncomeNotes += "\nRent";
                }

                if (incomeType.equals("friends_and_family")) {
                    otherIncomeNotes += "\nFriends and family";
                }
            }
        }

        return otherIncomeNotes.trim();
    }
}
