package uk.gov.justice.laa.crime.applications.adaptor.factory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PoundSterling {
    private final Number pennies;

    private PoundSterling(Number amountInPennies) {
        this.pennies = amountInPennies;
    }

    public static PoundSterling ofPennies(Number amountInPennies) {
        return new PoundSterling(amountInPennies);
    }

    public BigDecimal toPounds() {
        BigDecimal pennies = new BigDecimal(String.valueOf(this.pennies));
        BigDecimal pounds = pennies.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        return pounds.setScale(2);
    }
}
