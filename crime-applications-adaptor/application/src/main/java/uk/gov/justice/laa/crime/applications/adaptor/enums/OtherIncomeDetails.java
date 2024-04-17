package uk.gov.justice.laa.crime.applications.adaptor.enums;

import java.util.Arrays;

public enum OtherIncomeDetails {
  PRIVATE_PENSION("private_pension", "PRIV_PENS"),
  STATE_PENSION("state_pension", "STATE_PEN"),
  MAINTENANCE("maintenance", "MAINT_INC"),
  INTEREST_INVESTMENT("interest_investment", "SAVINGS"),
  OTHER("other", Constants.OTHER_INC),
  STUDENT_LOAN_GRANT("student_loan_grant", Constants.OTHER_INC),
  BOARD_FROM_FAMILY("board_from_family", Constants.OTHER_INC),
  RENT("rent", Constants.OTHER_INC),
  FROM_FRIENDS_RELATIVES("from_friends_relatives", Constants.OTHER_INC),
  FINANCIAL_SUPPORT_WITH_ACCESS("financial_support_with_access", Constants.OTHER_INC);

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
