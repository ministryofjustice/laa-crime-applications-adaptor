Feature: Retrieve application data from the Crime Apply Datastore.

  Scenario Outline: Retrieve valid application data from Crime Apply Data Store by USN.
    Given an application with usn <usn> exists in the datastore
    When the GET internal V1 crimeapply endpoint is called with usn <usn> and user "<user>"
    Then the returned response should match the contents of "<expectedResponseFile>"

    Examples:
      | scnNo | usn      | user    | expectedResponseFile             |
      | 00    | 10000987 | test-a1 | 10000987_passported.json         |
      | 01    | 10000988 | test-a1 | 10000988_non_passported.json     |
      | 02    | 10000340 | test-a1 | 10000340_capital_equity.json     |
      | 03    | 10000390 | test-a1 | 10000390_means_with_partner.json |

  Scenario Outline: Retrieve application data that does not exist in the Crime Apply Datastore.
    Given an application with usn <usn> does not exists in the datastore
    When the GET internal V1 crimeapply endpoint is called with usn <usn> and user "<user>"
    Then the returned response should indicate the application is not found

    Examples:
      | scnNo | usn      | user |
      | 00    | 00000000 | test |