package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.enums.BenefitDetails;
import uk.gov.justice.laa.crime.applications.adaptor.enums.HousingDetails;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OutgoingDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Benefit;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Outgoing;

import java.math.BigDecimal;
import java.util.*;

public class OutgoingsMapper {
    private static final String HOUSING = "housing";
    private static final String BOARD_LODGINGS_TYPE = "board_lodgings";
    private static final String BOARD_LODGINGS = "Board lodgings";

    public List<AssessmentDetail> mapOutgoings(List<Outgoing> outgoings, Object housingPaymentType) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (Objects.nonNull(outgoings)) {
            for (Outgoing outgoing : outgoings) {
                AssessmentDetail assessmentDetail = new AssessmentDetail();
                String outgoingsType = outgoing.getType().value();
                // If the type is 'housing', we map against the housing codes instead
                if (outgoingsType.equals(HOUSING) && Objects.nonNull(housingPaymentType)) {
                    assessmentDetail.setAssessmentDetailCode(String.valueOf(HousingDetails.findByValue((String) housingPaymentType).getCode()));
                } else {
                    OutgoingDetails outgoingDetail = OutgoingDetails.findByValue(outgoingsType);
                    assessmentDetail.setAssessmentDetailCode(outgoingDetail.getCode());
                }

                assessmentDetail.setApplicantAmount(new BigDecimal(outgoing.getAmount()));
                mapFrequency(assessmentDetail, outgoing.getFrequency());

                assessmentDetails.add(assessmentDetail);
            }
        }

        return assessmentDetails;
    }

    private void mapFrequency(AssessmentDetail assessmentDetail, Outgoing.Frequency frequency) {
        switch (frequency) {
            case WEEK -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.WEEKLY);
            case FORTNIGHT -> assessmentDetail.setApplicantFrequency( AssessmentDetail.ApplicantFrequency._2_WEEKLY);
            case FOUR_WEEKS -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency._4_WEEKLY);
            case MONTH -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.MONTHLY);
            case ANNUAL -> assessmentDetail.setApplicantFrequency(AssessmentDetail.ApplicantFrequency.ANNUALLY);
        }
    }

    public String mapOtherHousingFeesNotes(List<Outgoing> outgoings, Object housingPaymentType) {
        StringBuilder otherHousingFeesNotes = new StringBuilder();
        if (Objects.nonNull(outgoings)) {
            for (Outgoing outgoing : outgoings) {
                String outgoingsType = outgoing.getType().value();

                // Check housing payment type - this affects where 'housing' maps to
                if (outgoingsType.equals(HOUSING) && Objects.nonNull(housingPaymentType) && housingPaymentType.equals(BOARD_LODGINGS_TYPE)) {
                    // If board_lodgings, we need to create a field for the notes associated with it
                    otherHousingFeesNotes.append("\n" + BOARD_LODGINGS);

                    if (Objects.nonNull(outgoing.getDetails())) {
                        otherHousingFeesNotes.append("\n" + outgoing.getDetails());
                    }
                }
            }
        }

        return otherHousingFeesNotes.toString().trim();
    }
}
