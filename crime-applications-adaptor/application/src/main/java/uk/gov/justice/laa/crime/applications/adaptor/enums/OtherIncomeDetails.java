package uk.gov.justice.laa.crime.applications.adaptor.enums;

import java.util.Arrays;

public enum OtherIncomeDetails {
  PRIVATE_PENSION("private_pension", "PRIV_PENS"),
  STATE_PENSION("state_pension", "STATE_PEN"),
  MAINTENANCE("maintenance", "MAINT_INC"),
  INTEREST_INVESTMENT("interest_investment", "SAVINGS"),
  WORK_BENEFITS("work_benefits", "WORK_BEN"),
  OTHER("other", Constants.OTHER_INC);

  private final String value;
  private final String code;

  private static class Constants {
    private static final String OTHER_INC = "OTHER_INC";
  }

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
    return Arrays.stream(values())
        .filter(otherIncomeDetails -> otherIncomeDetails.getValue().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
