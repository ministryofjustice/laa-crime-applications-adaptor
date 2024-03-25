package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.HousingDetails;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OutgoingDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.AssessmentDetail;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Outgoing;
import uk.gov.justice.laa.crime.applications.adaptor.util.FrequencyMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OutgoingsMapper {
    private static final String HOUSING = "housing";
    private static final String BOARD_LODGINGS_TYPE = "board_lodgings";
    private static final String BOARD_LODGINGS = "Board lodgings";

    public List<AssessmentDetail> mapOutgoings(List<Outgoing> outgoings, Object housingPaymentType) {
        List<AssessmentDetail> assessmentDetails = new ArrayList<>();

        if (Objects.nonNull(outgoings)) {
            for (Outgoing outgoing : outgoings) {
                AssessmentDetail assessmentDetail = new AssessmentDetail();
                String outgoingsType = outgoing.getPaymentType().value();
                // If the type is 'housing', we map against the housing codes instead
                if (outgoingsType.equals(HOUSING) && Objects.nonNull(housingPaymentType)) {
                    assessmentDetail.setAssessmentDetailCode(String.valueOf(HousingDetails.findByValue((String) housingPaymentType).getCode()));
                } else {
                    OutgoingDetails outgoingDetail = OutgoingDetails.findByValue(outgoingsType);
                    assessmentDetail.setAssessmentDetailCode(outgoingDetail.getCode());
                }

                assessmentDetail.setApplicantAmount(new BigDecimal(outgoing.getAmount()));
                assessmentDetail.setApplicantFrequency(FrequencyMapper.mapFrequency(outgoing.getFrequency().value()));

                assessmentDetails.add(assessmentDetail);
            }
        }

        return assessmentDetails;
    }

    public String mapOtherHousingFeesNotes(List<Outgoing> outgoings, Object housingPaymentType) {
        List<String> otherBenefitNotes = new ArrayList<>();
        if (Objects.isNull(outgoings)) {
            return StringUtils.EMPTY;
        }

        for (Outgoing outgoing : outgoings) {
            String outgoingsType = outgoing.getPaymentType().value();

            // Check housing payment type - this affects where 'housing' maps to
            if (HOUSING.equals(outgoingsType) && Objects.nonNull(housingPaymentType) && housingPaymentType.equals(BOARD_LODGINGS_TYPE)) {
                // If board_lodgings, we need to create a field for the notes associated with it
                otherBenefitNotes.add(BOARD_LODGINGS);

                if (Objects.nonNull(outgoing.getDetails())) {
                    otherBenefitNotes.add(outgoing.getDetails().toString());
                }
            }
        }

        return String.join("\n", otherBenefitNotes);
    }
}
