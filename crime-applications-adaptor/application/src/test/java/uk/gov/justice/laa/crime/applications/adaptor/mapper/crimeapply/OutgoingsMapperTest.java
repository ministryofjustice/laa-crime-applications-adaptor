package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OutgoingDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Outgoing;

class OutgoingsMapperTest {
  private OutgoingsMapper outgoingsMapper;
  private static final int AMOUNT = 150;
  private static final String DETAILS = "Here are some details about the outgoing.";
  private static final String BOARD_AND_LODGING = "Board and lodging";

  @BeforeEach
  void setUp() {
    outgoingsMapper = new OutgoingsMapper();
  }

  private Outgoing getOutgoingObject() {
    Outgoing outgoing = new Outgoing();
    outgoing.setAmount(AMOUNT);
    outgoing.setPaymentType(Outgoing.PaymentType.COUNCIL_TAX);
    outgoing.setFrequency(Outgoing.Frequency.MONTH);

    return outgoing;
  }

  private AssessmentDetail getAssessmentDetailObject() {
    AssessmentDetail assessmentDetail = new AssessmentDetail();
    assessmentDetail.setAssessmentDetailCode(OutgoingDetails.COUNCIL_TAX.getCode());
    assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.MONTHLY);
    assessmentDetail.setApplicantAmount(new BigDecimal(AMOUNT));

    return assessmentDetail;
  }

  @Test
  void shouldMapCouncilTaxToCouncil() {
    Outgoing outgoing = getOutgoingObject();
    List<Outgoing> outgoings = List.of(outgoing);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = outgoingsMapper.mapOutgoings(outgoings);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapChildcareToChildCost() {
    Outgoing outgoing = getOutgoingObject();
    outgoing.setPaymentType(Outgoing.PaymentType.CHILDCARE);
    List<Outgoing> outgoings = List.of(outgoing);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(OutgoingDetails.CHILDCARE.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = outgoingsMapper.mapOutgoings(outgoings);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapMaintenanceToMaintCost() {
    Outgoing outgoing = getOutgoingObject();
    outgoing.setPaymentType(Outgoing.PaymentType.MAINTENANCE);
    List<Outgoing> outgoings = List.of(outgoing);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(OutgoingDetails.MAINTENANCE.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = outgoingsMapper.mapOutgoings(outgoings);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldMapLegalAidToOtherLac() {
    Outgoing outgoing = getOutgoingObject();
    outgoing.setPaymentType(Outgoing.PaymentType.LEGAL_AID_CONTRIBUTION);
    List<Outgoing> outgoings = List.of(outgoing);

    AssessmentDetail assessmentDetail = getAssessmentDetailObject();
    assessmentDetail.setAssessmentDetailCode(OutgoingDetails.LEGAL_AID_CONTRIBUTION.getCode());

    List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
    List<AssessmentDetail> actualAssessmentDetail = outgoingsMapper.mapOutgoings(outgoings);

    assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
  }

  @Test
  void shouldAddHousingTypeBoardLodgingsToOtherHousingFeesNotes() {
    Outgoing outgoing = getOutgoingObject();
    outgoing.setPaymentType(Outgoing.PaymentType.BOARD_AND_LODGING);
    outgoing.setDetails(DETAILS);
    List<Outgoing> outgoings = List.of(outgoing);

    String expectedOutgoingsNotes = BOARD_AND_LODGING + "\n" + DETAILS;
    String actualOutgoingsNotes = outgoingsMapper.mapOtherHousingFeesNotes(outgoings);

    assertEquals(expectedOutgoingsNotes, actualOutgoingsNotes);
  }
}
