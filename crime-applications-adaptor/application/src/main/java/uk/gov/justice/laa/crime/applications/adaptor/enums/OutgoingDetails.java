package uk.gov.justice.laa.crime.applications.adaptor.enums;

import java.util.Arrays;

public enum OutgoingDetails {
  BOARD_AND_LODGING("board_and_lodging", "OTHER_HOUS"),
  RENT("rent", "RENT_MORT"),
  MORTGAGE("mortgage", "RENT_MORT"),
  COUNCIL_TAX("council_tax", "COUNCIL"),
  CHILDCARE("childcare", "CHILD_COST"),
  MAINTENANCE("maintenance", "MAINT_COST"),
  LEGAL_AID_CONTRIBUTION("legal_aid_contribution", "OTHER_LAC");

  private final String value;
  private final String code;

  OutgoingDetails(String value, String code) {
    this.value = value;
    this.code = code;
  }

  public String getValue() {
    return value;
  }

  public String getCode() {
    return code;
  }

  public static OutgoingDetails findByValue(String value) {
    return Arrays.stream(values())
        .filter(outgoingDetails -> outgoingDetails.getValue().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
