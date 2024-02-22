package uk.gov.justice.laa.crime.applications.adaptor.enums;

import java.util.Arrays;

public enum BenefitDetails {
    CHILD("child", "CHILD_BEN"),
    WORKING_OR_CHILD_TAX_CREDIT("working_or_child_tax_credit", "TAX_CRED"),
    INCAPACITY("incapacity", "INCAP_BEN"),
    INDUSTRIAL_INJURIES_DISABLEMENT("industrial_injuries_disablement", "INJ_BEN"),
    OTHER("other", "OTHER_BEN"),
    UNIVERSAL_CREDIT("universal_credit", "OTHER_BEN"),
    JSA("jsa", "OTHER_BEN");

    private final String value;
    private final String code;

    BenefitDetails(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static BenefitDetails findByValue(String value) {
        return Arrays.stream(values()).filter(benefitDetails -> benefitDetails.getValue().equalsIgnoreCase(value)).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
