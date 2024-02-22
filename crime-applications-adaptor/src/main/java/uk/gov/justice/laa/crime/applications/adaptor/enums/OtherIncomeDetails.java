package uk.gov.justice.laa.crime.applications.adaptor.enums;

import java.util.Arrays;

public enum OtherIncomeDetails {
    PRIVATE_PENSION("private_pension", "PRIV_PENS"),
    STATE_PENSION("state_pension", "STATE_PEN"),
    MAINTENANCE("maintenance", "MAINT_INC"),
    INTEREST("interest", "SAVINGS"),
    OTHER("other", "OTHER_INC"),

    STUDENT("student", "OTHER_INC"),
    BOARD_FROM_FAMILY("board_from_family", "OTHER_INC"),
    RENT("rent", "OTHER_INC"),
    FRIENDS_AND_FAMILY("friends_and_family", "OTHER_INC");

    private final String value;
    private final String code;

    OtherIncomeDetails(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static OtherIncomeDetails findByValue(String value) {
        return Arrays.stream(values()).filter(otherIncomeDetails -> otherIncomeDetails.getValue().equalsIgnoreCase(value)).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
