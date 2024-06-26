package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply.BenefitsMapper.DEFAULT_OWNERSHIP_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.applications.adaptor.enums.OutgoingDetails;
import uk.gov.justice.laa.crime.applications.adaptor.util.AssessmentDetailMapperUtil;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Outgoing;

public class OutgoingsMapper {
  private static final String BOARD_AND_LODGING_TYPE = "board_and_lodging";
  private static final String BOARD_AND_LODGING = "Board and lodging";

  public List<AssessmentDetail> mapOutgoings(List<Outgoing> outgoings) {
    List<AssessmentDetail> assessmentDetails = new ArrayList<>();

    if (Objects.nonNull(outgoings)) {
      for (Outgoing outgoing : outgoings) {
        String outgoingsType = outgoing.getPaymentType().value();
        OutgoingDetails outgoingDetail = OutgoingDetails.findByValue(outgoingsType);
        AssessmentDetail assessmentDetail =
            AssessmentDetailMapperUtil.mapMeansAssessmentDetails(
                DEFAULT_OWNERSHIP_TYPE,
                outgoingDetail.getCode(),
                outgoing.getAmount(),
                outgoing.getFrequency().value());
        assessmentDetails.add(assessmentDetail);
      }
    }

    return assessmentDetails;
  }

  public String mapOtherHousingFeesNotes(List<Outgoing> outgoings) {
    List<String> otherBenefitNotes = new ArrayList<>();
    if (Objects.isNull(outgoings)) {
      return StringUtils.EMPTY;
    }

    for (Outgoing outgoing : outgoings) {
      String outgoingsType = outgoing.getPaymentType().value();

      // Check housing payment type - this affects where 'housing' maps to
      if (BOARD_AND_LODGING_TYPE.equals(outgoingsType)) {
        // If board_and_lodging, we need to create a field for the notes associated with it
        otherBenefitNotes.add(BOARD_AND_LODGING);

        if (Objects.nonNull(outgoing.getDetails())) {
          otherBenefitNotes.add(outgoing.getDetails().toString());
        }
      }
    }

    return String.join("\n", otherBenefitNotes);
  }
}
