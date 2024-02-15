package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.IncomeAndExpenditure;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.OtherIncome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherIncomeMapper {
    private static final Map<String, String> OTHER_INCOME_CODES = new HashMap<>();
    private static final Map<String, Integer> FREQUENCY = new HashMap<>();

    public OtherIncomeMapper() {
        OTHER_INCOME_CODES.put("private_pension", "PRIV_PENS");
        OTHER_INCOME_CODES.put("state_pension", "STATE_PEN");
        OTHER_INCOME_CODES.put("maintenance", "MAINT_INC");
        OTHER_INCOME_CODES.put("interest", "SAVINGS");
        OTHER_INCOME_CODES.put("other", "OTHER_INC");

        FREQUENCY.put("week", 52);
        FREQUENCY.put("fortnight", 26);
        FREQUENCY.put("four_weeks", 13);
        FREQUENCY.put("month", 12);
        FREQUENCY.put("annual", 1);
    }

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
        StringBuilder sb = new StringBuilder();

        if (otherIncome != null) {
            for (OtherIncome other : otherIncome) {
                String incomeType = other.getType().value();

                if (other.getDetails() != null) {
                    sb.append("\n" + other.getDetails());
                }

                // We don't have these income types in MAAT, so append them to the notes
                if (incomeType.equals("student")) {
                    sb.append("\nStudent");
                }

                if (incomeType.equals("board_from_family")) {
                    sb.append("\nBoard from family");
                }

                if (incomeType.equals("rent")) {
                    sb.append("\nRent");
                }

                if (incomeType.equals("friends_and_family")) {
                    sb.append("\nFriends and family");
                }
            }
        }

        String otherIncomeNotes = sb.toString();

        return otherIncomeNotes.trim();
    }
}
