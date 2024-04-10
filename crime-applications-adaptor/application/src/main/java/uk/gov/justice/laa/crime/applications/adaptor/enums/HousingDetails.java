package uk.gov.justice.laa.crime.applications.adaptor.enums;

import java.util.Arrays;

public enum HousingDetails {
    BOARD_LODGINGS("board_lodgings", "OTHER_HOUS"),
    RENT("rent", "RENT_MORT"),
    MORTGAGE("mortgage", "RENT_MORT");

    private final String value;
    private final String code;

    HousingDetails(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static HousingDetails findByValue(String value) {
        return Arrays.stream(values())
                .filter(housingDetails -> housingDetails.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
