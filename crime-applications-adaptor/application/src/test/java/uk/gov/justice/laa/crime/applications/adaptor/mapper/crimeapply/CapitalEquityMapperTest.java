package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.CapitalEquity;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

class CapitalEquityMapperTest {

  private CapitalEquityMapper capitalEquityMapper;

  @BeforeEach
  void setUp() {
    capitalEquityMapper = new CapitalEquityMapper();
  }

  @Test
  void shouldSuccessfullyMapCrimeApplyCapitalDetailsToAdapterCapitalDetails() throws JSONException {
    MaatApplicationExternal crimeApplyWithCapitalDetails =
        TestData.getMaatApplicationWithCapitalDetails();

    CapitalEquity actualCaseDetails = capitalEquityMapper.map(crimeApplyWithCapitalDetails);

    String actualCaseDetailsJSON = JsonUtils.objectToJson(actualCaseDetails);
    JSONAssert.assertEquals(
        """
            {
              "capitalProperty": [
                {
                  "propertyType": "SEMI",
                  "bedrooms": "3",
                  "declaredMortgageCharges": 39000,
                  "declaredMarketValue": 4800000,
                  "percentageOwnedApplicant": 62.0,
                  "percentageOwnedPartner": 0,
                  "address": {
                    "line1": "296 Oak Court",
                    "line2": "Higham",
                    "city": "London",
                    "country": "United Kingdom",
                    "postCode": "E1 4AD"
                  },
                  "thirdPartyOwner": [
                    {
                      "ownerRelation": "FAMILY",
                      "ownerName": "Freya Brennan"
                    }
                  ]
                },
                {
                  "propertyType": "COMMERCIAL",
                  "declaredMortgageCharges": 0,
                  "declaredMarketValue": 880000,
                  "percentageOwnedApplicant": 100.0,
                  "percentageOwnedPartner": 0,
                  "address": {
                    "line1": "1 Park Road",
                    "line2": "Oldtown",
                    "city": "London",
                    "country": "United Kingdom",
                    "postCode": "E1 2DA"
                  }
                },
                {
                  "propertyType": "LAND",
                  "declaredMortgageCharges": 22000,
                  "declaredMarketValue": 720000,
                  "percentageOwnedApplicant": 100.0,
                  "percentageOwnedPartner": 0,
                  "address": {
                    "line1": "1 High Street",
                    "line2": "Newtown",
                    "city": "London",
                    "country": "United Kingdom",
                    "postCode": "SW1A 2AA"
                  }
                }
              ],
              "capital": [
                {
                  "capitalType": "PEPS",
                  "assetAmount": 50000,
                  "otherDescription": "Private equity plan",
                  "accountOwner": "APPLICANT"
                },
                {
                  "capitalType": "SHARES",
                  "assetAmount": 10000,
                  "otherDescription": "500 Shares in Acme Ltd",
                  "accountOwner": "APPLICANT"
                },
                {
                  "capitalType": "SAVINGS",
                  "assetAmount": 10000,
                  "bankName": "Lloyds Bank",
                  "branchSortCode": "12-23-45",
                  "accountOwner": "APPLICANT"
                },
                {
                  "capitalType": "CASH ISA",
                  "assetAmount": 10000,
                  "bankName": "Lloyds Bank",
                  "branchSortCode": "12-23-45",
                  "accountOwner": "APPLICANT"
                },
                {
                  "capitalType": "PREMIUM BONDS",
                  "assetAmount": 2000,
                  "accountOwner": "APPLICANT"
                },
                {
                  "capitalType": "PREMIUM BONDS",
                  "assetAmount": 100000000
                },
                {
                  "capitalType": "TRUST FUND",
                  "assetAmount": 500000
                }
              ]
            }
            """,
        actualCaseDetailsJSON,
        JSONCompareMode.STRICT);
  }
}
