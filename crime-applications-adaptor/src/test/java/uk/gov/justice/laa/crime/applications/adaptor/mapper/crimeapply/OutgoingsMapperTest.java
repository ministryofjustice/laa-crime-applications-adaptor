package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OutgoingDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Outgoing;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OutgoingsMapperTest {
    private OutgoingsMapper outgoingsMapper;
    private final int AMOUNT = 150;
    private final String DETAILS = "Here are some details about the outgoing.";
    private final String BOARD_LODGINGS = "Board lodgings";

    @BeforeEach
    void setUp() { outgoingsMapper = new OutgoingsMapper(); }

    private Outgoing getOutgoingObject() {
        Outgoing outgoing = new Outgoing();
        outgoing.setAmount(AMOUNT);
        outgoing.setType(Outgoing.Type.COUNCIL_TAX);
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
        List<AssessmentDetail> actualAssessmentDetail = outgoingsMapper.mapOutgoings(outgoings, null);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapChildcareToChildCost() {
        Outgoing outgoing = getOutgoingObject();
        outgoing.setType(Outgoing.Type.CHILDCARE);
        List<Outgoing> outgoings = List.of(outgoing);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(OutgoingDetails.CHILDCARE.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = outgoingsMapper.mapOutgoings(outgoings, null);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapMaintenanceToMaintCost() {
        Outgoing outgoing = getOutgoingObject();
        outgoing.setType(Outgoing.Type.MAINTENANCE);
        List<Outgoing> outgoings = List.of(outgoing);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(OutgoingDetails.MAINTENANCE.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = outgoingsMapper.mapOutgoings(outgoings, null);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapLegalAidToOtherLac() {
        Outgoing outgoing = getOutgoingObject();
        outgoing.setType(Outgoing.Type.LEGAL_AID);
        List<Outgoing> outgoings = List.of(outgoing);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(OutgoingDetails.LEGAL_AID.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = outgoingsMapper.mapOutgoings(outgoings, null);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldAddHousingTypeBoardLodgingsToOtherHousingFeesNotes() {
        Outgoing outgoing = getOutgoingObject();
        outgoing.setType(Outgoing.Type.HOUSING);
        outgoing.setDetails(DETAILS);
        List<Outgoing> outgoings = List.of(outgoing);
        Object housingPaymentType = "board_lodgings";

        String expectedOutgoingsNotes = BOARD_LODGINGS + "\n" + DETAILS;
        String actualOutgoingsNotes = outgoingsMapper.mapOtherHousingFeesNotes(outgoings, housingPaymentType);

        assertEquals(expectedOutgoingsNotes, actualOutgoingsNotes);
    }
}
