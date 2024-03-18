package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.FullMeansAssessment;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Outgoing;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.OutgoingsDetails;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

class FullMeansAssessmentMapperTest {

  private FullMeansAssessmentMapper fullMeansAssessmentMapper;

  @BeforeEach
  void setUp() {
    fullMeansAssessmentMapper = new FullMeansAssessmentMapper();
  }

  @Test
  void shouldSuccessfullyMapCrimeApplyOutgoingsDetailsToFullMeansAssessment() throws JSONException {
    OutgoingsDetails crimeApplyOutgoingsDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json")
            .getMeansDetails()
            .getOutgoingsDetails();

    FullMeansAssessment fullMeansAssessment =
        fullMeansAssessmentMapper.map(crimeApplyOutgoingsDetails);

    String actualFullMeansAssessmentJSON = JsonUtils.objectToJson(fullMeansAssessment);
    String expectedFullMeansAssessmentJSON =
        FileUtils.readFileToString(
            "data/expected/crimeapplication/FullMeansAssessment_mapped.json");
    JSONAssert.assertEquals(
        expectedFullMeansAssessmentJSON, actualFullMeansAssessmentJSON, JSONCompareMode.STRICT);
  }

  @Test
  void shouldSuccessfullyMapNullCrimeApplyOutgoingsDetailsToEmptyAdapterFullMeansAssessment()
      throws JSONException {
    OutgoingsDetails crimeApplyOutgoingsDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json")
            .getMeansDetails()
            .getOutgoingsDetails();

    crimeApplyOutgoingsDetails.setOutgoings(null);

    FullMeansAssessment fullMeansAssessment =
        fullMeansAssessmentMapper.map(crimeApplyOutgoingsDetails);

    String actualFullMeansAssessmentJSON = JsonUtils.objectToJson(fullMeansAssessment);
    JSONAssert.assertEquals("{}", actualFullMeansAssessmentJSON, JSONCompareMode.STRICT);
  }

  @Test
  void shouldMapOtherHousingFeeNotes_whenTypeIsHousingAndHousingPaymentTypeIsBoardLodgings() {
    OutgoingsDetails crimeApplyOutgoingsDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json")
            .getMeansDetails()
            .getOutgoingsDetails();

    crimeApplyOutgoingsDetails.setHousingPaymentType("board_lodgings");
    Outgoing outgoing = new Outgoing();
    outgoing.setType(Outgoing.Type.HOUSING);
    outgoing.setAmount(100);
    outgoing.setFrequency(Outgoing.Frequency.MONTH);
    outgoing.setDetails("Details about housing");

    List<Outgoing> outgoings = new ArrayList<>();
    outgoings.add(outgoing);

    crimeApplyOutgoingsDetails.setOutgoings(outgoings);
    FullMeansAssessment fullMeansAssessment =
        fullMeansAssessmentMapper.map(crimeApplyOutgoingsDetails);

    assertEquals(
        "Board lodgings\nDetails about housing", fullMeansAssessment.getOtherHousingNote());
  }
}
