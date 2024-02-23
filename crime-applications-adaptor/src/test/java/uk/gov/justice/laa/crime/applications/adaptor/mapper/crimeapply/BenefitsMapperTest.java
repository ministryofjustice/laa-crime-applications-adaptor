package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.enums.BenefitDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Benefit;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BenefitsMapperTest {

    private BenefitsMapper benefitsMapper;
    private final int AMOUNT = 100;
    private final String DETAILS = "Here are some details about the benefit";

    @BeforeEach
    void setUp() { benefitsMapper = new BenefitsMapper(); }

    private Benefit getBenefitObject() {
        Benefit benefit = new Benefit();
        benefit.setAmount(AMOUNT);
        benefit.setDetails(DETAILS);
        benefit.setType(Benefit.Type.CHILD);
        benefit.setFrequency(Benefit.Frequency.ANNUAL);

        return benefit;
    }

    private AssessmentDetail getAssessmentDetailObject() {
        AssessmentDetail assessmentDetail = new AssessmentDetail();
        assessmentDetail.setAssessmentDetailCode(BenefitDetails.CHILD.getCode());
        assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
        assessmentDetail.setApplicantAmount(new BigDecimal(AMOUNT));

        return assessmentDetail;
    }

    @Test
    void shouldMapChildToChildBen() {
        Benefit benefit = getBenefitObject();
        List<Benefit> benefits = List.of(benefit);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapWorkingOrChildTaxCreditToTaxCred() {
        Benefit benefit = getBenefitObject();
        benefit.setType(Benefit.Type.WORKING_OR_CHILD_TAX_CREDIT);
        List<Benefit> benefits = List.of(benefit);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(BenefitDetails.WORKING_OR_CHILD_TAX_CREDIT.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapIncapacityToIncapBen() {
        Benefit benefit = getBenefitObject();
        benefit.setType(Benefit.Type.INCAPACITY);
        List<Benefit> benefits = List.of(benefit);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(BenefitDetails.INCAPACITY.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapIndustryInjuriesDisabledToInjBen() {
        Benefit benefit = getBenefitObject();
        benefit.setType(Benefit.Type.INDUSTRIAL_INJURIES_DISABLEMENT);
        List<Benefit> benefits = List.of(benefit);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(BenefitDetails.INDUSTRIAL_INJURIES_DISABLEMENT.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapOtherToOtherBen() {
        Benefit benefit = getBenefitObject();
        benefit.setType(Benefit.Type.OTHER);
        List<Benefit> benefits = List.of(benefit);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(BenefitDetails.OTHER.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapUniversalCreditToOtherBen() {
        Benefit benefit = getBenefitObject();
        benefit.setType(Benefit.Type.UNIVERSAL_CREDIT);
        List<Benefit> benefits = List.of(benefit);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(BenefitDetails.OTHER.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldMapJsaToOtherBen() {
        Benefit benefit = getBenefitObject();
        benefit.setType(Benefit.Type.JSA);
        List<Benefit> benefits = List.of(benefit);

        AssessmentDetail assessmentDetail = getAssessmentDetailObject();
        assessmentDetail.setAssessmentDetailCode(BenefitDetails.OTHER.getCode());

        List<AssessmentDetail> expectedAssessmentDetail = List.of(assessmentDetail);
        List<AssessmentDetail> actualAssessmentDetail = benefitsMapper.mapBenefits(benefits);

        assertEquals(expectedAssessmentDetail, actualAssessmentDetail);
    }

    @Test
    void shouldAddBenefitDetailsToNotes() {
        Benefit benefit = getBenefitObject();
        List<Benefit> benefits = List.of(benefit);

        String actualBenefitNotes = benefitsMapper.mapOtherBenefitNotes(benefits);

        assertEquals(DETAILS, actualBenefitNotes);
    }

    @Test
    void shouldAddUniversalCreditToNotes() {
        Benefit benefit = getBenefitObject();
        benefit.setType(Benefit.Type.UNIVERSAL_CREDIT);
        List<Benefit> benefits = List.of(benefit);

        String expectedBenefitNotes = DETAILS + "\nUniversal Credit";
        String actualBenefitNotes = benefitsMapper.mapOtherBenefitNotes(benefits);

        assertEquals(expectedBenefitNotes, actualBenefitNotes);
    }

    @Test
    void shouldAddJsaToNotes() {
        Benefit benefit = getBenefitObject();
        benefit.setType(Benefit.Type.JSA);
        List<Benefit> benefits = List.of(benefit);

        String expectedBenefitNotes = DETAILS + "\nJSA";
        String actualBenefitNotes = benefitsMapper.mapOtherBenefitNotes(benefits);

        assertEquals(expectedBenefitNotes, actualBenefitNotes);
    }
}
