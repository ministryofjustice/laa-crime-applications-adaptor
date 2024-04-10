Feature: Retrieve application data from the Crime Apply Datastore.

  Scenario Outline: Retrieve valid application data from Crime Apply Data Store by USN.
    Given an application with usn <usn> exists in the datastore
    When the GET internal V1 crimeapply endpoint is called with usn <usn> and user "<user>"
    Then the returned response should match the contents of "<expectedResponseFile>"

    Examples:
      | scnNo | usn      | user    | expectedResponseFile |
      | 00    | 10000987 | test-a1 | 10000987.json        |

