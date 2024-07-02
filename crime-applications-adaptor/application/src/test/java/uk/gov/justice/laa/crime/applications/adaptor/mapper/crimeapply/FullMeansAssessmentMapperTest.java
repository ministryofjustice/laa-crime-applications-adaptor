package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.FullMeansAssessment;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Means;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Outgoing;

class FullMeansAssessmentMapperTest {

  private FullMeansAssessmentMapper fullMeansAssessmentMapper;

  @BeforeEach
  void setUp() {
    fullMeansAssessmentMapper = new FullMeansAssessmentMapper();
  }

  @Test
  void shouldSuccessfullyMapCrimeApplyOutgoingsDetailsToFullMeansAssessment() throws JSONException {
    Means meansDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json").getMeansDetails();

    FullMeansAssessment fullMeansAssessment = fullMeansAssessmentMapper.map(meansDetails);

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
    Means meansDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json").getMeansDetails();

    meansDetails.getOutgoingsDetails().setOutgoings(null);

    FullMeansAssessment fullMeansAssessment = fullMeansAssessmentMapper.map(meansDetails);

    String actualFullMeansAssessmentJSON = JsonUtils.objectToJson(fullMeansAssessment);
    JSONAssert.assertEquals("{}", actualFullMeansAssessmentJSON, JSONCompareMode.STRICT);
  }

  @Test
  void shouldMapOtherHousingFeeNotes_whenTypeIsHousingAndHousingPaymentTypeIsBoardLodgings() {
    Means meansDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json").getMeansDetails();

    Outgoing outgoing = new Outgoing();
    outgoing.setPaymentType(Outgoing.PaymentType.BOARD_AND_LODGING);
    outgoing.setAmount(100);
    outgoing.setFrequency(Outgoing.Frequency.MONTH);
    outgoing.setDetails("Details about housing");

    List<Outgoing> outgoings = new ArrayList<>();
    outgoings.add(outgoing);

    meansDetails.getOutgoingsDetails().setOutgoings(outgoings);
    FullMeansAssessment fullMeansAssessment = fullMeansAssessmentMapper.map(meansDetails);

    assertEquals(
        "Board and lodging\nDetails about housing", fullMeansAssessment.getOtherHousingNote());
  }
}
