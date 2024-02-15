package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.IncomeAndExpenditure;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Outgoing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutgoingsMapper {
    private static final Map<String, String> OUTGOINGS_CODES = new HashMap<>();
    private static final Map<String, String> HOUSING_CODES = new HashMap<>();
    private static final Map<String, Integer> FREQUENCY = new HashMap<>();

    public OutgoingsMapper() {
        OUTGOINGS_CODES.put("council_tax", "COUNCIL");
        OUTGOINGS_CODES.put("childcare", "CHILD_COST");
        OUTGOINGS_CODES.put("maintenance", "MAINT_COST");
        OUTGOINGS_CODES.put("legal_aid", "OTHER_LAC");

        HOUSING_CODES.put("board_lodgings", "OTHER_HOUS");
        HOUSING_CODES.put("rent", "RENT_MORT");
        HOUSING_CODES.put("mortgage", "RENT_MORT");

        FREQUENCY.put("week", 52);
        FREQUENCY.put("fortnight", 26);
        FREQUENCY.put("four_weeks", 13);
        FREQUENCY.put("month", 12);
        FREQUENCY.put("annual", 1);
    }

    public List<IncomeAndExpenditure> mapOutgoings(List<Outgoing> outgoings, Object housingPaymentType) {
        List<IncomeAndExpenditure> expenditure = new ArrayList<>();

        if (outgoings != null) {
            for (Outgoing outgoing : outgoings) {
                IncomeAndExpenditure incomeAndExpenditure = new IncomeAndExpenditure();
                String outgoingsType = outgoing.getType().value();
                String maatDetailCode = OUTGOINGS_CODES.get(outgoingsType);
                Integer amount = outgoing.getAmount();

                // Check housing payment type - this affects where 'housing' maps to
                if (outgoingsType.equals("housing")) {
                    if (housingPaymentType != null) {
                        maatDetailCode = HOUSING_CODES.get(housingPaymentType);

                        if (maatDetailCode == "OTHER_HOUS") {
                            // For other housing, we need to half the value and round to nearest 2
                            Double half = amount / 2.0;
                            Double rounded = Math.ceil(half / 2) * 2;
                            amount = rounded.intValue();
                        }
                    }
                }

                incomeAndExpenditure.setMaatDetailCode(maatDetailCode);
                incomeAndExpenditure.setAmount(amount);
                incomeAndExpenditure.setFrequency(FREQUENCY.get(outgoing.getFrequency().value()));

                expenditure.add(incomeAndExpenditure);
            }
        }

        return expenditure;
    }

    public String mapOtherHousingFeesNotes(List<Outgoing> outgoings, Object housingPaymentType) {
        StringBuilder sb = new StringBuilder();

        if (outgoings != null) {
            for (Outgoing outgoing : outgoings) {
                String outgoingsType = outgoing.getType().value();

                // Check housing payment type - this affects where 'housing' maps to
                if (outgoingsType.equals("housing")) {
                    // If board_lodgings, we need to create a field for the notes associated with it
                    if (housingPaymentType != null && housingPaymentType.equals("board_lodgings")) {
                        if (outgoing.getDetails() != null) {
                            sb.append("\nBoard lodgings");
                            sb.append("\n" + outgoing.getDetails());
                        }
                    }
                }
            }
        }

        String otherHousingFeesNotes = sb.toString();

        return otherHousingFeesNotes.trim();
    }
}
